package Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.Chat;
import com.example.insta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Timer;

import Model.Post;
import Model.Profile;
import Model.User;

import static android.content.ContentValues.TAG;

public class ChatsRecyclerViewAdapter extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<String> friends;
    FirebaseFirestore fStore;

    public ChatsRecyclerViewAdapter(final Context context, List<String> friends) {
        this.context = context;
        this.friends = friends;

        fStore = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("UID", friends.get(position));
                context.startActivity(intent);
            }
        });

        String UID = friends.get(position);
        fStore.collection("Users")
                .document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (ds.exists()) {
                            Profile profile = ds.toObject(Profile.class);

                            Bitmap bitmap = BitmapFactory.decodeByteArray(profile.getProfilePic().toBytes()
                                    , 0, profile.getProfilePic().toBytes().length);
                            holder.badge.setImageBitmap(bitmap);
                            holder.name.setText(profile.getName());
                        }
                    }
                });

    }


    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        QuickContactBadge badge;
        TextView name;
        LinearLayout ll;

        public ViewHolder(@NonNull View v) {
            super(v);

            badge = v.findViewById(R.id.profile_image);
            name = v.findViewById(R.id.name);
            ll = v.findViewById(R.id.relativeLayout);
        }
    }
}
