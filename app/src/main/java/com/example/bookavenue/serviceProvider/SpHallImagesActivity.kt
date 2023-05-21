package com.example.bookavenue.serviceProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookavenue.Models.SpImageModel
import com.example.bookavenue.SpImageAdapter
import com.example.bookavenue.databinding.ActivitySpHallImagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SpHallImagesActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySpHallImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySpHallImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager=GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        val imgList:MutableList<SpImageModel> =ArrayList()
        val spImageAdapter=SpImageAdapter(this,applicationContext,imgList)
        binding.recyclerView.adapter=spImageAdapter
        FirebaseDatabase.getInstance().getReference("hall data").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("photoUrl").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    imgList.clear()
                  for(dataSnapshot in snapshot.children){
                  imgList.add(SpImageModel(dataSnapshot.value.toString()))
                  }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext,error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}