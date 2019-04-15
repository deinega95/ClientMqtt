package com.deinega95.clientMqtt.di.modules

import android.app.Application
import com.deinega95.clientMqtt.di.scopes.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val app: Application) {

    @Provides
    @ForApplication
    @Singleton
    internal fun provideApplicationContext() = app

}