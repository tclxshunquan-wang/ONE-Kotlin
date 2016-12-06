package com.jm.data.utils

import android.util.Log


object XLog {

     fun v(tag: String, msg: String) {
        if (Constant.DEBUT_LOG) {
            Log.v(tag, msg)
        }
    }

     fun d(tag: String, msg: String) {
        if (Constant.DEBUT_LOG) {
            Log.d(tag, msg)
        }
    }

     fun i(tag: String, msg: String) {
        if (Constant.DEBUT_LOG) {
            Log.i(tag, msg)
        }
    }

     fun w(tag: String, msg: String) {
        if (Constant.DEBUT_LOG) {
            Log.w(tag, msg)
        }
    }

     fun e(tag: String, msg: String) {
        if (Constant.DEBUT_LOG) {
            Log.e(tag, msg)
        }
    }

     fun e(tag: String, msg: String, tr: Throwable) {
        if (Constant.DEBUT_LOG) {
            Log.e(tag, msg, tr)
        }
    }
}
