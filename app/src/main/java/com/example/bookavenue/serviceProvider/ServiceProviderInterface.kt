package com.example.bookavenue.serviceProvider

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.bookavenue.Models.UserInterfaceModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityServiceProviderInterfaceBinding
import com.example.bookavenue.user.UserSigninActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class ServiceProviderInterface() : AppCompatActivity() {
    private lateinit var binding: ActivityServiceProviderInterfaceBinding
    private lateinit var database: DatabaseReference
    lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceProviderInterfaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedItemId = intent.getIntExtra("SELECTED_ITEM_ID", R.id.form)

        // Set the selected item in the bottom navigation bar
        binding.navBottom.selectedItemId = selectedItemId
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
            false
        }
    }
}

        database = Firebase.database.reference
        storage = FirebaseStorage.getInstance()

        binding.serviceImgBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }

        binding.serviceProviderInterfaceBtn.setOnClickListener {
            val hallName: String = binding.etHallName.text.toString()
            val location: String = binding.etHallLocation.text.toString()
            val contactNo: String = binding.etContactNo.text.toString()
            val perHead: String = binding.etPerHeadCharges.text.toString()
            val menu: String = binding.etMenu.text.toString()
            val address: String = binding.etAddress.text.toString()
            val getUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

            val user = UserInterfaceModel(hallName,menu, location, address, contactNo, perHead, getUid, 1f)
            database.child("hall data").child(getUid).setValue(user)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sign_out,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.signOut -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, ProviderSignInActivity::class.java))
            }
            R.id.chat->{
                startActivity(Intent(applicationContext,SpChatActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @Deprecated("Deprecated in Java", ReplaceWith("super.onActivityResult(requestCode, resultCode, data)",
        "androidx.appcompat.app.AppCompatActivity"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImgArray:MutableList<Uri> =ArrayList()
    if(data?.clipData !=null){
val count:Int= data.clipData!!.itemCount
        for(i in 0 until count){
            val imageUri:Uri= data.clipData!!.getItemAt(i).uri
            selectedImgArray.add(imageUri)
uploadImagesToFirebase(selectedImgArray as ArrayList<Uri>)

        }
    }

    }
//    private fun uploadImagesToFirebase(imagesUriList: ArrayList<Uri>) {
//        val images:kotlin.collections.MutableList<Uri> =kotlin.collections.ArrayList()
//        val databaseRef = FirebaseDatabase.getInstance().getReference("hall data").child(FirebaseAuth.getInstance().uid.toString())
//        for (imageUri in imagesUriList) {
//            imagesUriList.clear()
//            val imageName = UUID.randomUUID().toString()
//            val imageRef = storage.reference.child(FirebaseAuth.getInstance().uid.toString()).child("images/$imageName.jpg")
//            imageRef.putFile(imageUri)
//                .addOnSuccessListener {
//                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//                        Toast.makeText(applicationContext,"Successfully completed",Toast.LENGTH_SHORT).show()
////                                            val photoUrl=downloadUrl.toString()
//                        images.add(downloadUrl)
//                        for(i in 0 until images.size){
//                            databaseRef.child("photoUrl").child("img $i").setValue(downloadUrl.toString())
//                        }
//                    }
//                }
//                .addOnFailureListener {
//                    Toast.makeText(applicationContext,"Failure occur",Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
private fun uploadImagesToFirebase(imagesUriList: ArrayList<Uri>) {
    val databaseRef = FirebaseDatabase.getInstance().getReference("hall data").child(FirebaseAuth.getInstance().uid.toString())
    for ((index, imageUri) in imagesUriList.withIndex()) {
        val imageName = UUID.randomUUID().toString()
        val imageRef = storage.reference.child(FirebaseAuth.getInstance().uid.toString()).child("images/$imageName.jpg")
        imageRef.putFile(imageUri)
            .addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                    Toast.makeText(applicationContext, "Successfully completed", Toast.LENGTH_SHORT).show()
                    val imageUrl = downloadUrl.toString()
                    databaseRef.child("photoUrl").child("img$index").setValue(imageUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failure occurred", Toast.LENGTH_SHORT).show()
            }
    }
}

}