package com.example.bookavenue.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.UserLoginModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityUserProfileImgBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileImgActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserProfileImgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserProfileImgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
    .addValueEventListener(object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
     val data=snapshot.getValue(UserLoginModel::class.java)
            if (data != null) {
                Glide.with(applicationContext).load(data.profileimg).placeholder(R.drawable.ic_profile).into(binding.imageView)
            }
        }

        override fun onCancelled(error: DatabaseError) {
           Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
        }
    })
    }
}