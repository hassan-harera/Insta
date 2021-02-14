package com.whiteside.insta

import Controller.Connection
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    var password: TextInputEditText? = null
    var email: TextInputEditText? = null
    var repassword: TextInputEditText? = null
    var name: TextInputEditText? = null
    var register: Button? = null
    var auth: FirebaseAuth? = null
    var fStore: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        name = findViewById(R.id.name_register)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password_register)
        repassword = findViewById(R.id.confirm_password_register)
        fStore = FirebaseFirestore.getInstance()
        register = findViewById(R.id.register_signup)
        auth = FirebaseAuth.getInstance()
    }

    fun registerClicked(view: View?) {
        val password = password!!.text.toString()
        val username = email!!.text.toString()
        val repassword = repassword!!.text.toString()
        val name = name!!.text.toString()
        if (name == "") {
            this.name!!.error = "Name is required"
        } else if (username == "") {
            email!!.error = "Email is required"
        } else if (password == "") {
            this.password!!.error = "Password is required"
        } else if (repassword == "") {
            this.repassword!!.error = "Reenter password"
        } else if (password != repassword) {
            this.repassword!!.error = "must be identical"
        } else {
            auth!!.createUserWithEmailAndPassword(username, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                        addUserToDatabase();
                    succeedRegister()
                } else {
                    failedRegister()
                }
            }
        }
    }

    private fun failedRegister() {
        if (!Connection.isConnected(this)) {
            internetError()
        } else {
            accountIsRegisteredError()
        }
    }

    private fun internetError() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Internet Problem")
        dialog.setMessage("There is an internet problem")
        dialog.setPositiveButton("Try again") { dialog, which ->
            registerClicked(register!!.rootView)
            dialog.cancel()
        }
        dialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }.show()
    }

    private fun accountIsRegisteredError() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Account is already registered")
        dialog.setPositiveButton("Login") { dialog, which ->
            dialog.cancel()
            goLogin()
        }
        dialog.setNegativeButton("Close") { dialog, which -> dialog.cancel() }.show()
    }

    private fun succeedRegister() {
        Toast.makeText(this, "successful register", Toast.LENGTH_LONG).show()
        val intent = Intent(this, WallActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun loginClicked(view: View?) {
        goLogin()
    }

    private fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}