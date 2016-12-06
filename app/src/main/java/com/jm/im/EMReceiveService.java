package com.jm.im;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shunq-wang on 2016/12/2.
 */

public class EMReceiveService extends Service{
    private Context mCt;
    private EMMessageListener msgListener;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiveMsg();

        return super.onStartCommand(intent, flags, startId);
    }
    private void receiveMsg(){

        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                Log.v("EMReceiveService",messages.get(0).getUserName());
                Log.v("EMReceiveService",messages.get(0).getBody().toString());
                Log.v("EMReceiveService",messages.get(0).getMsgId());
                Intent broadIntent =new  Intent();
                broadIntent.setAction("com.jm.EMBroadCast");
                broadIntent.putExtra("EMMsgData", (Serializable) messages);
                sendBroadcast(broadIntent);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

}
