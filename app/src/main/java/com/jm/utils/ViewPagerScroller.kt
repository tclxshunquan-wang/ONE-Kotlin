package com.jm.utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.widget.Scroller

/**
 * Created by shunq-wang on 2016/11/28.
 */
class ViewPagerScroller(ctx:Context): Scroller(ctx){
     var mScrollDuration = 2000 // 滑动速度

    /**
     * 设置速度速度

     * @param duration
     */
    fun setScrollDuration(duration: Int) {
        this.mScrollDuration = duration
    }

  override  fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration)
    }

    fun initViewPagerScroll(viewPager: ViewPager) {
        try {
            val mScroller = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            mScroller.set(viewPager, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}