package com.deinega95.clientMqtt.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.model.Message
import kotlinx.android.synthetic.main.item_message.view.*

class MessagesRecyclerViewAdapter : RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder>() {

    private var data:List<Message> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message,parent, false))
    }

    override fun getItemCount(): Int {

        Log.e("getItemCount",data.size.toString())
        return data.size
        }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }


    fun setData(messages: ArrayList<Message>) {
        Log.e("setdata",messages.size.toString())
        Log.e("setdata",this.toString())
        this.data = messages
        notifyDataSetChanged()
    }

    inner class MyViewHolder(v: View): RecyclerView.ViewHolder(v){


        fun bind(mes:Message){

            Log.e("bind",mes.text)
            itemView.message.text = mes.text
        }
    }
}