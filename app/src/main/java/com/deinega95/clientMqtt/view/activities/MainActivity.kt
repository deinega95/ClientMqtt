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

    companion object{
        const val FROM_NOTIFICATION = "FROM_NOTIFICATION"
        const val TIME_PUSH = "TIME_PUSH"
    }

    init {
        App.instance.mainComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyLog.show("onCreate time=${ intent?.extras?.get(TIME_PUSH)}:::")
        MyLog.show("onCreate time=${ intent?.extras?.get(FROM_NOTIFICATION)}")
        if (!prefsManager.isAuthorized()) {
            viewRouter.showAuthorizationActivity()
        } else {
            setContentView(R.layout.activity_main)
        }
    }
}