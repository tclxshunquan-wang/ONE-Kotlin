package com.jm.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jm.R
import com.jm.data.utils.dip2px
import com.yanzhenjie.recyclerview.swipe.ResCompat

/**
 * Created by shunq-wang on 2016/11/30.
 */
class ListViewDecoration(ctx: Context) : RecyclerView.ItemDecoration() {
    var mDrawable: Drawable? = null
    var mCt: Context = ctx

    init {
        mDrawable = ResCompat.getDrawable(ctx, R.drawable.divider_recycler)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = dip2px(mCt, 75f)
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0..childCount - 1 - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            // 以下计算主要用来确定绘制的位置
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDrawable?.intrinsicHeight!!
            mDrawable?.setBounds(left, top, right, bottom)
            mDrawable?.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, 0, 0, mDrawable?.intrinsicHeight!!)
    }
}