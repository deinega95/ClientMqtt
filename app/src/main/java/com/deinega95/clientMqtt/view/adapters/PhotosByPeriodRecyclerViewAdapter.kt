package com.deinega95.clientMqtt.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.PhotosByPeriodPresenter
import java.util.*
import javax.inject.Inject

class PhotosByPeriodRecyclerViewAdapter(var cnt: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @Inject
    lateinit var presenter: PhotosByPeriodPresenter

    private var data: List<Message> = ArrayList()

    init {
        App.instance.photoByPeriodComponent?.inject(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0/* && data.isNotEmpty() */-> R.layout.item_empty_header
            else -> R.layout.item_photo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_photo -> MyViewholder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            R.layout.item_empty_header -> EmptyHeaderViewholder(
                LayoutInflater.from(parent.context).inflate(
                    viewType,
                    parent,
                    false
                )
            )
            else -> throw Exception("invalid viewType")
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewholder -> holder.bind(data[position])
            is EmptyHeaderViewholder -> return
        }
    }

    fun setData(data: List<Message>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class MyViewholder(v: View) : RecyclerView.ViewHolder(v) {
        init {
/*            itemView.setOnClickListener {
                presenter.onItemClicked(data[adapterPosition - 1])
            }*/
        }

        fun bind(item: Message) {

        }
    }


    inner class EmptyHeaderViewholder(v: View) : RecyclerView.ViewHolder(v)
}
