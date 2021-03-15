package com.whiteside.insta.ui.wall

import com.whiteside.insta.adapter.FragmentAdapter
import com.whiteside.insta.model.Profile
import android.animation.StateListAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.facebook.login.LoginManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.GoogleSignIn
import com.whiteside.insta.GoogleSignIn.getGoogleSignInClient
import com.whiteside.insta.R
import com.whiteside.insta.databinding.ActivityWallBinding
import com.whiteside.insta.ui.chats.Chats
import com.whiteside.insta.ui.edit_profile.EditProfileActivity
import com.whiteside.insta.ui.feed.FeedFragment
import com.whiteside.insta.ui.login.LoginActivity
import com.whiteside.insta.ui.notifications.NotificationsFragment
import com.whiteside.insta.ui.profile.ProfileFragment
import com.whiteside.insta.ui.search_resault.SearchResult
import com.whiteside.insta.ui.visit_profile.VisitProfile
import java.util.*
import kotlin.collections.ArrayList

class WallActivity : AppCompatActivity() {
    private lateinit var bind: ActivityWallBinding
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
    var fStore = FirebaseFirestore.getInstance()
    private var toolbar: Toolbar? = null
    private var app_bar: AppBarLayout? = null
    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_wall)

        toolbar = findViewById(R.id.toolbar)
        app_bar = findViewById(R.id.app_bar)
        toolbar!!.setOnMenuItemClickListener { item: MenuItem -> onOptionsItemSelected(item) }
        toolbar!!.title = "Insta"

        val searchView = findViewById<SearchView>(R.id.search_token)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String): Boolean {
                searchProfile(name)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        val list = arrayListOf(
            FeedFragment(),
            ProfileFragment(),
            NotificationsFragment(),
            Chats()
        )

        val res = arrayListOf(
            (R.drawable.home),
            (R.drawable.man),
            (R.drawable.notification),
            (R.drawable.chats)
        )

        viewPager = bind.viewPager
        val adapter = FragmentAdapter(this, list)
        viewPager!!.adapter = adapter
        viewPager!!.stateListAnimator = StateListAnimator()
        viewPager!!.setCurrentItem(0, true)

        viewPager!!.setCurrentItem(0, true)

        tabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(
            tabLayout!!,
            viewPager!!
        ) { tab: TabLayout.Tab, position: Int -> tab.setIcon(res[position]) }.attach()
        viewPager!!.currentItem = 1
    }


    private fun searchProfile(name: String) {
        val intent = Intent(this, SearchResult::class.java)
        intent.putExtra("search", name)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.edit_profile) {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        } else if (itemId == R.id.logout) {
            logout()
        }
        return true
    }

    fun getToken() {
        val userId = auth.uid
        val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Token", userId)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Token copied", Toast.LENGTH_SHORT).show()
    }

    fun logout() {
        val client = GoogleSignIn.getGoogleSignInClient(this)
        client.signOut().addOnFailureListener { it.printStackTrace() }
        LoginManager.getInstance().logOut()
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}