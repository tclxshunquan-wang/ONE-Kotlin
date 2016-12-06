package com.jm.activity.ui

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.hyphenate.chat.EMMessage
import com.jm.MainActivity
import com.jm.R
import com.jm.activity.IMActivity
import com.jm.data.beans.IMData
import com.jm.data.beans.UserInfo
import com.jm.data.cache.ServiceCache
import com.jm.data.utils.XLog
import com.jm.data.utils.getScreenHeight
import com.jm.data.utils.getScreenWidth
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by shunq-wang on 2016/11/22.
 */

class IMActivityUI : AnkoComponent<IMActivity> {
    val TGA = IMActivityUI::class.java.canonicalName

    companion object {
        val LEFT_IMAGE = 0x01f001
        val RIGHT_IMAGE = 0x01f002
        val LEFT_TEXT = 0x01f003
        val RIGHT_TEXT = 0x01f004
    }

    override fun createView(ui: AnkoContext<IMActivity>) = with(ui) {
        linearLayout {
            lparams {
                width = matchParent
                height = matchParent
            }
            include<RelativeLayout>(R.layout.jm_im).apply {
                lparams {
                    width = matchParent
                    height = matchParent
                }
                val im_bottom = find<RelativeLayout>(R.id.im_bottom)
                ui.owner.im_edit = find<MaterialEditText>(R.id.im_edit).apply {

                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {
                            XLog.v(TGA, "afterTextChanged")
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            XLog.v(TGA, "beforeTextChanged")
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            XLog.v(TGA, "onTextChanged")
                        }

                    })


                }
                ui.owner.im_ecm = find<RelativeLayout>(R.id.im_ecm)
                ui.owner.im_bottom = find<RelativeLayout>(R.id.im_bottom)
                find<ImageView>(R.id.im_more).apply {
                    onClick {
//                        ui.owner.addViewToECM()
                        ui.owner.sendMessage()
                    }

                }
                find<SwipeRefreshLayout>(R.id.im_swip).apply {

                }
                find<RecyclerView>(R.id.im_recycle).apply {
                    layoutManager = LinearLayoutManager(ui.owner.ctx)
                    ui.owner.myAdapter = MyRecycleAdapter(ui.owner.ctx)
                    adapter = ui.owner.myAdapter

                }
            }
        }
    }

    /**
     * RecycleViewAdapter
     * */

    inner class MyRecycleAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var mCt = context
        var leftDataList = ArrayList<IMData>()
        var userinfo:UserInfo?=null
        init {
            userinfo= ServiceCache.getUserFromLocal(mCt)
        }

        override fun getItemViewType(position: Int): Int {
             if (userinfo?.UserName==leftDataList[position].From){
                 return 1
            }else{
                 return 0
             }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when (holder) {
                is Left_Holder -> {
                    holder.left_txt?.text = leftDataList[position].Content.toString().replace("txt","").replace("\"","").replace(":","")
                }
                is Right_Holder -> {
                    holder.right_txt?.text = leftDataList[position].Content.toString().replace("txt","").replace("\"","").replace(":","")
                }
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            if (viewType  == 0) {
                return Left_Holder(left_layout().createView(AnkoContext.Companion.create(mCt)))
            } else {
                return Right_Holder(right_layout().createView(AnkoContext.Companion.create(mCt)))
            }
        }

        override fun getItemCount(): Int {
            return leftDataList.size
        }

        inner class Left_Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var left_txt: TextView? = null
            var left_img: ImageView? = null

            init {
                left_txt = itemView?.find<TextView>(LEFT_TEXT)
                left_img = itemView?.find<ImageView>(LEFT_IMAGE)
            }

        }

        inner class Right_Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var right_txt: TextView? = null
            var right_img: ImageView? = null

            init {
                right_txt = itemView?.find<TextView>(RIGHT_TEXT)
                right_img = itemView?.find<ImageView>(RIGHT_IMAGE)
            }

        }

        inner class left_layout : AnkoComponent<Context> {

            override fun createView(ui: AnkoContext<Context>) = with(ui) {
                val screenWidth = getScreenWidth(ui.ctx)
                val screenHeigh = getScreenHeight(ui.ctx)
                relativeLayout {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    padding = dip(15)
                    imageView {
                        id = LEFT_IMAGE
                        lparams {
                            width = dip(30)
                            height = dip(30)
                            topMargin = dip(2)
                            addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        }
                        scaleType = ImageView.ScaleType.FIT_XY
                        backgroundResource = R.mipmap.chat_default
                    }
                    linearLayout {
                        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                        layoutParams = params.apply {
                            addRule(RelativeLayout.RIGHT_OF, LEFT_IMAGE)
                            addRule(RelativeLayout.ALIGN_PARENT_TOP)
                            leftMargin = dip(5)
                        }
                        padding = dip(2)
                        backgroundResource = R.mipmap.chat_box_l
                        textView {
                            id = LEFT_TEXT
                            lparams {
                                width = wrapContent
                                height = wrapContent
                            }
                            gravity=Gravity.CENTER_VERTICAL
                            textSize = 12f
                        }
                    }
                }
            }
        }

        inner class right_layout : AnkoComponent<Context> {
            override fun createView(ui: AnkoContext<Context>) = with(ui) {
                val screenWidth = getScreenWidth(ui.ctx)
                val screenHeigh = getScreenHeight(ui.ctx)
                relativeLayout {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    padding = dip(15)
                    imageView {
                        id = RIGHT_IMAGE
                        lparams {
                            width = dip(30)
                            height = dip(30)
                            topMargin = dip(2)
                            addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                        }
                        scaleType = ImageView.ScaleType.FIT_XY
                        backgroundResource = R.mipmap.chat_default
                    }
                    linearLayout {
                        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                        layoutParams = params.apply {
                            addRule(RelativeLayout.LEFT_OF, RIGHT_IMAGE)
                            addRule(RelativeLayout.ALIGN_PARENT_TOP)
                            rightMargin = dip(5)
                        }
                        padding = dip(2)
                        backgroundResource = R.mipmap.chat_box_r
                        textView {
                            id = RIGHT_TEXT
                            lparams {
                                width = wrapContent
                                height = wrapContent
                            }
                            gravity=Gravity.CENTER_VERTICAL
                            textSize = 12f
                        }
                    }
                }
            }
        }
    }
}