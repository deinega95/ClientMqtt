package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.di.scopes.PhotoByPeriodScope
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.utils.toDateWithYear
import com.deinega95.clientMqtt.view.fragments.interfaces.ISelectPeriodPhotoFragment
import java.util.*
import javax.inject.Inject

@PhotoByPeriodScope
class SelectPeriodPhotoPresenter @Inject constructor() : BasePresenter<ISelectPeriodPhotoFragment>(), Observer {

    @Inject
    lateinit var mqttService: MqttService
    @Inject
    lateinit var viewRouter: ViewRouter

    private var startDate = Calendar.getInstance()
    private var endDate = Calendar.getInstance()


    override fun viewAttached() {
        mqttService.addObserver(this)
        updateStartDate()
        updateEndDate()
    }

    override fun viewDettached() {
        mqttService.deleteObserver(this)
        startDate = Calendar.getInstance()
        endDate = Calendar.getInstance()
    }

    fun onSelectStartDateClicked() {
        view?.showSelectStartDateDialog(R.string.selecting_start_date) { day, month, year ->
            startDate.set(Calendar.DAY_OF_MONTH, day)
            startDate.set(Calendar.MONTH, month)
            startDate.set(Calendar.YEAR, year)
            updateStartDate()
        }
    }


    fun onSelectStartTimeClicked() {
        view?.showSelectStartTimeDialog(R.string.selecting_start_time) { hours, minutes ->
            startDate.set(Calendar.HOUR_OF_DAY, hours)
            startDate.set(Calendar.MINUTE, minutes)
            updateStartDate()
        }
    }

    fun onSelectEndDateClicked() {
        view?.showSelectEndDateDialog(R.string.selecting_end_date) { day, month, year ->
            endDate.set(Calendar.DAY_OF_MONTH, day)
            endDate.set(Calendar.MONTH, month)
            endDate.set(Calendar.YEAR, year)
            updateEndDate()
        }
    }


    fun onSelectEndTimeClicked() {
        view?.showSelectEndTimeDialog(R.string.selecting_end_time) { hours, minutes ->
            endDate.set(Calendar.HOUR_OF_DAY, hours)
            endDate.set(Calendar.MINUTE, minutes)
            updateEndDate()
        }
    }

    private fun updateEndDate() {
        val time = endDate.time
        view?.showEndDate(time.toDateWithYear())
    }

    private fun updateStartDate() {
        val time = startDate.time
        view?.showStartDate(time.toDateWithYear())
    }

    fun onReadyClickListener() {
        val startTime = startDate.timeInMillis
        val endTime = endDate.timeInMillis
        val topicWithPhoto = "/${System.currentTimeMillis()}"
        if (endTime >= startTime) {
            mqttService.getPhotoByPeriod(topicWithPhoto, startTime, endTime)
        } else {
            viewRouter.showError(App.instance.getString(R.string.start_date_more_end_date_error))
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg is Int){
            MyLog.show("update selectPhotoPeriod $arg")
            if (arg == 0){
                viewRouter.showMessage(R.string.not_exist_photo_by_selected_period)
            }else{
                viewRouter.showPhotosByPeriodFragment()
            }
        }
    }
}