package com.jm.fragment.ui

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.jm.R
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import org.jetbrains.anko.*

/**
 * Created by shunq-wang on 2016/11/30.
 */
class ChatFragmentUI:AnkoComponent<Context>{
    override fun createView(ui: AnkoContext<Context>)= with(ui) {
        relativeLayout {
            lparams {
                width= matchParent
                height= matchParent
            }
            include<LinearLayout>(R.layout.jm_chat_fragment).apply {
                lparams {
                    width = matchParent
                    height = matchParent
                }
                find<TextView>(R.id.head_title).apply {
                    text="消息"
                }

                find<SwipeMenuRecyclerView>(R.id.chat_recycle).apply {

                }
            }
        }
    }

}