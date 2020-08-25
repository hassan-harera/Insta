package Controller;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import Model.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Post> list;
    Context context;

    StorageReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage storage;

    InstaDatabaseHelper databaseHelper;

    public RecyclerViewAdapter(Context context, List<Post> list) {
        this.list = list;
        this.context = context;

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();
        reference = storage.getReference();

        databaseHelper = new InstaDatabaseHelper(context);

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.recName.setText(list.get(position).getName());
        holder.recDetails.setText(list.get(position).getDetails());

        int id = list.get(position).getId();

        final long Resulation = 1024 * 1024;
        OnCompleteListener<byte[]> listener = new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    holder.recImage.setImageBitmap(BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length));
                } else {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        };
        reference.child("Users").child(user.getUid()).child("Posts").child(id+"").getBytes(Resulation).addOnCompleteListener(listener);
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
