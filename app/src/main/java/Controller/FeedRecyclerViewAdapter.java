package Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.Format;
import java.util.Date;
import java.util.DuplicateFormatFlagsException;
import java.util.List;

import Model.Post;
import Model.User;

public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    List<Post> list;
    StorageReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    InstaDatabaseHelper databaseHelper;

    public FeedRecyclerViewAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();
        reference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        databaseHelper = new InstaDatabaseHelper(context);
        databaseReference = database.getReference();
    }

    @NonNull
    @Override
    public FeedRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view_card, parent, false);
        return new FeedRecyclerViewAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.love.setBackgroundResource(list.get(position).getLiked() ? R.drawable.loved : R.drawable.love);

        holder.love.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                holder.love.setEnabled(false);
                if (!list.get(position).getLiked()) {
                    list.get(position).setLiked(true);
                    list.get(position).setLikes(list.get(position).getLikes() + 1);
                    holder.loveList.setText((list.get(position).getLikes()) + " Loves");
                    holder.love.setBackgroundResource(R.drawable.loved);
                    doChanges(list.get(position));
                    holder.love.setEnabled(true);
                } else {
                    list.get(position).setLiked(false);
                    list.get(position).setLikes(list.get(position).getLikes() - 1);
                    holder.loveList.setText((list.get(position).getLikes()) + " Loves");
                    holder.love.setBackgroundResource(R.drawable.love);
                    undoChanges(list.get(position));
                    holder.love.setEnabled(true);
                }
            }
        });

        if (databaseHelper.checkPost(list.get(position).getUID(), list.get(position).getId())) {
            Bitmap bitmap = databaseHelper.getPost(list.get(position).getUID(), list.get(position).getId()).getBitmap();
            holder.recImage.setImageBitmap(bitmap);
            holder.date.setText(list.get(position).getDate());
            holder.caption.setText(list.get(position).getCaption());
            holder.loveList.setText(list.get(position).getLikes() + " Loves");
            databaseHelper.updatePost(list.get(position));
            getUser(holder, position);
        } else {
            getPost(holder, position);
            getUser(holder, position);
        }



    }

    private void getUser(final FeedRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (!databaseHelper.checkUser(list.get(position).getUID())) {
            final User user = new User();
            user.setUid(list.get(position).getUID());

            reference.child("Users").child(list.get(position).getUID())
                    .child("Profile Pic").getBytes(1024 * 1024).
                    addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.user_pic.setImageBitmap(bitmap);
                            user.setProfilePic(bitmap);

                            databaseReference.child("Users").child(list.get(position).getUID())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot ds) {
                                            user.setName(ds.child("Name").getValue(String.class));
                                            holder.name.setText(user.getName());
                                            databaseHelper.insertUser(user);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                        }
                    });
        } else {
            User user = databaseHelper.getUser(list.get(position).getUID());
            holder.user_pic.setImageBitmap(user.getProfilePic());
            holder.name.setText(user.getName());
        }
        holder.bar.setVisibility(View.GONE);
    }

    private void getPost(final ViewHolder holder, final int position) {
        reference.child("Users").child(list.get(position).getUID()).child("Posts").child(list.get(position).getId() + "").
                getBytes(1024 * 1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                    holder.recImage.setImageBitmap(bitmap);
                    holder.caption.setText(list.get(position).getCaption());
                    holder.loveList.setText(list.get(position).getLikes() + " Loves");
                    holder.date.setText(list.get(position).getDate());
                    Post post = list.get(position);
                    post.setBitmap(bitmap);
                    post.setDate(list.get(position).getDate());
                    holder.bar.setVisibility(View.GONE);
                    if (!databaseHelper.checkPost(post.getUID(), post.getId())) {
                        databaseHelper.insertPost(post);
                    } else {
                        databaseHelper.updatePost(post);
                    }
                } else {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void undoChanges(final Post post) {
        databaseReference.child("Users").child(post.getUID()).child("Posts").child(post.getId() + "")
                .child("Likes").child(user.getUid()).removeValue();
    }

    private void doChanges(final Post post) {
        DatabaseReference dbr = databaseReference.child("Users").child(post.getUID()).child("Posts").child(post.getId() + "")
                .child("Likes").child(user.getUid());

        dbr.child("Post ID")
                .setValue(post.getId());
        dbr.child("UID")
                .setValue(user.getUid());
        dbr.child("Date")
                .setValue(new Date().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView caption, loveList, name, date;
        ImageView recImage, user_pic;
        Button love;
        ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.caption);
            recImage = itemView.findViewById(R.id.rec_image);
            love = itemView.findViewById(R.id.love);
            loveList = itemView.findViewById(R.id.love_list);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            user_pic = itemView.findViewById(R.id.user_pic);
            bar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
