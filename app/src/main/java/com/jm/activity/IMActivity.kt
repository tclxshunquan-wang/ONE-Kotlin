package com.jm.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.widget.RelativeLayout
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMMessageBody
import com.hyphenate.chat.adapter.message.EMAMessage
import com.hyphenate.chat.adapter.message.EMAMessageBody
import com.jm.MainActivity
import com.jm.R
import com.jm.activity.ui.IMActivityUI
import com.jm.data.beans.IMData
import com.jm.data.beans.UserInfo
import com.jm.data.cache.ServiceCache
import com.jm.data.utils.*
import com.jm.im.EMBroadCast
import com.jm.im.EMReceiveService
import com.jm.utils.KeyboardChangeListener
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.setContentView

class IMActivity : AppCompatActivity() {
    var keyBoardHeight: Int = 0
    val TGA = IMActivityUI::class.java.canonicalName
    var keyBoard: KeyboardChangeListener? = null
    var im_ecm: RelativeLayout? = null
    var im_bottom: RelativeLayout? = null
    var im_edit: MaterialEditText? = null
    var myAdapter: IMActivityUI.MyRecycleAdapter?=null

    var EMServiceType:String= "com.jm.EMBroadCast"
    var myBroadCast:EMBroadCast?=null
    var userinfo: UserInfo?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IMActivityUI().setContentView(this@IMActivity)
        userinfo=ServiceCache.getUserFromLocal(this@IMActivity)
        //监听键盘弹出隐藏
        keyBoard = KeyboardChangeListener(this).apply {
            setKeyBoardListener(object : KeyboardChangeListener.KeyBoardListener {
                override fun onKeyboardChange(isShow: Boolean, keyboardHeight: Int) {
                    XLog.v(TGA, "im_bottom-->>${im_ecm?.height}")
                    if (isShow) {
                        //键盘弹起，im_ecm布局填充布局
                        keyBoardHeight = keyboardHeight
                        if (im_ecm?.height!! > 0) {
                            removeViewFromECM()
                        }
                    }
                }
            })
        }
        val itf = IntentFilter()
        itf.addAction(EMServiceType)
        myBroadCast=EMBroadCast(EMHandler,EMServiceType)
        registerReceiver(myBroadCast, itf)
    }


    override fun onResume() {
        super.onResume()
        startEMReceiveService()
    }

    fun removeViewFromECM() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        im_ecm?.apply {
            backgroundColor = resources.getColor(R.color.green_g400)
            layoutParams = params
        }
    }

    fun addViewToECM() {
        if (showSoftInputIsOpened(this@IMActivity)) {
            closeSoftInput(im_edit!!, this@IMActivity)
        }
        if (im_ecm?.height!! > 0) {
            return
        }
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyBoardHeight)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        im_ecm?.apply {
            backgroundColor = resources.getColor(R.color.green_g400)
            layoutParams = params
        }

    }
    /**
     * 发送消息，刷新列表
     */

    fun sendMessage(){

      try {
          var toUser:String="222"
          if(userinfo?.UserName=="111")
          {toUser="222"}
          else if(userinfo?.UserName=="222")
          {toUser="111"}
          val msgEdit: Editable =im_edit?.text!!
          if(!msgEdit.isNullOrEmpty()) {
              im_edit?.setText("")
              val msgStr = msgEdit.toString()
              //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
              val message = EMMessage.createTxtSendMessage(msgStr, toUser)
              //如果是群聊，设置chattype，默认是单聊
              message.setMessageStatusCallback(object : EMCallBack {
                  override fun onSuccess() {
                      EMHandler.sendMessage(Message().apply {
                          what = Constant.EMSERVICE_FAILE
                          obj = IMData().apply {
                              From =userinfo?.UserName
                              To = toUser
                              Content = msgStr
                          }
                      })

                  }

                  override fun onProgress(p0: Int, p1: String?) {
                  }

                  override fun onError(p0: Int, p1: String?) {
                  }

              })
              //发送消息
              EMClient.getInstance().chatManager().sendMessage(message)
          }
      }catch (e:Exception){
          XLog.v("--->>","$e")
      }
    }
    /**
     * 接受消息，刷新列表
     * */
    var EMHandler=object:Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
                Constant.EMSERVICE_SUCCESS->{
                    val msgData=msg?.obj as List<*>
                    val data=msgData[0] as EMMessage
                    XLog.v(TGA, "EMHandler-->>${data.userName}")
                    myAdapter?.leftDataList?.add(IMData().apply {
                        From=data.from
                        To=data.to
                        Content=data.body.toString()
                    })
                    myAdapter?.notifyDataSetChanged()
                }
                Constant.EMSERVICE_FAILE->{
                    val data=msg?.obj as IMData
                    myAdapter?.leftDataList?.add(data)
                    myAdapter?.notifyDataSetChanged()
                }
            }
        }
    }
    /**
     * 开启接受消息服务
     * */
    fun  startEMReceiveService(){
       if( checkService(this@IMActivity,"com.jm.im.EMReceiveService"))
       {
           return
       }
        Thread(Runnable {
            startService(Intent(this@IMActivity, EMReceiveService::class.java))
        }).start()
    }

    override fun onDestroy() {
        keyBoard?.destroy()
        unregisterReceiver(myBroadCast)
        this@IMActivity.stopService(Intent(this@IMActivity, EMReceiveService::class.java).apply {
            action=EMServiceType
        })
        super.onDestroy()
    }
}
