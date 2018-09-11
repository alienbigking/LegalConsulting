package com.gkzxhn.legalconsulting.utils


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.DecimalFormat
import java.util.regex.Pattern


 /**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
//强大的Toast
fun Context.showToast(message: String): Toast {
    var toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
//    toast.setGravity(Gravity.CENTER,0,0)
    toast.show()
    return toast
}

//Activity切换
inline fun <reified T : Activity> Activity.newIntent() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).
            unsubscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
}

//判断手机号格式是否正确
fun Context.isMobileNO(mobiles: String): Boolean {
    var telRegex = "[1][345678]\\d{9}"
    return when (mobiles.isEmpty()) {
        true -> false
        else -> mobiles.matches(telRegex)
    }
}

fun Context.dp2px(dpValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (pxValue / scale + 0.5).toInt()
}

private fun String.matches(regex: String): Boolean {
    return Pattern.matches(regex, this)
}

/**
 * 格式化数字，保留两位小数
 */
fun Context.df(double: Double): String {
    return DecimalFormat("#0.00").format(double)
}

fun Context.stringBank(string: String): String {
    if ("" == string){
        return ""
    }
    var sb = StringBuilder()
    var b = 0
    for (i in 0 until string.length) {
        if (i == 0) {
            sb.append(string[i])
        } else {
            b = i + 1
            if (b % 4 == 0) {
                sb.append(string[i])
                if (i != string.length){
                    sb.append(" ")
                }
            }else{
                sb.append(string[i])
            }
        }
    }
    return sb.toString()
}







