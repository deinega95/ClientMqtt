package com.deinega95.clientMqtt.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.presenter.InputServerPresenter
import com.deinega95.clientMqtt.view.activities.AuthorizationActivity
import com.deinega95.clientMqtt.view.activities.MainActivity
import com.deinega95.clientMqtt.view.fragments.interfaces.IInputServerFragment
import kotlinx.android.synthetic.main.fragment_input_server.*
import javax.inject.Inject

class InputServerFragment : BaseFragment(), IInputServerFragment {
    @Inject
    lateinit var presenter: InputServerPresenter


    init {
        App.instance.authorizationComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_input_server, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.viewReady(this)
        connectBtn.setOnClickListener {
            presenter.onConnectClicked(addressBroker.text.toString(), name.text.toString())
        }
    }

    override fun showMainActivity() {
        var intent = Intent(context!!, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun showError(mess: String?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDied(this)
    }

}