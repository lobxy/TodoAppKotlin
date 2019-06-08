package com.lobxy.todoapp_kotlin.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.lobxy.todoapp_kotlin.R

class NoteDetailActivity : AppCompatActivity() {

    lateinit var textTitle: TextView
    lateinit var textDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        textTitle = findViewById(R.id.noteDetailTitle)
        textDesc = findViewById(R.id.noteDetailDesc)

        //todo: get data from intent and show them here.

        getData()
    }

    private fun getData() {
        val bundle = intent.extras
        if (bundle != null) {
            val title = bundle.getString("title")
            val desc = bundle.getString("desc")
            val pushId = bundle.getString("pushId")

            bundle.getString("completed")
        } else {
            Log.i("NoteDetail", "Bundle error")
        }

    }
}
