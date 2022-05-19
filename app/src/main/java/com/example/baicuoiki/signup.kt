package com.example.baicuoiki

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class signup : AppCompatActivity() {
    private lateinit var msignupemail: EditText
    private lateinit var msignuppassword:EditText
    private lateinit var msignup: RelativeLayout
    private lateinit var mgotologin: TextView


    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar!!.hide()

        msignupemail = findViewById(R.id.signupemail)
        msignuppassword = findViewById(R.id.signuppassword)
        msignup = findViewById(R.id.signup)
        mgotologin = findViewById(R.id.gotologin)

        firebaseAuth = FirebaseAuth.getInstance()





        mgotologin.setOnClickListener {
            val intent = Intent(this@signup, main::class.java)
            startActivity(intent)
        }

        msignup.setOnClickListener {
            val mail = msignupemail.text.toString().trim { it <= ' ' }
            val password = msignuppassword.text.toString().trim { it <= ' ' }
            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "không được bỏ trống", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.length < 7) {
                Toast.makeText(
                    applicationContext,
                    "mật khẩu phải có 7 kí tự",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                /// registered the user to firebase
                firebaseAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "đăng ký thành công",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            sendEmailVerification()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "đăng ký thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
    private fun sendEmailVerification() {
        val firebaseUser = firebaseAuth!!.currentUser
        firebaseUser?.sendEmailVerification()?.addOnCompleteListener {
            Toast.makeText(
                applicationContext,
                "email đã gửi, xác minh và đăng nhập lại",
                Toast.LENGTH_SHORT
            ).show()
            firebaseAuth!!.signOut()
            finish()
            startActivity(Intent(this@signup, main::class.java))
        }
            ?: Toast.makeText(
                applicationContext,
                "không gửi được email xác minh",
                Toast.LENGTH_SHORT
            ).show()
    }
}