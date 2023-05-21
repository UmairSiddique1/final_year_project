package com.example.bookavenue.Adapters
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookavenue.Models.FavouriteModel
import com.example.bookavenue.Models.UserInterfaceModel
import com.example.bookavenue.Utilss
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.like.LikeButton
import com.like.OnLikeListener


class UserInterfaceAdapter(val context: Context, private var userInterfaceModel: MutableList<UserInterfaceModel>, private val switchAdapter:Int):RecyclerView.Adapter<UserInterfaceAdapter.ViewHolder>() {
    private lateinit var database: DatabaseReference
    private lateinit var imageUrl:String
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
val hallImage:ImageView=view.findViewById(com.example.bookavenue.R.id.iv_hall)
        val hallName:TextView=view.findViewById(com.example.bookavenue.R.id.tv_hallName)
        val cityName:TextView=view.findViewById(com.example.bookavenue.R.id.tv_cityName)
        val rating:RatingBar=view.findViewById(com.example.bookavenue.R.id.ratingBar)
        val likeBtn= view.findViewById<LikeButton>(com.example.bookavenue.R.id.starbutton)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view:View=LayoutInflater.from(context).inflate(com.example.bookavenue.R.layout.layout_userinterface,parent,false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userInterfacePosition = userInterfaceModel[position]
        //SETTING HALL NAME AND LOCATION VALUE
        holder.hallName.text = userInterfacePosition.hallName
        holder.cityName.text = userInterfacePosition.location
        //STORING RATING VALUE IN SHARED PREFERENCE

            holder.rating.onRatingBarChangeListener = null
            holder.rating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    Utilss.saveRating(context,position,rating)
                }
            }
            val sharedPref: SharedPreferences =context.getSharedPreferences("ratings", Context.MODE_PRIVATE)
            val value: Float = sharedPref.getFloat("ratings_$position", 1f)
            userInterfacePosition.ratingValue = value
            holder.rating.rating = userInterfacePosition.ratingValue!!


            // OnClickListener on the like button
            val sharedPreferencesBtn: SharedPreferences = context.getSharedPreferences("save", MODE_PRIVATE)
            userInterfacePosition.likeButton=sharedPreferencesBtn.getBoolean("value$position", false)
            holder.likeBtn.isLiked = userInterfacePosition.likeButton!!
        val hallNames = mutableListOf<String>()
        val cityNames = mutableListOf<String>()

        for (i in userInterfaceModel.indices) {
            if (userInterfacePosition.likeButton == true) {
                hallNames.add(userInterfacePosition.hallName.toString())
                cityNames.add(userInterfacePosition.location.toString())
            }
        }

        val sharedPreferences: SharedPreferences = context.getSharedPreferences("transferData", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet("hallNames", hallNames.toSet())
        editor.putStringSet("cityNames", cityNames.toSet())
        editor.apply()

            holder.likeBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(p0: LikeButton?) {


                    FirebaseDatabase.getInstance().getReference("hall data")
                        .child(userInterfacePosition.getUid.toString()).child("photoUrl")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val firstChild: DataSnapshot = snapshot.children.iterator().next()
                                    imageUrl = firstChild.getValue(String::class.java).toString()
                                    database = Firebase.database.reference
                                    val favouriteModel=FavouriteModel(userInterfacePosition.hallName,userInterfacePosition.location,imageUrl)
                                    database.child("Favourites")
                                        .child(FirebaseAuth.getInstance().uid.toString())
                                        .child("favourite_$position") // Use position as the child key
                                        .setValue(favouriteModel)

                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        })


                    Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                    Utilss.saveBooleanValue(context,position,true)
                    p0!!.isLiked = true

                }

                override fun unLiked(p0: LikeButton?) {
                    Toast.makeText(context, "UnLiked", Toast.LENGTH_SHORT).show()
                 Utilss.saveBooleanValue(context,position,false)
                    p0!!.isLiked = false
                    FirebaseDatabase.getInstance().getReference("Favourites").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("favourite_$position").removeValue()
                }
            })
           Utilss.getDatabaseImg(context,userInterfacePosition,holder)
        }
        override fun getItemCount(): Int {
            return userInterfaceModel.size
        }
//    fun filterList(filterList:List<UserInterfaceModel>){
//userInterfaceModel=filterList
//        notifyDataSetChanged()
//    }
    fun updateList(userInterfaceModelArray: MutableList<UserInterfaceModel>) {
        this.userInterfaceModel.clear()
        this.userInterfaceModel.addAll(userInterfaceModelArray)
        notifyDataSetChanged()
    }
}