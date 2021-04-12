package com.devroid.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.devroid.calendarlib.verticalCalendar.ProgressData
import kotlinx.android.synthetic.main.activity_vertical.*

class VerticalActivity : AppCompatActivity() {
    private var start = 1
    private var end = -1
    private val progress: ArrayList<ProgressData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical)

        calendarView.apply {
            onClickListener { day, month, year ->
                Log.i("debugLog", "day = $year / $month / $day")
            }

            val firstCount = getCalendarDayCount(start, end)

            repeat(firstCount) {
                progress.add(
                    ProgressData(
                        currentCalProgress = 10,
                        totalCalProgress = 100,
                        currentGoalProgress = 10,
                        totalGoalProgress = 100
                    )
                )
            }
            //이번달부터 end달까지
            setCalendarProgress(start, end, progress)
            setScroll(100)

            onScrollListener { date ->
                date?.let {
                    if (it.isNotEmpty())
                        topText.text = it
                }
            }

            onScrollEndListener {
                start -= 1
                end -= 1

                val nextCount = getCalendarDayCount(start, end)

                repeat(nextCount) {
                    progress.add(
                        ProgressData(
                            currentCalProgress = 10,
                            totalCalProgress = 100,
                            currentGoalProgress = 10,
                            totalGoalProgress = 100
                        )
                    )
                }

                setCalendarProgress(start, end, progress)
            }
        }
    }
}