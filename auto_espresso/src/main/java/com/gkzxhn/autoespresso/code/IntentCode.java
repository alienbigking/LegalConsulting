package com.gkzxhn.autoespresso.code;

import android.app.Activity;
import android.app.Instrumentation;

import com.gkzxhn.autoespresso.config.Config;
import com.gkzxhn.autoespresso.operate.TIntent;
import com.gkzxhn.autoespresso.util.TUtils;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class IntentCode extends BaseCode {
    /**
     *拦截指定classname的Intent
     * @param procedureName
     * @param className
     * @param resultCode 如：Activity.RESULT_OK
     * @return
     */
    public static String intercept_classname(String procedureName,String className,String resultCode) {
        if(resultCode.isEmpty())resultCode="Activity.RESULT_OK";
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",new Instrumentation.ActivityResult(%s,null))"+END_LINE;
        else
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",Instrumentation.ActivityResult(%s,null))"+END_LINE;
        return String.format(format,procedureName,className,resultCode);
    }
    /**
     *拦截指定classname的Intent 并随机一张本地图片地址
     * @param procedureName
     * @param className
     * @param extraKey
     * @param resultCode eg:Activity.RESULT_OK
     * @return
     */
    public static String intercept_classname_extra_image(String procedureName,String className,String resultCode, String extraKey) {
        if(resultCode.isEmpty())resultCode="Activity.RESULT_OK";
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",new Instrumentation.ActivityResult(%s,TIntent.getImageIntent(mActivityTestRule.getActivity(),\"%s\",%s)))"+END_LINE;
        else
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",Instrumentation.ActivityResult(%s,TIntent.getImageIntent(this,\"%s\",%s)))"+END_LINE;
        return String.format(format,procedureName,className,resultCode,extraKey,false);
    }
    /**
     *拦截指定classname的Intent 并extras bundle返回随机一张本地图片地址
     * @param procedureName
     * @param className
     * @param extraKey
     * @param resultCode eg:Activity.RESULT_OK
     * @return
     */
    public static String intercept_classname_extras_image(String procedureName,String className,String resultCode, String extraKey) {
        if(resultCode.isEmpty())resultCode="Activity.RESULT_OK";
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",new Instrumentation.ActivityResult(%s,TIntent.getImageIntent(mActivityTestRule.getActivity(),\"%s\",%s)))"+END_LINE;
        else
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",Instrumentation.ActivityResult(%s,TIntent.getImageIntent(this,\"%s\",%s)))"+END_LINE;
        return String.format(format,procedureName,className,resultCode,extraKey,true);
    }
    /**
     *拦截指定classname的Intent 并随机返回extra指定数量的本地图片地址
     * @param procedureName
     * @param className
     * @param extraKey
     * @param resultCode eg:Activity.RESULT_OK
     * @return
     */
    public static String intercept_classname_extra_images(String procedureName,String className,String resultCode, String extraKey,String imagePathsCount) {
        if(resultCode.isEmpty())resultCode="Activity.RESULT_OK";
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",new Instrumentation.ActivityResult(%s,TIntent.getImagesIntent(mActivityTestRule.getActivity(),\"%s\",%s,%s)))"+END_LINE;
        else
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",Instrumentation.ActivityResult(%s,TIntent.getImagesIntent(this,\"%s\",%s,%s)))"+END_LINE;
        return String.format(format,procedureName,className,resultCode,extraKey,imagePathsCount,false);
    }
    /**
     *拦截指定classname的Intent 并随机返回extras bundle指定数量的本地图片地址
     * @param procedureName
     * @param className
     * @param extraKey
     * @param resultCode eg:Activity.RESULT_OK
     * @return
     */
    public static String intercept_classname_extras_images(String procedureName,String className,String resultCode, String extraKey,String imagePathsCount) {
        if(resultCode.isEmpty())resultCode="Activity.RESULT_OK";
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",new Instrumentation.ActivityResult(%s,TIntent.getImagesIntent(mActivityTestRule.getActivity(),\"%s\",%s,%s)))"+END_LINE;
        else
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_classname(\"%s\",Instrumentation.ActivityResult(%s,TIntent.getImagesIntent(this,\"%s\",%s,%s)))"+END_LINE;
        return String.format(format,procedureName,className,resultCode,extraKey,imagePathsCount,true);
    }


    /**
     * 验证指定classname的Intent
     * @param procedureName
     * @param className
     * @return
     */
    public static String verify_classname(String procedureName,String className) {
        String format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.verify_classname(\"%s\")"+END_LINE;
        return String.format(format,procedureName,className);
    }


    /**
     * 验证指定classname的Intent 并返回了指定键值对
     * @param procedureName
     * @param className
     * @param key
     * @param value
     * @return
     */
    public static String verify_classname_extra(String procedureName,String className ,String key, String value) {
        String format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.verify_classname_extra(\"%s\",\"%s\",%s)"+END_LINE;
        return String.format(format,procedureName,className,key,value);
    }


    /**
     * 验证指定classname的Intent 并返回了bundle指定键值对
     * @param procedureName
     * @param className
     * @param key
     * @param value
     * @return
     */
    public static String verify_classname_extras(String procedureName,String className,String key, String value) {
        String format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.verify_classname_extras(\"%s\",\"%s\",%s)"+END_LINE;
        return String.format(format,procedureName,className,key,value);
    }


    /**
     * 拦截指定Action的Intent
     * @param procedureName
     * @param action
     * @return
     */
    public static String intercept_action(String procedureName,String action,String resultCode) {
        if(resultCode.isEmpty())resultCode="Activity.RESULT_OK";
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_action(%s,new Instrumentation.ActivityResult(%s,null))"+END_LINE;
        else
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_action(%s,Instrumentation.ActivityResult(%s,null))"+END_LINE;
        return String.format(format,procedureName,action,resultCode);
    }

    /**
     * 验证指定Action的Intent
     * @param procedureName
     * @param action
     * @return
     */
    public static String verify_action(String procedureName,String action) {
        String format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.verify_action(%s)"+END_LINE;
        return String.format(format,procedureName,action);
    }

    /**
     * 拦截指定Action和package的Intent
     * @param procedureName
     * @param action
     * @param packageName
     * @return
     */
    public static String verify_action_topackage(String procedureName,String action,String packageName) {
        String format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.verify_action_topackage(%s,\"%s\")"+END_LINE;
        return String.format(format,procedureName,action,packageName);
    }

    /**
     * 验证不包含指定的Intent
     * @param procedureName
     * @param className
     * @return
     */
    public static String verify_filter_classname(String procedureName,String className) {
        String format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.verify_filter_classname(\"%s\")"+END_LINE;
        return String.format(format,procedureName,className);
    }


    /**
     * 拦截指定包名
     * @param procedureName
     * @param packageName
     * @param resultCode
     * @return
     */
    public static String intercept_packagename(String procedureName,String packageName,String resultCode) {
        if(resultCode.length()==0)resultCode="Activity.RESULT_OK";
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+Config.WRAP
                    +TABS_LINE+"TIntent.intercept_packagename(\"%s\",new Instrumentation.ActivityResult(%s,null))"+END_LINE;
        else
          format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.intercept_packagename(\"%s\",Instrumentation.ActivityResult(%s,null))"+END_LINE;
        return String.format(format,procedureName,packageName,resultCode);
    }

    /**
     * 验证指定包名
     * @param procedureName
     * @param packageName
     * @return
     */
    public static String verify_packagename(String procedureName,String packageName) {
        String format=TABS_LINE+"//%s"+Config.WRAP
                +TABS_LINE+"TIntent.verify_packagename(\"%s\")"+END_LINE;
        return String.format(format,procedureName,packageName);
    }
}
