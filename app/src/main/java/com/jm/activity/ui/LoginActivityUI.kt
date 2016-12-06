package com.jm.activity.ui

import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.jm.R
import com.jm.activity.LoginActivity
import com.jm.data.utils.Toast_Short
import com.jm.data.utils.getScreenHeight
import com.jm.data.utils.getScreenWidth
import com.jm.data.utils.getShapeDrawable
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.*

/**
 * Created by shunq-wang on 2016/11/22.
 */

class LoginActivityUI : AnkoComponent<LoginActivity> {
    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        val screenWidth = getScreenWidth(ui.ctx)
        val screenHeigh = getScreenHeight(ui.ctx)

        linearLayout {
            lparams {
                width = matchParent
                height = matchParent
            }
            include<RelativeLayout>(R.layout.jm_login).apply {
                lparams {
                    width = matchParent
                    height = matchParent
                }
                ui.owner.login_send = find<TextView>(R.id.login_send).apply {
                    background = getShapeDrawable(resources.getColor(R.color.main,resources.newTheme()), 10f, 1,resources.getColor(R.color.main,resources.newTheme()), null, null)
                    onClick {
                        if (ui.owner.login_username?.text.isNullOrEmpty()) {
                            Toast_Short("手机号码不能为空", ui.owner.ctx)
                        } else {
                            ui.owner.sendCode()
                        }
                    }
                }
                ui.owner.login_sub = find<Button>(R.id.login_sub).apply {
                    background = getShapeDrawable(resources.getColor(R.color.main,resources.newTheme()), 10f, 1, resources.getColor(R.color.main,resources.newTheme()), null, null)
                    onClick {
                        if(ui.owner.login_username?.text.isNullOrEmpty()){
                            Toast_Short("手机号码不能为空", ui.owner.ctx)
                        }else if(ui.owner.login_yzm?.text.isNullOrEmpty()){
                            Toast_Short("验证码不能为空", ui.owner.ctx)
                        }else{
                            ui.owner.loginSys(ui.owner.login_username?.text.toString(),ui.owner.login_yzm?.text.toString())
                        }
                    }
                }
                ui.owner.login_username=find<MaterialEditText>(R.id.login_username).apply {
                    maxLines=1
                    inputType=InputType.TYPE_CLASS_NUMBER
                }
                ui.owner.login_yzm=find<MaterialEditText>(R.id.login_yzm).apply {
                    maxLines=1
                    inputType=InputType.TYPE_CLASS_NUMBER
                }
            }

        }
    }

}