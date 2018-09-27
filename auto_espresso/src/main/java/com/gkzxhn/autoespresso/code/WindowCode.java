package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.config.Config;
import com.gkzxhn.autoespresso.util.TUtils;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class WindowCode extends BaseCode {
    /**
     * 验证Dialog中是否有指定文字
     * dialog显示遮住后，底部的页面控件不能检测到
     * @param procedureName
     * @param text
     * @return
     */
    public static String dialog_check_text(String procedureName,String text) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWindow.dialog_check_text(\"%s\")"+END_LINE;
        return String.format(format,procedureName,text);
    }

    /**
     * 验证Dialog中是否有包含文字
     * dialog显示遮住后，底部的页面控件不能检测到
     * @param procedureName
     * @param text
     * @return
     */
    public static String dialog_check_contains_text(String procedureName,String text) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWindow.dialog_check_contains_text(\"%s\")"+END_LINE;
        return String.format(format,procedureName,text);
    }

    /**
     * 点击Popwindow中的控件
     * @param procedureName
     * @param childId
     * @return
     */
    public static String popwindow_click_item(String procedureName,String childId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWindow.popwindow_click_item(%s)"+END_LINE;
        return String.format(format,procedureName,childId);
    }

    /**
     * 点击Alertwindow中的控件
     * @param procedureName
     * @param text
     * @return
     */
    public static String alertwindow_click_text(String procedureName,String text) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWindow.alertwindow_click_text(\"%s\")"+END_LINE;
        return String.format(format,procedureName,text);
    }

    /**
     * 验证Toast
     * @param procedureName
     * @param text
     * @return
     */
    public static String toast_check_text(String procedureName,String text) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.toast_check_text(mActivityTestRule.getActivity(),\"%s\")"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.toast_check_text(this,\"%s\")"+END_LINE;
        return String.format(format,procedureName,text);
    }
    /**
     * 验证Toast  自定义Toast
     * @param procedureName
     * @param text
     * @return
     */
    public static String toast_check_id_text(String procedureName,String id,String text) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.toast_check_id_text(mActivityTestRule.getActivity(),%s,\"%s\")"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.toast_check_id_text(this,%s,\"%s\")"+END_LINE;
        return String.format(format,procedureName,id,text);
    }
    /**验证Toast 不是指定文字
     * @param procedureName
     * @param text
     * @return
     */
    public static String toast_check_filter_text(String procedureName,String text) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.toast_check_filter_text(mActivityTestRule.getActivity(),\"%s\")"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.toast_check_filter_text(this,\"%s\")"+END_LINE;
        return String.format(format,procedureName,text);
    }

    /**
     * 根据文本，多窗口切换
     * @param procedureName
     * @param text
     * @return
     */
    public static String change_window_by_text(String procedureName,String text) {
        String format="";
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.change_window_by_text(mActivityTestRule.getActivity(),\"%s\")"+END_LINE;
        else
            format=TABS_LINE+"//%s"+END_LINE
                    +TABS_LINE+"TWindow.change_window_by_text(this,\"%s\")"+END_LINE;
        return String.format(format,procedureName,text);
    }
}
