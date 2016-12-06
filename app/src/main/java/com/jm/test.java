package com.jm;

import android.os.Bundle;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jm.activity.BaseActivity;
import com.jm.data.utils.XLog;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by shunq-wang on 2016/11/24.
 */

public class test extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jm_main);


        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                XLog.INSTANCE.e("main", messages.get(0).getUserName());

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
}
