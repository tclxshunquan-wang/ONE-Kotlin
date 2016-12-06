package com.jm.activity

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jm.R
import com.jm.activity.ui.InfoActivityUI
import com.jm.activity.ui.StartActivityUI
import com.jm.data.utils.XLog
import com.jm.utils.Rotate3dAnimation
import com.jm.utils.ViewPagerScroller
import org.jetbrains.anko.*
import java.util.*

class InfoActivity : AppCompatActivity() {
    val EXTRA_NAME = "cheese_name"
    var info_viewpage: ViewPager? = null
    var info_viewpage_adapter:StartViewPage?=null
    var info_viewpage_position:Int=0
    var toolbar: Toolbar? = null
    var collapsingToolbar: CollapsingToolbarLayout? = null
    var info_tip: TextView? = null
    var info_tip_txt: Int? = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InfoActivityUI().setContentView(this@InfoActivity)
        val cheeseName = EXTRA_NAME
        this.setSupportActionBar(toolbar)
        collapsingToolbar?.apply {
            setCollapsedTitleTextColor(resources.getColor(R.color.white, null))
            setExpandedTitleTextColor(resources.getColorStateList(R.color.main, null))
            scrollBarSize = 12
            title = cheeseName
            backgroundColor=resources.getColor(R.color.white, null)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        loadBackdrop()
        //开启viewpage 动画
        ViewPagerScroller(this@InfoActivity).apply {
            setScrollDuration(1000)
            initViewPagerScroll(info_viewpage!!)
        }
        applyRotation(0f, 360f)
        Timer().schedule(pageTimer(),1000,3000)
    }
    fun floatClick(){
    }
    private fun loadBackdrop() {
//        val imageView = findViewById(R.id.backdrop) as ImageView
//        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * viewPageAdapter
     * */
    inner class StartViewPage(ctx: AnkoContext<InfoActivity>) : PagerAdapter() {
        var mCt = ctx
        var pageClick: StartActivityUI.PageClick? = null
        var mImgList = arrayListOf(
                pageImage("http://pic250.quanjing.com/imageclk005/ic01430270.jpg"),
                pageImage("http://pic250.quanjing.com/imageclk007/ic01704475.jpg"),
                pageImage("http://pic250.quanjing.com/imageclk009/ic02900107.jpg")
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
            return mImgList[position]
        }

        fun pageImage(url: String): View {
            val contentView = LinearLayout(mCt.ctx).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                backgroundColor = resources.getColor(R.color.white, null)
                relativeLayout {
                    lparams {
                        width = matchParent
                        height = matchParent
                    }
                    var img=imageView {
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

                }
            }
            return contentView
        }

    }

    interface PageClick {
        fun click(position: Int)
    }

    /**
     * tip小动画
     * */
     fun applyRotation(start: Float, end: Float) {
        // 计算中心点
        val centerX = info_tip?.width!! / 2.0f
        val centerY = info_tip?.height!! / 2.0f
        // 设置监听
        info_tip?.startAnimation(Rotate3dAnimation(start, end,
                centerX, centerY, 310.0f, true).apply {
            duration = 500
            fillAfter = true
            interpolator = AccelerateInterpolator()
            setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    info_tip?.post {tvRun()}
                }
            })
        })
        info_tip?.text="$info_tip_txt"
    }

    inner class tvRun:Runnable{
        override fun run() {
            val centerX = info_tip?.width!! / 2.0f
            val centerY = info_tip?.height!! / 2.0f
            info_tip?.requestFocus()
            // 开始动画
            info_tip?.startAnimation(Rotate3dAnimation(90f, 0f, centerX, centerY, 310.0f,
                    false).apply {
                duration = 500
                fillAfter = true
                interpolator = DecelerateInterpolator()
            })
            info_tip?.text="$info_tip_txt"
        }
    }
    /**
     * 自动轮播
     * */
    inner class pageTimer: TimerTask() {
        override fun run() {
                runOnUiThread {
                    info_viewpage?.setCurrentItem(info_viewpage_position,true)
                }
                if (info_viewpage_position < info_viewpage_adapter?.mImgList?.size!!-1){
                    info_viewpage_position++
                }else{
                    info_viewpage_position=0
                }
        }
    }
}
