package com.example.baicuoiki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class fogotpassword : AppCompatActivity() {
    private lateinit var mforgotpassword: EditText
    private lateinit var mpasswordrecoverbutton: Button
    private lateinit var mgobacktologin: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fogotpassword)
        supportActionBar!!.hide()

        mforgotpassword = findViewById(R.id.forgotpassword)
        mpasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton)
        mgobacktologin = findViewById(R.id.gobacktologin)

        firebaseAuth = FirebaseAuth.getInstance()


        mgobacktologin.setOnClickListener {
            val intent = Intent(this@fogotpassword, main::class.java)
            startActivity(intent)
        }

        mpasswordrecoverbutton.setOnClickListener {
            val mail = mforgotpassword.text.toString().trim { it <= ' ' }
            if (mail.isEmpty()) {
                Toast.makeText(applicationContext, "nhập mail của bạn", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //we have to send password recover email
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Mail đã gửi, xem email để lấy lại mật khẩu",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        startActivity(Intent(this@fogotpassword, main::class.java))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Email hoặc tài khoản không đúng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}