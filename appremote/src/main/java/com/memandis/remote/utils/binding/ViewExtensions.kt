package com.memandis.remote.utils.binding

import android.annotation.SuppressLint
import android.os.Build
import android.view.DisplayCutout
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.Job


import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.Px
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.memandis.remote.R
import com.memandis.remote.utils.app.AppConstants
import com.memandis.remote.utils.app.calculateForegroundColorToPair

//#################//Project##############################################
val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toPx: Int
    @Px
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun View.setMargins(@Px l: Int = marginLeft,
                    @Px t: Int = marginTop,
                    @Px r: Int = marginRight,
                    @Px b: Int = marginBottom) {
    if(layoutParams is MarginLayoutParams) {
        val p = layoutParams as MarginLayoutParams
        p.setMargins(l, t, r, b)
        requestLayout()
    }
}

fun Chip.changeBackgroundColor(colorInt: Int = Color.BLACK) {
    val finalBackground = if(colorInt == AppConstants.DEFAULT_COLOR){
        context.getColor(R.color.colorPrimary)
    }else{
        colorInt
    }
    val colorPair = calculateForegroundColorToPair(finalBackground)
    val foreground = if(colorPair.first == Color.BLACK){
        context!!.getColor(R.color.blackAlpha60)
    }else{
        colorPair.first
    }
    setTextColor(foreground)
    closeIconTint = ColorStateList.valueOf(foreground)
    chipBackgroundColor = ColorStateList.valueOf(colorPair.second)
}

fun FloatingActionButton.changeBackgroundColor(colorInt: Int = Color.BLACK){
    val finalBackground = if(colorInt == AppConstants.DEFAULT_COLOR){
        context.getColor(R.color.colorPrimary)
    }else{
        colorInt
    }
    val colorPair = calculateForegroundColorToPair(finalBackground)
    val foreground = if(colorPair.first == Color.BLACK){
        context!!.getColor(R.color.blackAlpha60)
    }else{
        colorPair.first
    }

    imageTintList = ColorStateList.valueOf(foreground)
    backgroundTintList = ColorStateList.valueOf(colorPair.second)
}

//###############################################################################


/** Combination of all flags required to put activity into immersive mode */
const val FLAGS_FULLSCREEN =  View.SYSTEM_UI_FLAG_LOW_PROFILE or
                              View.SYSTEM_UI_FLAG_FULLSCREEN or
                              View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                              View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                              View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                              View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

/** Milliseconds used for UI animations */
const val ANIMATION_FAST_MILLIS = 50L
const val ANIMATION_SLOW_MILLIS = 100L

//project viewpager1
fun View.toTransitionGroup() = this to transitionName

/**
 * Simulate a button click, including a small delay while it is being pressed to trigger the
 * appropriate animations.
 */
fun ImageButton.simulateClick(delay: Long = ANIMATION_FAST_MILLIS) {
    performClick()
    isPressed = true
    invalidate()
    postDelayed({
        invalidate()
        isPressed = false
    }, delay)
}

/** Pad this view with the insets provided by the device cutout (i.e. notch) */
@RequiresApi(Build.VERSION_CODES.P)
fun View.padWithDisplayCutout() {

    /** Helper method that applies padding from cutout's safe insets */
    fun doPadding(cutout: DisplayCutout) = setPadding(
        cutout.safeInsetLeft,
        cutout.safeInsetTop,
        cutout.safeInsetRight,
        cutout.safeInsetBottom)

    // Apply padding using the display cutout designated "safe area"
    rootWindowInsets?.displayCutout?.let { doPadding(it) }

    // Set a listener for window insets since view.rootWindowInsets may not be ready yet
    setOnApplyWindowInsetsListener { _, insets ->
        insets.displayCutout?.let { doPadding(it) }
        insets
    }
}
/**
 * Allows calls like
 *
 * `supportFragmentManager.inTransaction { add(...) }`
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

/**
 * Cancel the Job if it's active.
 */
fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}

@SuppressLint("RestrictedApi")
fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    // Create a snapshot of the view's padding state
    val paddingState = createStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

private fun createStateForView(view: View) = ViewPaddingState(
    view.paddingLeft,
    view.paddingTop,
    view.paddingRight,
    view.paddingBottom,
    view.paddingStart,
    view.paddingEnd
)

data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

/** Same as [AlertDialog.show] but setting immersive mode in the dialog's window */
fun AlertDialog.showImmersive() {
    // Set the dialog to not focusable
    window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

    // Make sure that the dialog's window is in full screen
    window?.decorView?.systemUiVisibility = FLAGS_FULLSCREEN

    // Show the dialog while still in immersive mode
    show()

    // Set the dialog to focusable again
    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
}