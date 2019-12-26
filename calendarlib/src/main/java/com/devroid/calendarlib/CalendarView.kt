package com.devroid.calendarlib

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
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

class CalendarView : LinearLayout {

    private var currentDate = Calendar.getInstance()
    private var dateColor = Color.BLACK
    private var weekColor = Color.BLACK
    private var dayColor = Color.BLACK
    private var daySize = 12f
    private var inflater: LayoutInflater? = null

    private var weekArray: ArrayList<TextView> = arrayListOf()

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
            updateCalendar()
        }

        calendar_prev_button.setOnClickListener {
            currentDate.add(Calendar.MONTH, -1)
            updateCalendar()
        }
    }

    private fun updateCalendar() {
        val days: ArrayList<Date> = ArrayList()

        val calendar: Calendar = currentDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val monthPrevCount: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val monthDayCount: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DAY_OF_MONTH, -monthPrevCount)

        val totalDayCount = monthPrevCount + monthDayCount

        while (days.size < totalDayCount) {
            days.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        recycler_calendarView.adapter = CalendarViewAdapter(currentDate, days, dayColor, daySize)
        recycler_calendarView.layoutManager = GridLayoutManager(context, 7)

        calendar_date.text =
            SimpleDateFormat("yyyy-M", Locale.KOREAN).format(currentDate.time)
    }

    fun setTitle(size: Float) {
        calendar_date.textSize = size
    }

    fun setTitle(size: Float, color: Int = Color.BLACK) {
        dateColor = color
        calendar_date.textSize = size
        calendar_date.setTextColor(dateColor)
    }

    fun setWeek(size: Float) {
        repeat(weekArray.size) { i ->
            weekArray[i].textSize = size
        }
    }

    fun setWeek(size: Float, color: Int = Color.BLACK) {
        weekColor = color
        repeat(weekArray.size) { i ->
            weekArray[i].setTextColor(weekColor)
            weekArray[i].textSize = size
        }
    }

    fun setDay(size: Float) {
        daySize = size
        updateCalendar()
    }

    fun setDay(size: Float, color: Int = Color.BLACK) {
        daySize = size
        dayColor = color
        updateCalendar()
    }

    class CalendarViewAdapter(
        private val calendar: Calendar,
        private val day: ArrayList<Date>,
        private val dayColor: Int,
        private val daySize: Float
    ) :
        RecyclerView.Adapter<CalendarViewAdapter.ViewHolder>() {

        private var dayFormatter = SimpleDateFormat("d", Locale.KOREAN)
        private var monthFormatter = SimpleDateFormat("yyyy-M", Locale.KOREAN)

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

                    day_text.setTextColor(dayColor)
                    day_text.textSize = daySize
                    day_text.text = dayFormatter.format(day[position])

                    setOnClickListener {
                        Log.i("debugLog", day[position].toString())
                    }
                }
            }
        }

        inner class ViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer
    }
}