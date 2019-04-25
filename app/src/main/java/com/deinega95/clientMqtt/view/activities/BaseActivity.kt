package com.deinega95.clientMqtt.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.view.fragments.BaseFragment
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var viewRouter: ViewRouter

    init {
        App.instance.component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MyLog.show("baseactivity onCreate")
        super.onCreate(savedInstanceState)

        viewRouter.setCurrentActivity(this)
    }

    override fun onResume() {
        super.onResume()

        MyLog.show("onResume")
        viewRouter.setCurrentActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        MyLog.show("onDestroy")
        viewRouter.removeCurrentActivity(this)
    }

    fun replaceFragment(fr: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fr)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    fun showErrorMessage(error: String?) {
        MaterialDialog(this)
            .title(R.string.error)
            .message(text = error ?: "")
            .positiveButton(res = R.string.ok)
            .show()
    }

    fun showConfirmDialog(text: String, callback: () -> Unit) {
        MaterialDialog(this)
            .message(text = text)
            .cancelable(true)
            .positiveButton(res = R.string.ok, click = {
                callback.invoke()
            })
            .negativeButton(res = R.string.cancel)
            .show()

    }
}