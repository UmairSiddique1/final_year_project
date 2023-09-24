package com.example.bookavenue.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bookavenue.FirebaseUIActivity
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityUserSigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserSigninActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityUserSigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.userSigninTV.setOnClickListener {
            startActivity(Intent(applicationContext, UserSignUpActivity::class.java))
        }



        binding.userSigninBtn.setOnClickListener {
            val userEmail: String = binding.userSigninEmailET.text.toString()
            val userPass: String = binding.userSigninPassET.text.toString()
            auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, UserHomeActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext, "Failure occur", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

       binding.btnLogin.setOnClickListener {
           startActivity(Intent(applicationContext,FirebaseUIActivity::class.java))
       }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(applicationContext, UserHomeActivity::class.java))
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
       launcher.launch(signInIntent)
    }
private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
    result->
    if(result.resultCode==Activity.RESULT_OK){
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task)
    }
}

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        if(completedTask.isSuccessful){
val account:GoogleSignInAccount?=completedTask.result
            if(account!=null){
                updateUI(account)
            }
        }
        else{

        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
startActivity(Intent(applicationContext, UserHomeActivity::class.java))
                Toast.makeText(applicationContext,"Successful",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}