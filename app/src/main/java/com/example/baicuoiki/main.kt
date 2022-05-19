package com.example.baicuoiki

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class main : AppCompatActivity() {
    private lateinit var mloginemail: EditText
    private lateinit var mloginpassword:EditText
    private lateinit var mlogin: RelativeLayout
    private lateinit var mgotosignup:RelativeLayout
    private lateinit var mgotoforgotpassword: TextView
    private lateinit var mprogressbarofmainactivity: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!!!.hide()

        mloginemail = findViewById(R.id.loginemail)
        mloginpassword = findViewById(R.id.loginpassword)
        mlogin = findViewById(R.id.login)
        mgotoforgotpassword = findViewById(R.id.gotoforgotpassword)
        mgotosignup = findViewById(R.id.gotosignup)
        mprogressbarofmainactivity = findViewById<ProgressBar>(R.id.progressbarofmainactivity)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            finish()
            startActivity(Intent(this@main, notesactivity::class.java))
        }



        mgotosignup.setOnClickListener {
            startActivity(
                Intent(
                    this@main,
                    signup::class.java
                )
            )
        }

        mgotoforgotpassword.setOnClickListener {
            startActivity(
                Intent(
                    this@main,
                    fogotpassword::class.java
                )
            )
        }


        mlogin.setOnClickListener {
            val mail = mloginemail.text.toString().trim { it <= ' ' }
            val password = mloginpassword.text.toString().trim { it <= ' ' }
            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "không được bỏ trống", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // login the user
                mprogressbarofmainactivity.setVisibility(View.VISIBLE)
                firebaseAuth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            checkmailverfication()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "không đúng tài khoản",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            mprogressbarofmainactivity.setVisibility(View.INVISIBLE)
                        }
                    }
            }
        }

    }

    private fun checkmailverfication() {
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser!!.isEmailVerified == true) {
            Toast.makeText(applicationContext, "đăng nhập", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this@main, notesactivity::class.java))
        } else {
            mprogressbarofmainactivity.visibility = View.INVISIBLE
            Toast.makeText(applicationContext, "xác minh thư của bạn", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
        }
    }

}