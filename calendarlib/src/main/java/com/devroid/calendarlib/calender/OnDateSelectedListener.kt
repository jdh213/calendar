package com.devroid.calendarlib.calender

import android.view.View
import java.util.*

interface OnDateSelectedListener {
    fun dateSelected(view: View, position: Int, date: Date?) {}
}

interface OnMonthChangeListener {
    fun prevMonth(date: Date?) {}
    fun nextMonth(date: Date?) {}
}