package com.devroid.calendarlib.verticalCalendar

interface OnDayClickListener {
    fun onClick(day: Int, month: Int, year: Int) {}
}

interface OnScrollListener {
    fun onScroll(date: String) {}
    fun onScrollEnd() {}
}