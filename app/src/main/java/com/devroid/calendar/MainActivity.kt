package com.devroid.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView.setOnDateSelectedListener { view, i, date ->
            Log.i("debugLog", "main position : $i / date : $date")
        }

//        calendarView.apply {
////            setTitle(20f)
////            setWeek(14f, Color.RED)
////            setDay(14f, Color.BLUE)
////        }

    }
}
