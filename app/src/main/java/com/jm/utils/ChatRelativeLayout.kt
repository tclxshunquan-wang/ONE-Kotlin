package com.jm.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.jm.data.utils.showSoftInputIsOpened

/**
 * Created by shunq-wang on 2016/12/1.
 */
class ChatRelativeLayout(ctx:Context,attr:AttributeSet,defStyleAttr:Int?=null,defStyleRes:Int?=null):RelativeLayout(ctx,attr,defStyleAttr!!,defStyleRes!!){

    var chatLayout:RelativeLayout?=null
    var mCt:Context=ctx
    init {
        chatLayout=this
    }

    fun setChatLayoutMargin(){
        if(showSoftInputIsOpened(mCt)){

        }
    }
}