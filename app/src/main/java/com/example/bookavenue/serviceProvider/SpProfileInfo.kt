package com.example.bookavenue.serviceProvider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.UserInterfaceModel
import com.example.bookavenue.Models.UserLoginModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivitySpProfileInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SpProfileInfo : AppCompatActivity() {
    private lateinit var binding:ActivitySpProfileInfoBinding
    private lateinit var image: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySpProfileInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedItemId = intent.getIntExtra("SELECTED_ITEM_ID", R.id.profileinfo)

        // Set the selected item in the bottom navigation bar
        binding.navBottom.selectedItemId = selectedItemId
        binding.cardViewImages.setOnClickListener {
            startActivity(Intent(applicationContext,SpHallImagesActivity::class.java))
        }
        binding.navBottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.chat->{
                    val intent = Intent(applicationContext,SpChatActivity::class.java)
                    intent.putExtra("SELECTED_ITEM_ID", R.id.chat)
                    startActivity(intent)
                    true
                }
                R.id.profileinfo->{
                    val intent = Intent(applicationContext,SpProfileInfo::class.java)
                    intent.putExtra("SELECTED_ITEM_ID", R.id.profileinfo)
                    startActivity(intent)
                    true
                }
                R.id.form->{
                    val intent = Intent(applicationContext,ServiceProviderInterface::class.java)
                    intent.putExtra("SELECTED_ITEM_ID", R.id.form)
                    startActivity(intent)
                    true
                }
                else -> {
//                    startActivity(Intent(applicationContext,ServiceProviderInterface::class.java))
//                    binding.navBottom.selectedItemId=R.id.form
                    false
                }
            }
        }

  FirebaseDatabase.getInstance().getReference("Service provider").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object:ValueEventListener{
      override fun onDataChange(snapshot: DataSnapshot) {
         val data=snapshot.getValue(UserLoginModel::class.java)
          binding.tvSpProfile.text= data!!.name+"\n"+data!!.email
image=data.profileimg.toString()
          Glide.with(applicationContext).load(image).placeholder(R.drawable.ic_profile).into(binding.ivSpProfile)
      }

      override fun onCancelled(error: DatabaseError) {
          Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
      }
  })

        FirebaseDatabase.getInstance().getReference("hall data").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val hallData=snapshot.getValue(UserInterfaceModel::class.java)
                if (hallData != null) {
                    binding.tvProfileHallName.text=hallData.hallName.toString()
                    binding.tvProfileCityName.text=hallData.location.toString()
                    binding.tvProfileContactNo.text=hallData.contactNo
                    binding.tvProfileAddress.text=hallData.address
                    binding.tvProfileMenu.text=hallData.menu
                    binding.tvProfilePerHead.text=hallData.perHeadCharges
                }
            }
            override fun onCancelled(error: DatabaseError) {
Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })



        binding.ivSpProfile.setOnLongClickListener {
            val intent=Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,1)
            return@setOnLongClickListener true
        }
        binding.ivSpProfile.setOnClickListener{
            val intent=Intent(applicationContext, SpProfileImgActivity::class.java)
            intent.putExtra("image",image)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            if(data!=null){
                val data1=data.data
                val storage=Firebase.storage
                val storageRef=storage.reference.child("spprofileimg").child(FirebaseAuth.getInstance().currentUser!!.uid)
                if (data1 != null) {
                   val uploadTask= storageRef.putFile(data1)
                uploadTask.continueWithTask {task->
                      if(!task.isSuccessful){
                          task.exception?.let {
                              throw it
                          }
                      }
                      storageRef.downloadUrl}
                      .addOnCompleteListener { task ->
                          if (task.isSuccessful) {
                              val downloadUri = task.result
                              FirebaseDatabase.getInstance().getReference("Service provider").child(FirebaseAuth.getInstance().currentUser!!.uid)
                                  .child("profileimg").setValue(downloadUri.toString())
                          } else {
                              // Handle failures
                              // ...
                          }
                  }

                }

            }
        }
    }
}