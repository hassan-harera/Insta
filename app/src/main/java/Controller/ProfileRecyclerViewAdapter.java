package Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import Model.Post;

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
        if (databaseHelper.checkProfilePost(id)) {
            Post post = databaseHelper.getProfilePost(id);
            holder.recImage.setImageBitmap(post.getBitmap());
            holder.recName.setText(list.get(position).getTitle());
            holder.recDetails.setText(list.get(position).getDescription());
        } else {
            final long resolution = 4096 * 4096;
            reference.child("Users").child(user.getUid()).child("Posts").child(id + "").
                    getBytes(resolution).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
                    if (task.isSuccessful()) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                        holder.recImage.setImageBitmap(bitmap);
                        holder.recName.setText(list.get(position).getTitle());
                        holder.recDetails.setText(list.get(position).getDescription());
                        Post post = list.get(position);
                        post.setBitmap(bitmap);
                        databaseHelper.insertProfilePost(post);
                    } else {
                        Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recName, recDetails;
        ImageView recImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recDetails = itemView.findViewById(R.id.rec_details);
            recImage = itemView.findViewById(R.id.rec_image);
            recName = itemView.findViewById(R.id.rec_name);

        }
    }
}
