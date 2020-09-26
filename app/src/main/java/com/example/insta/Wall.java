package com.example.insta;

import android.animation.StateListAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.List;

import Controller.FragmentAdapter;


public class Wall extends AppCompatActivity {

    List<Fragment> list;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    FirebaseAuth auth;
    FirebaseFirestore fStore;

    Toolbar toolbar;
    private RelativeLayout logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore.setFirestoreSettings(new FirebaseFirestoreSettings.
                Builder().
                setCacheSizeBytes(50000000).setPersistenceEnabled(true).build());

        toolbar = findViewById(R.id.toolbar);
        logo = findViewById(R.id.logo);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        toolbar.setTitle("Insta");


        final SearchView searchView = findViewById(R.id.search_token);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String UID) {
                fStore.collection("Users")
                        .document(UID).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    visitProfile(UID);
                                } else {
                                    Toast.makeText(Wall.this, "Incorrect user token", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        list = new ArrayList();
        list.add(new AddImage());
        list.add(new Feed());
        list.add(new ViewProfile());
        list.add(new Notifications());
        list.add(new Chats());

        viewPager = findViewById(R.id.view_pager);
        FragmentAdapter adapter = new FragmentAdapter(this, list);
        viewPager.setAdapter(adapter);
        viewPager.setStateListAnimator(new StateListAnimator());
        viewPager.setCurrentItem(0, true);

        final List<Integer> res = new ArrayList();
        res.add(R.drawable.add_image);
        res.add(R.drawable.home);
        res.add(R.drawable.man);
        res.add(R.drawable.notification);
        res.add(R.drawable.chats);

        tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setIcon(res.get(position));
            }
        }).attach();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.setVisibility(View.INVISIBLE);
            }
        }, 3000);

        viewPager.setCurrentItem(1);
    }

    private void visitProfile(String UID) {
        Intent intent = new Intent(this, VisitProfile.class);
        intent.putExtra("UID", UID);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.edit_profile) {
            Intent intent = new Intent(this, EditProfile.class);
            intent.putExtra("Token", auth.getUid());
            startActivity(intent);
        } else if (itemId == R.id.logout) {
            logout();
        } else if (itemId == R.id.get_token) {
            getToken();
        } else if (itemId == R.id.search_token) {

        }
        return true;
    }

    private void getToken() {
        String userId = auth.getUid();

        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Token", userId);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Token copied", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
        auth.signOut();
    }


}