package com.example.bookavenue.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookavenue.Adapters.SpUsersAdapter
import com.example.bookavenue.Models.SpUserModel
import com.example.bookavenue.databinding.ActivityUserChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView2.layoutManager=LinearLayoutManager(this)
        val list:MutableList<SpUserModel> =ArrayList()
        val spUsersAdapter=SpUsersAdapter(applicationContext,list)
        binding.recyclerView2.adapter=spUsersAdapter

        FirebaseDatabase.getInstance().getReference("Service provider").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
       for(dataSnapshot in snapshot.children){
           val users=dataSnapshot.getValue(SpUserModel::class.java)
           if (users != null) {
               list.add(users)
           }
           spUsersAdapter.notifyDataSetChanged()
       }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}