package com.lobxy.todoapp_kotlin.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lobxy.todoapp_kotlin.R
import com.lobxy.todoapp_kotlin.controller.TodoViewHolder
import com.lobxy.todoapp_kotlin.model.Todo

class MainActivity : AppCompatActivity() {

    //TODO: 1. pass data to detail. 2. Swipe to delete 3.implement clicklistener

    private lateinit var mAuth: FirebaseAuth

    private var mUid: String = ""

    private lateinit var mRecyclerView: RecyclerView

    private var mAdapter: FirebaseRecyclerAdapter<Todo, TodoViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mUid = mAuth.currentUser!!.uid

        loadData()

        val addNoteButton: Button = findViewById(R.id.mainButtonAddNote)

        addNoteButton.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    private fun loadData() {

        mRecyclerView = findViewById(R.id.mainRecyclerView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Todos").child(mUid)

        val mQuery = reference.orderByKey()

        val mOptions = FirebaseRecyclerOptions.Builder<Todo>()
            .setQuery(mQuery, Todo::class.java)
            .setLifecycleOwner(this)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<Todo, TodoViewHolder>(mOptions) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.todo_listitem, parent, false)

                return TodoViewHolder(view)
            }

            override fun getItem(position: Int): Todo {
                return super.getItem(position)
            }

            override fun onBindViewHolder(viewHolder: TodoViewHolder, position: Int, model: Todo) {
                viewHolder.setModel(model)
            }
        }
        mRecyclerView.adapter = mAdapter

        //pass data to detail activity.
//        val i: Intent = Intent(this@MainActivity, NoteDetailActivity::class.java)
//        var bundle = Bundle()
//        bundle.putString("title", title)
//        bundle.putString("desc", itemDesc)
//        bundle.putString("pushId", pushId)
//        bundle.putString("completed", completed)
//        i.putExtras(bundle)
//        startActivity(i)

    }

    private fun checkUserData() {
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("userData")
        ref.child(mUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.i("Main", "User data retrieval error : " + error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //user data exists.
                    loadData()
                } else
                    Toast.makeText(this@MainActivity, "User data not found", Toast.LENGTH_SHORT).show()

            }
        })
    }

}
