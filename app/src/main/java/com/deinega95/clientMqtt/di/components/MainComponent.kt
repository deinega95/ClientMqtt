package com.deinega95.clientMqtt.di.components

import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.view.fragments.TopicFragment
import dagger.Subcomponent

@Subcomponent
@MainScope
interface MainComponent {
    fun inject(topicFragment: TopicFragment)
}