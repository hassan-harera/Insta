package com.whiteside.insta

import Controller.Image
import Model.Profile
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.whiteside.insta.databinding.ActivityEditProfileBinding
import java.io.ByteArrayOutputStream
import java.io.IOException

class EditProfile : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    private var fStore: FirebaseFirestore? = null
    private var profile: Profile? = null
    private var bind: ActivityEditProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(bind!!.root)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        progressBar = findViewById(R.id.progress_bar)
        info
        bind!!.userProfileEmail.text = auth!!.currentUser!!.email
        bind!!.userProfilePhoto.setOnClickListener{addImageClicked()}
    }

    private val info: Unit
        get() {
            fStore!!.collection("Users")
                    .document(auth!!.uid!!)
                    .get()
                    .addOnSuccessListener { ds: DocumentSnapshot ->
                        profile = Profile()
                        profile = ds.toObject(Profile::class.java)
                        val bytes = profile!!.profilePic!!.toBytes()
                        bind!!.userProfilePhoto.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                        bind!!.EditProfileName.setText(profile!!.name)
                        bind!!.EditProfileBio.setText(profile!!.bio)
                        bind!!.userProfileEmail.text = ds.getString("email")
                    }
        }

    fun editClicked(view: View?) {
        progressBar!!.visibility = View.VISIBLE
        profile!!.name = bind!!.EditProfileName.text.toString()
        profile!!.bio = bind!!.EditProfileBio.text.toString()
        fStore!!.collection("Users")
                .document(auth!!.uid!!)
                .set(profile!!, SetOptions.merge())
                .addOnCompleteListener {
                    Toast.makeText(this@EditProfile, "Successfully Edited", Toast.LENGTH_SHORT).show()
                    finish()
                }
    }

    fun addImageClicked() {
        bind!!.userProfilePhoto.isEnabled = false
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        bind!!.userProfilePhoto.isEnabled = true
        if (data != null && requestCode == 123 && resultCode == RESULT_OK) {
            val uri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                bind!!.userProfilePhoto.setImageBitmap(bitmap)
                val stream = ByteArrayOutputStream()
                Image.getReducedBitmap(bitmap, 512).compress(Bitmap.CompressFormat.PNG, 50, stream)
                profile!!.profilePic = Blob.fromBytes(stream.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}