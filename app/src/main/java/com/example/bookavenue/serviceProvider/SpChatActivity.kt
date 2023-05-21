package com.example.bookavenue.serviceProvider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookavenue.Adapters.SpUsersAdapter
import com.example.bookavenue.Models.SpUserModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivitySpChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SpChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpChatBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivitySpChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val selectedItemId = intent.getIntExtra("SELECTED_ITEM_ID", R.id.chat)

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
//                    startActivity(Intent(applicationContext,ServiceProviderInterface::class.java))
                    false
                }
            }
        }

        database = Firebase.database.reference
        binding.rvSpUser.layoutManager=LinearLayoutManager(this)
val spUserModel1:MutableList<SpUserModel> =ArrayList()
        val spUsersAdapter=SpUsersAdapter(applicationContext,spUserModel1)
        binding.rvSpUser.adapter=spUsersAdapter
        database.child("Users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                spUserModel1.clear()
                for(dataSnapshot:DataSnapshot in snapshot.children){
                    val users=dataSnapshot.getValue(SpUserModel::class.java)
                    if (users != null) {
                        spUserModel1.add(users)
                    }
                }
                spUsersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(applicationContext,"Failure occur",Toast.LENGTH_SHORT).show()
            }
        })
    }
}