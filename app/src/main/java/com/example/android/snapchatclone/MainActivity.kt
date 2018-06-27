package com.example.android.snapchatclone

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (mAuth.currentUser != null) {
            login()
        }
    }

    fun goClicked(view: View) {
        Log.i("goClicked", "Yes");

        var email: String = emailEditText?.text.toString()
        var password: String = passwordEditText?.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        login()
                        Toast.makeText(this, "Login is Successfull", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.result.user.uid).child("email").setValue(email)
                                Toast.makeText(this, "Sign Up Successfull", Toast.LENGTH_SHORT).show()
                                login()
                            } else {
                                Toast.makeText(this, "Log in failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    // ...
                }
    }

    fun login() {
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}
