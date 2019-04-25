package com.deinega95.clientMqtt.di.components

import com.deinega95.clientMqtt.di.scopes.ImageViewerScope
import com.deinega95.clientMqtt.view.fragments.ImageViewerFragment
import dagger.Subcomponent

@ImageViewerScope
@Subcomponent
interface ImageViewerComponent {
    fun inject(imageViewerFragment: ImageViewerFragment)
}