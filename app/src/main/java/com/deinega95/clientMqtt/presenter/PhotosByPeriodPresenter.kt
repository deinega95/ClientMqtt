package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.di.scopes.PhotoByPeriodScope
import com.deinega95.clientMqtt.view.fragments.interfaces.IPhotosByPeriodFragment
import javax.inject.Inject

@PhotoByPeriodScope
class PhotosByPeriodPresenter @Inject constructor(): BasePresenter<IPhotosByPeriodFragment>() {
    override fun viewDettached() {

    }

    override fun viewAttached() {

    }
}