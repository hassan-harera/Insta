package com.whiteside.insta;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import Controller.FragmentAdapter;
import Model.Profile;


public class FeedActivity extends AppCompatActivity {

    private List<Fragment> list;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private FirebaseAuth auth;
    private FirebaseFirestore fStore;

    private Toolbar toolbar;
    private RelativeLayout logo;
    private AppBarLayout app_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        app_bar = findViewById(R.id.app_bar);
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
            public boolean onQueryTextSubmit(String name) {
                final List<String> list = searchUser(name);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSearchResult(list);
                    }
                }, 3000);
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

        viewPager.setCurrentItem(1);
    }

    private void getSearchResult(List<String> list) {
        if (list.isEmpty())
            Toast.makeText(FeedActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(FeedActivity.this, SearchResult.class);
            String arr[] = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }
            intent.putExtra("list", arr);
            startActivity(intent);
        }
    }

    private List<String> searchUser(final String name) {
        final Queue<String> q = new LinkedList<>();
        q.add(auth.getUid());
        final Map<String, Boolean> visited = new HashMap<>();
        visited.put(auth.getUid(), true);

        final int[] count = {1};
        final List<String> list = new ArrayList();

        while (!q.isEmpty() && count[0] < 5000) {
            final String cur = q.poll();
            count[0]++;
            final Profile[] p = new Profile[1];

            fStore.collection("Users")
                    .document(cur)
                    .get()

                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot ds) {
                            p[0] = ds.toObject(Profile.class);

                            if (p[0].getName().contains(name)) {
                                list.add(cur);
                            }
                        }
                    });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (String user : p[0].getFriends()) {
                        if (!visited.containsKey(user)) {
                            q.add(user);
                            visited.put(user, true);
                        }
                    }
                }
            }, 500);
        }
        return list;
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
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
