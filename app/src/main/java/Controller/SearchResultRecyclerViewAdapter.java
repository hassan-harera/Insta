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
import com.example.insta.VisitProfile;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import Model.Profile;

import static android.content.ContentValues.TAG;

public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder> {

    List<String> list;
    Context context;
    private FirebaseFirestore fStore;

    public SearchResultRecyclerViewAdapter(List<String> list, Context context) {
        fStore = FirebaseFirestore.getInstance();
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchResultRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VisitProfile.class);
                intent.putExtra("UID", list.get(position));
                context.startActivity(intent);
            }
        });

        String UID = list.get(position);
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
        return list.size();
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
