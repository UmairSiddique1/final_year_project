package com.example.bookavenue.Fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookavenue.Adapters.LikeAdapter
import com.example.bookavenue.Adapters.UserInterfaceAdapter
import com.example.bookavenue.Models.FavouriteModel
import com.example.bookavenue.Models.UserInterfaceModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.FragmentFavouritesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FavouritesFragment : Fragment() {
    private lateinit var  binding: FragmentFavouritesBinding
    // This property is only valid between onCreateView and
// onDestroyView.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val firebaseDatabase=FirebaseDatabase.getInstance()
        val myRef:DatabaseReference=firebaseDatabase.getReference("Favourites")
        binding.favRv.layoutManager = LinearLayoutManager(context)
 val favouriteList:MutableList<FavouriteModel> =ArrayList()
        val likeAdapter=LikeAdapter(requireContext(),favouriteList)
binding.favRv.adapter=likeAdapter
        myRef.child(FirebaseAuth.getInstance().uid.toString()).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            for(datasnapShot:DataSnapshot in snapshot.children){
                val value: FavouriteModel? =datasnapShot.getValue(FavouriteModel::class.java)
                if (value != null) {
                    favouriteList.add(value)
                    likeAdapter.notifyDataSetChanged()
                }
            }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        return binding.root
    }
}