package com.example.bookavenue

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.bookavenue.databinding.ActivityHallDetailBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HallDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHallDetailBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHallDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.getReference("hall data")
        val intent= intent
        val hallName=intent.getStringExtra("hallName")
        val getUid=intent.getStringExtra("getUid")
        val getContactNo=intent.getStringExtra("contact")
         binding.tvHallName.text=hallName+" "+getUid
        binding.tvContactNo.text=getContactNo
        binding.tvLocation.text=intent.getStringExtra("location")
        binding.tvPerHeadCharges.text=intent.getStringExtra("perHead")
        binding.tvMenu.text=intent.getStringExtra("menu")
binding.tvAddress.text=intent.getStringExtra("address")
        binding.btnCall.setOnClickListener {
            dialPhoneNumber(getContactNo.toString())
        }
        // GOOGLE MAP ACTIVITY
        binding.btnGooglemap.setOnClickListener {
            val intent1=Intent(applicationContext,GoogleMap_Activity::class.java)
            intent1.putExtra("address",binding.tvAddress.text.toString())
           startActivity(intent1)
        }

// CHAT ACTIVITY
        binding.btnChat.setOnClickListener {
            val intent1=Intent(applicationContext,ChatsActivity::class.java)
            intent1.putExtra("hallName",hallName)
            intent1.putExtra("getUid",getUid)
            startActivity(intent1)
        }
        val imageArray:MutableList<SlideModel> =ArrayList()
database.child(getUid.toString()).child("photoUrl").addValueEventListener(object :ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
       imageArray.clear()
        for(dataSnapshot:DataSnapshot in snapshot.children){
            imageArray.add(SlideModel(dataSnapshot.value.toString()))
        }
        binding.imageslider.setImageList(imageArray,ScaleTypes.CENTER_CROP)
    }

    override fun onCancelled(error: DatabaseError)=Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_SHORT).show()

})
    }
    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivity(intent)
        }
    }
}