package Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.DuplicateFormatFlagsException;
import java.util.List;

import Model.Post;

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
                    list.get(position).setLikes(list.get(position).getLikes()+1);
                    holder.loveList.setText((list.get(position).getLikes()) + " Loves");
                    holder.love.setBackgroundResource(R.drawable.loved);
                    doChanges(list.get(position));
                    holder.love.setEnabled(true);
                } else {
                    list.get(position).setLiked(false);
                    list.get(position).setLikes(list.get(position).getLikes()- 1);
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
            holder.caption.setText(list.get(position).getCaption());
            holder.loveList.setText(list.get(position).getLikes() + " Loves");
            holder.bar.setVisibility(View.GONE);
            databaseHelper.updatePost(list.get(position));
        } else {
            final long resolution = 4096 * 4096;
            reference.child("Users").child(list.get(position).getUID()).child("Posts").child(list.get(position).getId() + "").
                    getBytes(resolution).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
                    if (task.isSuccessful()) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                        holder.recImage.setImageBitmap(bitmap);
                        holder.caption.setText(list.get(position).getCaption());
                        holder.loveList.setText(list.get(position).getLikes() + " Loves");
                        Post post = list.get(position);
                        post.setBitmap(bitmap);
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
    }

    private void undoChanges(final Post post) {
        databaseReference.child("Users").child(post.getUID()).child("Notifications")
                .child("Likes").child(user.getUid() + " " + post.getId()).removeValue();

        databaseReference.child("Users").child(post.getUID()).child("Posts")
                .child(post.getId() + "").child("Likes").child(user.getUid())
                .removeValue();
    }

    private void doChanges(final Post post) {
        String name = databaseHelper.getUser(user.getUid()).getName();

        DatabaseReference dbr = databaseReference.child("Users").child(post.getUID()).child("Notifications")
                .child("Likes").child(user.getUid() + " " + post.getId());

        dbr.child("Message")
                .setValue(name + " Liked your picture");
        dbr.child("Post ID")
                .setValue(post.getId());
        dbr.child("UID")
                .setValue(user.getUid());

        databaseReference.child("Users").child(post.getUID()).child("Posts")
                .child(post.getId() + "").child("Likes").child(user.getUid())
                .setValue(user.getUid());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView caption, loveList;
        ImageView recImage;
        Button love;
        ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.caption);
            recImage = itemView.findViewById(R.id.rec_image);
            love = itemView.findViewById(R.id.love);
            loveList = itemView.findViewById(R.id.love_list);
            bar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
