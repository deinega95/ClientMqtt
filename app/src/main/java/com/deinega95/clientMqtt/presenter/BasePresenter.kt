package com.deinega95.clientMqtt.presenter

abstract class BasePresenter<V> {
    var view : V?=null

    fun viewReady(view:V){
        this.view = view
        viewAttached()
    }

    fun viewDied(view:V){
        this.view = view
        viewDettached()
    }

    abstract fun viewDettached()

    abstract fun viewAttached()

}