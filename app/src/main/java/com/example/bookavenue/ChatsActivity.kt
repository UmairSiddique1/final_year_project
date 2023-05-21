package com.example.bookavenue

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookavenue.Adapters.ChatAdapter
import com.example.bookavenue.Models.MessageModel
import com.example.bookavenue.databinding.ActivityChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ChatsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatsBinding
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val intent=intent
        val hallName=intent.getStringExtra("hallName")
        val receiverUid=intent.getStringExtra("getUid")
        val senderUid=FirebaseAuth.getInstance().uid
        senderRoom=senderUid+receiverUid
        receiverRoom=receiverUid+senderUid
        binding.tvChatHallName.text=hallName


        val messageList:MutableList<MessageModel> =ArrayList()
//        val chatsAdapter=ChatsAdapter(applicationContext,messageList)
        val chatsAdapter=ChatAdapter(applicationContext,messageList,"","")
        binding.rvMessages.layoutManager=LinearLayoutManager(this)
        binding.rvMessages.adapter=chatsAdapter

val firebaseDatabase=FirebaseDatabase.getInstance()
firebaseDatabase.reference.child("chats").child(senderRoom)
    .child("messages").addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            messageList.clear()
            for(dataSnapshot:DataSnapshot in snapshot.children){
                val msg = dataSnapshot.getValue(MessageModel::class.java)
                if (msg != null) {
                    messageList.add(msg)
                }
            }
            chatsAdapter.notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
Toast.makeText(applicationContext,"Failed to add msg",Toast.LENGTH_LONG).show()
        }
    })

       binding.btnSend.setOnClickListener {
    val message:String=binding.messEt.text.toString()
    val date= Date()
    val messageModel=MessageModel(message,senderUid,date.time)
           binding.messEt.text=null
firebaseDatabase.reference.child("chats").child(senderRoom).child("messages").push()
    .setValue(messageModel)
    .addOnSuccessListener {
        Toast.makeText(applicationContext,"Successful 1",Toast.LENGTH_LONG).show()
    firebaseDatabase.reference.child("chats").child(receiverRoom).child("messages").push()
        .setValue(messageModel)
        .addOnSuccessListener {
            Toast.makeText(applicationContext,"Successful 2",Toast.LENGTH_LONG).show()
        }
}.addOnFailureListener{
Toast.makeText(applicationContext,"Failure occur receiver",Toast.LENGTH_LONG).show()
    }
}
    }
}