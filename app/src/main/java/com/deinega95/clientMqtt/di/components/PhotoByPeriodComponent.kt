package com.deinega95.clientMqtt.di.components

import com.deinega95.clientMqtt.di.scopes.PhotoByPeriodScope
import com.deinega95.clientMqtt.view.adapters.PhotosByPeriodRecyclerViewAdapter
import com.deinega95.clientMqtt.view.fragments.PhotosByPeriodFragment
import com.deinega95.clientMqtt.view.fragments.SelectPeriodPhotoFragment
import dagger.Subcomponent

@PhotoByPeriodScope
@Subcomponent
interface PhotoByPeriodComponent {
    fun inject(selectPeriodPhotoFragment: SelectPeriodPhotoFragment)
    fun inject(photosByPeriodFragment: PhotosByPeriodFragment)
    fun inject(photosByPeriodRecyclerViewAdapter: PhotosByPeriodRecyclerViewAdapter)
}