package com.gkzxhn.autoespresso.operate;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.ViewPagerActions.clickBetweenTwoTitles;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollLeft;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollRight;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollToFirst;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollToLast;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollToPage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TViewPager {
    public static void click_between_two_titles(int id,String title1, String title2){
        onView(withId(id)).perform(clickBetweenTwoTitles(title1,title2));
    }
    /**
     * 向左滑动
     */
    public static void scroll_left(int id,boolean smoothScroll){
        onView(withId(id)).perform(scrollLeft(smoothScroll));
    }

    /**
     * 向右滑动
     */
    public static void scroll_right(int id,boolean smoothScroll){
        onView(withId(id)).perform(scrollRight(smoothScroll));
    }

    /**
     * 滑动到第一项
     */
    public static void scroll_to_first(int id,boolean smoothScroll){
        onView(withId(id)).perform(scrollToFirst(smoothScroll));
    }

    /**
     * 滑动到最后一项
     */
    public static void scroll_to_last(int id,boolean smoothScroll){
        onView(withId(id)).perform(scrollToLast(smoothScroll));
    }

    /**
     * 滑动到指定项
     */
    public static void scroll_to_page(int id,int page, boolean smoothScroll){
        onView(withId(id)).perform(scrollToPage(page,smoothScroll));
    }
}
