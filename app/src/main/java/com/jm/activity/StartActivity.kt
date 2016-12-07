package com.jm.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.jm.activity.ui.StartActivityUI
import com.jm.data.utils.SPUtil
import org.jetbrains.anko.setContentView

class StartActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StartActivityUI().setContentView(this@StartActivity)
        if(!SPUtil.getBoolean(this@StartActivity, SPUtil.AUTH_TAGS_FILE, SPUtil.AUTH_FIRST_TAG,true)){
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
            finish()
        }
    }

    fun txtClick(position: Int) {
        SPUtil.saveBoolean(this@StartActivity,SPUtil.AUTH_TAGS_FILE,SPUtil.AUTH_FIRST_TAG,false)
        startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
    }
    var pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {

        }

    }
}
