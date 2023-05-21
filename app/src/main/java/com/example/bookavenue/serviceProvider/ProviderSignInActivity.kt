package com.example.bookavenue.serviceProvider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityProviderSignInBinding
import com.example.bookavenue.databinding.ActivitySpChat2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProviderSignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProviderSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.providerSignInTV.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        binding.providerSignInBtn.setOnClickListener {
            val signInEmail: String = binding.providerSignInEmailET.text.toString()
            val signInPass: String = binding.providerSignInPasswordET.text.toString()
            auth.signInWithEmailAndPassword(signInEmail, signInPass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext,ServiceProviderInterface::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext, "Failure occur", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(applicationContext, ServiceProviderInterface::class.java))
        }
    }
}