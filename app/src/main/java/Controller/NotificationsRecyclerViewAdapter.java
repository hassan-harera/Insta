package Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.example.insta.ViewPost;
import com.example.insta.VisitProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;

import Model.Notifications.FriendRequestNotification;
import Model.Notifications.LikeNotification;
import Model.Notifications.Notification;
import Model.Post;

import static android.content.ContentValues.TAG;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder> {

    List<Notification> notifications;
    Context context;

    FirebaseAuth auth;
    private FirebaseFirestore fStore;


    public NotificationsRecyclerViewAdapter(List<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;

        if (notifications.isEmpty()) {
            Toast.makeText(context, "No Notifications To View", Toast.LENGTH_SHORT).show();
        }

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(50000000).build());

    }

    @NonNull
    @Override
    public NotificationsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new LikeHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.like_view_card, parent, false));
        } else {
            return new FriendRequestHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_request_view_card, parent, false));
        }
    }


    @Override
    public int getItemViewType(int position) {
        return notifications.get(position).getType();
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        switch (notifications.get(position).getType()) {
            case 1:
                final LikeHolder lh = (LikeHolder) holder;
                final LikeNotification ln = (LikeNotification) notifications.get(position);

                lh.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewPost.class);
                        intent.putExtra("Post ID", ln.getPostID());
                        intent.putExtra("UID", auth.getUid());
                        context.startActivity(intent);
                    }
                });


                fStore.collection("Users")
                        .document(auth.getUid())
                        .collection("Posts")
                        .document(ln.getPostID())
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.e(TAG, e.getStackTrace().toString());
                                } else if (ds.exists()) {
                                    Post post = ds.toObject(Post.class);
                                    lh.postIMG.setImageBitmap(BitmapFactory.decodeByteArray(post.getPostImage().toBytes()
                                            , 0, post.getPostImage().toBytes().length));
                                    lh.date.setText(ln.getDate().toDate().toString());
                                    lh.message.setText(ln.getLikeNumbers() + " Liked your picture");
                                }
                            }
                        });


                break;

            case 2:
                final FriendRequestHolder fh = (FriendRequestHolder) holder;
                final FriendRequestNotification frn = (FriendRequestNotification) notifications.get(position);

                fh.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VisitProfile.class);
                        intent.putExtra("Token", frn.getUID());
                        context.startActivity(intent);
                    }
                });

                fh.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirm(position);
                        notifications.remove(position);
                        notifyDataSetChanged();
                    }
                });

                fh.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(position);
                        notifications.remove(position);
                        notifyDataSetChanged();
                    }
                });

                fStore.collection("Users")
                        .document(frn.getUID())
                        .get(position).getUID())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        byte[] bytes = ds.getBlob("postImage").toBytes();
                        fh.profilePic.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    }
                });
        }
    }

    private void delete(int i) {
        DatabaseReference dRef = dbr.child("Users").child(auth.getUid());

        DatabaseReference dRef2 = dbr.child("Users").child(notifications.get(i).getUID());

        dRef.child("Friend Requests").child(notifications.get(i).getUID()).removeValue();

        dRef2.child("Friend Requests").child(auth.getUid()).removeValue();

        Toast.makeText(context, "Request deleted", Toast.LENGTH_SHORT).show();
    }

    private void confirm(int i) {
        DatabaseReference dRef = dbr.child("Users").child(auth.getUid());
        dRef.child("Friends").child(notifications.get(i).getUID()).setValue(notifications.get(i).getUID());

        DatabaseReference dRef2 = dbr.child("Users").child(notifications.get(i).getUID());
        dRef2.child("Friends").child(auth.getUid()).setValue(auth.getUid());

        dRef.child("Friend Requests").child(notifications.get(i).getUID()).removeValue();

        dRef2.child("Friend Requests").child(auth.getUid()).removeValue();

        Toast.makeText(context, "Request accepted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class LikeHolder extends ViewHolder {
        ImageView postIMG;
        TextView message, date;
        LinearLayout ll;

        public LikeHolder(@NonNull View v) {
            super(v);
            postIMG = v.findViewById(R.id.profile_image);
            message = v.findViewById(R.id.request_message);
            date = v.findViewById(R.id.request_date);
            ll = v.findViewById(R.id.ll);
        }
    }

    public class FriendRequestHolder extends ViewHolder {
        ImageView profilePic;
        TextView message, date, confirm, delete;
        LinearLayout ll;

        public FriendRequestHolder(@NonNull View v) {
            super(v);
            profilePic = v.findViewById(R.id.profile_image);
            message = v.findViewById(R.id.request_message);
            ll = v.findViewById(R.id.ll);
            confirm = v.findViewById(R.id.confirm);
            date = v.findViewById(R.id.request_date);
            delete = v.findViewById(R.id.delete);
        }
    }
}


