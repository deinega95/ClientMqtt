package com.deinega95.clientMqtt.view.activities

import android.os.Bundle
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.storage.PrefsManager
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var prefsManager: PrefsManager

    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!prefsManager.isAuthorized()) {
            viewRouter.showAuthorizationActivity()
        } else {
            setContentView(R.layout.activity_main)
        }
    }
}