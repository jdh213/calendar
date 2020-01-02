package com.devroid.calendarlib

import android.view.View
import java.util.*

interface OnDateSelectedListener {
    fun dateSelected(view: View, position: Int, date: Date) {}
}