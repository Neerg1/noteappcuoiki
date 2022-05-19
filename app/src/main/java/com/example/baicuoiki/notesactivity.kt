package com.example.baicuoiki

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class notesactivity : AppCompatActivity() {
    private lateinit var mcreatenotesfab: FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth


    private lateinit var mrecyclerview: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager


    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var noteAdapter: FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notesactivity)

        mcreatenotesfab = findViewById(R.id.createnotefab)
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseFirestore = FirebaseFirestore.getInstance()

        supportActionBar!!.title = "NoteApp"

        mcreatenotesfab.setOnClickListener {
            startActivity(
                Intent(
                    this@notesactivity,
                    createnote::class.java
                )
            )
        }


        val query =
            firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes")
                .orderBy("title", Query.Direction.ASCENDING)

        val allusernotes = FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(
            query,
            firebasemodel::class.java
        ).build()

        noteAdapter =
            object : FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
                @RequiresApi(api = Build.VERSION_CODES.M)
                override fun onBindViewHolder(
                    noteViewHolder: NoteViewHolder,
                    i: Int,
                    firebasemodel: firebasemodel
                ) {
                    val popupbutton =
                        noteViewHolder.itemView.findViewById<ImageView>(R.id.menupopbutton)
                    val colourcode: Int = getRandomColor()
                    noteViewHolder.mnote.setBackgroundColor(
                        noteViewHolder.itemView.resources.getColor(
                            colourcode,
                            null
                        )
                    )
                    noteViewHolder.notetitle.setText(firebasemodel.title)
                    noteViewHolder.notecontent.setText(firebasemodel.content)
                    val docId = noteAdapter.snapshots.getSnapshot(i).id
                    noteViewHolder.itemView.setOnClickListener { v ->
                        //we have to open note detail activity
                        val intent = Intent(v.context, notedetails::class.java)
                        intent.putExtra("title", firebasemodel.title)
                        intent.putExtra("content", firebasemodel.content)
                        intent.putExtra("noteId", docId)
                        v.context.startActivity(intent)

                        // Toast.makeText(getApplicationContext(),"This is Clicked",Toast.LENGTH_SHORT).show();
                    }
                    popupbutton.setOnClickListener { v ->
                        val popupMenu = PopupMenu(v.context, v)
                        popupMenu.gravity = Gravity.END
                        popupMenu.menu.add("sửa").setOnMenuItemClickListener {
                            val intent = Intent(v.context, editnoteactivity::class.java)
                            intent.putExtra("title", firebasemodel.title)
                            intent.putExtra("content", firebasemodel.content)
                            intent.putExtra("noteId", docId)
                            v.context.startActivity(intent)
                            false
                        }
                        popupMenu.menu.add("xóa")
                            .setOnMenuItemClickListener { //Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                                val documentReference =
                                    firebaseFirestore.collection("notes").document(firebaseUser.uid)
                                        .collection("myNotes").document(docId)
                                documentReference.delete().addOnSuccessListener {
                                    Toast.makeText(
                                        v.context,
                                        "đã xóa",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        v.context,
                                        "xóa thất bại",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                false
                            }
                        popupMenu.show()
                    }
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.notes_layout, parent, false)
                    return NoteViewHolder(view)
                }
            }


        mrecyclerview = findViewById(R.id.recyclerview)
        mrecyclerview.setHasFixedSize(true)
        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mrecyclerview.layoutManager = staggeredGridLayoutManager
        mrecyclerview.adapter = noteAdapter

    }
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notetitle: TextView
        val notecontent: TextView
        var mnote: LinearLayout

        init {
            notetitle = itemView.findViewById(R.id.notetitle)
            notecontent = itemView.findViewById(R.id.notecontent)
            mnote = itemView.findViewById(R.id.note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                firebaseAuth.signOut()
                finish()
                startActivity(Intent(this@notesactivity, main::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (noteAdapter != null) {
            noteAdapter.stopListening()
        }
    }


    private fun getRandomColor(): Int {
        val colorcode: MutableList<Int> = java.util.ArrayList()
        colorcode.add(R.color.gray)
        colorcode.add(R.color.pink)
        colorcode.add(R.color.lightgreen)
        colorcode.add(R.color.skyblue)
        colorcode.add(R.color.color1)
        colorcode.add(R.color.color2)
        colorcode.add(R.color.color3)
        colorcode.add(R.color.color4)
        colorcode.add(R.color.color5)
        colorcode.add(R.color.green)
        val random = Random()
        val number = random.nextInt(colorcode.size)
        return colorcode[number]
    }
}