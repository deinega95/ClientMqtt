package com.deinega95.clientMqtt.di.components

import com.deinega95.clientMqtt.di.modules.AndroidModule
import com.deinega95.clientMqtt.services.MyFirebaseMessagingService
import com.deinega95.clientMqtt.view.activities.BaseActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AndroidModule::class])
@Singleton
interface ApplicationComponent {
    fun getAuthorizationComponent(): AuthorizationComponent
    fun getMainComponent(): MainComponent
    fun inject(baseActivity: BaseActivity)
    fun inject(firebase: MyFirebaseMessagingService)

}