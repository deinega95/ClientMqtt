package com.deinega95.clientMqtt.di

import android.app.Application
import com.deinega95.clientMqtt.di.components.*
import com.deinega95.clientMqtt.di.modules.AndroidModule
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import io.realm.Realm
import io.realm.RealmConfiguration


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

    var imageViewerComponent: ImageViewerComponent? = null
        get() {
            if (field == null) {
                field = mainComponent?.getImageViewerComponent()!!
            }
            return field
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
        BigImageViewer.initialize(GlideImageLoader.with(instance))

        Realm.init(instance)
        val realmConfig = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .name("mqtt-diplom-realm")
            //    .inMemory()   /////when set,saving only for session
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    fun clear() {
        imageViewerComponent = null
        photoByPeriodComponent = null
        mainComponent = null
        authorizationComponent = null
    }
}