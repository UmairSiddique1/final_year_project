package com.example.bookavenue.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookavenue.UserHomeActivity
import com.example.bookavenue.databinding.ActivityUserSigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserSigninActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityUserSigninBinding
    private lateinit var auth: FirebaseAuth
//private lateinit var  mGoogleSignInClient:GoogleSignInClient
    val RC_SIGN_IN = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
binding.userSigninTV.setOnClickListener {
 startActivity(Intent(applicationContext,UserSignUpActivity::class.java))
}

        binding.userSigninBtn.setOnClickListener {
        val userEmail:String=binding.userSigninEmailET.text.toString()
            val userPass:String=binding.userSigninPassET.text.toString()
            auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                   Toast.makeText(applicationContext,"Successful",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext,UserHomeActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext,"Failure occur",Toast.LENGTH_SHORT).show()
                    }
                }
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
             mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        }
        binding.userGoogleBtn.setOnClickListener {
          signIn()
        }
    }

    override fun onStart() {
        super.onStart()

            // Check if user is signed in (non-null) and update UI accordingly.
            val currentUser = auth.currentUser
            if(currentUser != null){
               startActivity(Intent(applicationContext,UserHomeActivity::class.java))
            }
    }
    private fun signIn() {
        val signInIntent1 = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent1, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, Update the UI here
            Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(applicationContext, "Sign in failed: "+ e.getStatusCode(), Toast.LENGTH_SHORT).show()
        }
    }

        }