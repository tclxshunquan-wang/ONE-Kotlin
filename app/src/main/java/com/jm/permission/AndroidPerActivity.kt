package com.jm.permission

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import java.util.*

/**
 * Created by shunq-wang on 2016/12/7.
 * Android 权限操作类
 */

open class AndroidPerActivity : AppCompatActivity() {
    var mCt:Context=this@AndroidPerActivity
    var REQUEST_CODE_PERMISSION = 0x00099
    var reqPreCallback:AndroidPreCallback?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(arrayOf(Manifest.permission.READ_PHONE_STATE),0x00001)
    }

    /**
     * 请求权限
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    fun requestPermission(permissions: Array<String>, requestCode: Int) {
        REQUEST_CODE_PERMISSION = requestCode
        if (checkPermissions(permissions)) {
            reqPreCallback?.requestPreSuccess(REQUEST_CODE_PERMISSION)
        } else {
            val needPermissions = getDeniedPermissions(permissions)
            ActivityCompat.requestPermissions(this, needPermissions.toArray(arrayOfNulls<String>(needPermissions.size)), REQUEST_CODE_PERMISSION)
        }
    }
    /**
     * 检测所有的权限是否都已授权
     * @param permissions
     * @return
     */
    private fun checkPermissions(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(mCt, permission) !== PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 获取权限集中需要申请权限的列表
     * @param permissions
     * @return
     */
    private fun getDeniedPermissions(permissions: Array<String>):ArrayList<String>{
        val needRequestPermissionList = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(mCt, permission) !== PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(mCt as Activity, permission)) {
                needRequestPermissionList.add(permission)
            }
        }
        return needRequestPermissionList
    }

    /**
     * 系统请求权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override  fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                reqPreCallback?.requestPreSuccess(REQUEST_CODE_PERMISSION)
            } else {
                reqPreCallback?.requestPreFaile(REQUEST_CODE_PERMISSION)
                showTipsDialog()
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     * @param grantResults
     * @return
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
    /**
     * 显示提示对话框
     */
    private fun showTipsDialog() {
        AlertDialog.Builder(mCt).setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消")
                { dialog, which ->

                }
                .setPositiveButton("确定")
                { dialog, which ->
                 startAppSettings()
                }.show()
    }

    /**
     * 启动当前应用设置页面
     */
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + mCt.packageName)
        mCt.startActivity(intent)
    }


}