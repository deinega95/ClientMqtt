package com.deinega95.clientMqtt.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.presenter.InputServerPresenter
import com.deinega95.clientMqtt.presenter.TopicPresenter
import com.deinega95.clientMqtt.view.activities.MainActivity
import com.deinega95.clientMqtt.view.fragments.interfaces.IInputServerFragment
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import kotlinx.android.synthetic.main.fragment_topic.*
import javax.inject.Inject


class TopicFragment : BaseFragment(), ITopicFragment {
    @Inject
    lateinit var presenter: TopicPresenter


    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_topic, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.viewReady(this)
        sendBtn.setOnClickListener {
            presenter.onSendClicked(message.text.toString())
        }
    }



    override fun showError(mess: String?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDied(this)
    }

}