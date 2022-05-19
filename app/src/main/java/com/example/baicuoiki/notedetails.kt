package com.example.baicuoiki

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class notedetails : AppCompatActivity() {
    private lateinit var mtitleofnotedetail: TextView
    private lateinit var mcontentofnotedetail:TextView
    private lateinit var mgotoeditnote: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notedetails)
        mtitleofnotedetail = findViewById(R.id.titleofnotedetail)
        mcontentofnotedetail = findViewById(R.id.contentofnotedetail)
        mgotoeditnote = findViewById(R.id.gotoeditnote)
//        val toolbar = findViewById<Toolbar>(R.id.toolbarofnotedetail)
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val data = intent

        mgotoeditnote.setOnClickListener { v ->
            val intent = Intent(v.context, editnoteactivity::class.java)
            intent.putExtra("title", data.getStringExtra("title"))
            intent.putExtra("content", data.getStringExtra("content"))
            intent.putExtra("noteId", data.getStringExtra("noteId"))
            v.context.startActivity(intent)
        }

        mcontentofnotedetail.text = data.getStringExtra("content")
        mtitleofnotedetail.text = data.getStringExtra("title")
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
