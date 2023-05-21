package com.example.bookavenue.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.FavouriteModel
import com.example.bookavenue.R

class LikeAdapter(val context: Context, private val userInterfaceModel: List<FavouriteModel>): RecyclerView.Adapter<LikeAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val hallName:TextView=view.findViewById(com.example.bookavenue.R.id.tv_hallName1)
        val cityName:TextView=view.findViewById(com.example.bookavenue.R.id.tv_cityName1)
        val imgRes:ImageView=view.findViewById(R.id.fav_iv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
   val userInterfacePosition:FavouriteModel=userInterfaceModel[position]
        holder.hallName.text=userInterfacePosition.hallName.toString()
        holder.cityName.text=userInterfacePosition.cityName.toString()
Glide.with(context).load(userInterfacePosition.imageUrl).into(holder.imgRes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.layout_favourites,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
return userInterfaceModel.size
    }

}