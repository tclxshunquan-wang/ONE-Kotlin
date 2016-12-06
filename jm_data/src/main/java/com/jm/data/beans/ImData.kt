package com.jm.data.beans

import java.io.Serializable

/**
 * Created by shunq-wang on 2016/12/2.
 */
/**
 * IM消息实体
 */
data class IMData(
        var From: String? = null,
        var To: String? = null,
        var ContentType: String? = null,
        var Content: String? = null,
        var HeadImg: String? = null,
        var ImgUrl: String? = null,
        var VideoContent: String? = null
) : Serializable