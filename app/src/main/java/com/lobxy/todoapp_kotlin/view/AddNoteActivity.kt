package com.lobxy.todoapp_kotlin.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lobxy.todoapp_kotlin.R
import com.lobxy.todoapp_kotlin.model.Todo

class AddNoteActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mReference: DatabaseReference

    private var desc: String = ""
    private var title: String = ""

    private lateinit var submitButton: ImageView
    private lateinit var cancelButton: ImageView

    private lateinit var mProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        mAuth = FirebaseAuth.getInstance()
        mReference = FirebaseDatabase.getInstance().getReference("Todos")

        mProgressBar = findViewById(R.id.addNoteProgressBar)

        cancelButton = findViewById(R.id.addNoteImageCancel)
        submitButton = findViewById(R.id.addNoteImageSubmit)

        val editTitle = findViewById<EditText>(R.id.addNoteEditTitle)
        val editDesc = findViewById<EditText>(R.id.addNoteEditDescription)

        submitButton.setOnClickListener {
            desc = editDesc.text.trim().toString()
            title = editTitle.text.trim().toString()

            if (title.isBlank() || desc.isEmpty())
                Toast.makeText(
                    this, "Fields empty", Toast.LENGTH_SHORT
                ).show()
            else
                when {
                    title.isBlank() -> Toast.makeText(this, "Title Empty", Toast.LENGTH_SHORT).show()
                    desc.isEmpty() -> Toast.makeText(this, "Description Empty", Toast.LENGTH_SHORT).show()
                    else -> submitData()
                }
        }

        cancelButton.setOnClickListener {
            finish()
        }

    }

    private fun submitData() {
        submitButton.isEnabled = false
        cancelButton.isEnabled = false

        mProgressBar.visibility = View.VISIBLE

        val pushId: String = mReference.push().key.toString()
        val todo = Todo(title, desc, pushId, false)

        mReference.child(mAuth.currentUser!!.uid).child(pushId).setValue(todo).addOnCompleteListener { task ->
            mProgressBar.visibility = View.INVISIBLE
            submitButton.isEnabled = true
            cancelButton.isEnabled = true

            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.i("Auth", "data submission error: " + task.exception!!.message)
            }
        }
    }
}
