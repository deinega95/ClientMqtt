package com.deinega95.clientMqtt.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.presenter.TopicPresenter
import kotlinx.android.synthetic.main.item_topic.view.*
import javax.inject.Inject

class TopicRecyclerViewAdapter : RecyclerView.Adapter<TopicRecyclerViewAdapter.MyViewHolder>() {

    @Inject
    lateinit var presenter: TopicPresenter
    private var data: List<String> = listOf()

    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicRecyclerViewAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_topic
    }

    override fun onBindViewHolder(holder: TopicRecyclerViewAdapter.MyViewHolder, position: Int) {
        holder.bind(data[position])
    }


    fun setData(messages: List<String>) {
        this.data = messages
        notifyDataSetChanged()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
                presenter.onTopicClicked(data[adapterPosition])
            }
        }

        fun bind(name: String) {
            itemView.topicTV.text = name
        }
    }
}