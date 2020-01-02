package com.devroid.calendarlib

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.control_calendar.view.*
import kotlinx.android.synthetic.main.control_calendar_day.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarView : LinearLayout, CalendarSet {

    private var inflater: LayoutInflater? = null
    private var currentDate = Calendar.getInstance()

    private var dateColor = context.getColor(R.color.black)
    private var weekColor = context.getColor(R.color.black)
    private var dayColor = context.getColor(R.color.black)
    private var daySize: Float? = null
    private var daySelectPo = -1

    private var weekArray: ArrayList<TextView> = arrayListOf()

    private val days: ArrayList<Date> = ArrayList()
    private val daySelectFlag: ArrayList<Boolean> = ArrayList()

    private var onDateSelectedListener: OnDateSelectedListener? = null

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
            currentDate.add(Calendar.MONTH, 1)
            daySelectPo = -1
            updateCalendar()
        }

        calendar_prev_button.setOnClickListener {
            currentDate.add(Calendar.MONTH, -1)
            daySelectPo = -1
            updateCalendar()
        }
    }

    private fun updateCalendar() {
        days.clear()
        daySelectFlag.clear()

        val calendar: Calendar = currentDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val monthPrevCount: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val monthDayCount: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DAY_OF_MONTH, -monthPrevCount)

        val totalDayCount = monthPrevCount + monthDayCount

        while (days.size < totalDayCount) {
            days.add(calendar.time)
            daySelectFlag.add(false)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        if (daySelectPo != -1) {
            daySelectFlag[daySelectPo] = true
        }

        val calendarAdapter = CalendarViewAdapter(currentDate, days, daySelectFlag, this)
        calendarAdapter.onDateSelectedListener = onDateSelectedListener

        recycler_calendarView.layoutManager = GridLayoutManager(context, 7)
        recycler_calendarView.adapter = calendarAdapter

        calendar_date.text =
            SimpleDateFormat("yyyy년 M월", Locale.KOREAN).format(currentDate.time)
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

    override fun getWeekColor(): Int {
        return weekColor
    }

    override fun setDay(size: Float) {
        daySize = size
        updateCalendar()
    }

    override fun setDay(size: Float, color: Int) {
        daySize = size
        dayColor = color
        updateCalendar()
    }

    override fun getDayColor(): Int {
        return dayColor
    }

    override fun getDaySize(): Float? {
        return daySize
    }

    fun setOnDateSelectedListener(listener: (View, Int, Date) -> Unit) {
        onDateSelectedListener = object : OnDateSelectedListener {
            override fun dateSelected(view: View, position: Int, date: Date) {
                daySelectPo = position
                updateCalendar()
                listener(view, position, date)
            }
        }
        updateCalendar()
    }

    /**
     * CalendarView Day Adapter
     */

    class CalendarViewAdapter(
        private val calendar: Calendar,
        private val day: ArrayList<Date>,
        private val daySelectFlag: ArrayList<Boolean>,
        private val calendarSet: CalendarSet
    ) :
        RecyclerView.Adapter<CalendarViewAdapter.ViewHolder>() {

        var onDateSelectedListener: OnDateSelectedListener? = null

        private var dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        private var dayFormatter = SimpleDateFormat("d", Locale.KOREAN)
        private var monthFormatter = SimpleDateFormat("yyyy년 M월", Locale.KOREAN)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.control_calendar_day, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return day.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.apply {
                if (monthFormatter.format(day[position]) == monthFormatter.format(calendar.time)) {

                    day_text.text = dayFormatter.format(day[position])

                    if (dateFormatter.format(day[position]) == dateFormatter.format(Calendar.getInstance().time)) {
                        day_text.setTextAppearance(R.style.day_bold_textStyle)
                    } else {
                        day_text.setTextAppearance(R.style.day_textStyle)
                    }

                    calendarSet.apply {
                        if (daySelectFlag[position]) {
                            day_text.setTextColor(Color.RED)
                        } else {
                            day_text.setTextColor(getDayColor())
                        }

                        getDaySize()?.let {
                            day_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, it)
                        }
                    }

                    setOnClickListener {
                        onDateSelectedListener?.dateSelected(it, position, day[position])
                    }
                }
            }
        }

        inner class ViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer
    }
}