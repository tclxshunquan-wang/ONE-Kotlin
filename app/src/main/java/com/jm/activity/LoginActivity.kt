package com.jm.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.jm.MainActivity
import com.jm.R
import com.jm.activity.ui.LoginActivityUI
import com.jm.data.beans.UserInfo
import com.jm.data.service.JmService
import com.jm.data.utils.*
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class LoginActivity : BaseActivity() {
    val TIME_DOWN:Int=0x00f1
    val TGA:String=LoginActivity::getLocalClassName.name
    var jmService: JmService? = null
    var login_send:TextView?=null
    var login_sub:TextView?=null
    var login_username: MaterialEditText?=null
    var login_yzm:MaterialEditText?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginActivityUI().setContentView(this@LoginActivity)
        jmService = JmService(this@LoginActivity)
    }

    fun sendCode() {
        CountDown(10,object: Handler() {
            override fun handleMessage(msg: Message?) {
                if(msg?.what==TIME_DOWN){
                    find<TextView>(R.id.login_send).apply {
                        if(msg?.obj.toString().toInt() > 0){
                            background= getShapeDrawable(resources.getColor(R.color.grey_a600,resources.newTheme()),10f,1, resources.getColor(R.color.grey_a600,resources.newTheme()),null,null)
                            text="(${msg?.obj.toString()})重新获取"
                        }else{
                            background= getShapeDrawable(resources.getColor(R.color.main,resources.newTheme()),10f,1,resources.getColor(R.color.main,resources.newTheme()),null,null)
                            text="获取验证码"
                        }
                    }
                }
            }
        })
//        jmService?.GetObject(object: AbsCallbackListener {
//            override fun onSuccess(t: Any?, call: Call?, response: Response?) {
//            }
//
//            override fun onError(call: Call?, response: Response?, e: Exception?) {
//            }
//        })
    }

    fun loginSys(username:String,code:String){
        loginEMC(username,code)
    }
    /**
     * 登陆环信
     */
    fun loginEMC(username:String,password:String) {
//        val loading=Jm_Loading(this@LoginActivity)
//        loading.show()
        EMClient.getInstance().logout(true)
        EMClient.getInstance().login(username, password, object : EMCallBack {
            override fun onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
//                this@LoginActivity.runOnUiThread { loading.dismiss()}
                SPUtil.saveSerData(this@LoginActivity,UserInfo(1,username),SPUtil.USER_INFO_FILE)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
                XLog.v(TGA, "登录聊天服务器成功")
            }

            override fun onProgress(p0: Int, p1: String?) {
            }

            override fun onError(p0: Int, p1: String?) {
                try {
                when(p0){
                    204->{
                        //不存在此用户
                        EMClient.getInstance().createAccount(username, password)//同步方法
                        loginEMC(username, password)
                        XLog.v(TGA, "ECM注册用户成功^ _ ^")
                    }
                    206->{
                        //账户在另外一台设备登录p0
                        this@LoginActivity.runOnUiThread {
                            login_username?.setText("")
                            login_yzm?.setText("")
                            Toast_Short("用户已经登陆",this@LoginActivity)
                        }
                        return
                    }
                    202->{
                        this@LoginActivity.runOnUiThread {
                            login_username?.setText("")
                            login_yzm?.setText("")
                            Toast_Short("用户错误", this@LoginActivity)
                        }
                        return
                        //用户id或密码错误
                    }
                    200->{
                        //用户已登录
                    }
                }
                }catch (e : HyphenateException){
                    XLog.v(TGA, "ECM注册用户失败(∩_∩)")
                }
                XLog.v(TGA, "登录聊天服务器失败！$p0 --$p1")
            }

        })
    }
    /**
     * 倒计时
     */
    fun CountDown(limitSec: Int,handle:Handler) {
        var Sec = limitSec
        Thread(Runnable {
            while (Sec > 0) {
                handle.sendMessage(Message.obtain().apply {
                    what=TIME_DOWN
                    obj=--Sec
                })
                Thread.sleep(1000)
            }
        }).start()
    }
}
