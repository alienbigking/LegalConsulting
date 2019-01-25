package com.gkzxhn.legalconsulting.utils

import android.text.TextUtils
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Explanation：
 * @author LSX
 * Created on 2018/1/31.
 */

object StringUtils {

    /**
     * 自1970年至今将秒数转化为日期
     * @param time
     * @return
     */
    fun MstoDate(time: String): String {
        var str = ""
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (!TextUtils.isEmpty(time)) {
            str = dateFormat.format(java.lang.Long.parseLong(time))
        }
        return str
    }

    /**
     * 自1970年至今将秒数转化为日期
     *
     * @param time
     * @return
     */
    fun MstoDateNoYear(time: String): String {
        var str = ""
        val dateFormat = SimpleDateFormat("MM-dd HH:mm:ss")
        if (!TextUtils.isEmpty(time)) {
            str = dateFormat.format(java.lang.Long.parseLong(time) * 1000)
        }
        return str
    }

    /**
     * 自1970年至今将秒数转化为日期
     *
     * @param time
     * @return
     */
    fun MstoDateOnlyYmd(time: String): String {
        var str = ""
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        if (!TextUtils.isEmpty(time)) {
            str = dateFormat.format(java.lang.Long.parseLong(time) * 1000)
        }
        return str
    }

    /**
     * 手机号185****0113
     *
     * @return
     */
    fun phoneChange(phone: String): String {
        return if (phone.length == 11) {
            phone.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        } else {
            phone
        }
    }

    /**
     * 验证手机格式
     */
    fun isMobileNO(mobileNums: String): Boolean {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、177（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        val telRegex = "[1][35789]\\d{9}"// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return if (TextUtils.isEmpty(mobileNums))
            false
        else
            mobileNums.matches(telRegex.toRegex())
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/7 2:40 PM.
     * @description： 将2018-11-07T05:54:21.000+0000  改成标准时间
     */
    @Throws(ParseException::class)
    fun parseDate(dateStr: String?): String {
        if (dateStr == null) {
            return "0000-00-00 00:00:00"
        }
        val df = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ")
        val result: Date
        result = df.parse(dateStr)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        sdf.timeZone = TimeZone.getTimeZone("GMT")
        return sdf.format(result)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/20 4:49 PM.
     * @description： 保留两位小数点
     */
    fun formatStringTwo(f: Double): String {
        val df = DecimalFormat("0.00")
        return df.format(f)
    }

}
