package com.devroid.calendarlib.verticalCalendar

import android.graphics.Color
import java.util.*

data class VerticalCalendarData(
    var type: String = "empty",
    var calendar: GregorianCalendar? = null,
    var monthPosition: Int = 0,
    var progressData: ProgressData = ProgressData()
)

data class ProgressData(
    var currentCalProgress: Int = 0,
    var totalCalProgress: Int = 100,
    var currentGoalProgress: Int = 0,
    var totalGoalProgress: Int = 100,
    var calColor: Int = Color.parseColor("#F36262"),
    var goalColor: Int = Color.parseColor("#62B4FF")
)

data class CalendarPositionData(
    var index: Int = 0,
    var calendarData: VerticalCalendarData? = null
)