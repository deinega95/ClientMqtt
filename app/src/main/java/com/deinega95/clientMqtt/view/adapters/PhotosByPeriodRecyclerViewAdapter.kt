package com.deinega95.clientMqtt.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.PhotosByPeriodPresenter
import com.deinega95.clientMqtt.utils.toBitmap
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_photo.view.*
import java.util.*
import javax.inject.Inject

class PhotosByPeriodRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @Inject
    lateinit var presenter: PhotosByPeriodPresenter

    private var data: List<Message> = ArrayList()
    private var allPhoto: Int = 0

    init {
        App.instance.photoByPeriodComponent?.inject(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.item_header
            else -> R.layout.item_photo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_photo -> MyViewholder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            R.layout.item_header -> HeaderViewholder(
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
            is MyViewholder -> holder.bind(data[position - 1])
            is HeaderViewholder -> holder.bind()
            else -> throw RuntimeException("invalid holder in photosByPeriodAdapter")
        }
    }

    fun setData(data: List<Message>, allPhoto: Int) {
        this.data = data
        this.allPhoto = allPhoto
        notifyDataSetChanged()
    }

    inner class MyViewholder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
                presenter.onItemClicked(data[adapterPosition - 1])
            }
        }

        fun bind(item: Message) {
            val bmp = item.image?.toBitmap()
            itemView.photo.setImageBitmap(bmp)
        }
    }


    inner class HeaderViewholder(v: View) : RecyclerView.ViewHolder(v) {

        fun bind() {
            itemView.allPhotosTV.text =
                itemView.context.getString(R.string.geted_photo_from) + " ${data.size} / $allPhoto"
            if (data.size == allPhoto) {
                itemView.progressBar.visibility = GONE
            } else {
                itemView.progressBar.visibility = VISIBLE
            }
        }
    }
}
