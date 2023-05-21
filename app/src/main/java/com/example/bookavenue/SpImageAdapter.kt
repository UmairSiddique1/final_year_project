package com.example.bookavenue


import android.content.Context
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.SpImageModel
import com.example.bookavenue.serviceProvider.SpHallImagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SpImageAdapter(private val activity: SpHallImagesActivity,val context: Context, private val imageUrl:List<SpImageModel>):RecyclerView.Adapter<SpImageAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
val image: ImageView =view.findViewById(R.id.iv_layoutSpImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.layout_sp_hallimages,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(imageUrl[position].image).into(holder.image)
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure you want to delete this User")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
FirebaseDatabase.getInstance().getReference("hall data").child(FirebaseAuth.getInstance().currentUser!!.uid)
    .child("photoUrl").child("img$position").removeValue()
                })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            builder.create().show()
            true
        }
    }

    override fun getItemCount(): Int {
  return imageUrl.size
    }
//    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imageView: ImageView = itemView.findViewById(R.id.iv_layoutSpImg)
//
//        init {
//            imageView.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val imageURL = imageUrl[position]
//
//                    // Remove the image URL from the database
//                FirebaseDatabase.getInstance().getReference("hall data").child(FirebaseAuth.getInstance().currentUser!!.uid)
//                    .child("photoUrl")
//                    .child(imageURL.toString()).removeValue()
//                }
//            }
//        }
//    }
}