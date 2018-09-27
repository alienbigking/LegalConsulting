package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.util.TUtils;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class ViewPagerCode extends BaseCode {


    /**
     *  向左滑动
     * @param procedureName
     * @param id
     * @param smoothScroll
     * @return
     */
    public static String scroll_left(String procedureName,String id,String smoothScroll) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TViewPager.scroll_left(%s,%s)"+END_LINE;
        return String.format(format,procedureName,id, TUtils.valueToBoolean(smoothScroll));
    }

    /**
     *  向右滑动
     * @param procedureName
     * @param id
     * @param smoothScroll
     * @return
     */
    public static String scroll_right(String procedureName,String id,String smoothScroll) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TViewPager.scroll_right(%s,%s)"+END_LINE;
        return String.format(format,procedureName,id, TUtils.valueToBoolean(smoothScroll));
    }

    /**
     *  滑动到第一项
     * @param procedureName
     * @param id
     * @param smoothScroll
     * @return
     */

    public static String scroll_to_first(String procedureName,String id,String smoothScroll) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TViewPager.scroll_to_first(%s,%s)"+END_LINE;
        return String.format(format,procedureName,id, TUtils.valueToBoolean(smoothScroll));
    }

    /**
     * 滑动到最后一项
     * @param procedureName
     * @param id
     * @param smoothScroll
     * @return
     */
    public static String scroll_to_last(String procedureName,String id,String smoothScroll) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TViewPager.scroll_to_last(%s,%s)"+END_LINE;
        return String.format(format,procedureName,id, TUtils.valueToBoolean(smoothScroll));
    }

    /**
     * 滑动到指定项
     * @param procedureName
     * @param id
     * @param page
     * @param smoothScroll
     * @return
     */
    public static String scroll_to_page(String procedureName,String id,String page,String smoothScroll) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TViewPager.scroll_to_page(%s,%s,%s)"+END_LINE;
        return String.format(format,procedureName,id,TUtils.valueToInt(page),TUtils.valueToBoolean(smoothScroll));
    }

    /**
     * 点击两项
     * @param procedureName
     * @param id
     * @param title1
     * @param title2
     * @return
     */
    public static String click_between_two_titles(String procedureName,String id,String title1, String title2) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TViewPager.click_between_two_titles(%s,\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,id,title1,title2);
    }
}
