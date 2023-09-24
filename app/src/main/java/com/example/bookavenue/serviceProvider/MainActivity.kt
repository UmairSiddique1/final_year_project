package com.example.bookavenue.serviceProvider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bookavenue.Models.UserLoginModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityMainBinding
import com.example.bookavenue.user.UserHomeActivity
import com.example.bookavenue.user.UserSigninActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
       setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference
       binding.providerSignupTV.setOnClickListener {
startActivity(Intent(applicationContext, ProviderSignInActivity::class.java))
        }
        val providerSignUpBtn:Button=findViewById(R.id.providerSignupBtn)
        providerSignUpBtn.setOnClickListener {
            val signUpName:String=binding.providerSignUpNameET.text.toString()
            val signUpEmail:String=binding.providerSignUpEmailET.text.toString()
            val signUpPass:String=binding.providerSignUpPassET.text.toString()
//            val getUid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
            auth.createUserWithEmailAndPassword(signUpEmail,signUpPass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext,"Successful",Toast.LENGTH_SHORT).show()
                        val userLogin=UserLoginModel(signUpName,signUpEmail,signUpPass,FirebaseAuth.getInstance().currentUser?.uid.toString())
                        database.child("Service provider").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).setValue(userLogin)

                        startActivity(Intent(applicationContext,ServiceProviderInterface::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext,"Failure occur",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.tvSp.setOnClickListener {startActivity(Intent(applicationContext,UserSigninActivity::class.java))}

    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(applicationContext,ServiceProviderInterface::class.java))
        }
    }
}