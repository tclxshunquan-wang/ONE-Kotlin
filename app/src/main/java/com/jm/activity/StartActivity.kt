package com.jm.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.jm.activity.ui.StartActivityUI
import org.jetbrains.anko.setContentView

class StartActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StartActivityUI().setContentView(this@StartActivity)

    }

    fun txtClick(position: Int) {
        startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        finish()
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
