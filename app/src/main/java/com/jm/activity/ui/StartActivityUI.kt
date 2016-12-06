package com.jm.activity.ui

import android.content.Context
import android.opengl.Visibility
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jm.R
import com.jm.activity.StartActivity
import org.jetbrains.anko.*

/**
 * Created by shunq-wang on 2016/11/22.
 */

class StartActivityUI : AnkoComponent<StartActivity> {
    override fun createView(ui: AnkoContext<StartActivity>) = with(ui) {
        linearLayout {
            lparams {
                width = matchParent
                height = matchParent
            }
            include<RelativeLayout>(R.layout.jm_start).apply {
                lparams {
                    width = matchParent
                    height = matchParent
                }
                find<ViewPager>(R.id.start_viewpage).apply {
                    adapter = StartViewPage(ui).apply {
                        pageClick = object : PageClick {
                            override fun click(position: Int) {
                                ui.owner.txtClick(position)
                            }
                        }
                    }
                    addOnPageChangeListener(ui.owner.pageChangeListener)

                }
            }
        }
    }

    /**
     * viewPageAdapter
     * */
   inner class StartViewPage(ctx: AnkoContext<StartActivity>) : PagerAdapter() {
        var mCt = ctx
        var txt: TextView? = null
        var pageClick: PageClick? = null
        var mImgList = arrayListOf(
                pageImage("http://www.2cto.com/uploadfile/Collfiles/20160216/2016021609182623.png"),
                pageImage("http://images2015.cnblogs.com/blog/831804/201511/831804-20151120214337827-1144134874.png")
        )
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view?.equals(`object`) ?: false
        }

        override fun getCount(): Int {
            return mImgList.size
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container!!.removeView(mImgList[position])
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            container?.addView(mImgList[position])
            txt?.apply {
                if (position == mImgList.size - 1) {
                    visibility = View.VISIBLE
                } else {
                    visibility = View.VISIBLE
                }
                onClick {
                    pageClick?.click(position)
                }
            }
            return mImgList[position]
        }

        fun pageImage(url: String): View {
            val contentView=LinearLayout(mCt.ctx).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                relativeLayout {
                    lparams {
                        width = matchParent
                        height = matchParent
                    }
                    val img=imageView {
                        lparams {
                            width = matchParent
                            height = matchParent
                        }
                        scaleType = ImageView.ScaleType.FIT_XY
                    }
                    Glide.with(context)
                            .load(url)
                            .placeholder(R.mipmap.null_data)
                            .into(img)
                    txt = textView {
                        lparams {
                            width = dip(40)
                            height = dip(20)
                            addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                            margin = dip(15)

                        }
                        text = "进入"
                        visibility = View.GONE
                    }
                }
            }
            return contentView
        }
    }

    interface PageClick {
        fun click(position: Int)
    }


}