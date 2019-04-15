package com.deinega95.clientMqtt.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.afollestad.materialdialogs.MaterialDialog
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.utils.FileUtils
import com.deinega95.clientMqtt.utils.MyLog

abstract class GalleryFragment : BaseFragment() {

    private val RESULT_LOAD_IMAGE = 4
    private var callback: (path: String) -> Unit = {}

    fun showPhotoDialog(callback: (path: String) -> Unit) {
        this.callback = callback
        checkFilePermission()
    }


    private fun checkFilePermission() {
        MyLog.show("checkFilePermission")
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                MyLog.show("requestPermissions")
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            }
        } else {

            showGallery()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) showGallery()
                else showNotPermissionDialog(R.string.not_access_galery)
            }

        }
    }

    private fun showNotPermissionDialog(message: Int) {
        MaterialDialog(context!!)
            .title(R.string.error)
            .message(message)
            .positiveButton(res = R.string.ok)
            .show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            var path = FileUtils.getPath(context!!, data.data!!)
            callback.invoke(path!!)
        }
    }


    private fun showGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }
}
