package com.example.baicuoiki

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class createnote : AppCompatActivity() {
    private lateinit var mcreatetitleofnote: EditText
    private lateinit var mcreatecontentofnote:EditText
    private lateinit var msavenote: FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var mprogressbarofcreatenote: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createnote)

        msavenote = findViewById(R.id.savenote)
        mcreatecontentofnote = findViewById(R.id.createcontentofnote)
        mcreatetitleofnote = findViewById(R.id.createtitleofnote)


        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        msavenote.setOnClickListener {
            val title = mcreatetitleofnote.text.toString()
            val content = mcreatecontentofnote.text.toString()
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(applicationContext, "không bỏ trống", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val documentReference =
                    firebaseFirestore.collection("notes").document(firebaseUser.uid)
                        .collection("myNotes").document()
                val note: MutableMap<String, Any> =
                    HashMap()
                note["title"] = title
                note["content"] = content
                documentReference.set(note).addOnSuccessListener {
                    Toast.makeText(this,"tạo thành công",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@createnote, notesactivity::class.java))
                }.addOnFailureListener {
                    Toast.makeText(this, "tạo không thành công", Toast.LENGTH_SHORT)
                        .show()
                    // startActivity(new Intent(createnote.this,notesactivity.class));
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}