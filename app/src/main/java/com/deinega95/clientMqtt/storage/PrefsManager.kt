package com.deinega95.clientMqtt.storage

import android.content.Context
import android.content.SharedPreferences
import com.deinega95.clientMqtt.di.App
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefsManager @Inject constructor() {

    private val sharedPreferences: SharedPreferences

    companion object {

        private const val NAME_PREFS = "Client_MQTT_NAME"
        private const val SERVER = "SERVER"
        private const val CLIENT_ID = "CLIENT_ID"
    }


    init {
        sharedPreferences = App.instance.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE)
    }

    fun getServer(): String? {
        return sharedPreferences.getString(SERVER, null)
    }

    fun getClientId(): String? {

        return sharedPreferences.getString(CLIENT_ID, null)
    }

    fun saveServer(server: String, clientId: String) {
        sharedPreferences
            .edit()
            .putString(SERVER, server)
            .putString(CLIENT_ID, clientId)
            .apply()
    }

    fun isAuthorized(): Boolean {
        return (getServer()!=null && getClientId()!=null)
    }
}
