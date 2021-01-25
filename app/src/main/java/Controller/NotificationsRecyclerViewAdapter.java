package Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.whiteside.insta.R;
import com.whiteside.insta.ViewPost;
import com.whiteside.insta.VisitProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import Model.Date_Time;
import Model.Notifications.FriendRequestNotification;
import Model.Notifications.LikeNotification;
import Model.Notifications.Notification;
import Model.Post;
import Model.Profile;

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
        Notification n = notifications.get(position);

        switch (n.getType()) {
            case 1:
                final LikeHolder lh = (LikeHolder) holder;
                final LikeNotification ln = (LikeNotification) n;

                lh.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewPost.class);
                        intent.putExtra("Post ID", ln.getPostID());
                        intent.putExtra("UID", ln.getUID());
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
                                    lh.date.setText(Date_Time.timeFromNow(ln.getDate()));
                                    lh.message.setText(post.getLikes().size() + " people reacted to your post");
                                }
                            }
                        });
                break;

            case 2:
                final FriendRequestHolder fh = (FriendRequestHolder) holder;
                final FriendRequestNotification frn = (FriendRequestNotification) n;

                fh.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VisitProfile.class);
                        intent.putExtra("UID", frn.getUID());
                        context.startActivity(intent);
                    }
                });

                fh.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirm(frn);
                        notifications.remove(frn.getUID());
                        notifyDataSetChanged();
                    }
                });

                fh.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(frn);
                        notifications.remove(frn.getUID());
                        notifyDataSetChanged();
                    }
                });

                fStore.collection("Users")
                        .document(frn.getUID())
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.e(TAG, e.getStackTrace().toString());
                                } else if (ds.exists()) {
                                    Profile profile = ds.toObject(Profile.class);
                                    fh.profilePic.setImageBitmap(BitmapFactory.decodeByteArray(profile.getProfilePic()
                                                    .toBytes(), 0,
                                            profile.getProfilePic().toBytes().length));
                                    fh.date.setText(Date_Time.timeFromNow(frn.getDate()));
                                    fh.message.setText(profile.getName() + "\nsent you a friend request");
                                }
                            }
                        });
        }
    }


    private void confirm(final FriendRequestNotification n) {
        final Profile[] profile = new Profile[1];

        fStore.collection("Users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        profile[0] = ds.toObject(Profile.class);

                        profile[0].addFriend(n.getUID());
                        profile[0].removeFriendRequest(n.getUID());

                        fStore.collection("Users")
                                .document(auth.getUid())
                                .set(profile[0], SetOptions.merge());
                    }
                });


        fStore.collection("Users")
                .document(n.getUID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        profile[0] = ds.toObject(Profile.class);

                        profile[0].addFriend(auth.getUid());

                        fStore.collection("Users")
                                .document(n.getUID())
                                .set(profile[0], SetOptions.merge());
                    }
                });
    }

    private void delete(final FriendRequestNotification n) {
        final Profile[] profile = new Profile[1];
        fStore.collection("Users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                         profile[0] = ds.toObject(Profile.class);

                        profile[0].removeFriendRequest(n.getUID());


                    }
                });

        fStore.collection("Users")
                .document(auth.getUid())
                .set(profile[0], SetOptions.merge());

    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void update(List<Notification> list) {
        this.notifications = list;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class LikeHolder extends ViewHolder {
        ImageView postIMG;
        TextView message, date;
        RelativeLayout relativeLayout;

        public LikeHolder(@NonNull View v) {
            super(v);
            postIMG = v.findViewById(R.id.profile_image);
            message = v.findViewById(R.id.request_message);
            date = v.findViewById(R.id.request_date);
            relativeLayout = v.findViewById(R.id.relativeLayout);
        }
    }

    public class FriendRequestHolder extends ViewHolder {
        ImageView profilePic;
        TextView message, date, confirm, delete;
        RelativeLayout relativeLayout;

        public FriendRequestHolder(@NonNull View v) {
            super(v);
            profilePic = v.findViewById(R.id.profile_image);
            message = v.findViewById(R.id.request_message);
            relativeLayout = v.findViewById(R.id.relativeLayout);
            confirm = v.findViewById(R.id.confirm);
            date = v.findViewById(R.id.request_date);
            delete = v.findViewById(R.id.delete);
        }
    }
}


