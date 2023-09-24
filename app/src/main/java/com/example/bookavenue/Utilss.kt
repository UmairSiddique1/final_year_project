package com.example.bookavenue

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.bookavenue.Adapters.UserInterfaceAdapter
import com.example.bookavenue.Models.UserInterfaceModel
import com.example.bookavenue.user.HallDetailActivity
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException
import java.util.*

class Utilss {
    companion object{
         fun getDatabaseImg(context:Context, userInterfaceModel: UserInterfaceModel,holder:UserInterfaceAdapter.ViewHolder):Unit{
            FirebaseDatabase.getInstance().getReference("hall data")
                .child(userInterfaceModel.getUid.toString()).child("photoUrl")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val firstChild: DataSnapshot = snapshot.children.iterator().next()
                            val imageUrl = firstChild.getValue(String::class.java).toString()
                            Glide.with(context).load(imageUrl).into(holder.hallImage)

                            //SENDING THE DATA TO HALL DETAIL ACTIVITY.
                            holder.itemView.setOnClickListener {
                                val intent = Intent(context, HallDetailActivity::class.java)
                                intent.putExtra("hallName", userInterfaceModel.hallName)
                                intent.putExtra("photoUrl", imageUrl)
                                intent.putExtra("address",userInterfaceModel.address)
                                intent.putExtra("menu", userInterfaceModel.menu)
                                intent.putExtra("contact", userInterfaceModel.contactNo)
                                intent.putExtra("location", userInterfaceModel.location)
                                intent.putExtra("perHead", userInterfaceModel.perHeadCharges)
                                intent.putExtra("getUid", userInterfaceModel.getUid)
                                context.startActivity(intent)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }

                })
        }
        fun saveRating(context: Context, position: Int, rating: Float) {
            val sharedPref = context.getSharedPreferences("ratings", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val ratingValue: Float = rating
            editor.putFloat("ratings_$position", ratingValue)
            editor.apply()
        }
        fun saveBooleanValue(context: Context, position: Int, value: Boolean) {
            val editor = context.getSharedPreferences("save", Context.MODE_PRIVATE).edit()
            editor.putBoolean("value$position", value)
            editor.apply()
        }

       fun getCityName(latitude: Double, longitude: Double,context: Context): String? {
            var cityName: String? = "Not found"
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                for (adr in addresses!!) {
                    if (adr != null) {
                        val city = adr.locality
                        if (city != null && city != "") {
                            cityName = city
                        } else {
                            Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (i: IOException) {
                i.printStackTrace()
            }
            return cityName
        }

        fun likeButtonMethod(holder: UserInterfaceAdapter.ViewHolder){
            FirebaseDatabase.getInstance().getReference("hall data").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("likeButton").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                    val data=snapshot.value
                     if (data==true){
                       holder.likeBtn.isLiked=true
                     }
                     else{
                         holder.likeBtn.isLiked=false
                     }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }

}