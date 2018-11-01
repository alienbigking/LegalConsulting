package com.gkzxhn.legalconsulting.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import java.io.*


/**
 * @author：liushaoxiang
 * @date：2018/10/26 2:13 PM
 * @description：图片处理工具
 */
object ImageUtils {
    fun calculateInSampleSize(options: BitmapFactory.Options,
                              reqWidth: Int, reqHeight: Int): Int {
        // 源图片的高度和宽度
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(res: Resources, resId: Int,
                                        reqWidth: Int, reqHeight: Int): Bitmap {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun decodeSampledBitmapFromFilePath(imagePath: String,
                                        reqWidth: Int, reqHeight: Int): Bitmap {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(imagePath, options)
    }
//    /storage/emulated/0/DCIM/Camera/IMG_20180523_173706.jpg
//    /mnt/sdcard/
    /**
     * base64编码字符集转化成图片文件。
     * @param base64Str
     * @param path 文件存储路径
     * @return 是否成功
     */

    fun base64ToFile(base64Str: String, path: String): Boolean {
//        var base64Str="ClTVOTtCN3TyPo5HVNZiM1Wys6PvIVzS5pOAr18f039cxLvj7z+S2trlWG/HaXUgMIHK3Jlkr0XL6n3GVyzu0B0DAQ4hX5Xs/oYwV9+5hSl5QkAlG6agXbcgv4q3Tq2Kzmcko834l8F28NdzRO6kThYqbFfKI4HMtTqsDavsheuO9XwZxG/4PikVYZ2bWymlCjeIbojTq4cAvV0L5RSjuUaZSR1Ulh+P6L8gllLVCEJKOhUbG9wBgZBNkJNJ4wxN3VaNLVBx/GeuO7h/rwo6nCqGYGsDgjHMaR36npRz7zKT330nVd3Ruo3y9069abFmswPJ7r1gcLG2iur65qMNiQc/ZoXvLw8TKDkKwkEHRhpAmLYs3TcHRfO9t11clbJti8zBQvmYzOTTYgd6pGg0UBnxP0DLJeXtSqXUl9Z4sCGTGAW0z6NWYXidIaedaAg5sqU3ZwJNfscJwaFdwXGQZ/kRCKNdOpUTOiqI3l5pRedfN9sNjSbKJ4MTWZSbzINJ8kWkxTJYtSBw9OdfhwamK1bquuvb0rWr+Ebqit/oAUzqHC+JTbheFmaBrwEnMiWK6DtX9tM7uArEEvh4BX3TuMLUV7lMsprsWLRQEOiK5VSaFsnbSafSacpGHCgM5XFnWtRp5Mkcb3dXrcULLY02iS6ntv/rle/0Jfr/3oqgZr2FpHanaRSiTmICtrLIq6cjIzaixQV15GihIhJkQa982jS2wBg1ALjUmYvbDR7cotCJaBLjg1MjoxWC7NCYchZWj3C/K8Z7OZkoi7Z6iW76iFgJNc7wtkKR0iAlpg1hrIIAH25pvFFa0Zmxvduv93UdCVdO1u9f9fFChuOYW4W8ipazTNVW/s6fDY17e/9VDf/OaRjo5K1et9kA7m++6s13XjmW195We+aLO089GB5vNTjc6OFEczPbo9Ujx2tbV1yaoklAv/By5v0GO2misKAAAAAElFTkSuQmCC"
        val data = Base64.decode(base64Str, Base64.DEFAULT)
        for (i in data.indices) {
            //调整异常数据
            if (data[i] < 0) {
                data[i] = (data[i] + 256).toByte()
            }

        }
        var os: OutputStream? = null
        try {
            os = FileOutputStream(path)
            os.write(data)
            os.flush()
            os.close()
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 将图片转换成Base64编码的字符串
     * @param path
     * @return base64编码的字符串
     */
    fun imageToBase64(path: String): String? {
        if (TextUtils.isEmpty(path)) {
            return null
        }
        var `is`: InputStream? = null
        var data: ByteArray? = null
        var result: String? = null
        try {
            `is` = FileInputStream(path)
            //创建一个字符流大小的数组。
            data = ByteArray(`is`.available())
            //写入数组
            `is`.read(data)
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != `is`) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        }
        return result
    }


}