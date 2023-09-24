package com.example.bookavenue.user

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.bookavenue.ChatsActivity
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityFavHallDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavHallDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFavHallDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFavHallDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent=intent
        binding.tvHallName.text=intent.getStringExtra("hallName")
        binding.tvLocation.text=intent.getStringExtra("cityName")
        binding.tvAddress.text=intent.getStringExtra("address")
        binding.tvPerHeadCharges.text=intent.getStringExtra("perHeadCharges")
        binding.tvMenu.text=intent.getStringExtra("menu")
        binding.tvContactNo.text=intent.getStringExtra("contactNo")
        binding.btnGooglemap.setOnClickListener {
            val intent4=Intent(applicationContext, GoogleMap_Activity::class.java)
            intent4.putExtra("address",binding.tvAddress.text.toString())
            startActivity(intent4)
        }
        val imageArray:MutableList<SlideModel> =ArrayList()
        FirebaseDatabase.getInstance().getReference("hall data").child(intent.getStringExtra("uid").toString())
            .child("photoUrl").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    imageArray.clear()
                    for(dataSnapshot:DataSnapshot in snapshot.children){
                        imageArray.add(SlideModel(dataSnapshot.value.toString()))
                    }
                    binding.imageslider.setImageList(imageArray, ScaleTypes.CENTER_CROP)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}