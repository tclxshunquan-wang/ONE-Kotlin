package com.jm

import android.os.Process
import com.facebook.drawee.backends.pipeline.Fresco
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.jm.application.BaseApplication
import com.jm.data.utils.Constant
import com.jm.data.utils.SPUtil
import com.jm.data.utils.XLog
import com.jm.data.utils.getAppName

/**
 * Created by shunq-wang on 2016/11/21.
 */
class JmApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()


        //            LeakCanary.install(this);
        Constant.BASE_URL = BuildConfig.API_HOST
        Constant.DEBUT_LOG = BuildConfig.DEBUT_LOG
        Fresco.initialize(applicationContext)

        /**
         * 初始化环信SDK
         * */
        val options = EMOptions()

        // 默认添加好友时，是不需要验证的，改成需要验证
        options.acceptInvitationAlways = false
        val processAppName = getAppName(applicationContext, Process.myPid())
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (!processAppName.equals(applicationContext.packageName, ignoreCase = true)) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return
        }
        EMClient.getInstance().init(applicationContext, options)
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true)

    }

    override fun onLowMemory() {
        super.onLowMemory()
        XLog.v("JmApplication","onLowMemory")
    }

    override fun onTerminate() {
        super.onTerminate()
        XLog.v("JmApplication","onTerminate")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        XLog.v("JmApplication","onTrimMemory")
    }


}