package com.devroid.calendarlib.verticalCalendar

import java.util.*

data class VerticalCalendarData(
    var type: String = "empty",
    var calendar: GregorianCalendar? = null,
    var monthPosition: Int = 0
)

data class CalendarPositionData(
    var index: Int = 0,
    var calendarData: VerticalCalendarData? = null
)