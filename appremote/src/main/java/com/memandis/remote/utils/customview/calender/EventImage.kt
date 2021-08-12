package com.memandis.remote.utils.customview.calender

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

sealed class EventImage {
    object EmptyEventImage : EventImage()
    data class EventImageResource(@DrawableRes val drawableRes: Int) : EventImage()
    data class EventImageDrawable(val drawable: Drawable) : EventImage()
}