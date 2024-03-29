package com.deinega95.clientMqtt.services

import android.content.Intent
import androidx.annotation.StringRes
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.view.activities.AuthorizationActivity
import com.deinega95.clientMqtt.view.activities.BaseActivity
import com.deinega95.clientMqtt.view.activities.MainActivity
import com.deinega95.clientMqtt.view.activities.PhotoByPeriodActivity
import com.deinega95.clientMqtt.view.fragments.ImageViewerFragment
import com.deinega95.clientMqtt.view.fragments.InputServerFragment
import com.deinega95.clientMqtt.view.fragments.PhotosByPeriodFragment
import com.deinega95.clientMqtt.view.fragments.TopicFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewRouter @Inject constructor() {
    private var currentActivity: BaseActivity? = null

    fun setCurrentActivity(activity: BaseActivity) {
        MyLog.show("setCurrentActivity $activity")
        this.currentActivity = activity
    }

    fun removeCurrentActivity(activity: BaseActivity) {
        MyLog.show("removeCurrentActivity $activity")
        if (activity == currentActivity) {
            currentActivity = null
        }
    }

    fun showTopicScreen() {
        currentActivity?.replaceFragment(TopicFragment())
    }

    fun showInputServerScreen() {
        MyLog.show("showInputServerScreen $currentActivity")
        currentActivity?.replaceFragment(InputServerFragment())
    }

    fun exitApp() {

        currentActivity?.finish()
        App.instance.clear()
    }

    fun showError(error: String?) {
        currentActivity?.showErrorMessage(error)
    }
    fun showError(@StringRes error: Int) {
        currentActivity?.showErrorMessage(currentActivity?.applicationContext?.getString(error))
    }

    fun showMainActivity() {
        val intent = Intent(currentActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        currentActivity?.startActivity(intent)
    }

    fun showAuthorizationActivity() {
        val intent = Intent(currentActivity, AuthorizationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        currentActivity?.startActivity(intent)
    }

    fun showConfirmDialog(text: String, callback: () -> Unit) {
        currentActivity?.showConfirmDialog(text, callback)

    }

    fun showConfirmDialog(@StringRes text:  Int, callback: () -> Unit) {
        currentActivity?.showConfirmDialog(text, callback)

    }

    fun startPhotosActivity() {
        val intent = Intent(currentActivity, PhotoByPeriodActivity::class.java)
        currentActivity?.startActivity(intent)
    }

    fun showPhotosByPeriodFragment() {
        currentActivity?.replaceFragment(PhotosByPeriodFragment())
    }

    fun closeFragment() {
        currentActivity?.onBackPressed()
    }

    fun showImageViewer(type: ImageViewerFragment.Companion.ImageViewerTypeEnum, photoId: Long) {
        currentActivity?.replaceFragment(ImageViewerFragment.newInstance(type,photoId))
    }

    fun showMessage(@StringRes text: Int) {
        currentActivity?.showMessage(text)
    }

}