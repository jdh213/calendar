package com.devroid.calendarlib.verticalCalendar

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devroid.calendarlib.databinding.VerticalDayViewBinding
import com.devroid.calendarlib.databinding.VerticalEmptyViewBinding
import com.devroid.calendarlib.databinding.VerticalMonthViewBinding
import java.text.SimpleDateFormat
import java.util.*


class VerticalCalendarViewAdapter(
    private val calendarList: ArrayList<VerticalCalendarData>,
    private val monthSize: Float,
    private val monthColor: Int = Color.BLACK,
    private val daySize: Float,
    private val dayColor: Int = Color.BLACK
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = VerticalMonthViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MonthHolder(binding)
            }
            1 -> {
                val binding = VerticalEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EmptyHolder(binding)
            }
            else -> {
                val binding = VerticalDayViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DayHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (calendarList[position].type) {
            "month" -> {
                (holder as MonthHolder).bind(calendarList[position])
            }
            "empty" -> {
                (holder as EmptyHolder).bind()
            }
            else -> {
                (holder as DayHolder).bind(
                    calendarList[position].calendar,
                    calendarList[position].progressData
                )
            }
        }
    }

    private fun setMonth(data: VerticalCalendarData, binding: VerticalMonthViewBinding) {
        with(binding) {
            monthTextClear(binding)
            val date = dateFormatter.format(data.calendar?.timeInMillis)
            when (data.monthPosition) {
                0 -> {
                    monthText1.text = date
                    monthText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize)
                    monthText1.setTextColor(monthColor)
                }

                1 -> {
                    monthText2.text = date
                    monthText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize)
                    monthText2.setTextColor(monthColor)
                }

                2 -> {
                    monthText3.text = date
                    monthText3.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize)
                    monthText3.setTextColor(monthColor)
                }

                3 -> {
                    monthText4.text = date
                    monthText4.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize)
                    monthText4.setTextColor(monthColor)
                }

                4 -> {
                    monthText5.text = date
                    monthText5.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize)
                    monthText5.setTextColor(monthColor)
                }

                5 -> {
                    monthText6.text = date
                    monthText6.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize)
                    monthText6.setTextColor(monthColor)
                }

                6 -> {
                    monthText7.text = date
                    monthText7.setTextSize(TypedValue.COMPLEX_UNIT_PX, monthSize)
                    monthText7.setTextColor(monthColor)
                }
            }
        }
    }

    private fun monthTextClear(binding: VerticalMonthViewBinding) {
        binding.monthText1.text = ""
        binding.monthText2.text = ""
        binding.monthText3.text = ""
        binding.monthText4.text = ""
        binding.monthText5.text = ""
        binding.monthText6.text = ""
        binding.monthText7.text = ""
    }

    inner class MonthHolder(private val binding: VerticalMonthViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(verticalCalendarData: VerticalCalendarData) = with(binding) {
            setMonth(verticalCalendarData, binding)
        }
    }

    inner class EmptyHolder(private val binding: VerticalEmptyViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding) {

        }
    }

    inner class DayHolder(private val binding: VerticalDayViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendar: GregorianCalendar?, progressData: ProgressData) = with(binding) {

            dayText.text = calendar?.get(Calendar.DAY_OF_MONTH).toString()
            dayText.setTextSize(TypedValue.COMPLEX_UNIT_PX, daySize)
            dayText.setTextColor(dayColor)

            dayGraph.apply {
                setOuterProgressColor(arrayListOf(progressData.calColor))
                setCenterProgressColor(arrayListOf(progressData.goalColor))

                setMaxProgressOuterView(progressData.totalCalProgress)
                setOuterProgress(progressData.currentCalProgress)

                setMaxProgressCenterView(progressData.totalGoalProgress)
                setCenterProgress(progressData.currentGoalProgress)
            }

            root.setOnClickListener {
                onClickListener?.onClick(
                    calendar?.get(Calendar.DAY_OF_MONTH) ?: 0,
                    calendar?.get(Calendar.MONTH) ?: 0,
                    calendar?.get(Calendar.YEAR) ?: 0
                )
            }
        }
    }
}