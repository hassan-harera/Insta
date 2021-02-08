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

import com.whiteside.insta.ui.ChatActivity;
import com.whiteside.insta.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import Model.Profile;

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
                Intent intent = new Intent(context, ChatActivity.class);
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
