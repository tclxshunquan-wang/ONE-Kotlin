package com.jm.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.jm.data.utils.XLog
import java.io.Serializable

/**
 * Created by shunq-wang
 * Date:2015/8/19 0019
 * Time:14:00
 */
class SPUtil {

    companion object {
        private val TAG = javaClass.simpleName
        const val USER_INFO_FILE = "UserInfo"
        const val BASE_PARAMS_FILE = "BASETags"
        const val SHOP_TAGS_FILE = "SHOPTags"
        const val AUTH_TAGS_FILE = "AuthTags"
        const val INVIDAIL_STATISTICS_DATA = "Invidaildata"

        /**
         * 从本地存储中获取数据（对象）
         */
        fun <T> getSerData(context: Context, clazz: Class<T>, name: String?, key: String?): T {
            var t: T? = null
            try {
                t = clazz.newInstance()
                val fileName = name ?: BASE_PARAMS_FILE
                val sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                val files = clazz.declaredFields
                for (field in files) {
                    if (field.name.indexOf("kotlinClass") > 0) {
                        continue
                    }
                    field.isAccessible = true
                    if (key == null || key.equals(field.name)) {
                        if (field.genericType.equals(String::class.java))
                            field.set(t, sp.getString(field.name, ""))
                        if (field.genericType.equals(Integer::class.java))
                            field.set(t, sp.getInt(field.name, 0))
                        if (field.genericType.equals(Int::class.java))
                            field.set(t, sp.getInt(field.name, 0))
                        if (field.genericType.equals(Float::class.java))
                            field.set(t, sp.getFloat(field.name, 0f))
                        if (field.genericType.equals(Long::class.java))
                            field.set(t, sp.getLong(field.name, 0L))
                        if (field.genericType.equals(Boolean::class.java))
                            field.set(t, sp.getBoolean(field.name, false))
                    }
                }
            } catch(e: Exception) {
                XLog.e(TAG!!, "SPUtil.getSerData error:${e.message}", e)
            }
            return t!!
        }

        fun clear(context: Context, name: String) {
            val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
            sp.edit().clear().commit()
        }

        /**
         * 将对象存储到SharedPreferences
         */
        fun saveSerData(context: Context, data: Serializable, name: String): Boolean {
            val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
            return fillEditor(sp, data).commit()
        }

        private fun fillEditor(sp: SharedPreferences, ser: Serializable): SharedPreferences.Editor {
            val editor = sp.edit()
            val fields = ser.javaClass.declaredFields
            try {
                for (field in fields) {
                    if (field.name.indexOf("kotlinClass") > 0 || field.name.equals("Args")) continue
                    field.isAccessible = true
                    val obj = field.get(ser)
                    obj ?: continue
                    if (obj is String) editor.putString(field.name, obj.toString())
                    if (obj is Int) editor.putInt(field.name, obj.toInt())
                    if (obj is Float) editor.putFloat(field.name, obj.toFloat())
                    if (obj is Boolean) editor.putBoolean(field.name, obj)
                    if (obj is Long) editor.putLong(field.name, obj.toLong())
                }
            } catch(e: Exception) {
                XLog.e(TAG!!, "SPUtil.getSerData error:${e.message}", e)
            }
            return editor
        }

        fun saveInt(context: Context, key: String, value: Int = 0): Boolean {
            val sharedPreferences = context.getSharedPreferences("easy_retail", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(key, value)
            return editor.commit()
        }

        fun getInt(context: Context, key: String, default: Int = 0): Int {
            val sharedPreferences = context.getSharedPreferences("easy_retail", Context.MODE_PRIVATE)
            return sharedPreferences.getInt(key, default)
        }

        fun saveString(context: Context, key: String, value: String? = "", fileName: String = INVIDAIL_STATISTICS_DATA): Boolean {
            val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            return editor.commit()
        }

        fun getCacheString(context: Context, key: String, fileName: String = INVIDAIL_STATISTICS_DATA): String {
            val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, "")
        }

        fun saveLong(context: Context, name: String, value: Long?): Boolean {
            val sharedPreferences = context.getSharedPreferences("easy_retail", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putLong(name, value!!)
            return editor.commit()
        }

        fun getLong(context: Context, name: String): Long {
            val sharedPreferences = context.getSharedPreferences("easy_retail", Context.MODE_PRIVATE)
            val value = sharedPreferences.getLong(name, 0)
            return value
        }

        fun getBoolean(context: Context, name: String, fName: String, default: Boolean = true): Boolean {
            val sharedPreferences = context.getSharedPreferences(fName, Context.MODE_PRIVATE)
            val value = sharedPreferences.getBoolean(name, default)
            return value
        }

        fun saveBoolean(context: Context, name: String, fName: String, value: Boolean = true): Boolean {
            val sharedPreperences = context.getSharedPreferences(fName, Context.MODE_PRIVATE)
            val editor = sharedPreperences.edit()
            editor.putBoolean(name, value)
            return editor.commit()
        }
    }


}