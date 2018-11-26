package com.gkzxhn.legalconsulting.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import com.gkzxhn.legalconsulting.common.App
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.util.*

/**
 * 作者：Administrator on 2017/6/10 16:08
 * 邮箱：zhanghuaiha@gmail.com
 */

object SystemUtil {

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    val systemLanguage: String
        get() = Locale.getDefault().language

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    val systemLanguageList: Array<Locale>
        get() = Locale.getAvailableLocales()

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val systemVersion: String
        get() = android.os.Build.VERSION.RELEASE

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    val systemModel: String
        get() = android.os.Build.MODEL

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    val deviceBrand: String
        get() = android.os.Build.BRAND

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    val imei: String?
        get() {
            val tm = App.mContext.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(App.mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                } else {
                    return tm.deviceId
                }
            }
            return null
        }

    /**
     * 获取缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getTotalCacheSize(context: Context): String {
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return getFormatSize(cacheSize.toDouble(), true)
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    @Throws(Exception::class)
    fun getFolderSize(file: File?): Long {
        var size: Long = 0
        try {
            val fileList = file!!.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                if (fileList[i].isDirectory) {
                    size = size + getFolderSize(fileList[i])
                } else {
                    size = size + fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            //            return size + "Byte";
            //            return "0K";
            return "0K"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 格式化单位  只取M
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double, d: Boolean): String {
        val kiloByte = size / 1024
        val megaByte = kiloByte / 1024

        if (kiloByte < 1) {
            return "0M"
        } else {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M"

        }


    }

    //首先我们先获取当前图片的旋转角度
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    //然后我们再根据图片的旋转角度，给图片进行相应角度的反向旋转
    fun rotateBitmap(bitmap: Bitmap?, rotate: Int): Bitmap? {
        if (bitmap == null)
            return null
        val w = bitmap.width
        val h = bitmap.height
        val mtx = Matrix()
        mtx.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
    }


}
