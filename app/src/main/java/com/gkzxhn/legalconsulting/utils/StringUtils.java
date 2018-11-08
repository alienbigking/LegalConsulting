package com.gkzxhn.legalconsulting.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Explanation：
 *
 * @author LSX
 * Created on 2018/1/31.
 */
public class StringUtils {

    /**
     * 自1970年至今将秒数转化为日期
     *
     * @param time
     * @return
     */
    public static String MstoDate(String time) {
        String str = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!TextUtils.isEmpty(time)) {
            str = dateFormat.format(Long.parseLong(time) * 1000);
        }
        return str;
    }

    /**
     * 自1970年至今将秒数转化为日期
     *
     * @param time
     * @return
     */
    public static String MstoDateNoYear(String time) {
        String str = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        if (!TextUtils.isEmpty(time)) {
            str = dateFormat.format(Long.parseLong(time) * 1000);
        }
        return str;
    }

    /**
     * 自1970年至今将秒数转化为日期
     *
     * @param time
     * @return
     */
    public static String MstoDateOnlyYmd(String time) {
        String str = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!TextUtils.isEmpty(time)) {
            str = dateFormat.format(Long.parseLong(time) * 1000);
        }
        return str;
    }

    /**
     * 手机号185****0113
     *
     * @return
     */
    public static String phoneChange(String phone) {
        if (phone.length() == 11) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else {
            return phone;
        }
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、177（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/7 2:40 PM.
     * @description： 将2018-11-07T05:54:21.000+0000  改成标准时间
     */
    public static String parseDate(String dateStr) throws ParseException {
        if (dateStr == null) {
            return "0000-00-00 00:00:00";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        Date result;
        result = df.parse(dateStr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(result);
    }


}
