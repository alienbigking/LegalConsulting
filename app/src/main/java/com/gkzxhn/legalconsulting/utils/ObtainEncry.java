package com.gkzxhn.legalconsulting.utils;

import android.util.Log;

import java.util.Iterator;
import java.util.Map;


 /**
 * Explanation:
 * @author LSX
 *    -----2018/9/11
 */
public class ObtainEncry {

    private static String MD5 = "7q238*whowi*&dkfSKDWIE812*(23z7^&*12ksjSKW7";

    public static String getEncry(Map<String, String> map){

        StringBuffer stringBuffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator= map.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            if (i == 0) {
                stringBuffer.append(next.getKey() + "=" + next.getValue());
            }else {
                stringBuffer.append("&"+next.getKey() + "=" + next.getValue());
            }
            i++;
        }
        String encry = stringBuffer.toString();
//        String encry = stringBuffer.toString()+MD5;
        Log.e("encry",encry);
        String md = Md5Utils.md5(encry);
        Log.e("md",md);
        return md;
    }
}
