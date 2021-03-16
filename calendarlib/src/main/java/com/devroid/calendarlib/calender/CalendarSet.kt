package com.devroid.calendarlib.calender

interface CalendarSet {
    fun setTitle(size: Float) {}
    fun setTitle(size: Float, color: Int) {}
    fun getTitleColor(): Int

    fun setWeek(size: Float) {}
    fun setWeek(size: Float, color: Int) {}
    fun getWeekColor(): Int

    fun setDay(size: Float) {}
    fun setDay(size: Float, color: Int) {}
    fun getDayColor(): Int
    fun getDaySize(): Float?

    fun setSelectColor(color: Int)
    fun getSelectColor(): Int

    fun setTodayColor(color: Int)
    fun getTodayColor(): Int

    fun setMarkerColor(color: Int)
    fun getMarkerColor(): Int
}