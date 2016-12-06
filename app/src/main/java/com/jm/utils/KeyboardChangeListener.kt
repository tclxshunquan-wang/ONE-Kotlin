package com.jm.utils

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by shunq-wang on 2016/12/1.
 */
class KeyboardChangeListener(contextObj:Activity):ViewTreeObserver.OnGlobalLayoutListener{
    private val TAG = "ListenerHandler"
    private var mContentView: View?
    private var mOriginHeight: Int = 0
    private var mPreHeight: Int = 0
    private var mKeyBoardListen: KeyBoardListener? = null

    interface KeyBoardListener {
        /**
         * call back
         * @param isShow true is show else hidden
         * *
         * @param keyboardHeight keyboard height
         */
        fun onKeyboardChange(isShow: Boolean, keyboardHeight: Int)
    }

    init {

        mContentView = findContentView(contextObj)
        if (mContentView != null) {
            addContentTreeObserver()
        }
    }

     fun setKeyBoardListener(keyBoardListen: KeyBoardListener) {
        this.mKeyBoardListen = keyBoardListen
    }

    private fun findContentView(contextObj: Activity): View {
        return contextObj.findViewById(android.R.id.content)
    }

    private fun addContentTreeObserver() {
        mContentView!!.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val currHeight = mContentView!!.getHeight()
        if (currHeight == 0) {
            Log.i(TAG, "currHeight is 0")
            return
        }
        var hasChange = false
        if (mPreHeight == 0) {
            mPreHeight = currHeight
            mOriginHeight = currHeight
        } else {
            if (mPreHeight != currHeight) {
                hasChange = true
                mPreHeight = currHeight
            } else {
                hasChange = false
            }
        }
        if (hasChange) {
            val isShow: Boolean
            var keyboardHeight = 0
            if (mOriginHeight == currHeight) {
                //hidden
                isShow = false
            } else {
                //show
                keyboardHeight = mOriginHeight - currHeight
                isShow = true
            }

            if (mKeyBoardListen != null) {
                mKeyBoardListen!!.onKeyboardChange(isShow, keyboardHeight)
            }
        }
    }

      fun destroy() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContentView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    }
}