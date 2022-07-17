package com.doublej.jjfitnessapp.ui.dashboard

import ScrollerDuration
import android.content.Context

import android.util.AttributeSet
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager
import java.lang.Exception


class ViewPagerDuration : ViewPager {
    constructor(context: Context?) : super(context!!) {
        postInitViewPager()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        postInitViewPager()
    }

    private var mScroller: ScrollerDuration? = null

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private fun postInitViewPager() {
        try {
            val scroller = ViewPager::class.java.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true
            mScroller = ScrollerDuration(
                context,
                interpolator[null] as Interpolator
            )
            scroller[this] = mScroller
        } catch (e: Exception) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    fun setScrollDurationFactor(scrollFactor: Float) {
        mScroller?.setScrollDurationFactor(scrollFactor)
    }
}