package com.example.bookavenue.serviceProvider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bookavenue.Adapters.ChatAdapter
import com.example.bookavenue.Models.MessageModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivitySpChat2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class SpChatActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivitySpChat2Binding
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    var database:FirebaseDatabase?=null
    var adapter: ChatAdapter?=null
    var message:kotlin.collections.ArrayList<MessageModel>?=null
    var storage: FirebaseStorage?=null
    var senderUid:String?=null
    var receiverUid:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpChat2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
database= FirebaseDatabase.getInstance()
        storage= FirebaseStorage.getInstance()
        val intent = intent
        binding.tvSpChatHallName.text = intent.getStringExtra("name")
         senderUid = FirebaseAuth.getInstance().uid
        receiverUid = intent.getStringExtra("getUid")
        val profileimg=intent.getStringExtra("profileimg")
        Glide.with(applicationContext).load(profileimg).placeholder(R.drawable.ic_profile).into(binding.spProfileImage)
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        message=ArrayList()
adapter= ChatAdapter(applicationContext, message!!,senderRoom,receiverRoom)
binding.rvSpMessages.layoutManager=LinearLayoutManager(this)
        binding.rvSpMessages.adapter=adapter

        database!!.reference.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   message!!.clear()
                    for(snapShot1 in snapshot.children){
                         val data=snapShot1.getValue(MessageModel::class.java)
                        data!!.messageId=snapShot1.key
                        message!!.add(data)
                    }
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        binding.btnSpSend.setOnClickListener{
            val messageText:String=binding.spMessEt.text.toString()
            val date=Date()
            val message=MessageModel(messageText,senderUid,date.time)
            binding.spMessEt.setText("")
            val randomKey= database!!.reference.push().key
            val lastMsgObj=HashMap<String,Any>()
            lastMsgObj["lastMsg"]=message.message!!
            lastMsgObj["lastMsgTime"]=date.time

            database!!.reference.child("chats").child(senderRoom).updateChildren(lastMsgObj)
            database!!.reference.child("chats").child(receiverRoom).updateChildren(lastMsgObj)
            database!!.reference.child("chats").child(senderRoom).child("messages").child(randomKey.toString())
                .setValue(message).addOnSuccessListener {
                    database!!.reference.child("chats").child(receiverRoom).child("messages").child(randomKey.toString())
                        .setValue(message)
                }
        }
        binding.btnSpSend1.setOnClickListener{
            val intent1=Intent()
            intent1.action=Intent.ACTION_GET_CONTENT
            intent1.type="image/*"
            startActivityForResult(intent1,25)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==25){
            if (data != null) {
                if(data.data!=null){
val selectedImage=data.data
                    val calender=Calendar.getInstance()
                    val reference= storage!!.reference.child("chats").child(calender.timeInMillis.toString().toString()+"")
               reference.putFile(selectedImage!!).addOnCompleteListener{task->
                   if(task.isSuccessful){
                       reference.downloadUrl.addOnSuccessListener {uri->
val filePath=uri.toString()
                           val messageText=binding.spMessEt.text.toString()
                           val date=Date()
                           val message=MessageModel(messageText,senderUid,date.time)
                           message.message="photo"
                           message.imageUrls=filePath
                           binding.spMessEt.setText("")
                           val randomKey= database!!.reference.push().key
                           val lastMsgObj=HashMap<String,Any>()
                           lastMsgObj["lastMsg"]=message.message!!
                           lastMsgObj["lastMsgTime"]=date.time
                           database!!.reference.child("chats").child(senderRoom)
                               .updateChildren(lastMsgObj)
                           database!!.reference.child("chats").child(receiverRoom)
                               .updateChildren(lastMsgObj)
                           database!!.reference.child("chats").child(senderRoom)
                               .child("messages").child(randomKey!!).setValue(message)
                           database!!.reference.child("chats").child(receiverRoom)
                               .child("messages").child(randomKey).setValue(message)
                       }
                   }
               }
                }
            }
        }
    }
}
