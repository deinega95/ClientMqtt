package com.deinega95.clientMqtt.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.TopicPresenter
import com.deinega95.clientMqtt.utils.toBitmap
import com.deinega95.clientMqtt.utils.toDate
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import javax.inject.Inject


class MessagesRecyclerViewAdapter : RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder>() {

    @Inject
    lateinit var presenter: TopicPresenter

    private var data: List<Message> = ArrayList()


    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {
            R.layout.item_message -> {
                MessageViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
            R.layout.item_image -> {
                ImageViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
            R.layout.empty_state_message -> {
                EmptyStateViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
            else -> throw java.lang.Exception("invalid oncreateiewholder")
        }

    }

    override fun getItemCount(): Int {
        return when (data.size) {
            0 -> 1
            else -> data.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            data.isEmpty() -> R.layout.empty_state_message
            data[position].type == "image" -> R.layout.item_image
            data[position].type == "text" -> R.layout.item_message
            else -> throw Exception("invalid type message")
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }


    fun setData(messages: List<Message>) {
        this.data = messages
        notifyDataSetChanged()
    }

    abstract class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun bind(pos: Int)
    }

    inner class MessageViewHolder(v: View) : MyViewHolder(v) {

        override fun bind(pos: Int) {
            val mes = data[pos]
            itemView.message.text = mes.text
            if (mes.time != null)
                itemView.date.text = mes.time!!.toDate()
        }
    }

    inner class ImageViewHolder(v: View) : MyViewHolder(v) {
        init {
            itemView.setOnClickListener {
                presenter.onImageClicked(data[adapterPosition])
            }
        }

        override fun bind(pos: Int) {
            val message = data[pos]
            val bmp = message.image?.toBitmap()
            itemView.image.setImageBitmap(bmp)
        }

    }

    inner class EmptyStateViewHolder(v: View) : MyViewHolder(v) {
        override fun bind(pos: Int) {}
    }
}