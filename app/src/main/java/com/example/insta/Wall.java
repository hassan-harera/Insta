package com.example.insta;

import android.animation.StateListAnimator;
import android.app.MediaRouteButton;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Controller.FragmentAdapter;
import Controller.InstaDatabaseHelper;


public class Wall extends AppCompatActivity {

    List<Fragment> list;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    FirebaseStorage storage;
    FirebaseUser user;
    StorageReference reference;
    FirebaseAuth auth;
    DatabaseReference dr;
    FirebaseDatabase firebaseDatabase;

    Toolbar toolbar;
    private RelativeLayout logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dr = firebaseDatabase.getReference();
        user = auth.getCurrentUser();
        reference = storage.getReference(user.getEmail());

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
            public boolean onQueryTextSubmit(final String query) {
                StorageReference storageReference = storage.getReference().child("Users").child(query).child("Profile Pic");
                storageReference.getBytes(1024 * 1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        if (task.isSuccessful()) {
                            goProfile(query);
                        } else {
                            Toast.makeText(Wall.this, "Incorrect Token", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
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
        }, 5000);

        viewPager.setCurrentItem(1);
    }

    private void addImage() {
        Intent intent = new Intent(this, AddImage.class);
        startActivity(intent);
    }


    private void goProfile(String query) {
        Intent intent = new Intent(this, VisitProfile.class);
        intent.putExtra("Token", query);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.edit_profile) {
            Intent intent = new Intent(this, EditProfile.class);
            intent.putExtra("Token", user.getUid());
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
        String userId = user.getUid();

        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Token", userId);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Token copied", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        InstaDatabaseHelper helper = new InstaDatabaseHelper(this);
        helper.resetDatabase();
        startActivity(intent);
        this.finish();
        auth.signOut();
    }


}