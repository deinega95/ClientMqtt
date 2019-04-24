package com.deinega95.clientMqtt.di

import android.app.Application
import com.deinega95.clientMqtt.di.components.*
import com.deinega95.clientMqtt.di.modules.AndroidModule


class App : Application() {
    companion object {
        lateinit var instance: App
    }

    var component: ApplicationComponent? = null
        get() {
            if (field == null) {
                field = DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
            }
            return field
        }

    var authorizationComponent: AuthorizationComponent? = null
        get() {
            if (field == null) {
                field = component?.getAuthorizationComponent()!!
            }
            return field
        }

    var mainComponent: MainComponent? = null
        get() {
            if (field == null) {
                field = component?.getMainComponent()!!
            }
            return field
        }

    var photoByPeriodComponent: PhotoByPeriodComponent? = null
        get() {
            if (field == null) {
                field = mainComponent?.getPhotoByPeriodComponent()!!
            }
            return field
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun clear(){
        mainComponent = null
        authorizationComponent = null
    }

}
