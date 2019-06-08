package com.lobxy.todoapp_kotlin.controller

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.lobxy.todoapp_kotlin.R
import com.lobxy.todoapp_kotlin.model.Todo

class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val todoTitle: TextView
    private val todoDesc: TextView

    init {
        todoTitle = itemView.findViewById(R.id.itemTitle) as TextView
        todoDesc = itemView.findViewById(R.id.itemDesc) as TextView
    }

    fun setModel(model: Todo?) {
        if (model == null || model!!.title == null) return
        itemView.tag = model.pushId
        todoTitle.text = model.title
        todoDesc.text = model.desc
    }
}