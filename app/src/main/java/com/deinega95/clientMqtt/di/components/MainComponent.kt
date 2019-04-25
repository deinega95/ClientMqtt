package com.deinega95.clientMqtt.di.components

import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.view.activities.MainActivity
import com.deinega95.clientMqtt.view.adapters.MessagesRecyclerViewAdapter
import com.deinega95.clientMqtt.view.adapters.TopicRecyclerViewAdapter
import com.deinega95.clientMqtt.view.fragments.TopicFragment
import dagger.Subcomponent

@Subcomponent
@MainScope
interface MainComponent {
    fun inject(topicFragment: TopicFragment)
    fun inject(topicFragment: MessagesRecyclerViewAdapter)
    fun inject(mainActivity: MainActivity)
    fun inject(topicRecyclerViewAdapter: TopicRecyclerViewAdapter)
    fun getPhotoByPeriodComponent(): PhotoByPeriodComponent
    fun getImageViewerComponent(): ImageViewerComponent
}