package com.example.bookavenue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.bookavenue.user.UserSignUpActivity

class SplashScreenActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // Simulate some processing time
            // You can replace this with your actual initialization tasks
            // Start the main activity after the splash time out
            startActivity(Intent(this, UserSignUpActivity::class.java))
            finish() // Finish the splash activity
        }, splashTimeOut)

        // Start the secondary thread
        Thread(Runnable {
            // Perform any additional tasks on the secondary thread
        }).start()
    }
}