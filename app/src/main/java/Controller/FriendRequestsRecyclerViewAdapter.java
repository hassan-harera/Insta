package Controller;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FriendRequestsRecyclerViewAdapter extends RecyclerView.Adapter<FriendRequestsRecyclerViewAdapter.ViewHolder> {

    List<String> uids;
    Context context;

    FirebaseAuth auth;
    FirebaseUser user;
    String currentUID;
    DatabaseReference dbr;
    FirebaseDatabase db;
    StorageReference sr;
    FirebaseStorage fs;

    public FriendRequestsRecyclerViewAdapter(List<String> uids, Context context) {
        this.uids = uids;
        this.context = context;

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        currentUID = user.getUid();
        db = FirebaseDatabase.getInstance();
        dbr = db.getReference();
        fs = FirebaseStorage.getInstance();
        sr = fs.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_view_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        dbr.child("Users").child(uids.get(position)).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.message.setText(snapshot.getValue(String.class)+ "\n" + "Sent you a friend request");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sr.child("Users").child(uids.get(position)).child("Profile Pic").getBytes(4096*4096).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] b) {
                holder.profilePic.setImageBitmap(BitmapFactory.decodeByteArray(b, 0,b.length));
            }
        });

    }

    @Override
    public int getItemCount() {
        return uids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView message;

        public ViewHolder(@NonNull View v) {
            super(v);

            profilePic = v.findViewById(R.id.profile_pic);
            message = v.findViewById(R.id.request_message);
        }
    }
}
