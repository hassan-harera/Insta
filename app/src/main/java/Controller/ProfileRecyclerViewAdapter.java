package Controller;

import android.annotation.SuppressLint;
import android.app.MediaRouteButton;
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

import java.util.List;

import Model.Post;
import Model.User;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    List<Post> list;
    StorageReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    InstaDatabaseHelper databaseHelper;

    public ProfileRecyclerViewAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();
        reference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseHelper = new InstaDatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProfileRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view_card, parent, false);
        return new ProfileRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int id = list.get(position).getId();
        if (databaseHelper.checkPost(list.get(position).getUID(), list.get(position).getId())) {
            Bitmap bitmap = databaseHelper.getPost(list.get(position).getUID(), list.get(position).getId()).getBitmap();
            holder.recImage.setImageBitmap(bitmap);
            holder.caption.setText(list.get(position).getCaption());
            holder.date.setText(list.get(position).getDate());
            holder.love_list.setText(list.get(position).getLikes() + " Loves");
            databaseHelper.updatePost(list.get(position));
            getUser(holder, position);
        } else {
            getPost(holder, position);
            getUser(holder, position);
        }
    }

    private void getUser(final ProfileRecyclerViewAdapter.ViewHolder holder, int position) {
        User user = databaseHelper.getUser(auth.getUid());
        holder.user_pic.setImageBitmap(user.getProfilePic());
        holder.name.setText(user.getName());
        holder.bar.setVisibility(View.GONE);
    }

    private void getPost(final ProfileRecyclerViewAdapter.ViewHolder holder, final int position) {
        reference.child("Users").child(list.get(position).getUID()).child("Posts").child(list.get(position).getId() + "").
                getBytes(1024 * 1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                    holder.recImage.setImageBitmap(bitmap);
                    holder.date.setText(list.get(position).getDate());
                    holder.caption.setText(list.get(position).getCaption());
                    holder.love_list.setText(list.get(position).getLikes() + " Loves");
                    Post post = list.get(position);
                    post.setBitmap(bitmap);
                    post.setLiked(false);
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


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;
        ImageView user_pic;
        public ProgressBar bar;
        TextView caption, love_list;
        ImageView recImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.caption);
            recImage = itemView.findViewById(R.id.rec_image);
            love_list = itemView.findViewById(R.id.love_list);
            bar = itemView.findViewById(R.id.progress_bar);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            user_pic = itemView.findViewById(R.id.user_pic);
        }
    }
}
