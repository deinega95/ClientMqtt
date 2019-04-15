package com.deinega95.clientMqtt.view.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.TopicPresenter
import com.deinega95.clientMqtt.view.adapters.MessagesRecyclerViewAdapter
import com.deinega95.clientMqtt.view.adapters.TopicRecyclerViewAdapter
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import kotlinx.android.synthetic.main.fragment_topic.*
import javax.inject.Inject


class TopicFragment : GalleryFragment(), ITopicFragment {
    @Inject
    lateinit var presenter: TopicPresenter

    private lateinit var messagesAdapter: MessagesRecyclerViewAdapter
    private lateinit var topicsAdapter: TopicRecyclerViewAdapter

    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topic, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        presenter.viewReady(this)
        setListeners()
    }



    private fun initList() {
        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        messagesAdapter = MessagesRecyclerViewAdapter()
        list.adapter = messagesAdapter

        topicList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        topicsAdapter = TopicRecyclerViewAdapter()
        topicList.adapter = topicsAdapter
    }

    private fun setListeners() {
        sendBtn.setOnClickListener {
            presenter.onSendClicked(message.text.toString())
        }

        addTopic.setOnClickListener {
            presenter.onAddTopicClicked()
        }
    }

    override fun setMessage(messages: ArrayList<Message>) {
        messagesAdapter.setData(messages)
    }

    override fun showError(mess: String?) {

    }

    override fun showAddTopicDialog() {
        val dialog = MaterialDialog(context!!)
            .customView(R.layout.dialog_add_topic)

        val btn = dialog.findViewById<Button>(R.id.subscribe)
        val topic = dialog.findViewById<EditText>(R.id.topicET)
        btn.setOnClickListener {
            presenter.onSubscribeClicked(topic.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }

    /*    override fun showPhotosDialog() {
        super.showPhotoDialog {
            val file = File(it)
            var compressedImage = Compressor(App.instance.applicationContext)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToFile(file)
            presenter.sendImage(compressedImage.readBytes())
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val photoItem = menu.findItem(R.id.getPhoto)
        photoItem.setOnMenuItemClickListener {
            presenter.getPhotoClicked()
            return@setOnMenuItemClickListener true
        }

        val exitItem = menu.findItem(R.id.exit)
        exitItem.setOnMenuItemClickListener {
            presenter.exitClicked()
            return@setOnMenuItemClickListener true
        }

        val settingsItem = menu.findItem(R.id.setting)
        settingsItem.setOnMenuItemClickListener {
            presenter.settingsClicked()
            return@setOnMenuItemClickListener true
        }
    }

    override fun showTopics(topics: List<String>) {
        topicsAdapter.setData(topics)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDied(this)
    }
}