package com.deinega95.clientMqtt.view.fragments

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.deinega95.clientMqtt.R


abstract class BaseFragment : Fragment() {

    var toolBar: Toolbar? = null

    protected fun setToolbar(@StringRes title: Int, withBackButton: Boolean,view: View,backButton: Int = R.drawable.ic_arrow_back) {
        setToolbar(getString(title), withBackButton, view, backButton)
    }

    private fun setToolbar(title: String, withBackButton: Boolean, view: View, backButton: Int = R.drawable.ic_arrow_back) {
        toolBar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = title
        if (withBackButton) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            toolBar!!.setNavigationOnClickListener { activity?.onBackPressed() }
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(context!!, backButton))
        }
    }

    override fun onDestroy() {
        toolBar?.setNavigationOnClickListener(null)
        super.onDestroy()
    }
}