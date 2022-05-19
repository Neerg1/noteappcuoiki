package com.example.baicuoiki

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class editnoteactivity : AppCompatActivity() {

    private lateinit var data: Intent
    private lateinit var medittitleofnote: EditText
    private lateinit var meditcontentofnote:EditText
    private lateinit var msaveeditnote: FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editnoteactivity)

        medittitleofnote = findViewById(R.id.edittitleofnote)
        meditcontentofnote = findViewById(R.id.editcontentofnote)
        msaveeditnote = findViewById(R.id.saveeditnote)

        data = intent

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!


//        val toolbar = findViewById<Toolbar>(R.id.toolbarofeditnote)
//        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        msaveeditnote.setOnClickListener(View.OnClickListener { //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();
            val newtitle = medittitleofnote.text.toString()
            val newcontent = meditcontentofnote.text.toString()
            if (newtitle.isEmpty() || newcontent.isEmpty()) {
                Toast.makeText(applicationContext, "không được bỏ trống", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                val documentReference =
                    firebaseFirestore.collection("notes").document(firebaseUser.uid)
                        .collection("myNotes").document(
                            data.getStringExtra("noteId")!!
                        )
                val note: MutableMap<String, Any> = HashMap()
                note["title"] = newtitle
                note["content"] = newcontent
                documentReference.set(note).addOnSuccessListener {
                    Toast.makeText(applicationContext, "đã cập nhât", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@editnoteactivity, notesactivity::class.java))
                }.addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "cập nhật thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


        val notetitle = data.getStringExtra("title")
        val notecontent = data.getStringExtra("content")
        meditcontentofnote.setText(notecontent)
        medittitleofnote.setText(notetitle)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}