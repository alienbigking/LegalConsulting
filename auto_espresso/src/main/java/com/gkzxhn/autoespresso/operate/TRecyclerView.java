package com.gkzxhn.autoespresso.operate;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TRecyclerView {
    /**上拉Recyclerview
     * @param recyclerViewId
     */
    public static void pull_from_start(int recyclerViewId) {
        //先滑动到顶部
        scroll_to_position(recyclerViewId, 0);
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(swipeDown());
    }

    /**下拉Recyclerview
     * @param recyclerViewId
     */
    public static void pull_from_end(int recyclerViewId) {
        //先滑动到顶部
        scroll_to_end(recyclerViewId);
        //上拉
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(swipeUp());
    }

    /**滑动Recyclerview到底部
     * @param recyclerViewId
     */
    public static void scroll_to_end(int recyclerViewId) {
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "Scroll Recyclerview to end";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });

    }

    /**检查Recyclerview的项数量是否为指定数量
     * @param recyclerViewId
     */
    public static void check_item_counts(int recyclerViewId, final int expectedCount) {
        onView(allOf(withId(recyclerViewId), isDisplayed())).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                RecyclerView recyclerView = (RecyclerView) view;
                assertThat(recyclerView.getAdapter().getItemCount(), is(expectedCount));
            }
        });
    }

    /**检查Recyclerview至少有一项
     * @param recyclerViewId
     */
    public static void check_has_child(int recyclerViewId) {
        onView(allOf(withId(recyclerViewId), isDisplayed())).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                RecyclerView recyclerView = (RecyclerView) view;
                assertThat(recyclerView.getAdapter().getItemCount()>0, is(true));
            }
        });
    }


    /**(显示中的)通过id点击RecyclerView中指定位置position，
     * 解决场景：两相同的Fragment中的RecyclerView的id都是相同的，点击显示的那个，否则根据id会报多个View的错误
     * scrollToItemBelowFold
     * @param recyclerViewId
     * @param position
     */
    public static void  click_item(int recyclerViewId, int position) {
        scroll_to_position(recyclerViewId, position);
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(
                actionOnItemAtPosition(position, click()));
    }
    /**(显示中的)通过id点击RecyclerView中指定位置position，
     * 解决场景：两相同的Fragment中的RecyclerView的id都是相同的，点击显示的那个，否则根据id会报多个View的错误
     * 长按
     * @param recyclerViewId
     * @param position
     */
    public static void  longclick_item(int recyclerViewId, int position) {
        scroll_to_position(recyclerViewId, position);
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(
                actionOnItemAtPosition (position, longClick()));
    }
    /**
     * 点击RecyclerView的某项的child 控件
     */
    public static void  click_item_view_id(int recyclerViewId,int position,final int itemId){
        scroll_to_position(recyclerViewId, position);
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(actionOnItemAtPosition(position, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View  v = view.findViewById(itemId);
                v.performClick();
            }
        }));
    }

    /**
     /**
     * 数据源是map 点击指定KeyMap项
     */
    public static void  click_item_map(String key, String value) {
        onData(allOf(is(instanceOf(Map.class)), hasEntry(equalTo(key), is(value)))).perform(click());
    }

    /**
     * 滑动到指定位置项
     */
    public static void scroll_to_position(int recyclerViewId, int position) {
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(scrollToPosition(position));
    }

    /**
     * 滑动到指定文字项
     */
    public static void scroll_to_text(int recyclerViewId, String text) {
        onView(allOf(withId(recyclerViewId), isDisplayed())).perform(scrollTo(withText(text)));
    }

    /**
     * 点击指定文字项
     */
    public static void click_item_text(int recyclerViewId, String text) {
        onView(withId(recyclerViewId)).perform(actionOnItem(withText(text), click()));
    }
    /**
     * 验证指定项控件文字
     */
    public static void check_item_view_text(int recyclerViewId, int position,final int childId,final String text){
        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition(position, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(childId);
                assertThat(((TextView)v).getText().toString(),is(text));
            }
        }));
//        }))
    }
    /***************************私有方法**********************************/
    /**检查Recyclerview的项数量是否为指定数量
     * @param recyclerViewId
     */
    public static int getChildCount(int recyclerViewId) {
        count=0;
        onView(allOf(withId(recyclerViewId), isDisplayed())).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                RecyclerView recyclerView = (RecyclerView) view;
                count=recyclerView.getAdapter().getItemCount();
            }
        });

        return count;
    }
    private static int count=0;
}
