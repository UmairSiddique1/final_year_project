package com.example.bookavenue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.bookavenue.databinding.ActivityHallImgBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HallImgActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHallImgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHallImgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val intent= intent
        val uid=intent.getStringExtra("uid")
        val imageArray:MutableList<SlideModel> =ArrayList()
        FirebaseDatabase.getInstance().getReference("hall data").child(uid.toString())
            .child("photoUrl").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    imageArray.clear()
                for(dataSnapshot in snapshot.children){
                    imageArray.add(SlideModel(dataSnapshot.value.toString()))
                }
                    binding.imageview.setImageList(imageArray,ScaleTypes.CENTER_CROP)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}