package com.jm.activity.ui

import android.content.Context
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jm.MainActivity
import com.jm.R
import com.jm.activity.InfoActivity
import com.jm.data.utils.XLog
import com.jm.data.utils.getScreenHeight
import com.jm.data.utils.getScreenWidth
import com.jm.utils.DepthPageTransformer
import com.jm.utils.cardView
import org.jetbrains.anko.*

/**
 * Created by shunq-wang on 2016/11/22.
 */

class InfoActivityUI : AnkoComponent<InfoActivity> {
    companion object {
        val IMAGE = 0x36f09
        val TEXT = 0x36f00
    }


    var recycle: RecyclerView? = null
    override fun createView(ui: AnkoContext<InfoActivity>) = with(ui) {
        linearLayout {
            lparams {
                width = matchParent
                height = matchParent
            }
            include<CoordinatorLayout>(R.layout.jm_info).apply {
                lparams {
                    width = matchParent
                    height = matchParent
                }
                recycle = find<RecyclerView>(R.id.info_recy).apply {
                    layoutManager = LinearLayoutManager(ui.owner.ctx)
                    adapter = MyRecycleAdapter(ui.owner.ctx)
                }
                ui.owner.info_tip=find<TextView>(R.id.info_tip)
                find<FloatingActionButton>(R.id.info_floatBut).apply {
                    onClick {
                        ui.owner.floatClick()
                    }
                }
                ui.owner.toolbar = find<Toolbar>(R.id.toolbar)
                ui.owner.collapsingToolbar = find<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
                ui.owner.info_viewpage=find<ViewPager>(R.id.info_viewPage).apply {
                    ui.owner.info_viewpage_adapter= ui.owner.StartViewPage(ui).apply {
                        pageClick = object : StartActivityUI.PageClick {
                            override fun click(position: Int) {
                            }
                        }
                    }
                    adapter=ui.owner.info_viewpage_adapter
                    setPageTransformer(true, DepthPageTransformer())
                    addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
                        override fun onPageScrollStateChanged(state: Int) {

                        }

                        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        }

                        override fun onPageSelected(position: Int) {
                            ui.owner.info_tip_txt=position
                            ui.owner.applyRotation(0f, 360f)
                        }

                    })

                }
            }

        }
    }

    /**
     * viewPageAdapter
     * */
    inner class MyRecycleAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var mCt = context
        var leftDataList = arrayListOf(
                MainActivity.leftData("个人信息",R.mipmap.ic_launcher,"http://pic250.quanjing.com/imageclk005/ic01430270.jpg"),
                MainActivity.leftData("会员充值", R.mipmap.ic_launcher,"http://pic250.quanjing.com/imageclk007/ic01704475.jpg"),
                MainActivity.leftData("我的邮箱", R.mipmap.ic_launcher,"http://pic250.quanjing.com/imageclk009/ic02900107.jpg")
        )

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when (holder) {
                is TopHolder -> {
                    XLog.v("INFO_TYPE", "TopHolder")
                    holder.text?.text = leftDataList[position].name
                }
                is CenterHolder -> {

                }
                is BottomHolder -> {

                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
            XLog.v("INFO_TYPE", "$viewType")
            return when (viewType) {
                0 -> TopHolder(TopView().createView(AnkoContext.create(mCt)))
                1 -> CenterHolder(CenterView().createView(AnkoContext.create(mCt)))
                2 -> BottomHolder(BottomView().createView(AnkoContext.create(mCt)))
                else -> null
            }
        }

        override fun getItemCount(): Int {
            return leftDataList.size
        }

    }

    class TopHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var text: TextView? = null

        init {
            text = itemView?.find<TextView>(TEXT)
        }
    }

    class CenterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    }

    class BottomHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    }

    inner class TopView : AnkoComponent<Context> {
        override fun createView(ui: AnkoContext<Context>) = with(ui) {
            val screenWidth = getScreenWidth(ui.ctx)
            val screenHeigh = getScreenHeight(ui.ctx)
            relativeLayout {
                lparams {
                    width = matchParent
                    height = wrapContent
                }
                cardView {
                    lparams {
                        width = matchParent
                        height = screenWidth*3/4
                        margin=dip(16)
                    }
                    cardElevation = 24f
                    cardBackgroundColor = resources.getColorStateList(R.color.white, null)
                    radius = 10f
                    textView {
                        id = TEXT
                        lparams {
                            width = wrapContent
                            height = wrapContent
                        }
                    }
                }
            }
        }
    }

    inner class CenterView : AnkoComponent<Context> {
        override fun createView(ui: AnkoContext<Context>) = with(ui) {
            val screenWidth = getScreenWidth(ui.ctx)
            val screenHeigh = getScreenHeight(ui.ctx)
            relativeLayout {
                lparams {
                    width = matchParent
                    height = wrapContent
                }
                cardView {
                    lparams {
                        width = matchParent
                        height = screenWidth*3/4
                        margin=dip(16)
                    }
                    cardElevation = 24f
                    cardBackgroundColor = resources.getColorStateList(R.color.grey_a400, null)
                    radius = 10f

                }
            }
        }
    }

    inner class BottomView : AnkoComponent<Context> {
        override fun createView(ui: AnkoContext<Context>) = with(ui) {
            val screenWidth = getScreenWidth(ui.ctx)
            val screenHeigh = getScreenHeight(ui.ctx)
            relativeLayout {
                lparams {
                    width = matchParent
                    height = wrapContent
                }
                cardView {
                    lparams {
                        width = matchParent
                        height = screenWidth*3/4
                        margin=dip(16)
                    }
                    cardElevation = 24f
                    cardBackgroundColor = resources.getColorStateList(R.color.a100, null)
                    radius = 10f
                }
            }
        }
    }
}