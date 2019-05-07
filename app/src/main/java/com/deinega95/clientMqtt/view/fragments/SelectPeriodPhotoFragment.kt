package com.deinega95.clientMqtt.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.presenter.SelectPeriodPhotoPresenter
import com.deinega95.clientMqtt.view.fragments.interfaces.ISelectPeriodPhotoFragment
import kotlinx.android.synthetic.main.fragment_select_period_photo.*
import java.util.*
import javax.inject.Inject

class SelectPeriodPhotoFragment : BaseFragment(), ISelectPeriodPhotoFragment {

    @Inject
    lateinit var presenter: SelectPeriodPhotoPresenter


    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePicker: TimePickerDialog
    private var startTime: TextView? = null
    private var endTime: TextView? = null
    private var startDateBtn: TextView? = null
    private var endDateBtn: TextView? = null
    private var startTimeBtn: TextView? = null
    private var endTimeBtn: TextView? = null


    init {
        App.instance.photoByPeriodComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_select_period_photo, container, false)
        setToolbar(R.string.photo_from_camera,  true, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectStartDateBtn.setOnClickListener {
            presenter.onSelectStartDateClicked()
        }
        selectEndDateBtn.setOnClickListener {
            presenter.onSelectEndDateClicked()
        }
        selectStartTimeBtn.setOnClickListener {
            presenter.onSelectStartTimeClicked()
        }
        selectEndTimeBtn.setOnClickListener {
            presenter.onSelectEndTimeClicked()
        }

        readyBtn.setOnClickListener {
            presenter.onReadyClickListener()
        }

        startTime = startDateTimeTV
        endTime = endDateTimeTV
        startDateBtn = selectStartDateBtn
        endDateBtn = selectEndDateBtn
        endTimeBtn = selectEndTimeBtn
        startTimeBtn = selectStartTimeBtn

        presenter.viewReady(this)
    }


    override fun showSelectStartDateDialog(title: Int, callback: (day: Int, month: Int, year: Int) -> Unit) {
        showDateDialog(title, callback)
    }

    override fun showSelectStartTimeDialog(title: Int, callback: (hours: Int, minutes: Int) -> Unit) {
        showTimeDialog(title, callback)
    }

    override fun showSelectEndTimeDialog(title: Int, callback: (hours: Int, minutes: Int) -> Unit) {
        showTimeDialog(title, callback)
    }

    override fun showSelectEndDateDialog(title: Int, callback: (day: Int, month: Int, year: Int) -> Unit) {
        showDateDialog(title, callback)
    }

    private fun showDateDialog(title: Int, callback: (day: Int, month: Int, year: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        datePickerDialog = DatePickerDialog(
            context!!, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { view, yearD, monthD, dayOfMonthD ->
                callback.invoke(dayOfMonthD, monthD, yearD)
            },
            year, month, day
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Выбрать", datePickerDialog)
        datePickerDialog.setTitle(title)
        datePickerDialog.show()
    }

    private fun showTimeDialog(title: Int, callback: (hours: Int, minutes: Int) -> Unit) {
        timePicker = TimePickerDialog(
            context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                callback.invoke(hourOfDay, minute)
            }, 12, 0, true
        )

        timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        timePicker.setTitle(title)
        timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Выбрать", timePicker)
        timePicker.show()
    }


    override fun showStartDate(date: String) {
        startTime?.text = date
    }

    override fun showEndDate(date: String) {
        endTime?.text = date
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDied(this)
    }

}