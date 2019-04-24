package com.deinega95.clientMqtt.view.fragments.interfaces

import androidx.annotation.StringRes

interface ISelectPeriodPhotoFragment {
     fun showSelectStartDateDialog(@StringRes title: Int,  callback:(day:Int, month:Int, year:Int)->Unit)
     fun showSelectStartTimeDialog(@StringRes title: Int, callback:(hours:Int, minutes:Int)->Unit)
     fun showSelectEndTimeDialog(@StringRes title: Int, callback:(hours:Int, minutes:Int)->Unit)
     fun showSelectEndDateDialog(@StringRes title: Int, callback:(day:Int, month:Int, year:Int)->Unit)
     fun showStartDate(date: String)
     fun showEndDate(date: String)
}