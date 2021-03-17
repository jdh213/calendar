package com.devroid.calendarlib.verticalCalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devroid.calendarlib.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vertical_day_view.view.*
import kotlinx.android.synthetic.main.vertical_month_view.view.*
import java.text.SimpleDateFormat
import java.util.*


class VerticalCalendarViewAdapter(
    private val calendarList: ArrayList<VerticalCalendarData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickListener: OnDayClickListener? = null

    private var dateFormatter = SimpleDateFormat("MM", Locale.KOREAN)

    private val MONTH_TYPE = 0
    private val EMPTY_TYPE = 1
    private val DAY_TYPE = 2

    override fun getItemViewType(position: Int): Int {
        return when (calendarList[position].type) {
            "month" -> {
                MONTH_TYPE
            }
            "empty" -> {
                EMPTY_TYPE
            }
            else -> {
                DAY_TYPE
            }

        }
    }


    override fun getItemCount(): Int {
        return calendarList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                MonthHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.vertical_month_view, parent, false)
                )
            }
            1 -> {
                EmptyHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.vertical_empty_view, parent, false)
                )
            }
            2 -> {
                DayHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.vertical_day_view, parent, false)
                )
            }
            else -> {
                EmptyHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.vertical_empty_view, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.let { view ->
            calendarList[position].apply {
                calendar?.let { calendar ->
                    when (getItemViewType(position)) {
                        0 -> {
                            val params =
                                view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                            params.isFullSpan = true

                            setMonth(this, view)
                        }
                        1 -> {

                        }
                        2 -> {
                            view.dayText.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

                            view.setOnClickListener {
                                onClickListener?.onClick(
                                    calendar.get(Calendar.DAY_OF_MONTH),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.YEAR)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setMonth(data: VerticalCalendarData, view: View) {
        monthTextClear(view)
        val date = dateFormatter.format(data.calendar?.timeInMillis)
        when (data.monthPosition) {
            0 -> {
                view.monthText1.text = date
            }

            1 -> {
                view.monthText2.text = date
            }

            2 -> {
                view.monthText3.text = date
            }

            3 -> {
                view.monthText4.text = date
            }

            4 -> {
                view.monthText5.text = date
            }

            5 -> {
                view.monthText6.text = date
            }

            6 -> {
                view.monthText7.text = date
            }
        }
    }

    private fun monthTextClear(view: View) {
        view.monthText1.text = ""
        view.monthText2.text = ""
        view.monthText3.text = ""
        view.monthText4.text = ""
        view.monthText5.text = ""
        view.monthText6.text = ""
        view.monthText7.text = ""
    }

    inner class MonthHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer

    inner class EmptyHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer

    inner class DayHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}