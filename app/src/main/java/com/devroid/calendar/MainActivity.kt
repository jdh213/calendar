package com.devroid.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.devroid.calendarlib.CalendarMarkerModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse("2020-02-11")
        val date2 = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse("2020-02-14")
        val array: ArrayList<CalendarMarkerModel> = arrayListOf()
        array.add(CalendarMarkerModel(date, true))
        array.add(CalendarMarkerModel(date2, false))

        calendarView.apply {
            setLineVisible(true)
            setMarkers(array)
            setOnDateSelectedListener { _, i, date ->
                Log.i("debugLog", "main position : $i / date : $date")
            }
            build()
        }
    }
}
