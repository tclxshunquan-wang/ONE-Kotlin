package com.jm.utils

/**
 * Created by shunq-wang on 2016/11/22.
 */
interface RecycleViewClickListner {

    //like
     fun onLikeClick(position: Int,T:Any)
    //hello
    fun onHelloClick(position: Int,T:Any)
    //short_click
    fun onShortClick(position: Int,T:Any)

}

interface RecycleViewShortClickListner {

    fun onShortClick(position: Int,T:Any)

}