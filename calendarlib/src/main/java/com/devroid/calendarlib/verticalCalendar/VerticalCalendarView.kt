package com.devroid.calendarlib.verticalCalendar

import android.app.Service
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devroid.calendarlib.R
import kotlinx.android.synthetic.main.vertical_calendar_view.view.*
import java.util.*

class VerticalCalendarView : FrameLayout {
    private val layoutInflater =
        context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val calendarList: ArrayList<VerticalCalendarData> = arrayListOf()
    private val calendarAdapter = VerticalCalendarViewAdapter(calendarList)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        val content = layoutInflater.inflate(R.layout.vertical_calendar_view, null, false)
        addView(content)

        rl_calendar.apply {
            layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)

//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//
//                    Log.i("debugLog", "scroll")
//                }
//            })

            adapter = calendarAdapter
        }

        setCalendarList()
    }

    private fun setCalendarList() {
        val nowCalendar = GregorianCalendar()

        for (i in -12..0) {
            val calendar = GregorianCalendar(
                nowCalendar.get(Calendar.YEAR),
                nowCalendar.get(Calendar.MONTH) + i,
                1,
                0,
                0,
                0
            )

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
            val endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            for (j in 0 until dayOfWeek) {
                calendarList.add(VerticalCalendarData("empty", null))
            }

            for (j in 1..endDay) {
                calendarList.add(
                    VerticalCalendarData(
                        "day", GregorianCalendar(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            j,
                            0,
                            0,
                            0
                        )
                    )
                )
            }
        }

        calendarAdapter.notifyDataSetChanged()

        Handler(Looper.getMainLooper()).postDelayed({
            //rl_calendar.scroll
        }, 300)
    }
}
