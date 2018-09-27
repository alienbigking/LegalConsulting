package com.gkzxhn.autoespresso.operate;

import android.app.Activity;
import android.support.test.espresso.matcher.RootMatchers;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.isSystemAlertWindow;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TWindow {
    /**验证Dialog  dialog显示遮住后，底部的页面控件不能检测到
     * @param text
     */
    public static void dialog_check_text(String text)   {
           onView(withText(text)).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()));
        //显示：isDisplayed() 不显示：doesNotExist()
    }
    /**验证Dialog  dialog显示遮住后，底部的页面控件不能检测到
     * @param text
     */
    public static void dialog_check_contains_text(String text)   {
          onView(withText(containsString(text))).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()));
        //显示：isDisplayed() 不显示：doesNotExist()
    }

    /**
     * 点击Popwindow中的child 通过childId
     */
    public static void popwindow_click_item(int childId){
        onView(withId(childId)).inRoot(isPlatformPopup()).perform(click());
    }

    public static void alertwindow_click_text(String text){
        onView(withText(text)).inRoot(isSystemAlertWindow()).perform(click());
    }

    /**验证Toast
     * @param activity
     * @param toastText
     */
    public static void toast_check_text(Activity activity, String toastText)  {
          onView(withText(toastText)).inRoot(withDecorView(not(activity.getWindow().getDecorView()))).check(matches(isDisplayed()));
        //显示：isDisplayed() 不显示：doesNotExist()
    }

    /**验证自定义Toast 根据id
     * @param activity
     * @param toastText
     */
    public static void toast_check_id_text(Activity activity, int id, String toastText)  {
        onView(withId(id)).inRoot(withDecorView(not(activity.getWindow().getDecorView()))).
                check(matches(allOf(isDisplayed(),withText(toastText))));
    }
    /**验证Toast 不是指定文字
     * @param activity
     * @param toastText
     */
    public static void toast_check_filter_text(Activity activity, String toastText)  {
        //因为Toast是AppCompatTextView 根据它判断，DecorView rootView／Toast
          onView(allOf(is(instanceOf(TextView.class)), not(withText(toastText)))).inRoot(withDecorView(not(activity.getWindow().getDecorView()))).
                  check(matches(isDisplayed()));
        //显示：isDisplayed() 不显示：doesNotExist()
    }

    /**根据文本，多窗口切换
     * @param text
     */
    public static void change_window_by_text(Activity activity,String text) {
        //根据文字找到指定窗口并切换
        onView(withText(text))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .perform(click());
    }
}
