package com.jm.data.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.jm.data.utils.XLog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.RandomAccessFile
import java.lang.reflect.Field
import java.math.BigDecimal
import java.net.Inet4Address
import java.net.NetworkInterface
import java.nio.channels.FileChannel
import java.security.DigestException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by wangsq on 2015/7/31 0031.
 */


fun getAppName(cTx: Context, pID:Int):String {
    var processName:String?= null
    val am =cTx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val l = am.runningAppProcesses
    val i = l.iterator()
    while (i.hasNext()) {
       val info = i.next()
        try {
            if (info.pid == pID) {
                processName = info.processName
                return processName;
            }
        } catch (e:Exception ) {
            // Log.d("Process", "Error>> :"+ e.toString());
        }
    }
    return processName!!
}


fun toMaps(data: Any): LinkedHashMap<String, String> {
    val map: LinkedHashMap<String, String> = LinkedHashMap()
    val fields: Array<out Field> = data.javaClass.declaredFields
    for (field: Field in fields) {
        if (field.name.indexOf("kotlinClass") > 0) {
            continue
        }
        field.isAccessible = true
        if (field.get(data) != null) map.put(field.name, field.get(data).toString())
    }
    return map
}

fun getTitleStyle(text: String, color: Int, startIndex: Int = 0, endIndex: Int = text.length - 1): SpannableStringBuilder {
    val style = SpannableStringBuilder(text)
    style.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    return style
}

/**
 * 将BITMAP图片转为BASE64字符串
 */
fun Bitmap2StrByBase64(bit: Bitmap): String {
    val bos = ByteArrayOutputStream()
    bit.compress(Bitmap.CompressFormat.JPEG, 60, bos)
    return String(Base64.encode(bos.toByteArray(), Base64.DEFAULT))
}

fun MD5(str: String): String {
    val data: ByteArray = str.toByteArray(charset("utf-8"))
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    md5.update(data)
    val md5Data: ByteArray = md5.digest()
    val sb: StringBuffer = StringBuffer("")
    var ivi: Int
    for (iv in md5Data) {
        ivi = iv.toInt()
        if (ivi < 0) {
            ivi = ivi.plus(256)
        }
        if (ivi < 16)
            sb.append("0")
        sb.append(Integer.toHexString(ivi))
    }
    return sb.toString()
}

fun Toast_Short(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Toast_Long(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun getDeviceId(context: Context): String {
//    val telephonyManager: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    return telephonyManager.deviceId ?: ""
    return "000000010000100101"
}

/**
 * 获取应用名称
 */
fun getAppName(context: Context): String {
    val packageManager = context.packageManager
    val packageInfo = packageManager.getApplicationInfo(context.packageName, 0)
    return packageManager.getApplicationLabel(packageInfo) as String
}

/**
 * 获取版本名称
 */
fun getAppVersion(context: Context): String {
    return context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1"
}

/**
 * 获取APP版本号
 */
fun getAppVersionCode(context: Context): Int {
    return context.packageManager.getPackageInfo(context.packageName, 0).versionCode
}

/**
 * 获取本机IP
 */
fun getLocalIP(context: Context): String? {
    val ip: String?
    if (getNetWorkType(context) == ConnectivityManager.TYPE_WIFI) {
        ip = getWIFIIpAddress(context)
    } else {
        ip = getGNETIpAddress(context)
    }
    return ip
}

/**
 * 获取网络状态
 */
fun getNetWorkType(context: Context): Int {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo = cm.activeNetworkInfo ?: return -1
    return netInfo.type
}

/**
 * 是否连接
 */
fun isNetWorkAvilizable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo? = cm.activeNetworkInfo ?: return false
    return netInfo?.isConnectedOrConnecting as Boolean
}

/**
 * 随机数字符串
 * @param len 需要随机数的长度
 */
fun randomNum(len: Int): String? {
    if (len == 0) return null
    val sb = StringBuffer("")
    val random = Random()
    while (sb.length < len) {
        sb.append(random.nextInt(10))
    }
    return sb.toString()
}

/**
 * 四舍五入保留小数
 * @param num
 * @param len 保留小数长度
 */

fun doubleOutPut(v: Double, num: Int): String {
    if (v == java.lang.Double.valueOf(v)!!.toInt().toDouble())
        return (java.lang.Double.valueOf(v)!!.toInt()).toString()
    else {
        return roundNum(v.toString(), num).toString()
    }

}

fun roundNum(num: String, len: Int): Double {
    val bd = BigDecimal(num)
    return bd.setScale(len, BigDecimal.ROUND_HALF_UP).toDouble()
}

fun getDensity(context: Context): Float? {
    val resources = context.resources
    return resources.displayMetrics.density
}


private fun getWIFIIpAddress(context: Context): String {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo
    val ipAddress = wifiInfo.ipAddress
    return "%d.%d.%d.%d".format((ipAddress and 255), (ipAddress shr 8 and 255), (ipAddress shr 16 and 255), (ipAddress shr 24 and 255))
}

private fun getGNETIpAddress(context: Context): String? {
    var gNetIP: String? = null
    try {
        val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf: NetworkInterface = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    gNetIP = inetAddress.hostAddress.toString()
                }
            }
        }
    } catch(e: Exception) {
        XLog.e("CommonsUtils", "getGNETIpAddress fun error." + e.message, e)
        gNetIP = null
    }
    return gNetIP
}

/**
 * 画边框方法

 * @param color      背景色
 * *
 * @param radius     圆角
 * *
 * @param stockWidth 边框宽度
 * *
 * @param stockColor 边框颜色
 * *
 * @param dashWidth  边框线间隔
 * *
 * @param dashGap    边框线长度
 * *
 * @return
 */
fun getShapeDrawable(color: Int, radius: Float, stockWidth: Int? = null, stockColor: Int? = null, dashWidth: Float? = null, dashGap: Float? = null): GradientDrawable {
    val gradientDrawable = GradientDrawable()
    val stockWidth = stockWidth ?: 0
    val stockColor = stockColor ?: Color.parseColor("#ffffff")
    val dashWidth = dashWidth ?: 0f
    val dashGap = dashGap ?: 1f
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setColor(color)
    gradientDrawable.setStroke(stockWidth, stockColor, dashWidth, dashGap)
    return gradientDrawable
}

/**
 * 画边框  倒圆角
 * @param color
 * *
 * @param topLeftRadius
 * *
 * @param topRightRadiu
 * *
 * @param bottomLeftRadius
 * *
 * @param bottomRightRadius
 * *
 * @param stockWidth
 * *
 * @param stockColor
 * *
 * @return
 */
fun getShapeDrawable(color: Int, topLeftRadius: Float, topRightRadiu: Float, bottomLeftRadius: Float, bottomRightRadius: Float, stockWidth: Int?, stockColor: Int?): GradientDrawable {
    var stockWidth = stockWidth
    var stockColor = stockColor
    val gradientDrawable = GradientDrawable()
    val f = floatArrayOf(topLeftRadius, topLeftRadius, topRightRadiu, topRightRadiu, bottomLeftRadius, bottomLeftRadius, bottomRightRadius, bottomRightRadius)
    gradientDrawable.setCornerRadii(f)
    gradientDrawable.setColor(color)
    stockWidth = if (stockWidth == null) 0 else stockWidth
    stockColor = if (stockColor == null) Color.parseColor("#ffffff") else stockColor
    gradientDrawable.setStroke(stockWidth, stockColor)
    return gradientDrawable
}

/**
 * activity切换

 * @param isCallback  选择哪一种切换模式，true为回调模式，false为简单模式
 * *
 * @param requestCode 回调模式下需要的请求码
 */
fun jump(context: Context, cls: Class<*>, bundle: Bundle, isCallback: Boolean, requestCode: Int?) {
    val intent = Intent()
    intent.setClass(context, cls)
    intent.putExtras(bundle)
    if (isCallback) {
        (context as Activity).startActivityForResult(intent, requestCode!!)
    } else {
        context.startActivity(intent)
    }
}

// 价格数据小数点后，三位转两位
fun numberFormat(num: Double): String {
    /*
         * NumberFormat nf=NumberFormat.getNumberInstance();
		 * nf.setMaximumFractionDigits(2); return nf.format(num);
		 */
    val df = DecimalFormat("#0.00")
    return df.format(num)
}

// 价格数据保留整数
fun priceFormat(num: Int): String {
    /*
		 * NumberFormat nf=NumberFormat.getNumberInstance();
		 * nf.setMaximumFractionDigits(2); return nf.format(num);
		 */
    val df = DecimalFormat("#0")
    return df.format(num.toLong())
}

public fun priceFormat(num: Double): String {
    /*
		 * NumberFormat nf=NumberFormat.getNumberInstance();
		 * nf.setMaximumFractionDigits(2); return nf.format(num);
		 */
    val df = DecimalFormat("#0")
    return df.format(num)
}

// 客单件保留一位小数
public fun guestSingleFormat(num: Int): String {
    /*
		 * NumberFormat nf=NumberFormat.getNumberInstance();
		 * nf.setMaximumFractionDigits(2); return nf.format(num);
		 */
    val df = DecimalFormat("#0.0")
    return df.format(num.toLong())
}

public fun guestSingleFormat(num: Double): String {
    /*
		 * NumberFormat nf=NumberFormat.getNumberInstance();
		 * nf.setMaximumFractionDigits(2); return nf.format(num);
		 */
    val df = DecimalFormat("#0.0")
    return df.format(num)
}

public fun formatTosepara(data: Double): String {
    var df = DecimalFormat("#,###.00");
    return df.format(data);
}

public fun formatT(data: Double): String {
    var df = DecimalFormat("#,##0.00");
    return df.format(data)
}

/**
 * 获取屏幕高度
 */
fun getScreenWidth(context: Context): Int {
    val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.widthPixels
}

/**
 * 获取屏幕宽度
 */
fun getScreenHeight(context: Context): Int {
    val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.heightPixels
}

/**
 * 获取当前屏幕截图，包含状态栏
 *
 * @param activity
 * @return
 */
fun snapShotWithStatusBar(activity: Activity): Bitmap {
    val view = activity.window.decorView
    view.isDrawingCacheEnabled = true
    view.buildDrawingCache()
    val bmp = view.drawingCache
    val width = getScreenWidth(activity)
    val height = getScreenHeight(activity)
    val bp: Bitmap?
    bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
    view.destroyDrawingCache()
    return bp
}

/**
 * 获取当前屏幕截图，不包含状态栏
 *
 * @param activity
 * @return
 */
fun snapShotWithoutStatusBar(activity: Activity): Bitmap {
    val view = activity.window.decorView
    view.isDrawingCacheEnabled = true
    view.buildDrawingCache()
    val bmp = view.drawingCache
    val frame = Rect()
    activity.window.decorView.getWindowVisibleDisplayFrame(frame)
    val statusBarHeight = frame.top

    val width = getScreenWidth(activity)
    val height = getScreenHeight(activity)
    val bp: Bitmap?
    bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
    view.destroyDrawingCache()
    return bp
}

fun dip2px(context: Context, dpValue: Float): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 *              设置listview高度
 */
fun getListViewHeight(listView: ListView) {
    val listAdapter = listView.adapter ?: return
    var totalHeight = 0
    for (i in 0..listAdapter.count - 1) {
        val listItem = listAdapter.getView(i, null, listView)
        listItem.measure(0, 0)
        totalHeight += listItem.measuredHeight
    }

    val params = listView.layoutParams

    params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
    listView.layoutParams = params
}


/**
 * 打电话
 */
fun callToNumber(number: String, mContext: Context) {
    mContext.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
}

/**
 * 发短信
 */
fun smsToNumber(number: String, mContext: Context) {
    mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("smsto:$number")))
}

fun getRunningActivityName(context: Context): String {
    val activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.getRunningTasks(1)[0].topActivity.className;
}

fun getRunningActivity(context: Context): ComponentName {
    val manager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return manager.getRunningTasks(1)[0].topActivity
}

/**
 * 更改TEXTVIEW中某些字的颜色(单个颜色)
 */
fun textViewColor(textView: TextView, color: Int, startIndex: Int, endIndex: Int) {
    val builder: SpannableStringBuilder = SpannableStringBuilder(textView.text.toString())
    builder.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    textView.text = builder
}

fun textViewColor(string: String, color: Int, startIndex: Int, endIndex: Int): SpannableStringBuilder {
    val builder: SpannableStringBuilder = SpannableStringBuilder(string)
    builder.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return builder
}

fun textViewBitmap(string: String, bitmap: Bitmap, startIndex: Int, endIndex: Int, context: Context): SpannableString {
    val span = SpannableString(string)
    val imgSpan = ImageSpan(context, bitmap)
    span.setSpan(imgSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

//如果输入法打开则关闭，如果没打开则打开
fun showSoftInput(mContext: Context) {
    val inputManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}

//打开输入法窗口:
fun showSoftInput(view: View, mContext: Context) {
    val inputManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

//强制关闭软键盘
fun closeSoftInput(view: View, mContext: Context) {
    val inputManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, 0);
}

//获取输入法打开的状态
fun showSoftInputIsOpened(mContext: Context): Boolean {
    val inputManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputManager.isActive
}

fun fileGetContent(file: File): ByteArray = with(file) {
    var byteArray: ByteArray? = null
    var fc: RandomAccessFile? = null
    try {
        fc = RandomAccessFile(file.path, "r")
        fc.channel.apply {
            var byteBuffer = map(FileChannel.MapMode.READ_ONLY, 0, size()).load()
            byteArray = ByteArray(size().toInt())
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(byteArray, 0, byteBuffer.remaining())
            }
        }
    } catch(e: Exception) {
        XLog.e("CommonsUtilsKt", "fileGetContent error ${e.message}", e)
    } finally {
        fc?.let {
            fc!!.channel.close()
        }
    }
    return byteArray!!
}

//日期转换
fun formatDate(date: String): String {
    val dateStr = date!!.substring(0, date!!.indexOf('.')).replace("T", " ")
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val format2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val data = format2.parse(dateStr.toString())

    return format.format(data)
}

//日期转换
fun formatDateShort(date: String): String {
    val format = SimpleDateFormat("yyyyMMdd")
    val format2 = SimpleDateFormat("dd")
    val data = format.parse(date.toString())
    return format2.format(data)
}

//判断是否包含汉子
fun isContainChinese(str: String): Boolean {
    var p = Pattern.compile("[\u4e00-\u9fa5]")
    var m = p.matcher(str)
    if (m.find()) {
        return true
    }
    return false
}

@Throws(DigestException::class)
fun SHA1(str:String): String {
    try {
        //指定sha1算法
        val digest = MessageDigest.getInstance("SHA-1")
        digest.update(str.toByteArray())
        //获取字节数组
        val messageDigest = digest.digest()
        // Create Hex String
        val hexString = StringBuffer()
        // 字节数组转换为 十六进制 数
        for (i in messageDigest.indices) {
            val shaHex = Integer.toHexString(messageDigest[i].toInt() and 0xFF)
            if (shaHex.length < 2) {
                hexString.append(0)
            }
            hexString.append(shaHex)
        }
        return hexString.toString().toUpperCase()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        throw DigestException("签名错误！")
    }
}

fun checkService(mCt:Context,serviceName:String):Boolean{
    var isWork = false
    val ser=mCt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runSer=ser.getRunningServices(50)
    if(runSer.size<=0)
        return false
    for (i in 0..runSer.size-1){
        var myName=runSer[i].service.className.toString()
        if(myName == serviceName){
            isWork=true
            break
        }
    }
    return isWork
}