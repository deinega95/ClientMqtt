package com.deinega95.clientMqtt.view.activities

import android.os.Bundle
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.utils.MyLog
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var prefsManager: PrefsManager

    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MyLog.show("baseactivity onCreate")
        super.onCreate(savedInstanceState)
        if (!prefsManager.isAuthorized()) {
            viewRouter.showAuthorizationActivity()
        } else {
            setContentView(R.layout.activity_main)
        }
    }

}