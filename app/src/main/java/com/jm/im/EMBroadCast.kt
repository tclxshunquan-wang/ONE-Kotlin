package com.jm.im

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.jm.data.utils.Constant

/**
 * Created by shunq-wang on 2016/12/2.
 */
class EMBroadCast(handler:Handler,Type:String):BroadcastReceiver(){
    var castType:String=Type
    var myhandler:Handler=handler
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.action==castType){
            myhandler.sendMessage(Message().apply {
                what=Constant.EMSERVICE_SUCCESS
                obj=p1?.getSerializableExtra("EMMsgData")
            })
        }
    }
}