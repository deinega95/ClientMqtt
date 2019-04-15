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
        private const val USERNAME = "USERNAME"
        private const val PASSWORD = "CLIENT_ID"
    }


    init {
        sharedPreferences = App.instance.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE)
    }

    fun getServer(): String? {
        return sharedPreferences.getString(SERVER, null)
    }

    fun getUsername(): String? {

        return sharedPreferences.getString(USERNAME, null)
    }

    fun saveServer(server: String,username:String?, pass:String?) {
        sharedPreferences
            .edit()
            .putString(SERVER, server)
            .putString(USERNAME, username)
            .putString(PASSWORD, pass)
            .apply()
    }

    fun isAuthorized(): Boolean {
        return (getServer()!=null && getUsername()!=null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString(PASSWORD, "")
    }


}
