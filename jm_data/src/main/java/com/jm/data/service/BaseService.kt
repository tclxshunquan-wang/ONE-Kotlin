package com.jm.data.service

import android.content.Context
import com.google.gson.Gson
import com.jm.data.beans.RequestParams
import com.jm.data.cache.ServiceCache
import com.jm.data.utils.getAppVersion
import com.jm.data.utils.getAppVersionCode
import com.jm.data.utils.getDeviceId
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by shunq-wang on 2016/11/23.
 */
open class BaseService {
    constructor(context: Context) {
        ServiceCache.baseRequest = ServiceCache.baseRequest ?: RequestParams()
        if (ServiceCache.baseRequest!!.DeviceId.isNullOrBlank()) ServiceCache.baseRequest!!.DeviceId = getDeviceId(context)
        if (ServiceCache.baseRequest!!.Ver.isNullOrBlank()) ServiceCache.baseRequest!!.Ver = getAppVersion(context)
        if (ServiceCache.baseRequest!!.VerCode == 0) ServiceCache.baseRequest!!.VerCode = getAppVersionCode(context)
    }

    fun getBaseParams(): RequestParams {
        ServiceCache.baseRequest!!.Timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val param = ServiceCache.baseRequest ?: RequestParams()
        param.Args = Gson().toJson(mapOf("1" to 1))
        return param
    }

}