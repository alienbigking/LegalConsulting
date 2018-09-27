package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.util.TUtils;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class RecyclerViewCode extends BaseCode{
    /**
     * 上拉
     * @param procedureName
     * @param recyclerViewId
     * @return
     */
    public static String pull_from_start(String procedureName,String recyclerViewId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.pull_from_start(%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId);
    }

    /**
     * 下拉
     * @param procedureName
     * @param recyclerViewId
     * @return
     */
    public static String pull_from_end(String procedureName,String recyclerViewId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.pull_from_end(%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId);
    }

    /**滑动到底部
     * @param procedureName
     * @param recyclerViewId
     * @return
     */
    public static String scroll_to_end(String procedureName,String recyclerViewId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.scroll_to_end(%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId);
    }

    /**
     * 验证列表的项数量为指定数量
     * @param procedureName
     * @param recyclerViewId
     * @param expectedCount
     * @return
     */
    public static String check_item_counts(String procedureName,String recyclerViewId,String expectedCount) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.check_item_counts(%s,%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId,expectedCount);
    }

    /**
     * 验证列表至少有一项
     * @param procedureName
     * @param recyclerViewId
     * @return
     */
    public static String check_has_child(String procedureName,String recyclerViewId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.check_has_child(%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId);
    }

    /**
     * (显示中的)点击列表中指定位置项
     * 解决场景：两相同的Fragment中的RecyclerView的id都是相同的，点击显示的那个，否则根据id会报多个View的错误
     * @param procedureName
     * @param recyclerViewId
     * @param position
     * @return
     */
    public static String click_item(String procedureName,String recyclerViewId,String position) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.click_item(%s,%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId, TUtils.valueToInt(position));
    }

    /**
     * (显示中的)长按列表中指定位置项
     * 解决场景：两相同的Fragment中的RecyclerView的id都是相同的，点击显示的那个，否则根据id会报多个View的错误
     * @param procedureName
     * @param recyclerViewId
     * @param position
     * @return
     */
    public static String longclick_item(String procedureName,String recyclerViewId,String position) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.longclick_item(%s,%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId, TUtils.valueToInt(position));
    }

    /**
     * 点击指定项控件
     * @param procedureName
     * @param recyclerViewId
     * @param position
     * @param itemId
     * @return
     */
    public static String click_item_view_id(String procedureName,String recyclerViewId,String position,String itemId) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.click_item_view_id(%s,%s,%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId, TUtils.valueToInt(position),itemId);
    }

    /**
     * 数据源是map 点击指定KeyMap项
     * @param procedureName
     * @param key
     * @param value
     * @return
     */
    public static String click_item_map(String procedureName,String key,String value) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.click_item_map(\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,key, value);
    }

    /**
     * 滑动到指定位置项
     * @param procedureName
     * @param recyclerViewId
     * @param position
     * @return
     */
    public static String scroll_to_position(String procedureName,String recyclerViewId,String position) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.scroll_to_position(%s,%s)"+END_LINE;
        return String.format(format,procedureName,recyclerViewId, TUtils.valueToInt(position));
    }

    /**
     * 滑动到指定文字项
     * @param procedureName
     * @param recyclerViewId
     * @param text
     * @return
     */
    public static String scroll_to_text(String procedureName,String recyclerViewId,String text) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.scroll_to_text(%s,\"%s\")"+END_LINE;
        return String.format(format,procedureName,recyclerViewId,text);
    }

    /**
     * 点击指定文字项
     * @param procedureName
     * @param recyclerViewId
     * @param text
     * @return
     */
    public static String click_item_text(String procedureName,String recyclerViewId,String text) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.click_item_text(%s,\"%s\")"+END_LINE;
        return String.format(format,procedureName,recyclerViewId,text);
    }

    /**
     *  验证指定项控件文字
     * @param procedureName
     * @param recyclerViewId
     * @param position
     * @param childId
     * @param text
     * @return
     */
    public static String check_item_view_text(String procedureName,String recyclerViewId,
                                              String position,String childId,String text) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TRecyclerView.check_item_view_text(%s,%s,%s,\"%s\")"+END_LINE;
        return String.format(format,procedureName,recyclerViewId, TUtils.valueToInt(position),childId,text);
    }
}
