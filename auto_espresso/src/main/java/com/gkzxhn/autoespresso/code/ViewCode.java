package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.util.TUtils;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class ViewCode extends BaseCode {
    /************************* input text*********************************************/
    /**
     * 清空并输入文本
     *
     * @param procedureName
     * @param id
     * @param text
     * @return
     */
    public static String input_text(String procedureName, String id, String text) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.input_text(%s,\"%s\")" + END_LINE;
        return String.format(format, procedureName, id, text);
    }

    /**
     * 追加输入文本
     *
     * @param procedureName
     * @param id
     * @param text
     * @return
     */
    public static String input_append_text(String procedureName, String id, String text) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.input_append_text(%s,\"%s\")" + END_LINE;
        return String.format(format, procedureName, id, text);
    }
    /************************* click*********************************************/
    /**
     * 点击指定id控件
     *
     * @param procedureName
     * @param id
     * @return
     */
    public static String click_id(String procedureName, String id) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.click_id(%s)" + END_LINE;
        return String.format(format, procedureName, id);
    }

    /**
     * 点击指定文字控件
     *
     * @param procedureName
     * @param text
     * @return
     */
    public static String click_text(String procedureName, String text) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.click_text(\"%s\")" + END_LINE;
        return String.format(format, procedureName, text);
    }

    /**
     * 点击指定背景控件
     *
     * @param procedureName
     * @param drawableId
     * @return
     */
    public static String click_background(String procedureName, String drawableId) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.click_background(%s)" + END_LINE;
        return String.format(format, procedureName, drawableId);
    }
    /************************* check text*********************************************/
    /**
     * 验证控件提示文字
     *
     * @param procedureName
     * @param id
     * @param text
     * @return
     */
    public static String check_id_hint_text(String procedureName, String id, String text) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_hint_text(%s,\"%s\")" + END_LINE;
        return String.format(format, procedureName, id, text);
    }

    /**
     * 验证控件文字是否包含指定文字
     *
     * @param procedureName
     * @param id
     * @param text
     * @return
     */
    public static String check_id_contains_text(String procedureName, String id, String text) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_contains_text(%s,\"%s\")" + END_LINE;
        return String.format(format, procedureName, id, text);
    }

    /**
     * 验证控件文本
     *
     * @param procedureName
     * @param id
     * @param text
     * @return
     */
    public static String check_id_text(String procedureName, String id, String text) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_text(%s,\"%s\")" + END_LINE;
        return String.format(format, procedureName, id, text);
    }

    /**
     * 验证控件是否可见
     *
     * @param procedureName
     * @param id
     * @param isDisplayed
     * @return
     */
    public static String check_id_displayed(String procedureName, String id, String isDisplayed) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_displayed(%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.valueToBoolean(isDisplayed));
    }

    /**
     * 验证指定文本是否显示
     *
     * @param procedureName
     * @param text
     * @param isDisplayed
     * @return
     */
    public static String check_text_displayed(String procedureName, String text, String isDisplayed) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_text_displayed(\"%s\",%s)" + END_LINE;
        return String.format(format, procedureName, text, TUtils.valueToBoolean(isDisplayed));
    }

    /**
     * 验证包含文字是否显示
     *
     * @param procedureName
     * @param text
     * @param isDisplayed
     * @return
     */
    public static String check_contains_text_displayed(String procedureName, String text, String isDisplayed) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_contains_text_displayed(\"%s\",%s)" + END_LINE;
        return String.format(format, procedureName, text, TUtils.valueToBoolean(isDisplayed));
    }

    /**
     * 验证指定文本不存在
     *
     * @param procedureName
     * @param text
     * @return
     */
    public static String check_text_not_exist(String procedureName, String text) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_text_not_exist(\"%s\")" + END_LINE;
        return String.format(format, procedureName, text);
    }

    /**
     * 验证控件不存在
     *
     * @param procedureName
     * @param id
     * @return
     */
    public static String check_id_not_exist(String procedureName, String id) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_not_exist(%s)" + END_LINE;
        return String.format(format, procedureName, id);
    }
    /************************* check view state*********************************************/

    /**
     * 验证控件是否被选中
     *
     * @param procedureName
     * @param id
     * @param isChecked
     * @return
     */
    public static String check_id_checked(String procedureName, String id, String isChecked) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_checked(%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.valueToBoolean(isChecked));
    }

    /**
     * 验证控件是否可用
     *
     * @param procedureName
     * @param id
     * @param isEnabled
     * @return
     */
    public static String check_id_enabled(String procedureName, String id, String isEnabled) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_enabled(%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.valueToBoolean(isEnabled));
    }

    /**
     * 验证控件是否可点击
     *
     * @param procedureName
     * @param id
     * @param isClickable
     * @return
     */
    public static String check_id_clickable(String procedureName, String id, String isClickable) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_clickable(%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.valueToBoolean(isClickable));
    }

    /**
     * 验证控件是否可长按
     *
     * @param procedureName
     * @param id
     * @param isLongClickable
     * @return
     */
    public static String check_id_longclickable(String procedureName, String id, String isLongClickable) {
        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_id_longclickable(%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.valueToBoolean(isLongClickable));
    }

    /**
     * 验证EditTextView控件输入的类型
     *
     * @param procedureName
     * @param id
     * @param inputType     eg: number  ->android:inputType="number"
     * @return
     */
    public static String check_input_type(String procedureName, String id, String inputType) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.check_input_type(%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.getInputType(inputType));
    }

    /**
     * 下滑
     *
     * @param procedureName
     * @param id
     * @return
     */
    public static String swipe_down(String procedureName, String id) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.swipe_down(%s)" + END_LINE;
        return String.format(format, procedureName, id);
    }

    /**
     * 上滑
     *
     * @param procedureName
     * @param id
     * @return
     */
    public static String swipe_up(String procedureName, String id) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.swipe_up(%s)" + END_LINE;
        return String.format(format, procedureName, id);
    }

    /**
     * 左滑
     *
     * @param procedureName
     * @param id
     * @return
     */
    public static String swipe_left(String procedureName, String id) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.swipe_left(%s)" + END_LINE;
        return String.format(format, procedureName, id);
    }

    /**
     * 右滑
     *
     * @param procedureName
     * @param id
     * @return
     */
    public static String swipe_right(String procedureName, String id) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.swipe_right(%s)" + END_LINE;
        return String.format(format, procedureName, id);
    }
    /************************************Spinner**************************************************/
    /**
     * 检查Spinner是否选中了指定文本
     *
     * @param procedureName
     * @param id
     * @param text
     * @return
     */
    public static String spinner_check_text(String procedureName, String id, String text) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.spinner_check_text(%s,\"%s\")" + END_LINE;
        return String.format(format, procedureName, id, text);
    }

    /**
     * Spinner选择了指定文本
     *
     * @param procedureName
     * @param id
     * @param text
     * @return
     */
    public static String spinner_select_text(String procedureName, String id, String text) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.spinner_select_text(%s,\"%s\")" + END_LINE;
        return String.format(format, procedureName, id, text);
    }
    /************************************Picker**************************************************/
    /**
     * 选择picker日期
     *
     * @param procedureName
     * @param id
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    public static String picker_set_date(String procedureName, String id, String year, String monthOfYear, String dayOfMonth) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.picker_set_date(%s,%s,%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.valueToInt(year),
                TUtils.valueToInt(monthOfYear), TUtils.valueToInt(dayOfMonth));
    }

    /**
     * 选中picker时间
     *
     * @param procedureName
     * @param id
     * @param hours
     * @param minutes
     * @return
     */
    public static String picker_set_time(String procedureName, String id, String hours, String minutes) {

        String format = TABS_LINE + "//%s" + END_LINE
                + TABS_LINE + "TView.picker_set_time(%s,%s,%s)" + END_LINE;
        return String.format(format, procedureName, id, TUtils.valueToInt(hours),
                TUtils.valueToInt(minutes));
    }
}
