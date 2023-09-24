package com.example.bookavenue.serviceProvider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.UserLoginModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityProfileImgBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SpProfileImgActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileImgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileImgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        FirebaseDatabase.getInstance().getReference("Service provider").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
             val data=snapshot.getValue(UserLoginModel::class.java)
                    if (data != null) {
                        Glide.with(applicationContext).load(data.profileimg).placeholder(R.drawable.ic_profile).into(binding.imageView2)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}