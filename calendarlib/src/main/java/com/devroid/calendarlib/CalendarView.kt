package com.devroid.calendarlib

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
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
import kotlin.math.ceil


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

    private val days: ArrayList<Date> = ArrayList()
    private val daySelectFlag: ArrayList<Boolean> = ArrayList()

    private var markers: ArrayList<CalendarMarkerModel> = ArrayList()
    private var markerColor = context.getColor(R.color.light_gray)

    private var onDateSelectedListener: OnDateSelectedListener? = null

    private var gestureDetector: GestureDetector? = null

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

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)

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

        val calendarAdapter =
            CalendarViewAdapter(currentDate, days, daySelectFlag, markers, lineVisible, this)
        calendarAdapter.onDateSelectedListener = onDateSelectedListener

        recycler_calendarView.layoutManager = GridLayoutManager(context, 7)
        recycler_calendarView.adapter = calendarAdapter

        recycler_calendarView.setOnTouchListener { v, event ->
            Log.i("debugLog", "event : $event")
            gestureDetector?.onTouchEvent(event)
            true
        }


        calendar_date.text =
            SimpleDateFormat("yyyy년 M월", Locale.KOREAN).format(currentDate.time)

    }

    fun setOnDateSelectedListener(listener: (View, Int, Date) -> Unit) {
        onDateSelectedListener = object : OnDateSelectedListener {
            override fun dateSelected(view: View, position: Int, date: Date) {
                daySelectPo = position
                updateCalendar()
                listener(view, position, date)
            }
        }
    }

    fun setMarkers(markers: ArrayList<CalendarMarkerModel>) {
        this.markers.clear()
        this.markers = markers
    }

    fun setLineVisible(flag: Boolean) {
        lineVisible = flag
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

    /**
     * CalendarView Day Adapter
     */

    class CalendarViewAdapter(
        private val calendar: Calendar,
        private val day: ArrayList<Date>,
        private val daySelectFlag: ArrayList<Boolean>,
        private val markers: ArrayList<CalendarMarkerModel>,
        private val lineVisible: Boolean,
        private val calendarSet: CalendarSet
    ) :
        RecyclerView.Adapter<CalendarViewAdapter.ViewHolder>() {

        var onDateSelectedListener: OnDateSelectedListener? = null

        private var dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        private var dayFormatter = SimpleDateFormat("d", Locale.KOREAN)
        private var monthFormatter = SimpleDateFormat("yyyy년 M월", Locale.KOREAN)
        private var circle = ShapeDrawable(OvalShape())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.control_calendar_day, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return day.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.apply {
                calendarSet.apply {
                    if (monthFormatter.format(day[position]) == monthFormatter.format(calendar.time)) {
                        day_text.text = dayFormatter.format(day[position])
                        circle.paint.color = context.getColor(R.color.pink)
                        day_back.background = circle

                        // 클릭된 일의 플래그 true 나머지 false
                        if (daySelectFlag[position]) {
                            day_text.setTextColor(getSelectColor())
                            day_back.visibility = View.VISIBLE
                        } else {
                            day_back.visibility = View.GONE
                            day_text.setTextColor(
                                if (dateFormatter.format(day[position]) == dateFormatter.format(
                                        Calendar.getInstance().time
                                    )
                                ) {
                                    getTodayColor()
                                } else {
                                    getDayColor()
                                }
                            )
                        }

                        getDaySize()?.let {
                            day_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, it)
                        }


                        val future = if (daySelectFlag[position]) {
                            false
                        } else {
                            day[position].after(Calendar.getInstance().time)
                        }

                        val markerCircle = GradientDrawable()
                        markerCircle.setStroke(2, getMarkerColor())
                        markerCircle.shape = GradientDrawable.OVAL

                        if (markers.size > 0) {
                            when (markers[position].markerType) {
                                MarkerType.INVISIBLE -> {
                                    mark_image.visibility = View.INVISIBLE
                                }

                                MarkerType.FILL -> {
                                    mark_image.visibility = View.VISIBLE

                                    markerCircle.setColor(getMarkerColor())
                                    mark_image.background = markerCircle

                                    if (future) {
                                        day_text.setTextColor(context.getColor(R.color.black))
                                    }
                                }

                                MarkerType.STOKE -> {
                                    mark_image.visibility = View.VISIBLE
                                    mark_image.background = markerCircle

                                    if (future) {
                                        day_text.setTextColor(context.getColor(R.color.black))
                                    }
                                }
                            }
                        }

                        setOnClickListener {
                            onDateSelectedListener?.dateSelected(it, position, day[position])
                        }
                    }

                    val rowCount = ceil(day.size / 7.0)
                    val positionRowCount = position / 7.0

                    if (lineVisible) {
                        line.visibility = (if (positionRowCount < rowCount - 1.0) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        })
                    } else {
                        line.visibility = View.GONE
                    }
                }
            }
        }

        inner class ViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer
    }
}