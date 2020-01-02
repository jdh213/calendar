package com.devroid.calendarlib

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
}