package com.deinega95.clientMqtt.storage

import com.deinega95.clientMqtt.model.Message
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DbManager @Inject constructor() {
    private val realm: Realm = Realm.getDefaultInstance()

    init {

        realm.isAutoRefresh = true
    }

    fun getMessages(): List<Message> {
        return realm.where(Message::class.java).findAll()

    }

    fun addMessage(mes: Message) {
        realm.beginTransaction()
        realm.insertOrUpdate(mes)
        realm.commitTransaction()
    }

    fun clearDb() {
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

    fun closeDb() {
        realm.close()
    }
}