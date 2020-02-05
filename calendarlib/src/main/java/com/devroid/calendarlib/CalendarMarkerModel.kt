package com.devroid.calendarlib

data class CalendarMarkerModel(
    val markerType: MarkerType = MarkerType.INVISIBLE
)

enum class MarkerType {
    FILL,
    STOKE,
    INVISIBLE
}