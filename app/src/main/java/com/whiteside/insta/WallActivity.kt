package com.whiteside.insta

import Controller.FragmentAdapter
import Model.Profile
import android.animation.StateListAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.facebook.login.LoginManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.GoogleSignIn.getGoogleSignInClient
import com.whiteside.insta.databinding.ActivityWallBinding
import java.util.*
import kotlin.collections.ArrayList

class WallActivity : AppCompatActivity() {
    private lateinit var bind: ActivityWallBinding
    private var list: ArrayList<Fragment> = ArrayList()
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
    private var auth: FirebaseAuth? = null
    private var fStore: FirebaseFirestore? = null
    private var toolbar: Toolbar? = null
    private var logo: RelativeLayout? = null
    private var app_bar: AppBarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityWallBinding.inflate(layoutInflater)
        setContentView(bind.root)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        toolbar = findViewById(R.id.toolbar)
        app_bar = findViewById(R.id.app_bar)
        logo = findViewById(R.id.logo)
        toolbar!!.setOnMenuItemClickListener({ item: MenuItem -> onOptionsItemSelected(item) })
        toolbar!!.title = "Insta"
        val searchView = findViewById<SearchView>(R.id.search_token)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String): Boolean {
                val list = searchUser(name)
                Handler().postDelayed({ getSearchResult(list) }, 3000)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
        list.add(AddImage())
        list.add(FeedFragment())
        list.add(ProfileActivity())
        list.add(Notifications())
        list.add(Chats())
        viewPager = bind.viewPager
        val adapter = FragmentAdapter(this, list)
        viewPager!!.adapter = adapter
        viewPager!!.stateListAnimator = StateListAnimator()
        viewPager!!.setCurrentItem(0, true)
        val res: ArrayList<Int> = ArrayList()
        res.add(R.drawable.add_image)
        res.add(R.drawable.home)
        res.add(R.drawable.man)
        res.add(R.drawable.notification)
        res.add(R.drawable.chats)
        tabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabLayout!!, viewPager!!, { tab: TabLayout.Tab, position: Int -> tab.setIcon(res[position]) }).attach()
        viewPager!!.currentItem = 1
    }

    private fun getSearchResult(list: List<String?>) {
        if (list.isEmpty()) Toast.makeText(this@WallActivity, "Not Found", Toast.LENGTH_SHORT).show() else {
            val intent = Intent(this@WallActivity, SearchResult::class.java)
            val arr = arrayOfNulls<String>(list.size)
            for (i in list.indices) {
                arr[i] = list[i]
            }
            intent.putExtra("list", arr)
            startActivity(intent)
        }
    }

    private fun searchUser(name: String): List<String?> {
        val q: Queue<String?> = LinkedList()
        q.add(auth!!.uid)
        val visited: MutableMap<String?, Boolean> = HashMap()
        visited[auth!!.uid] = true
        val count = intArrayOf(1)
        val list: ArrayList<String> = ArrayList()
        while (!q.isEmpty() && count[0] < 5000) {
            val cur = q.poll()
            count[0]++
            val p = arrayOfNulls<Profile>(1)
            fStore!!.collection("Users")
                    .document(cur!!)
                    .get()
                    .addOnSuccessListener { ds ->
                        p[0] = ds.toObject(Profile::class.java)
                        if (p[0]!!.name!!.contains(name)) {
                            list.add(cur)
                        }
                    }
            Handler().postDelayed({
                for (user in p[0]!!.getFriends()!!) {
                    if (!visited.containsKey(user)) {
                        q.add(user)
                        visited[user] = true
                    }
                }
            }, 500)
        }
        return list
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.edit_profile) {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("Token", auth!!.uid)
            startActivity(intent)
        } else if (itemId == R.id.logout) {
            logout()
        } else if (itemId == R.id.get_token) {
            token
        } else if (itemId == R.id.search_token) {
        }
        return true
    }

    private val token: Unit
        get() {
            val userId = auth!!.uid
            val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Token", userId)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Token copied", Toast.LENGTH_SHORT).show()
        }

    private fun logout() {
        val client = getGoogleSignInClient(this)
        client.signOut()
                .addOnFailureListener { e -> e.printStackTrace() }
        LoginManager.getInstance().logOut()
        auth!!.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}