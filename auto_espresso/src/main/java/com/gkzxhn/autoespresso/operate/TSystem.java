package com.gkzxhn.autoespresso.operate;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.pressMenuKey;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TSystem {
    /**
     * 点击系统返回键
     */
    public static void press_back(){
        onView(isRoot()).perform(pressBack());
    }
    /**
     * 点击系统菜单键
     */
    public static void press_menu_key(){
        onView(isRoot()).perform(pressMenuKey());
    }
    /**
     * 点击软键盘action键
     */
    public static void press_ime_action_key(){
        onView(isRoot()).perform(pressImeActionButton());
    }
    /**
     * 点击软键盘 指定键
     * @param keyCode eg:Keyevent.KEYCODE_BACK
     */
    public static void press_key(int keyCode){
        onView(isRoot()).perform(pressKey(keyCode));
    }
    /**
     *点击菜单项
     * @param id
     * @param menuItemId 菜单id
     */
    public static void navigate_to_menu_id(int id,int menuItemId){
        onView(withId(id)).perform(navigateTo(menuItemId));
    }

    /**
     * 验证Activity已经关闭
     */
    public static void check_finished(Activity activity){
        assertThat(activity.isFinishing(), is(true));
    }
    /**
     * 验证Activity是否Destroyed
     */
    public static void check_destroyed(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            assertThat(activity.isDestroyed(),  is(true));
        }
    }
    /**
     * 验证Activity是否横竖屏切换
     */
    public static void check_changing_configurations(Activity activity){
        assertThat(activity.isChangingConfigurations(),is(true));
    }
    /**
     * 延迟
     */
    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 打开wifi
     */
    public static void enableWifi(Activity activity){
        TPermissions.get_permission_shell(Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.WAKE_LOCK);
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }
    /**
     * 关闭wifi
     */
    public static void disableWifi(Activity activity){
        TPermissions.get_permission_shell(Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.WAKE_LOCK);
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }


}
