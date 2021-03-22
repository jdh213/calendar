package com.devroid.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.devroid.calendarlib.verticalCalendar.ProgressData
import kotlinx.android.synthetic.main.activity_vertical.*

class VerticalActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical)

        calendarView.apply {
            val progress: ArrayList<ProgressData> = arrayListOf()
            repeat(500) {
                progress.add(
                    ProgressData(
                        currentCalProgress = 50,
                        totalCalProgress = 100,
                        currentGoalProgress = 50,
                        totalGoalProgress = 100
                    )
                )
            }
            //이번달부터 end달까지
            setCalendarRange(0, -12, false, progress)

            //역순
            //setCalendarRange(-12, 1, true)

            //스크롤 마지막으로
            //setEndScroll(true)

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