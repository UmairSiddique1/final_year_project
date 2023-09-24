package com.example.bookavenue.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookavenue.Adapters.SpUsersAdapter
import com.example.bookavenue.Models.SpUserModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.FragmentChatsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatsFragment : Fragment() {
    private lateinit var binding:FragmentChatsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentChatsBinding.inflate(inflater,container,false)
        binding.rvChats.layoutManager=LinearLayoutManager(context)
        val list:MutableList<SpUserModel> =ArrayList()
        val adapter= context?.let { SpUsersAdapter(it,list) }
        binding.rvChats.adapter=adapter

        FirebaseDatabase.getInstance().getReference("Service provider").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
             for(dataSnapshot in snapshot.children){
                 val user=dataSnapshot.getValue(SpUserModel::class.java)
                 if (user != null) {
                     list.add(user)
                 }
                 adapter!!.notifyDataSetChanged()
             }
            }

            override fun onCancelled(error: DatabaseError) {
             Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}