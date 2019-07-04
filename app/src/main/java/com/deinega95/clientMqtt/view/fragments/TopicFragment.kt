package com.deinega95.clientMqtt.view.fragments

import android.os.Bundle
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.listItems
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.TopicPresenter
import com.deinega95.clientMqtt.view.adapters.MessagesRecyclerViewAdapter
import com.deinega95.clientMqtt.view.adapters.TopicRecyclerViewAdapter
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_topic.*
import javax.inject.Inject


class TopicFragment : GalleryFragment(), ITopicFragment {
    @Inject
    lateinit var presenter: TopicPresenter

    private lateinit var messagesAdapter: MessagesRecyclerViewAdapter
    private lateinit var topicsAdapter: TopicRecyclerViewAdapter
    private var messageET: TextInputEditText? = null

    private var clearMessage: ImageView? = null

    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topic, container, false)
        setToolbar(R.string.app_name, false, view)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageET = message

        clearMessage = clearMessagesBtn
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
            presenter.onSendClicked(messageET?.text.toString())
        }

        addTopic.setOnClickListener {
            presenter.onAddTopicClicked()
        }

        clearMessage?.setOnClickListener {
            presenter.onClearMessagesClicked()
        }
    }

    override fun setMessage(messages: List<Message>) {
        if (messages.isEmpty()) {
            clearMessage?.visibility = INVISIBLE
        } else {
            clearMessage?.visibility = VISIBLE
        }
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

    override fun showSelectGetPhotoDialog() {
        MaterialDialog(context!!).show {
            title(R.string.get_photo)
            listItems(R.array.get_photo) { _, _, text ->
                when (text) {
                    getString(R.string.get_current_photo) -> presenter.onCurrentPhotoClicked()
                    getString(R.string.period_photo) -> presenter.onPeriodPhotoClicked()
                }
            }
        }
    }

    override fun onDestroyView() {
        presenter.viewDied(this)
        super.onDestroyView()
    }
}