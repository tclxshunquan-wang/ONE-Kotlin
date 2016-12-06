package com.jm.data.service

import okhttp3.Call
import okhttp3.Response

/**
 * Created by shunq-wang on 2016/11/23.
 */
interface AbsCallbackListener{
    fun onSuccess(t: Any?, call: Call?, response: Response?)
    fun onError(call: Call?, response: Response?, e: Exception?)
}