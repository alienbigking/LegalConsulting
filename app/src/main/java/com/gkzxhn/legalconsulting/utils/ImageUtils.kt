package com.gkzxhn.legalconsulting.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import com.gkzxhn.legalconsulting.common.App.Companion.mContext
import com.gkzxhn.legalconsulting.common.Constants
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

    /**
     * base64编码字符集转化成图片文件。
     * @param base64Str
     * @param path 文件存储路径
     * @return 是否成功
     */

    fun base64ToFile(base64Str: String, path: String): Boolean {
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

    fun base64ToBitmap(fileName: String, base64Str: String): Bitmap? {
        val file1 = File(mContext.cacheDir, fileName)
        return if (base64Str.isNotEmpty() && base64Str.length > Constants.BASE_64_START.length) {
            val base64ToFile = ImageUtils.base64ToFile(base64Str.substring(Constants.BASE_64_START.length), file1.absolutePath)
            if (base64ToFile) {
                val decodeFile = BitmapFactory.decodeFile(file1.absolutePath)
                decodeFile
            } else {
                null
            }
        } else {
            null
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

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    fun base64ToBitmap(base64Data: String): Bitmap {

        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun bitmapToBase64(bitmap: Bitmap?): String? {

        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                baos.flush()
                baos.close()

                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result
    }


    /**
     * 质量压缩图片，图片占用内存减小，像素数不变，常用于上传
     * @param size 期望图片的大小，单位为kb
     * @param file
     * @return
     */
    fun compressImage(bitmap: Bitmap, file: File, size: Int): File? {
        var options = 100
        val baos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().size / 1024 > size) {
            options -= 10// 每次都减少10
            baos.reset()// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)
        }
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


}