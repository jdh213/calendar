package com.devroid.calendarlib.calender

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devroid.calendarlib.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.control_calendar_day.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class CalendarViewAdapter(
    private val calendar: Calendar,
    private val day: ArrayList<Date?>,
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
                day[position]?.let { date ->
                    if (monthFormatter.format(date) == monthFormatter.format(calendar.time)) {
                        day_text.text = dayFormatter.format(date)
                        circle.paint.color = context.getColor(R.color.pink)
                        day_back.background = circle

                        // 클릭된 일의 플래그 true 나머지 false
                        if (daySelectFlag[position]) {
                            day_text.setTextColor(getSelectColor())
                            day_back.visibility = View.VISIBLE
                        } else {
                            day_back.visibility = View.GONE
                            day_text.setTextColor(
                                if (dateFormatter.format(date) == dateFormatter.format(
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
                            day[position]?.after(Calendar.getInstance().time)
                        }

                        val markerCircle = GradientDrawable()
                        markerCircle.setStroke(2, getMarkerColor())
                        markerCircle.shape = GradientDrawable.OVAL

                        if (markers.size > 0) {
                            if (markers[position].date == null) {
                                mark_image.visibility = View.INVISIBLE
                            } else {
                                mark_image.visibility = View.VISIBLE
                                when (markers[position].complete) {
                                    true -> {
                                        markerCircle.setColor(getMarkerColor())
                                        mark_image.background = markerCircle
                                        future?.let {
                                            if (future) {
                                                day_text.setTextColor(context.getColor(R.color.black))
                                            }
                                        }
                                    }

                                    else -> {
                                        mark_image.background = markerCircle
                                        future?.let {
                                            if (future) {
                                                day_text.setTextColor(context.getColor(R.color.black))
                                            }
                                        }
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
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}