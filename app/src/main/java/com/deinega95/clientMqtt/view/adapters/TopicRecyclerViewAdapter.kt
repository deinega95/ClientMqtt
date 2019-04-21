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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {
            R.layout.item_topic -> TopicViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            R.layout.empty_state_topic -> EmptyStateViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    viewType,
                    parent,
                    false
                )
            )
            else -> throw RuntimeException("topicsAdapter invalid viewType=$viewType")
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
            data.isEmpty() -> R.layout.empty_state_topic
            else -> R.layout.item_topic
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }


    fun setData(messages: List<String>) {
        this.data = messages
        notifyDataSetChanged()
    }

    abstract class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun bind(pos: Int)
    }

    inner class TopicViewHolder(v: View) : MyViewHolder(v) {
        init {
            itemView.setOnClickListener {
                presenter.onTopicClicked(data[adapterPosition])
            }
        }

        override fun bind(pos: Int) {
            val text = data[pos]
            itemView.topicTV.text = text
        }
    }

    inner class EmptyStateViewHolder(v: View) : MyViewHolder(v) {
        override fun bind(pos: Int) {}
    }
}