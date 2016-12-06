package com.jm.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout
import com.jm.R
import com.jm.data.utils.Toast_Short

open class BaseActivity : AppCompatActivity() {
    var mExitTime: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //使状态栏透明
//            this.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//
//            // 如果想要给状态栏加点颜色，例如加点透明的阴影，就需要加上下面的三行代码
//            // 这是生成一个状态栏大小的矩形，给这个矩形添加颜色，添加 statusView 到布局中
//            val statusView = createStatusView(resources.getColor(R.color.main,null))
//            val decorView = this.window.decorView as ViewGroup
//            decorView.addView(statusView)
//
//
//            // 设置根布局的参数
//           val rootView = (this@BaseActivity.findViewById(android.R.id.content)!! as ViewGroup).getChildAt(0)!! as ViewGroup
//            rootView.fitsSystemWindows = false
//            rootView.clipToPadding = true
//        }

    }

    /**
     * 这个生成一个状态栏大小的矩形，给这个矩形，添加 statusView 到布局中
     * @param color
     * *
     * @return
     */
    fun createStatusView(color: Int): View {
        // 获得状态栏高度
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = this.resources.getDimensionPixelSize(resourceId)

        // 绘制一个和状态栏一样高的矩形
        val statusView = View(this)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight)
        statusView.layoutParams = params
        statusView.setBackgroundColor(color)
        return statusView
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime!! > 2000) {
                Toast_Short("再按一次退出程序", this)
                mExitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
