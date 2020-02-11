package com.devroid.calendarlib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CustomRecyclerView : RecyclerView {

    private val gestureDetector = GestureDetectorCompat(context, OnSwipeListener())
    private var calendarSwipe: CalendarSwipe? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setSwipeListener(calendarSwipe: CalendarSwipe) {
        this.calendarSwipe = calendarSwipe
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(e)
        return false
    }

    inner class OnSwipeListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 50
        private val SWIPE_VELOCITY_THRESHOLD = 50

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {
                val diffY = e2!!.y - e1!!.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            Log.i("CalendarSwipe", "left")
                            calendarSwipe?.onSwipeLeft()
                        } else {
                            Log.i("CalendarSwipe", "right")
                            calendarSwipe?.onSwipeRight()
                        }
                    }
                }
                /*else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        Log.i("debugLog", "bottom")
                        onSwipeBottom()
                    } else {
                        Log.i("debugLog", "top")
                        onSwipeTop()
                    }
                }

                 */
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.i("CalendarSwipe", "Swipe Exception : $exception")
            }
            return false
        }
    }
}