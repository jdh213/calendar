package com.devroid.calendarlib

import android.graphics.Color

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

    fun setSelectedToday(select: Boolean = false) {}
    fun getSelectedToday(): Boolean

    fun setSelectColor(color: Int = Color.RED)
    fun getSelectColor(): Int
}