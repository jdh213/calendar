package com.devroid.calendarlib.verticalCalendar

import android.app.Service
import android.content.Context
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

    private var startRange = -12
    private var endRange = 1
    private var reverse = false

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
        calendarAdapter?.setHasStableIds(true)

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

        if (reverse) {
            setReverseCalendarList(startRange, endRange)
        } else {
            setCalendarList(startRange, endRange)
        }
    }

    /**
     * 리버스 달력 세팅
     */
    private fun setReverseCalendarList(start: Int, end: Int) {
        calendarList.clear()

        val nowCalendar = GregorianCalendar()

        val monthPositionArray: ArrayList<Int> = arrayListOf()
        val monthArray: ArrayList<CalendarPositionData> = arrayListOf()

        for (i in start..end) {
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
    }

    /**
     * 정상적인 달력 세팅
     */
    private fun setCalendarList(start: Int, end: Int) {
        calendarList.clear()

        val nowCalendar = GregorianCalendar()

        val monthPositionArray: ArrayList<Int> = arrayListOf()
        val monthArray: ArrayList<CalendarPositionData> = arrayListOf()

        for (i in start downTo end) {
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
    }

    /**
     * 클릭 리스너
     */
    fun onClickListener(listener: (day: Int?, month: Int?, year: Int?) -> Unit) {
        calendarAdapter?.onClickListener = object : OnDayClickListener {
            override fun onClick(day: Int, month: Int, year: Int) {
                listener(day, month + 1, year)
            }
        }
    }

    /**
     * 스크롤 리스너
     */
    fun onScrollListener(listener: (date: String?) -> Unit) {
        onScrollListener = object : OnScrollListener {
            override fun onScroll(date: String) {
                listener(date)
            }
        }
    }

    /**
     * 현재 시각
     */
    fun getNowDate(): String = dateFormatter.format(GregorianCalendar().timeInMillis)

    /**
     * true = 스크롤 마지막으로
     */
    fun setEndScroll(end: Boolean = true) {
        if (end && reverse) {
            //l_calendar.scrollToPosition(calendarList.size - 20)
            rl_calendar.smoothScrollToPosition(calendarList.size - 20)
        }
    }

    /**
     * 달력 시작 월~ 종료 월 변경
     */
    fun setCalendarRange(start: Int, end: Int, reverse: Boolean) {
        startRange = start
        endRange = end

        this.reverse = reverse

        if (reverse) {
            setReverseCalendarList(startRange, endRange)
        } else {
            setCalendarList(startRange, endRange)
        }
    }
}
