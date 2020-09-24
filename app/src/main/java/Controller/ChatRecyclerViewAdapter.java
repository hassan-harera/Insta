package Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MessageHolder> {

    List<Model.Message> messages;
    Context context;


    FirebaseAuth auth;
    String currentUID;
    DatabaseReference dRef;
    StorageReference sRef;


    public ChatRecyclerViewAdapter(List<Model.Message> messages, Context context) {
        this.messages = messages;
        this.context = context;



        auth = FirebaseAuth.getInstance();
        currentUID = auth.getUid();
        dRef = FirebaseDatabase.getInstance().getReference();
        sRef = FirebaseStorage.getInstance().getReference();
    }

    public  void update (List<Model.Message> messages){
        this.messages = messages;
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getFrom().equals(auth.getUid())) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new SenderMessageHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.sender_message_card_view,
                    parent, false));
        } else {
            return new ReceiverMessageHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.reciever_message_card_view,
                    parent, false));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            SenderMessageHolder h = (SenderMessageHolder) holder;
            h.message.setText(messages.get(position).getMessage());
        } else {
            ReceiverMessageHolder h = (ReceiverMessageHolder) holder;
            h.message.setText(messages.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (messages.isEmpty()) {
            Toast.makeText(context, "No Messages", Toast.LENGTH_SHORT).show();
        }
        return messages.size();
    }


    public class MessageHolder extends RecyclerView.ViewHolder {
        public MessageHolder(@NonNull View v) {
            super(v);
        }
    }

    public class SenderMessageHolder extends MessageHolder {
        TextView message;
        public SenderMessageHolder(@NonNull View v) {
            super(v);
            message = v.findViewById(R.id.sender_message);
        }
    }

    public class ReceiverMessageHolder extends MessageHolder {
        TextView message;
        public ReceiverMessageHolder(@NonNull View v) {
            super(v);
            message = v.findViewById(R.id.receiver_message);
        }
    }
}
