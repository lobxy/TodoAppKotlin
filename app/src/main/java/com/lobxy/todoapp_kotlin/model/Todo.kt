package com.lobxy.todoapp_kotlin.model

class Todo {
    constructor()

    var title: String = ""
    var desc: String = ""
    var pushId: String = ""
    var completed: Boolean = true

    constructor(title: String, desc: String, pushId: String, completed: Boolean) {
        this.title = title
        this.desc = desc
        this.pushId = pushId
        this.completed = completed
    }

}