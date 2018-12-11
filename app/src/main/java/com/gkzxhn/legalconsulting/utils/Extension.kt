package com.gkzxhn.legalconsulting.utils


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Pattern


 /**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
//强大的Toast
fun Context.showToast(message: String): Toast {
    var toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
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

fun String.logE(t: Any) {
    Log.e(t::class.java.simpleName, this)
}

fun String.logI(t: Any) {
    Log.i(t::class.java.simpleName, this)
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
 * 质量压缩图片，图片占用内存减小，像素数不变，常用于上传
 * @param size 期望图片的大小，单位为kb
 * @param file
 * @return
 */
fun Bitmap.compressImage(file: File, size: Int): File? {
    var options = 100
    val baos = ByteArrayOutputStream()
    // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
    while (baos.toByteArray().size / 1024 > size) {
        options -= 10// 每次都减少10
        baos.reset()// 重置baos即清空baos
        // 这里压缩options%，把压缩后的数据存放到baos中
        this.compress(Bitmap.CompressFormat.JPEG, options, baos)
    }
    /*// 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;*/
    try {
        val fos = FileOutputStream(file)
        fos.write(baos.toByteArray())
        fos.flush()
        fos.close()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    return file
}






