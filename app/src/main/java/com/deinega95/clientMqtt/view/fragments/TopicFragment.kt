package com.deinega95.clientMqtt.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.TopicPresenter
import com.deinega95.clientMqtt.view.adapters.MessagesRecyclerViewAdapter
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import kotlinx.android.synthetic.main.fragment_topic.*
import javax.inject.Inject


class TopicFragment : BaseFragment(), ITopicFragment {
    @Inject
    lateinit var presenter: TopicPresenter

    private lateinit var adapter: MessagesRecyclerViewAdapter

    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_topic, container, false)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = MessagesRecyclerViewAdapter()
        list.adapter = adapter
        presenter.viewReady(this)

        sendBtn.setOnClickListener {
            presenter.onSendClicked(message.text.toString())
        }
    }

    override fun setMessage(messages: ArrayList<Message>) {
        Log.e("+++adapterSET", adapter.toString())
        adapter.setData(messages)

        /*list.adapter!!.notifyDataSetChanged()
        activity!!.runOnUiThread {
            adapter.notifyDataSetChanged()
            list.adapter!!.notifyDataSetChanged()
        }*/
    }

    override fun showError(mess: String?) {

    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDied(this)
    }
}