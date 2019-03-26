package com.deinega95.clientMqtt.di.components

import com.deinega95.clientMqtt.di.modules.AndroidModule
import com.deinega95.clientMqtt.view.activities.BaseActivity
import com.deinega95.clientMqtt.view.activities.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AndroidModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(baseActivity: MainActivity)
    fun getAuthorizationComponent(): AuthorizationComponent
    fun getMainComponent(): MainComponent

}