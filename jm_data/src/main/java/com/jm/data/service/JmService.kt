package com.jm.data.service

import android.content.Context
import com.google.gson.Gson
import com.jm.data.beans.RequestParams
import com.jm.data.beans.UserInfo
import com.jm.data.beans._ResponseData
import com.jm.data.utils.Constant
import com.jm.data.utils.Jm_Loading
import com.jm.data.utils.toMaps
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.AbsCallbackWrapper
import okhttp3.Call
import okhttp3.Response

/**
 * Created by shunq-wang on 2016/11/23.
 */

open class JmService(Ctx:Context):BaseService(Ctx){
    val mCt=Ctx
    val BaseUrl= Constant.BASE_URL
    var JMLoad:Jm_Loading?=null
    init {
        JMLoad=Jm_Loading(mCt)
    }

   fun GetObject(absclick:AbsCallbackListener?){
       JMLoad?.show()
       val Url=BaseUrl+""
       val params: RequestParams =getBaseParams()
       params.Args=Gson().toJson(mapOf(1 to 2))
       OkGo.post(Url).tag(mCt).params(toMaps(params)).execute(object: AbsCallbackWrapper<_ResponseData<UserInfo>>(){

           override fun onSuccess(t: _ResponseData<UserInfo>?, call: Call?, response: Response?) {
               absclick?.onSuccess(t?.result as Any, call, response)
           }
           override fun onError(call: Call?, response: Response?, e: Exception?) {
               absclick?.onError(call, response, e)
           }
           override fun onAfter(t: _ResponseData<UserInfo>?, e: Exception?) {
               super.onAfter(t, e)
               JMLoad?.dismiss()
           }
       })
    }


}