package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.config.Config;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class SystemCode extends BaseCode {
    /**
     * 点击系统返回键
     * @param procedureName
     * @return
     */
    public static String press_back(String procedureName) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TSystem.press_back()"+END_LINE;
        return String.format(format,procedureName);
    }

    /**
     * 点击系统菜单键
     * @param procedureName
     * @return
     */
    public static String press_menu_key(String procedureName) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TSystem.press_menu_key()"+END_LINE;
        return String.format(format,procedureName);
    }

    /**
     * 点击软键盘action键
     * @param procedureName
     * @return
     */
    public static String press_ime_action_key(String procedureName) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TSystem.press_ime_action_key()"+END_LINE;
        return String.format(format,procedureName);
    }

    /**
     * 点击指定键
     * @param procedureName
     * @param key eg:Keyevent.KEYCODE_BACK
     * @return
     */
    public static String press_key(String procedureName,String key) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TSystem.press_key(%s)"+END_LINE;
        return String.format(format,procedureName,key);
    }

    /**
     * 点击菜单项
     * @param procedureName
     * @param id
     * @param menuItemId
     * @return
     */
    public static String navigate_to_menu_id(String procedureName,String id,String menuItemId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TSystem.navigate_to_menu_id(%s,%s)"+END_LINE;
        return String.format(format,procedureName,id,menuItemId);
    }

    /**
     * 验证Activity是否Finished
     * @param procedureName
     * @return
     */
    public static String check_finished(String procedureName) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.check_finished(mActivityTestRule.getActivity())"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.check_finished(this)"+END_LINE;
        return String.format(format,procedureName);
    }

    /**
     * 验证Activity是否Destroyed
     * @param procedureName
     * @return
     */
    public static String check_destroyed(String procedureName) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.check_destroyed(mActivityTestRule.getActivity())"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.check_destroyed(this)"+END_LINE;
        return String.format(format,procedureName);
    }

    /**
     * 验证Activity是否横竖屏切换
     * @param procedureName
     * @return
     */
    public static String check_changing_configurations(String procedureName) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.check_changing_configurations(mActivityTestRule.getActivity())"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.check_changing_configurations(this)"+END_LINE;
        return String.format(format,procedureName);
    }
    /**
     * 延迟
     * @param procedureName
     * @return
     */
    public static String sleep(String procedureName,long time) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TSystem.sleep(%s)"+END_LINE;
        return String.format(format,procedureName,time);
    }
    /**
     * 截屏
     */
    public static String take_screenshot(String procedureName) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TScreenshot.save_bitmap(TScreenshot.take_screenshot(mActivityTestRule.getActivity()),%s)"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TScreenshot.save_bitmap(TScreenshot.take_screenshot(this),%s)"+END_LINE;
        return String.format(format,procedureName, Config.TAKE_SCREENSHOT);
    }

    /**
     * 关闭wifi
     */
    public static String disableWifi(String procedureName) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.disableWifi(mActivityTestRule.getActivity())"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.disableWifi(this)"+END_LINE;
        return String.format(format,procedureName);
    }
    /**
     * 打开wifi
     */
    public static String enableWifi(String procedureName) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.enableWifi(mActivityTestRule.getActivity())"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TSystem.enableWifi(this)"+END_LINE;
        return String.format(format,procedureName);
    }
}
