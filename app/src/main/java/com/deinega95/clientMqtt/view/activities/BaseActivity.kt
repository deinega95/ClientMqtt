package com.deinega95.clientMqtt.view.activities

import androidx.appcompat.app.AppCompatActivity
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.view.fragments.BaseFragment


abstract class BaseActivity : AppCompatActivity() {




    fun replaceFragment(fr: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fr)
            .commitNowAllowingStateLoss()
    }

}