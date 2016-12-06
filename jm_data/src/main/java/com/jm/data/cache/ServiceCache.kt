package com.jm.data.cache

import android.content.Context
import com.jm.data.utils.SPUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jm.data.beans.RequestParams
import com.jm.data.beans.UserInfo
import java.util.*

/**
 * Created by shunq-wang
 * Date:2015/8/14 0014
 * Time:15:33
 */
object ServiceCache {
    @JvmField var baseRequest: RequestParams? = null



    /**
     * 从本地缓存获取用户数据
     */
    fun getUserFromLocal(context: Context): UserInfo? {
        val user: UserInfo? = SPUtil.getSerData(context, UserInfo::class.java, SPUtil.USER_INFO_FILE, null)
        return if (user != null && user.AppUserId != null && user.AppUserId != 0) {
            user
        } else {
            null
        }
    }


    /**
     * 从本地缓存获取基础请求数据
     */
    fun getParamsFromLocal(context: Context): RequestParams? {
        val base: RequestParams? = SPUtil.getSerData(context, RequestParams::class.java, SPUtil.BASE_PARAMS_FILE, null)
        return if (base != null && base.AppUserId != null && base.AppUserId != 0) {
            base
        } else {
            null
        }
    }

    /**
     * 从本地缓存获取权限数据
     */
    fun getAuthFromLocal(context: Context): HashMap<String, Int>? {
        val jsonStr: String = SPUtil.getCacheString(context, "auth", SPUtil.AUTH_TAGS_FILE)
        if (jsonStr.isBlank()) return null
        val gson = Gson()
        val map: HashMap<String, Int>? = gson.fromJson(jsonStr, object : TypeToken<HashMap<String, Int>>() {}.type)

        return if (map != null && map.size > 0) {
            map
        } else {
            null
        }

    }
}