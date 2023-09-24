package com.example.bookavenue.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.SpUserModel
import com.example.bookavenue.Models.UserLoginModel
import com.example.bookavenue.R
import com.example.bookavenue.serviceProvider.SpChatActivity2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SpUsersAdapter(val context: Context, private val spUserModel: List<SpUserModel>): RecyclerView.Adapter<SpUsersAdapter.ViewHolder>() {
    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
val ivUsers:ImageView=view.findViewById(R.id.iv_sp_users)
        val tvUsers:TextView=view.findViewById(R.id.tv_sp_userName)
        val tvLastMsg:TextView=view.findViewById(R.id.tv_spLastMsg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.layout_sp_users,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val spUser=spUserModel[position]
        val chatRef = FirebaseDatabase.getInstance().getReference("chats")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val chatId = currentUserId + spUser.uid
        val messagesRef = chatRef.child(chatId).child("messages")
        val lastMessageQuery = messagesRef.orderByKey().limitToLast(1)
        lastMessageQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (messageSnapshot in dataSnapshot.children) {
                        val lastMessage = messageSnapshot.child("message").value.toString()
                        // Pass the last message to your adapter or update the UI
                        holder.tvLastMsg.text=lastMessage
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that may occur
            }
        })

//       Glide.with(context).load(spUser.profileimg).placeholder(R.drawable.ic_profile).into(holder.ivUsers)

        holder.tvUsers.text=spUser.name
        Glide.with(context).load(spUser.profileimg).placeholder(R.drawable.ic_profile).into(holder.ivUsers)
        holder.itemView.setOnClickListener {
            val intent=Intent(context, SpChatActivity2::class.java)
            intent.putExtra("getUid",spUser.uid)
            intent.putExtra("name",spUser.name)
            intent.putExtra("profileimg",spUser.profileimg)
           context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
      return spUserModel.size
    }

}
