package com.gkzxhn.legalconsulting.utils

import android.content.Context
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import java.io.File

/**
 * @classname：ProjectUtils
 * @author：liushaoxiang
 * @date：2018/10/12 11:12 AM
 * @description：本项目常用的方法
 */

object ProjectUtils {

    /**
     * @methodName： created by liushaoxiang on 2018/10/12 11:13 AM.
     * @description：给view设置触摸透明度变化
     */
    fun addViewTouchChange(v: View) {
        v.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.alpha = 0.8f
                }
                MotionEvent.ACTION_UP -> {
                    v.alpha = 1.0f
                }
            }
            /****** 返回false 不拦截点击事件的处理 ******/
            false
        }
    }


    /**
     * @methodName ： created by liushaoxiang on 2018/11/1 9:35 AM.
     * @description ： uri 转文件
     */
    fun uri2File(cacheDir: File, uri: Uri): File {
        val uriFile = File(uri.path)
        return File(cacheDir, uriFile.name)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/9 3:02 PM.
     * @description：认证是否通过
     */
    fun certificationStatus(): Boolean {
        val certificationStatus = App.SP.getString(Constants.SP_CERTIFICATIONSTATUS, "")
        return certificationStatus == Constants.CERTIFIED
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/20 8:56 PM.
     * @description：专业领域的转换
     */
    fun categoriesStrToType(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        return when (str) {
            "财产纠纷" -> "PROPERTY_DISPUTES"
            "婚姻家庭" -> "MARRIAGE_FAMILY"
            "交通事故" -> "TRAFFIC_ACCIDENT"
            "工伤赔偿" -> "WORK_COMPENSATION"
            "合同纠纷" -> "CONTRACT_DISPUTE"
            "刑事辩护" -> "CRIMINAL_DEFENSE"
            "房产纠纷" -> "HOUSING_DISPUTES"
            "劳动就业" -> "LABOR_EMPLOYMENT"
            else -> ""
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/9 9:57 AM.
     * @description：专业类别的转化
     */
    fun categoriesConversion(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        return when (str) {
            "PROPERTY_DISPUTES" -> "财产纠纷"
            "MARRIAGE_FAMILY" -> "婚姻家庭"
            "TRAFFIC_ACCIDENT" -> "交通事故"
            "WORK_COMPENSATION" -> "工伤赔偿"
            "CONTRACT_DISPUTE" -> "合同纠纷"
            "CRIMINAL_DEFENSE" -> "刑事辩护"
            "HOUSING_DISPUTES" -> "房产纠纷"
            "LABOR_EMPLOYMENT" -> "劳动就业"
            else -> {
                "其它"
            }


        }
    }


    fun loadImage(context: Context?, avatarURL: String?, imageview: ImageView?) {
        if (avatarURL != null) {
            Glide.with(context).load("$avatarURL?token=523b87c4419da5f9186dbe8aa90f37a3876b95e448fe2a")
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(120)))
                    .into(imageview)
        }
    }
}