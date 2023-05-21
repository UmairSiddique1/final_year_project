package com.example.bookavenue

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookavenue.databinding.ActivityFirebaseUiBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class FirebaseUIActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    var providers: List<IdpConfig> = listOf( // below is the line for adding
        // email and password authentication.
        EmailBuilder().build(),  // below line is used for adding google
        // authentication builder in our app.
        GoogleBuilder().build(),  // below line is used for adding phone
        // authentication builder in our app.
        PhoneBuilder().build())
    private lateinit var binding:ActivityFirebaseUiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFirebaseUiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mFirebaseAuth=FirebaseAuth.getInstance()

        // for our Firebase authentication.
        // for our Firebase authentication.
        mAuthStateListener = AuthStateListener { firebaseAuth ->
            // we are calling method for on authentication state changed.
            // below line is used for getting current user which is
            // authenticated previously.
            val user = firebaseAuth.currentUser

            // checking if the user
            // is null or not.
            if (user != null) {
                // if the user is already authenticated then we will
                // redirect our user to next screen which is our home screen.
                // we are redirecting to new screen via an intent.
                val i = Intent(applicationContext, UserHomeActivity::class.java)
                startActivity(i)
                // we are calling finish method to kill or
                // mainactivity which is displaying our login ui.
                finish()
            } else {
                // this method is called when our
                // user is not authenticated previously.
                startActivityForResult( // below line is used for getting
                    // our authentication instance.
                    AuthUI.getInstance() // below line is used to
                        // create our sign in intent
                        .createSignInIntentBuilder() // below line is used for adding smart
                        // lock for our authentication.
                        // smart lock is used to check if the user
                        // is authentication through different devices.
                        // currently we are disabling it.
                        .setIsSmartLockEnabled(false) // we are adding different login providers which
                        // we have mentioned above in our list.
                        // we can add more providers according to our
                        // requirement which are available in firebase.
                        .setAvailableProviders(providers) // below line is for customizing our theme for
                        // login screen and set logo method is used for
                        // adding logo for our login page.
                        .setLogo(R.drawable.ic_dialog_alert)
                        .setTheme(R.style.Theme) // after setting our theme and logo
                        // we are calling a build() method
                        // to build our login screen.
                        .build(),  // and lastly we are passing our const
                    // integer which is declared above.
                    1
                )
            }
        }
    }
    override fun onResume() {
        super.onResume()
        // we are calling our auth
        // listener method on app resume.
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        // here we are calling remove auth
        // listener method on stop.
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }
}