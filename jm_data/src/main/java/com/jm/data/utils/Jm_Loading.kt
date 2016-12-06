package com.jm.data.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import com.jm.data.R
import com.victor.loading.rotate.RotateLoading

/**
 * Created by shunq-wang on 2016/11/24.
 */
class Jm_Loading(ctx: Context) : Dialog(ctx,R.style.CustomDialog) {
    var loadingView: View? = null

    var loading: RotateLoading? = null

    init {
        loadingView = LayoutInflater.from(ctx).inflate(R.layout.jm_loading, null)
        loading = loadingView?.findViewById(R.id.jm_loading) as RotateLoading?
        loadingView?.findViewById(R.id.jm_rel).apply {
            let {
                it?.background=getShapeDrawable(Color.parseColor("#77000000"),15f,1, Color.parseColor("#77000000"),null,null)
            }
        }
        this.apply {
            setContentView(loadingView)
        }
    }

    override fun show() {
        super.show()
        loading?.start()
    }

    override fun dismiss() {
        super.dismiss()
        loading?.stop()
    }

}