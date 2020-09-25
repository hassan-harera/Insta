package Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;

import Model.Post;
import Model.Profile;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    List<Post> posts;
    FirebaseFirestore fStore;
    FirebaseAuth auth;


    public PostsRecyclerViewAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(50000000).build());
    }

    @NonNull
    @Override
    public PostsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view_card, parent, false);
        return new PostsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Post post = posts.get(position);
        fStore.collection("Users")
                .document(post.getUID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                        Profile profile = ds.toObject(Profile.class);
                        holder.profileImage.setImageBitmap(BitmapFactory.decodeByteArray(profile.getProfilePic().toBytes(),
                                0, profile.getProfilePic().toBytes().length));
                        holder.profileName.setText(profile.getName());
                        holder.postImage.setImageBitmap(getPostImageAsBitmap(post.getPostImage()));
                        holder.date.setText(post.getTime().toDate().toString());
                        holder.caption.setText(post.getCaption());
                        holder.love_number.setText(String.valueOf(post.getLikes().size()));
                        holder.love.setImageResource(post.getLiked() ? R.drawable.loved : R.drawable.love);
                        holder.bar.setVisibility(View.GONE);
                    }
                });


        holder.love.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                holder.love.setEnabled(false);
                if (!posts.get(position).getLiked()) {
                    posts.get(position).setLiked(true);
                    posts.get(position).addLike(auth.getUid());
                    holder.love_number.setText(String.valueOf(posts.get(position).getLikes().size()));
                    holder.love.setImageResource(R.drawable.loved);
                    setLike(posts.get(position));
                    holder.love.setEnabled(true);
                } else {
                    posts.get(position).setLiked(false);
                    posts.get(position).removeLike(auth.getUid());
                    holder.love_number.setText(String.valueOf(posts.get(position).getLikes().size()));
                    holder.love.setImageResource(R.drawable.love);
                    removeLike(posts.get(position));
                    holder.love.setEnabled(true);
                }
            }
        });
    }

    public Bitmap getPostImageAsBitmap(Blob postImage) {
        byte[] b = postImage.toBytes();
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    private void removeLike(final Post post) {
        fStore.collection("Users")
                .document(post.getUID())
                .collection("Posts")
                .document(post.getID())
                .update("likes", post.getLikes());
    }

    private void setLike(final Post post) {
        fStore.collection("Users")
                .document(post.getUID())
                .collection("Posts")
                .document(post.getID())
                .update("likes", post.getLikes());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, profileName, caption, love_number;
        ImageView love, postImage, profileImage;
        public ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.caption);
            postImage = itemView.findViewById(R.id.post_image);
            love_number = itemView.findViewById(R.id.love_number);
            bar = itemView.findViewById(R.id.progress_bar);
            profileImage = itemView.findViewById(R.id.profile_image);
            date = itemView.findViewById(R.id.date);
            profileName = itemView.findViewById(R.id.profile_name);
            love = itemView.findViewById(R.id.love);
        }
    }
}
