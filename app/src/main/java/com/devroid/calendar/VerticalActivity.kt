package com.devroid.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_vertical.*

class VerticalActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical)

        topText.text = calendarView.getNowDate()

        calendarView.onClickListener { day, month, year ->
            Log.i("debugLog", "day = $day / $month / $year")
        }

        calendarView.onScrollListener { date ->
            Log.i("debugLog", "date = $date")
            date?.let {
                if (it.isNotEmpty())
                    topText.text = it
            }
        }
    }
}