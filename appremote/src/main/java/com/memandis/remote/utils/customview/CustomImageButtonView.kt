package com.memandis.remote.utils.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class CustomImageButtonView : androidx.appcompat.widget.AppCompatImageButton {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return false
    }

    // Because we call this from onTouchEvent, this code will be executed for both
    // normal touch events and for when the system calls this using Accessibility
    override fun performClick(): Boolean {
        super.performClick()
        doSomething()
        return true
    }

    private fun doSomething() {
//        Toast.makeText(context, "did something", Toast.LENGTH_SHORT).show()
    }
}
