package com.gkzxhn.autoespresso.operate;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TListView {
    /**
     * 指定键值对已经显示在列表中
     * @param key
     * @param value
     */
    public static void check_item_displayed(String key,String value){
        onData(hasEntry(equalTo(key), is(value))).check(matches(isCompletelyDisplayed()));
    }

    /**
     * 点击指定项
     * @param listViewId
     * @param position
     */
    public static void click_item(int listViewId ,int position){
        onData(allOf())
                .inAdapterView(withId(listViewId)) // listview的id
                .atPosition(position)   // 所在位置
                .perform(scrollTo(),click());
    }

    /**
     * 点击指定项并其开始文字为指定文字
     * @param text
     * @param listViewId
     * @param position
     */
    public static void click_item_start_text(String text ,int listViewId,int position){
        onData(hasToString(startsWith(text)))
                .inAdapterView(withId(listViewId)).atPosition(position)
                .perform(click());

    }

    /**
     * 点击指定项控件
     * @param listViewId
     * @param position
     * @param childId
     */
    public static void click_item_view(int listViewId,int position,int childId){
        onData(allOf())
                .inAdapterView(withId(listViewId)) // listview的id
                .atPosition(position)   // 所在位置
                .onChildView(withId(childId))  // item中子控件id
                .perform(click());
    }

}
