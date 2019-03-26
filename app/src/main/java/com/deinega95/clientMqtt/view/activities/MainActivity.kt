package com.deinega95.clientMqtt.view.activities

import android.content.Intent
import android.os.Bundle
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.storage.PrefsManager
import kotlinx.android.synthetic.main.fragment_input_server.*
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var prefsManager: PrefsManager


    init {
        App.instance.component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!prefsManager.isAuthorized()) {
            var intent = Intent(this, AuthorizationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            return
        }

        setContentView(R.layout.activity_main)


    }


}
