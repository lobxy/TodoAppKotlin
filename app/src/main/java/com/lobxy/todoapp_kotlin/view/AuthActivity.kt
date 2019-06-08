package com.lobxy.todoapp_kotlin.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lobxy.todoapp_kotlin.R
import com.lobxy.todoapp_kotlin.model.User

class AuthActivity : AppCompatActivity() {

    private var loginMode: Boolean = true

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText

    private var mEmail: String = ""
    private var mName: String = ""
    private var mPassword: String = ""

    private lateinit var mAuth: FirebaseAuth

    private lateinit var mRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().getReference("userData")

        editName = findViewById(R.id.authName)
        editEmail = findViewById(R.id.authEmail)
        editPassword = findViewById(R.id.authPassword)

        val submitButton = findViewById<Button>(R.id.authButtonSubmit)
        val loginButton = findViewById<Button>(R.id.authButtonLogin)
        val registerButton = findViewById<Button>(R.id.authButtonRegister)

        loginButton.setOnClickListener {
            loginMode = true
            editName.visibility = View.INVISIBLE
        }
        registerButton.setOnClickListener {
            loginMode = false
            editName.visibility = View.VISIBLE
        }

        submitButton.setOnClickListener {
            validate()
        }

    }

    private fun validate() {
        mName = editName.text.trim().toString()
        mEmail = editEmail.text.trim().toString()
        mPassword = editPassword.text.trim().toString()

        if (mEmail.isEmpty())
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        else if (mPassword.isEmpty())
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show()
        else if (mPassword.length < 6)
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
        else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches())
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
        else {

            if (loginMode) {
                //login user
                mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.i("Auth", "Login Success")
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Log.i("Auth", "Login failed: " + task.exception)
                        }
                    }

            } else {
                if (mName.isEmpty())
                    Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()
                else {
                    //register User
                    mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.i("Auth", "Register Success")
                                uploadUserData(task.result!!.user.uid)
                            } else {
                                Log.i("Auth", "Register failed: " + task.exception)
                            }
                        }
                }

            }

        }

    }

    private fun uploadUserData(uid: String) {
        //save user data here.

        val user = User(mName, mEmail, uid, mPassword)

        mRef.child(uid).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.i("Auth", "data submission error: " + task.exception!!.message)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}
