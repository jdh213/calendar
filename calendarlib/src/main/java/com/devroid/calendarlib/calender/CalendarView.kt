package com.devroid.calendarlib.calender

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.devroid.calendarlib.R
import kotlinx.android.synthetic.main.control_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarView : LinearLayout, CalendarSet {

    private var inflater: LayoutInflater? = null
    private var currentDate = Calendar.getInstance()

    private var dateColor = context.getColor(R.color.black)
    private var weekColor = context.getColor(R.color.gray)
    private var dayColor = context.getColor(R.color.gray)
    private var todayColor = context.getColor(R.color.pink)
    private var daySize: Float? = null
    private var daySelectPo = -1
    private var lineVisible = true

    private var selectColor = context.getColor(R.color.white)

    private var weekArray: ArrayList<TextView> = arrayListOf()

    private val days: ArrayList<Date?> = ArrayList()
    private val daySelectFlag: ArrayList<Boolean> = ArrayList()

    private var markerArrayList: ArrayList<CalendarMarkerModel> = ArrayList()
    private var markerColor = context.getColor(R.color.light_gray)

    private var onDateSelectedListener: OnDateSelectedListener? = null
    private var onMonthChangeListener: OnMonthChangeListener? = null

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)

    private var prevFlag = true
    private var nextFlag = true

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater?.inflate(R.layout.control_calendar, this)

        weekArray =
            arrayListOf(week_sun, week_mon, week_tue, week_wed, week_thu, week_fri, week_sat)

        setListener()
        updateCalendar()
    }

    private fun setListener() {
        calendar_next_button.setOnClickListener {
            nextMonth()
        }

        calendar_prev_button.setOnClickListener {
            prevMonth()
        }
    }

    private fun updateCalendar() {
        //Log.i("debugLog", "setSelectDate : ${currentDate.time}")
        days.clear()
        daySelectFlag.clear()

        val tempMarkers: ArrayList<CalendarMarkerModel> = ArrayList()

        val calendar: Calendar = currentDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val monthPrevCount: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val monthDayCount: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DAY_OF_MONTH, -monthPrevCount)

        val totalDayCount = monthPrevCount + monthDayCount

        while (days.size < totalDayCount) {
            val time = dateFormatter.parse(dateFormatter.format(calendar.time))
            days.add(time)
            tempMarkers.add(CalendarMarkerModel(null, false))

            if (daySelectPo == -1) {
                daySelectFlag.add(
                    dateFormatter.format(calendar.time) == dateFormatter.format(Calendar.getInstance().time)
                )
            } else {
                daySelectFlag.add(false)
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        if (daySelectPo != -1) {
            daySelectFlag[daySelectPo] = true
        }

        if (markerArrayList.size > 0) {
            repeat(markerArrayList.size) { i ->
                markerArrayList[i].date?.let { date ->
                    val markerTime = dateFormatter.parse(dateFormatter.format(date))
                    val index = days.indexOf(markerTime)
                    if (index != -1) {
                        tempMarkers[index] =
                            CalendarMarkerModel(markerTime, markerArrayList[i].complete)
                    }
                }
            }
        }

        val calendarAdapter =
            CalendarViewAdapter(currentDate, days, daySelectFlag, tempMarkers, lineVisible, this)
        calendarAdapter.onDateSelectedListener = onDateSelectedListener

        recycler_calendarView.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = calendarAdapter
            setSwipeListener(object : CalendarSwipe {
                override fun onSwipeLeft() {
                    if (prevFlag) {
                        prevMonth()
                    }
                }

                override fun onSwipeRight() {
                    if (nextFlag) {
                        nextMonth()
                    }
                }
            })
        }

        calendar_date.text =
            SimpleDateFormat("yyyy년 M월", Locale.KOREAN).format(currentDate.time)
    }

    private fun prevMonth() {
        currentDate.add(Calendar.MONTH, -1)
        daySelectPo = -1
        updateCalendar()
        onMonthChangeListener?.prevMonth(currentDate.time)
    }

    private fun nextMonth() {
        currentDate.add(Calendar.MONTH, 1)
        daySelectPo = -1
        updateCalendar()
        onMonthChangeListener?.nextMonth(currentDate.time)
    }


    fun setSelectDate(selectDate: Date) {
        currentDate.time = selectDate
        daySelectPo = -1
        updateCalendar()
    }

    fun setOnDateSelectedListener(listener: (View, Int, Date?) -> Unit) {
        onDateSelectedListener = object : OnDateSelectedListener {
            override fun dateSelected(view: View, position: Int, date: Date?) {
                daySelectPo = position
//                date?.let {
//                    currentDate.time = it
//                    Log.i("debugLog", "setSelectDate : ${currentDate.time}")
//                }
                updateCalendar()
                listener(view, position, date)
            }
        }
    }

    fun setOnMonthChangeListener(listener: (Date?) -> Unit) {
        onMonthChangeListener = object : OnMonthChangeListener {
            override fun prevMonth(date: Date?) {
                listener(date)
            }

            override fun nextMonth(date: Date?) {
                date?.let {
                    listener(date)
                }
            }
        }
    }

    fun setMarkers(markers: ArrayList<CalendarMarkerModel>) {
        this.markerArrayList.clear()
        this.markerArrayList.addAll(markers)
    }

    fun setLineVisible(flag: Boolean) {
        lineVisible = flag
    }

    fun setVisiblePrevButton() {
        calendar_prev_button.visibility = View.VISIBLE
        prevFlag = true
    }

    fun setInVisiblePrevButton() {
        calendar_prev_button.visibility = View.INVISIBLE
        prevFlag = false
    }

    fun setVisibleNextButton() {
        calendar_next_button.visibility = View.VISIBLE
        nextFlag = true
    }

    fun setInVisibleNextButton() {
        calendar_next_button.visibility = View.INVISIBLE
        nextFlag = false
    }

    fun build() {
        updateCalendar()
    }


    /**
    Calendar Interface
     */

    override fun setTitle(size: Float) {
        calendar_date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
    }

    override fun setTitle(size: Float, color: Int) {
        dateColor = color
        calendar_date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
        calendar_date.setTextColor(dateColor)
    }

    override fun getTitleColor(): Int {
        return dateColor
    }

    override fun setWeek(size: Float) {
        repeat(weekArray.size) { i ->
            weekArray[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
        }
    }

    override fun setWeek(size: Float, color: Int) {
        weekColor = color
        repeat(weekArray.size) { i ->
            weekArray[i].setTextColor(weekColor)
            weekArray[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
        }
    }

    override fun getWeekColor() = weekColor

    override fun setDay(size: Float) {
        daySize = size
    }

    override fun setDay(size: Float, color: Int) {
        daySize = size
        dayColor = color
    }

    override fun getDayColor() = dayColor
    override fun getDaySize() = daySize

    override fun setSelectColor(color: Int) {
        selectColor = color
    }

    override fun getSelectColor() = selectColor

    override fun setTodayColor(color: Int) {
        todayColor = color
    }

    override fun getTodayColor() = todayColor

    override fun setMarkerColor(color: Int) {
        markerColor = color
    }

    override fun getMarkerColor() = markerColor

}