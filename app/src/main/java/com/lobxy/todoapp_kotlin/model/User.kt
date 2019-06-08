package com.lobxy.todoapp_kotlin.model

class User {
    constructor()

    var name: String = ""
    var email: String = ""
    var uid: String = ""
    var password: String = ""

    constructor(name: String, email: String, uid: String, password: String) {
        this.name = name
        this.email = email
        this.uid = uid
        this.password = password
    }

}