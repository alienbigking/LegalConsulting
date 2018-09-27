package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.util.TUtils;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class ListViewCode extends BaseCode {
    /**
     * 验证指定键值对已经显示在列表中
     * @param procedureName
     * @param key
     * @param value
     * @return
     */
    public static String check_item_displayed(String procedureName,String key,String value) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TListView.check_item_displayed(\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,key,value);
    }

    /**
     * 点击指定项
     * @param procedureName
     * @param listViewId
     * @param position
     * @return
     */
    public static String click_item(String procedureName,String listViewId ,String position) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TListView.click_item(%s,%s)"+END_LINE;
        return String.format(format,procedureName,listViewId, TUtils.valueToInt(position));
    }

    /**
     * 点击指定项并其开始文字为指定文字
     * @param procedureName
     * @param listViewId
     * @param position
     * @return
     */
    public static String click_item_start_text(String procedureName,String text,String listViewId ,String position) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TListView.click_item_start_text(\"%s\",%s,%s)"+END_LINE;
        return String.format(format,procedureName,text,listViewId, TUtils.valueToInt(position));
    }

    /**
     * 点击指定项控件
     * @param procedureName
     * @param listViewId
     * @param position
     * @param childId
     * @return
     */
    public static String click_item_view(String procedureName,String listViewId ,String position,String childId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TListView.click_item_view(%s,%s,%s)"+END_LINE;
        return String.format(format,procedureName,listViewId, TUtils.valueToInt(position),childId);
    }

}
