package com.example.bookavenue.Adapters

import android.content.Context
import android.provider.Telephony.Mms.Sent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.MessageModel
import com.example.bookavenue.R
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(var context: Context,messageModel:List<MessageModel>,
                  senderRoom:String,receiverRoom:String):RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    lateinit var messageModel:List<MessageModel>
    val ITEM_SENT:Int=1
    val ITEM_RECEIVE:Int=2
    private val senderRoom:String
    private val receiverRoom:String
    class SentViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textview: TextView =view.findViewById(R.id.tv_msg)
        val imageView: ImageView =view.findViewById(R.id.imageView)
    }
    class ReceiverViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textview: TextView =view.findViewById(R.id.tv_msg)
        val imageView: ImageView =view.findViewById(R.id.imageView)
    }
    init {
        if(messageModel!=null){
            this.messageModel=messageModel
        }
        this.senderRoom=senderRoom
        this.receiverRoom=receiverRoom
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==ITEM_SENT){
            val view= LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false)
            SentViewHolder(view)
        } else{
            val view= LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false)
            ReceiverViewHolder(view)
        }
    }
    override fun getItemViewType(position: Int): Int {
        val message:MessageModel=messageModel[position]
        return if(FirebaseAuth.getInstance().uid.equals(message.senderId)){
            ITEM_SENT
        } else{
            ITEM_RECEIVE
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message=messageModel[position]
        if(holder.javaClass== SentViewHolder::class.java){
            val viewHolder=holder as SentViewHolder
            if(message.message.equals("photo")){
                viewHolder.imageView.visibility=View.VISIBLE
                viewHolder.textview.visibility=View.GONE
                Glide.with(context).load(message.imageUrls).into(viewHolder.imageView)
            }
            viewHolder.textview.text=message.message
        }
        else{
            val viewHolder=holder as ReceiverViewHolder
            if(message.message.equals("photo")){
                viewHolder.imageView.visibility=View.VISIBLE
                viewHolder.textview.visibility=View.GONE
                Glide.with(context).load(message.imageUrls).into(viewHolder.imageView)
            }
            viewHolder.textview.text=message.message
        }
    }

    override fun getItemCount(): Int =messageModel.size
}















//class ChatsAdapter(val context: Context, private val messageModel: List<MessageModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//  private final val ITEM_SENT:Int=1
//    private final val ITEM_RECEIVE:Int=2
//    class SentViewHolder(view: View):RecyclerView.ViewHolder(view){
//        val textview:TextView=view.findViewById(R.id.tv_msg)
//        val imageView:ImageView=view.findViewById(R.id.imageView)
//    }
//    class ReceiverViewHolder(view: View):RecyclerView.ViewHolder(view){
//        val textview:TextView=view.findViewById(R.id.tv_msg)
//        val imageView:ImageView=view.findViewById(R.id.imageView)
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if(viewType==ITEM_SENT){
//            val view=LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false)
//            SentViewHolder(view)
//        } else{
//            val view=LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false)
//            ReceiverViewHolder(view)
//        }
//    }
//    override fun getItemViewType(position: Int): Int {
//        val message:MessageModel=messageModel[position]
//        return if(FirebaseAuth.getInstance().uid.equals(message.senderId)){
//            ITEM_SENT
//        } else{
//            ITEM_RECEIVE
//        }
//        return super.getItemViewType(position)
//    }
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//      if(holder.javaClass==SentViewHolder::class.java){
//          val viewHolder=holder as SentViewHolder
//          viewHolder.textview.text=messageModel[position].message
//      }
//        else{
//            val viewHolder=holder as ReceiverViewHolder
//          viewHolder.textview.text=messageModel[position].message
//      }
//    }
//    override fun getItemCount(): Int {
//      return messageModel.size
//    }
//}




