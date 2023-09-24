package com.example.bookavenue.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityEditNameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class EditNameActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEditName.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val newDisplayName = binding.etName.text.toString()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplayName)
                .build()
            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                   Toast.makeText(applicationContext,"Successfully Updated",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}