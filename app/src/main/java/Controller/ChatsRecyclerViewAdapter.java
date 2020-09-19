package Controller;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.Chat;
import com.example.insta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Model.User;

public class ChatsRecyclerViewAdapter extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<String> list;
    private InstaDatabaseHelper databaseHelper;
    private StorageReference reference;
    private DatabaseReference databaseReference;
    Timer timer;

    public ChatsRecyclerViewAdapter(final Context context, List<String> list) {
        this.context = context;
        this.list = list;
        databaseHelper = new InstaDatabaseHelper(context);
        reference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("UID", list.get(position));
                context.startActivity(intent);
            }
        });
        getFriend(holder, position);
    }

    private void getFriend(final ChatsRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (!databaseHelper.checkUser(list.get(position))) {
            final User user = new User();
            user.setUid(list.get(position));
            reference.child("Users").child(list.get(position))
                    .child("Profile Pic").getBytes(1024 * 1024).
                    addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.badge.setImageBitmap(bitmap);
                            user.setProfilePic(bitmap);

                            databaseReference.child("Users").child(list.get(position))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot ds) {
                                            user.setName(ds.child("Name").getValue(String.class));
                                            holder.name.setText(user.getName());
                                            databaseHelper.insertUser(user);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                        }
                    });
        } else {
            User user = databaseHelper.getUser(list.get(position));
            holder.badge.setImageBitmap(user.getProfilePic());
            holder.name.setText(user.getName());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        QuickContactBadge badge;
        TextView name;
        LinearLayout ll;

        public ViewHolder(@NonNull View v) {
            super(v);

            badge = v.findViewById(R.id.user_pic);
            name = v.findViewById(R.id.name);
            ll = v.findViewById(R.id.ll);
        }
    }
}
