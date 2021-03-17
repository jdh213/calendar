package com.devroid.calendarlib.verticalCalendar

import android.app.Service
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devroid.calendarlib.R
import kotlinx.android.synthetic.main.vertical_calendar_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class VerticalCalendarView : FrameLayout {
    private val layoutInflater =
        context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val calendarList: ArrayList<VerticalCalendarData> = arrayListOf()
    private var calendarAdapter: VerticalCalendarViewAdapter? = null

    private var onScrollListener: OnScrollListener? = null

    private var dateFormatter = SimpleDateFormat("yyyy. MM", Locale.KOREAN)

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

        calendarAdapter = VerticalCalendarViewAdapter(calendarList)

        rl_calendar.apply {
            layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
            adapter = calendarAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    var date = ""
                    val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager

                    val first = layoutManager.findFirstVisibleItemPositions(IntArray(7))[0]
                    val last = layoutManager.findLastVisibleItemPositions(IntArray(7))[0]

                    val center = (first + last) / 2

                    when {
                        !(recyclerView.canScrollVertically(-1)) -> {
                            //스크롤 처음
                            calendarList[first].calendar?.let {
                                date = dateFormatter.format(it.timeInMillis)
                            }
                        }
                        !(recyclerView.canScrollVertically(1)) -> {
                            //스크롤 마지막
                            calendarList[last].calendar?.let {
                                date = dateFormatter.format(it.timeInMillis)
                            }
                        }
                        else -> {
                            //중간지점
                            calendarList[center].calendar?.let {
                                date = dateFormatter.format(it.timeInMillis)
                            }
                        }
                    }
                    onScrollListener?.onScroll(date)
                }
            })
        }

        setCalendarList()
    }

    private fun setCalendarList() {
        val nowCalendar = GregorianCalendar()

        val monthPositionArray: ArrayList<Int> = arrayListOf()
        val monthArray: ArrayList<CalendarPositionData> = arrayListOf()

        for (i in -12..1) {
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

            calendarList.add(VerticalCalendarData("month", calendar, 0))

            for (j in 0 until dayOfWeek) {
                calendarList.add(VerticalCalendarData("empty", null))
            }

            monthPositionArray.add(dayOfWeek)

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

        calendarList.forEachIndexed { index, verticalCalendarData ->
            if (verticalCalendarData.type == "month") {
                monthArray.add(CalendarPositionData(index, verticalCalendarData))
            }
        }

        repeat(monthArray.size) {
            calendarList[monthArray[it].index] = VerticalCalendarData(
                "month", monthArray[it].calendarData?.calendar, monthPositionArray[it]
            )
        }

        calendarAdapter?.notifyDataSetChanged()

        Handler(Looper.getMainLooper()).postDelayed({
            //rl_calendar.scroll
        }, 300)
    }

    fun onClickListener(listener: (day: Int?, month: Int?, year: Int?) -> Unit) {
        calendarAdapter?.onClickListener = object : OnDayClickListener {
            override fun onClick(day: Int, month: Int, year: Int) {
                listener(day, month + 1, year)
            }
        }
    }

    fun onScrollListener(listener: (date: String?) -> Unit) {
        onScrollListener = object : OnScrollListener {
            override fun onScroll(date: String) {
                listener(date)
            }
        }
    }

    fun getNowDate() = dateFormatter.format(GregorianCalendar().timeInMillis)
}
