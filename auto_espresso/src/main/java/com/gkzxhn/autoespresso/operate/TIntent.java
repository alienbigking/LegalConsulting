package com.gkzxhn.autoespresso.operate;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.intent.matcher.BundleMatchers;
import android.support.test.espresso.intent.matcher.IntentMatchers;

import com.gkzxhn.autoespresso.util.TUtils;

import java.io.Serializable;
import java.util.List;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TIntent {
    /**
     * 获取一张图片
     * @param activity
     * @param extraKey
     * @param isBundle
     * @return
     */
    public static Intent getImageIntent(Activity activity,String extraKey,boolean isBundle){
        Intent intent=new Intent();
        List<String> radomImagePaths= TUtils.getRadomImagePaths(activity.getContentResolver(),1);
        if(isBundle){
            Bundle bundle=new Bundle();
            intent.putExtra(extraKey,  radomImagePaths.get(0));
            intent.putExtras(bundle);
        }else{
            intent.putExtra(extraKey,  radomImagePaths.get(0));
        }
        return intent;
    }

    /**
     * 返回多张图片
     * @param activity
     * @param extraKey
     * @param imagePathsCount
     * @param isBundle
     * @return
     */
    public static Intent getImagesIntent(Activity activity,String extraKey,int imagePathsCount,boolean isBundle){
        Intent intent=new Intent();
        List<String> radomImagePaths= TUtils.getRadomImagePaths(activity.getContentResolver(),imagePathsCount);
        if(isBundle){
            Bundle bundle=new Bundle();
            intent.putExtra(extraKey, (Serializable) radomImagePaths);
            intent.putExtras(bundle);
        }else{
            intent.putExtra(extraKey, (Serializable)  radomImagePaths);
        }
        return intent;
    }
    /**
     * 拦截Intent
     * 在click前执行
     * @param className 类全名，如com.test.MainActivity
     */
    public static void intercept_classname(String className, Instrumentation.ActivityResult result){
        if(result==null) intending(hasComponent(hasClassName(className)));
        else
        // result:模拟一个ActivityResult
        intending(hasComponent(hasClassName(className))).respondWith(result);
    }

    /**
     * 验证Intent
     * 在click后执行
     * @param className 类全名，如com.test.MainActivity
     */
    public static void verify_classname(String className){
        intended(hasComponent(hasClassName(className)));
    }

    /**
     * 验证Intent,在click后执行 如intent.putExtra("mykey","myvalue");
     * @param className 类全名，如com.test.MainActivity
     * @param key Intent中的可以
     * @param value 可为任意数据类型String /int/Float等
     */
    public static void verify_classname_extra(String className ,String key, Object value){
        intended(allOf(hasComponent(hasClassName(className)), IntentMatchers.hasExtra(equalTo(key), equalTo(value))));
    }

    /**
     * 验证Intent,在click后执行 extra key-value
     * @param className 类全名，如com.test.MainActivity
     */
    public static void verify_classname_extras(String className,String key, Object value){
        intended(allOf(hasComponent(hasClassName(className)),hasExtras(BundleMatchers.hasEntry(equalTo(key), equalTo(value)))));
    }
    /**
     * 拦截Intent,在click前执行
     */
    public static void intercept_action(String action, Instrumentation.ActivityResult result){
        if(result==null)intending(hasAction(action));
        else {
            // result:模拟一个ActivityResult
            //拦截action，并返回模拟后的result
            intending(hasAction(action)).respondWith(result);
        }
    }

    /**
     * 验证Intent,在click后执行
     */
    public static void verify_action(String action){
        intended(hasAction(action));
    }
    /**
     * 验证Intent,在click后执行
     */
    public static void verify_action_topackage(String action,String packageName){
        intended(allOf(hasAction(action), toPackage(packageName)));
    }
    /**
     * 验证Intent,不包含指定Classname
     */
    public static void verify_filter_classname(String className){
//        intended(hasAction(action))
        intended(hasComponent(not(hasClassName(className))));
    }
    /**
     * 拦截所有不同包名的Intent,即非本项目的Intent
     */
    public static void intercept_external_intents(Instrumentation.ActivityResult result){
        if(result==null)result= new Instrumentation.ActivityResult(Activity.RESULT_OK,null);
        //isInternal 测试包内的Intent  所有外部包not(isInternal())
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(result);
    }
    /**
     * 拦截所有相同包名的Intent,即本项目的Intent
     */
    public static void intercept_internal_intents(Instrumentation.ActivityResult result){
        if(result==null)result= new Instrumentation.ActivityResult(Activity.RESULT_OK,null);
        //isInternal 测试包内的Intent  所有外部包not(isInternal())
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(isInternal()).respondWith(result);

    }

    /**根据包名拦截Intent
     * @param packageName 包名 eg:联系人com.android.contacts 拨打电话 com.android.phone
     */
    public static void intercept_packagename(String packageName,Instrumentation.ActivityResult result){
        if(result==null)result= new Instrumentation.ActivityResult(Activity.RESULT_OK,null);
        intending(toPackage(packageName)).respondWith(result);
    }
    /**根据包名验证Intent是否被拦截
     * @param packageName 包名 eg:联系人com.android.contacts 拨打电话 com.android.phone
     */
    public static void  verify_packagename(String packageName){
        intended(toPackage(packageName));
    }
//    fun t(){
//    intent匹配器
//        intended(allOf(
//                hasAction(equalTo(Intent.ACTION_VIEW)),
//                hasCategories(hasItem(equalTo(Intent.CATEGORY_BROWSABLE))),
//                hasData(UriMatchers.hasHost(equalTo("www.google.com"))),
//                hasExtra(equalTo("key1"), equalTo("value1")),
//                toPackage("com.android.browser")))
//
//    }
    /*******************************私有方法********************************/
    /**
     * 拦截Intent,在click前执行
     */
    public static void  intercept_shortclassname(String shortClassName, Instrumentation.ActivityResult result){
        if(result==null)result= new Instrumentation.ActivityResult(Activity.RESULT_OK,null);
        // result:模拟一个ActivityResult
        intending(hasComponent(hasShortClassName(shortClassName))).respondWith(result);
    }
    /**
     * 验证Intent,在click后执行
     */
    public static void  verify_shortclassname(String shortClassName){
//        intended(hasAction(action))
        intended(hasComponent(hasShortClassName(shortClassName)));
    }
    /**
     * 验证Intent,在click后执行 如intent.putExtra("mykey","myvalue");
     * @param shortClassName 类全名，如com.test.MainActivity
     * @param key Intent中的可以
     * @param value 可为任意数据类型String /int/Float等
     */
    public static void  verify_shortclassname_extra(String shortClassName,String key,Object value){
        intended(allOf(hasComponent(hasShortClassName(shortClassName)),
                IntentMatchers.hasExtra(equalTo(key), equalTo(value))));
    }

    /**
     * 验证Intent,在click后执行 如 bundle.putString("myKey","myvalue");intent.putExtras(bundle);
     * @param shortClassName 类全名，如com.test.MainActivity
     * @param key Intent中的可以
     * @param value 可为任意数据类型String /int/Float等
     */
    public static void  verify_shortclassname_extras(String shortClassName,String key,Object value){
        intended(allOf(hasComponent(hasShortClassName(shortClassName)),
                hasExtras(BundleMatchers.hasEntry(equalTo(key), equalTo(value)))));
    }

    /**
     * 验证Intent,在click后执行
     */
    public static void verify_filter_shortclassname(String shortClassName){
//        intended(hasAction(action))
        intended(hasComponent(not(hasShortClassName(shortClassName))));
    }
}
