package com.devroid.calendarlib.verticalCalendar

import android.app.Service
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devroid.calendarlib.R
import com.devroid.calendarlib.databinding.VerticalCalendarViewBinding
import java.text.SimpleDateFormat
import java.util.*


class VerticalCalendarView : FrameLayout {

    private lateinit var binding: VerticalCalendarViewBinding

    private val layoutInflater =
        context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val calendarList: ArrayList<VerticalCalendarData> = arrayListOf()
    private val calendarDataList: ArrayList<ProgressData> = arrayListOf()

    private var calendarAdapter: VerticalCalendarViewAdapter? = null

    private var onScrollListener: OnScrollListener? = null
    private var onScrollEndListener: OnScrollEndListener? = null

    private var dateFormatter = SimpleDateFormat("yyyy. MM", Locale.KOREAN)

    private var startRange = 1
    private var endRange = -12

    private var monthTextSize = 14f
    private var monthTextColor = Color.BLACK

    private var dayTextSize = 12f
    private var dayTextColor = Color.BLACK

    private var todayTextColor = Color.RED
    private var todayBackgroundColor = Color.TRANSPARENT

    private var todayTextStyle = 0

    private var progressIndex = 0
    private val monthPositionArray: ArrayList<Int> = arrayListOf()
    private val monthArray: ArrayList<CalendarPositionData> = arrayListOf()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.VerticalCalendarView
            )
        )
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initAttr(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.VerticalCalendarView
            )
        )
        init()
    }

    private fun initAttr(typedArray: TypedArray) {
        monthTextSize =
            typedArray.getDimension(
                R.styleable.VerticalCalendarView_monthTextSize,
                monthTextSize
            )
        monthTextColor =
            typedArray.getColor(R.styleable.VerticalCalendarView_monthTextColor, monthTextColor)

        dayTextSize =
            typedArray.getDimension(
                R.styleable.VerticalCalendarView_dayTextSize,
                dayTextSize
            )
        dayTextColor =
            typedArray.getColor(R.styleable.VerticalCalendarView_dayTextColor, dayTextColor)

        todayTextColor =
            typedArray.getColor(R.styleable.VerticalCalendarView_todayTextColor, todayTextColor)

        todayTextStyle =
            typedArray.getInt(R.styleable.VerticalCalendarView_todayTextStyle, todayTextStyle)

        todayBackgroundColor = typedArray.getInt(R.styleable.VerticalCalendarView_todayBackgroundColor, todayBackgroundColor)
    }

    private fun init() {
        binding = VerticalCalendarViewBinding.inflate(layoutInflater)
        addView(binding.root)

        calendarAdapter = VerticalCalendarViewAdapter(
            calendarList,
            monthTextSize, monthTextColor,
            dayTextSize, dayTextColor, todayTextColor, todayTextStyle, todayBackgroundColor
        )

        //calendarAdapter?.setHasStableIds(true)

        val manager = GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        binding.rlCalendar.apply {
            layoutManager = manager
            adapter = calendarAdapter

            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {

                    //private val MONTH_TYPE = 0
                    //private val EMPTY_TYPE = 1
                    //private val DAY_TYPE = 2

                    return when (calendarAdapter?.getItemViewType(position)) {
                        0 -> {
                            7
                        }
                        1 -> {
                            1
                        }
                        else -> {
                            1
                        }
                    }
                }
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    var date = ""
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    layoutManager.findFirstVisibleItemPosition()

                    val first = layoutManager.findFirstVisibleItemPosition()
                    val last = layoutManager.findLastVisibleItemPosition()

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

                            onScrollEndListener?.onScrollEnd()
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
    }

    /*
    /**
     * 리버스 달력 세팅
     */
    private fun setReverseCalendarList(
        start: Int,
        end: Int
    ) {
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

            calendarList.add(VerticalCalendarData("month", calendar))

            for (j in 0 until dayOfWeek) {
                calendarList.add(VerticalCalendarData("empty"))
            }

            monthPositionArray.add(dayOfWeek)

            for (j in 1..endDay) {
                val progressPosition = if (i >= 0) {
                    0
                } else {
                    i + endDay + j
                }

                calendarList.add(
                    VerticalCalendarData(
                        "day", GregorianCalendar(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            j,
                            0,
                            0,
                            0
                        ), progressData = calendarDataList[progressPosition]
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
     */

    /**
     * 정상적인 달력 세팅
     */
    private fun setCalendarList(start: Int, end: Int) {
        monthArray.clear()

        val nowCalendar = GregorianCalendar()

        for (i in (start downTo end)) {
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

            calendarList.add(
                VerticalCalendarData(
                    "month",
                    calendar,
                    0
                )
            )

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
                        ), progressData = try {
                            calendarDataList[progressIndex]
                        } catch (e: Exception) {
                            ProgressData()
                        }
                    )
                )

                progressIndex++
            }
        }

        calendarList.forEachIndexed { index, verticalCalendarData ->
            if (verticalCalendarData.type == "month") {
                monthArray.add(CalendarPositionData(index, verticalCalendarData))
            }
        }

        repeat(monthArray.size) {
            calendarList[monthArray[it].index] = VerticalCalendarData(
                "month",
                monthArray[it].calendarData?.calendar,
                monthPositionArray[it]
            )
        }

        binding.rlCalendar.post {
            calendarAdapter?.notifyDataSetChanged()
        }
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
     * 스크롤 끝 리스너
     */
    fun onScrollEndListener(listener: () -> Unit) {
        onScrollEndListener = object : OnScrollEndListener {
            override fun onScrollEnd() {
                listener()
            }
        }
    }

    /**
     * 현재 시각
     */
    fun getNowDate(): String = dateFormatter.format(GregorianCalendar().timeInMillis)

    /**
     * 스크롤 값만큼 이동
     */
    fun setScroll(scrollY: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.rlCalendar.scrollBy(0, scrollY)
        }, 100)
    }

    /**
     * 달력 시작 월 ~ 종료 월 변경
     */
    fun setCalendarProgress(
        startRange: Int,
        endRange: Int,
        progressData: ArrayList<ProgressData>
    ) {
        this.startRange = startRange
        this.endRange = endRange

        calendarDataList.addAll(progressData)

        setCalendarList(startRange, endRange)
    }


    /**
     * 정상적인 달력 세팅
     */
    fun getCalendarDayCount(start: Int, end: Int): Int {
        var count = 0
        val nowCalendar = GregorianCalendar()

        for (i in (start downTo end)) {
            val calendar = GregorianCalendar(
                nowCalendar.get(Calendar.YEAR),
                nowCalendar.get(Calendar.MONTH) + i,
                1,
                0,
                0,
                0
            )

            val endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            for (j in 1..endDay) {
                count++
            }
        }

        return count
    }
}
