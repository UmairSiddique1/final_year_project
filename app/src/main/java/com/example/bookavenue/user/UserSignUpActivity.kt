package com.example.bookavenue.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookavenue.Models.UserLoginModel
import com.example.bookavenue.databinding.ActivityUserSignUpBinding
import com.example.bookavenue.serviceProvider.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.loginAsSpTV.setOnClickListener {
            startActivity(Intent(applicationContext,MainActivity::class.java))
        }
        binding.userSignupTV.setOnClickListener {
        startActivity(Intent(applicationContext,UserSigninActivity::class.java))
        }

        binding.userSignupBtn.setOnClickListener {
            val userEmail:String=binding.userSignUpETEmail.text.toString()
            val userPass:String=binding.userSignUpETPass.text.toString()
            val userName:String=binding.userSignUpETName.text.toString()
//            val getUid:String=auth.currentUser?.uid.toString()

            auth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                       Toast.makeText(applicationContext,"Successful",Toast.LENGTH_SHORT).show()
                        val data=UserLoginModel(userName,userEmail,userPass, auth.currentUser?.uid.toString())
                        database.child("Users").child( auth.currentUser?.uid.toString()).setValue(data)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext,"Failure occur",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
