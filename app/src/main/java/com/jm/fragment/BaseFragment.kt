package com.jm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by shunq-wang on 2016/11/28.
 */
open class BaseFragment:Fragment() {

//    var toolbar:Toolbar?=null
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        toolbar=view?.find<Toolbar>(R.id.jm_toolbar)
    }

//    fun setToolbar(logo:Int?=null,title:String?=null,subTitle:String?=null){
//        toolbar.apply {
//            let {
//                it?.logo=resources.getDrawable(logo?:R.mipmap.home_up,null)
//                it?.title= title?:""
//                it?.subtitle=subTitle?:""
//            }
//        }
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//    }

}