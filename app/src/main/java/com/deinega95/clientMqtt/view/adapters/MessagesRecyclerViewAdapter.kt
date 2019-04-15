package com.deinega95.clientMqtt.view.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.TopicPresenter
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.utils.toDate
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import javax.inject.Inject


class MessagesRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Inject
    lateinit var presenter: TopicPresenter

    private var data: List<Message> = ArrayList()


    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_message -> {
                MyViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
            R.layout.item_image -> {
                ImageViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
            else -> throw java.lang.Exception("invalid oncreateiewholder")
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].type) {
            "image" -> R.layout.item_image
            "text" -> R.layout.item_message
            else -> throw Exception("invalid type message")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> holder.bind(data[position])
            is MyViewHolder -> holder.bind(data[position])
        }
    }


    fun setData(messages: ArrayList<Message>) {
        Log.e("setdata", messages.size.toString())
        Log.e("setdata", this.toString())
        this.data = messages
        notifyDataSetChanged()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
           //     presenter.onItemClicked(data[adapterPosition])
            }
        }

        fun bind(mes: Message) {
            Log.e("bind", mes.text)
            itemView.message.text = mes.text

            itemView.date.text = mes.time!!.toDate()
        }
    }

    inner class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
           //     presenter.onItemClicked(data[adapterPosition])
            }
        }

        fun bind(message: Message) {
            val mes = message.image!!.copyOfRange(6, message.image!!.size)
            val bmp = BitmapFactory.decodeByteArray(mes, 0, mes.size)
            MyLog.show("bmp=${bmp.height}")
            MyLog.show("bmp=${bmp.width}")
            itemView.image.setImageBitmap(
                Bitmap.createScaledBitmap(
                    bmp, 300,
                    500, false
                )
            )
        }
    }
}