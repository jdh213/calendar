package com.devroid.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_vertical.*

class VerticalActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical)

        //topText.text = calendarView.getNowDate()

        calendarView.apply {
            setCalendarRange(1, -12, false)
            //setCalendarRange(-12, 1, true)
            setEndScroll(true)

            onClickListener { day, month, year ->
                Log.i("debugLog", "day = $year / $month / $day")
            }

            onScrollListener { date ->
                Log.i("debugLog", "date = $date")
                date?.let {
                    if (it.isNotEmpty())
                        topText.text = it
                }
            }
        }
    }
}