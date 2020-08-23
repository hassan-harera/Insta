package com.example.insta;

public class Test {

    /*
            DatabaseReference images = databaseReference.child(firebaseUser.getUid()).child("Images");
        images.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iterable = snapshot.getChildren();
                for (DataSnapshot  ds : iterable) {
                    Post p = new Post();
                    p.setId((int) ds.child("Id").getValue());
                    p.setName((String) ds.child("Name").getValue());
                    p.setDetails((String) ds.child("Details").getValue());
                    insertPost(p);
                    list.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
     */

    /*
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <androidx.cardview.widget.CardView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_marginTop="10dp"
            app:cardCornerRadius="150dp">

            <ImageView
                android:id="@+id/profile_img"
                android:layout_width="220dp"
                android:layout_height="220dp"
                android:layout_gravity="center"
                android:layout_margin="20dp"
                android:scaleType="centerCrop" />
        </androidx.cardview.widget.CardView>

        <TextView
            android:id="@+id/name"
            android:layout_width="120dp"
            android:layout_height="60dp"
            android:layout_gravity="center"
            android:layout_marginTop="30dp"
            android:textSize="24dp" />

        <TextView
            android:id="@+id/username"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_margin="30dp" />

        <TextView
            android:id="@+id/ss"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_margin="30dp" />
    </LinearLayout>
     */
}
