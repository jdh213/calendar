package com.devroid.calendarlib.verticalCalendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devroid.calendarlib.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vertical_month_view.view.*
import java.util.*

class VerticalCalendarViewAdapter(private val calendarList: ArrayList<VerticalCalendarData>) :
    RecyclerView.Adapter<VerticalCalendarViewAdapter.VerticalCalendarHolder>() {

    override fun getItemCount(): Int {
        Log.i("debugLog", "size : ${calendarList.size}")
        return calendarList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalCalendarHolder {
        return VerticalCalendarHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vertical_month_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VerticalCalendarHolder, position: Int) {
        holder.itemView.apply {
            calendarList[position].apply {
                calendar?.let {
                    dayText.text = it.get(Calendar.DAY_OF_MONTH).toString()
                }
            }
        }
    }

    inner class VerticalCalendarHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}