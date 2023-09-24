package com.example.bookavenue.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityEditEmailBinding
import com.google.firebase.auth.FirebaseAuth

class EditEmailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEdit.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val newEmailAddress =binding.etEmail.text.toString()
            user?.updateEmail(newEmailAddress)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,"Successful",Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(applicationContext, task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}