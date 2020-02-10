package com.devroid.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.devroid.calendarlib.CalendarMarkerModel
import com.devroid.calendarlib.MarkerType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val array: ArrayList<CalendarMarkerModel> = arrayListOf()

        repeat(50) { i ->
            when {
                i % 3 == 0 -> array.add(CalendarMarkerModel(MarkerType.INVISIBLE))
                i % 3 == 1 -> {
                    array.add(CalendarMarkerModel(MarkerType.FILL))
                }
                else -> {
                    array.add(CalendarMarkerModel(MarkerType.STOKE))
                }
            }
        }

        calendarView.apply {
            setLineVisible(true)
            setMarkers(array)
            setOnDateSelectedListener { view, i, date ->
                Log.i("debugLog", "main position : $i / date : $date")
            }
            build()
        }
    }
}
