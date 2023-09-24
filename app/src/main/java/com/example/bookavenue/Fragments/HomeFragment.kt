package com.example.bookavenue.Fragments

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookavenue.Adapters.UserInterfaceAdapter
import com.example.bookavenue.Models.UserInterfaceModel
import com.example.bookavenue.user.PriceFilterActivity
import com.example.bookavenue.R
import com.example.bookavenue.Utilss
import com.example.bookavenue.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.like.LikeButton
import com.like.OnLikeListener

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var locationManager:LocationManager
    private var location:Location? = null
    private lateinit var menuItem: MenuItem
    private lateinit var searchView:androidx.appcompat.widget.SearchView
    private val PERMISSION_CODE = 1
    private var holder: UserInterfaceAdapter.ViewHolder? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentHomeBinding.inflate(inflater, container, false)

        // SETTING THE DATA IN RECYCLERVIEW
        val firebaseDatabase=FirebaseDatabase.getInstance()
        val myRef:DatabaseReference=firebaseDatabase.getReference("hall data")
        binding.rvUserInterface.layoutManager=LinearLayoutManager(context)
        val userInterfaceModelArray:MutableList<UserInterfaceModel> =ArrayList()
        val userInterfaceAdapter= UserInterfaceAdapter(requireContext(),userInterfaceModelArray,0)
       binding.rvUserInterface.adapter=userInterfaceAdapter

        // FETCHING THE CITY OF USER RUNNING THE CODE IN SECONDARY THREAD
val runnable=Runnable(){
    locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_CODE
        ) }
    location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

    val handler=Handler(Looper.getMainLooper())
    handler.post {
        try {
            binding.etCityname.text = location.let {
                it?.let { it1 -> Utilss.getCityName(it1.latitude, it.longitude, requireContext()) }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
        val thread=Thread(runnable)
        thread.start()

// ONCLICK LISTENER ON THE FILTER BUTTON
        binding.ivFilter.setOnClickListener {
            startActivity(Intent(requireContext(), PriceFilterActivity::class.java))
        }

        //GETTING DATA FROM DATABASE
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sharedPreferences = context?.getSharedPreferences("price-range", Context.MODE_PRIVATE)
            val fromPrice = sharedPreferences?.getInt("from", 0)
            val toPrice = sharedPreferences?.getInt("to", 0)
                for (snapshot in dataSnapshot.children) {
                    val userInterfaceModel = snapshot.getValue(UserInterfaceModel::class.java)
                    if (userInterfaceModel != null) {
                        if (fromPrice != null) {
                            if( userInterfaceModel.perHeadCharges.toString().toInt() in (fromPrice + 1) until toPrice!!){
                                userInterfaceModelArray.add(userInterfaceModel)
                            }
                        }
                    } else {
                       Toast.makeText(context,"Nothing found",Toast.LENGTH_SHORT).show()
                    }
                }

                userInterfaceAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    // CREATING SEARCH BAR ON FRAGMENT
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.searchmenu,menu)
menuItem=menu.findItem(R.id.actionSearch)
        searchView= MenuItemCompat.getActionView(menuItem) as androidx.appcompat.widget.SearchView
        searchView.setIconifiedByDefault(true)
        val searchManager:SearchManager= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mySearch(query)
                searchLocation(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mySearch(newText)
                searchLocation(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun mySearch(query: String?) {
FirebaseDatabase.getInstance().getReference("hall data").orderByChild("hallName").startAt(query).endAt(
    query + "\uf8ff")
    .addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val userInterfaceModelArray = mutableListOf<UserInterfaceModel>()
            for (dataSnapshot in snapshot.children) {
                val userInterfaceModel = dataSnapshot.getValue(UserInterfaceModel::class.java)
                if (userInterfaceModel != null) {
                        userInterfaceModelArray.add(userInterfaceModel)
                }
                else{
                    Toast.makeText(context,"Noting found",Toast.LENGTH_SHORT).show()
                }
            }
            val userInterfaceAdapter=UserInterfaceAdapter(requireContext(),userInterfaceModelArray,0)
//            userInterfaceAdapter.filterList(userInterfaceModelArray)
            binding.rvUserInterface.adapter=userInterfaceAdapter
        }

        override fun onCancelled(error: DatabaseError) {
      Toast.makeText(requireContext(),"Failure occur in search view",Toast.LENGTH_LONG).show()
        }
    })
    }
    private fun searchLocation(query: String?){
        FirebaseDatabase.getInstance().getReference("hall data").orderByChild("location").startAt(query).endAt(
            query + "\uf8ff")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInterfaceModelArray = mutableListOf<UserInterfaceModel>()
                    for (dataSnapshot in snapshot.children) {
                        val userInterfaceModel = dataSnapshot.getValue(UserInterfaceModel::class.java)
                        if (userInterfaceModel != null) {
                            userInterfaceModelArray.add(userInterfaceModel)
                        }
                        else{
                            Toast.makeText(context,"Noting found",Toast.LENGTH_SHORT).show()
                        }
                    }
                    val userInterfaceAdapter=UserInterfaceAdapter(requireContext(),userInterfaceModelArray,0)
//            userInterfaceAdapter.filterList(userInterfaceModelArray)
                    binding.rvUserInterface.adapter=userInterfaceAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),"Failure occur in search view",Toast.LENGTH_LONG).show()
                }
            })
    }

}
