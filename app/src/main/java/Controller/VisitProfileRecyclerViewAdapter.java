package Controller;

import android.app.MediaRouteButton;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import Model.Post;

public class VisitProfileRecyclerViewAdapter extends RecyclerView.Adapter<VisitProfileRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final String visitedUID;
    private final Bitmap profilePic;
    private final String name;
    List<Post> list;
    StorageReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    public VisitProfileRecyclerViewAdapter(List<Post> list, String visitedUID, Context context,
                                           Bitmap profilePic, String name) {
        this.list = list;
        this.visitedUID = visitedUID;
        this.context = context;
        this.profilePic = profilePic;
        this.name = name;

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();
        reference = storage.getReference();
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public VisitProfileRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view_card, parent, false);
        return new VisitProfileRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int id = list.get(position).getId();
        final long Resulation = 4096 * 4096;
        reference.child("Users").child(visitedUID).child("Posts").child(id + "").getBytes(Resulation)
                .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        if (task.isSuccessful()) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                            holder.recImage.setImageBitmap(bitmap);
                            holder.caption.setText(list.get(position).getCaption());
                            holder.date.setText(list.get(position).getDate());
                            holder.name.setText(name);
                            holder.user_pic.setImageBitmap(profilePic);
                            Post post = list.get(position);
                            holder.bar.setVisibility(View.GONE);
                            post.setBitmap(bitmap);
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
