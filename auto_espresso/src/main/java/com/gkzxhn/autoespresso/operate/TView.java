package com.gkzxhn.autoespresso.operate;

import android.app.Activity;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Raleigh.Luo on 18/3/13.
 * onView(ViewMatchers) 视图匹配
 * perform(ViewActions) 视图操作
 * check(ViewAssetions) 视图验证
 */

public class TView {
    /************************* input text*********************************************/

    /**清空并输入文本
     * replaceText可以输入中文 typeText不能输入中文
     * @param id
     * @param text
     */
    public static void input_text(int id, String text) {
        onView(withId(id)).perform(clearText(), replaceText(text), closeSoftKeyboard());
    }

    /**追加输入文本
     * replaceText可以输入中文 typeText不能输入中文
     * @param id
     * @param text
     */
    public static void input_append_text(int id, String text) {
        onView(withId(id)).perform(replaceText(text), closeSoftKeyboard());
    }
    /************************* click*********************************************/

    /**点击id
     * @param id
     */
    public static void click_id(int id) {
        onView(withId(id)).perform(click());
    }

    /**点击text
     * @param text
     */
    public static void click_text(String text) {
        onView(withText(text)).perform(click());
    }

    /**点击指定背景资源id
     * @param drawableId
     */
    public static void click_background(int drawableId) {
        onView(hasBackground(drawableId)).perform(click());
    }

    /************************* check text*********************************************/

    /**验证控件提示文字
     * @param id
     */
    public static void check_id_hint_text(int id,String text) {
        //可同时调用多个action
        onView( withId(id)).check(matches(withHint(text)));
    }
    /**验证控件文字是否包含指定文字
     * @param id
     */
    public static void check_id_contains_text(int id,String containsText )  {
        //可同时调用多个action
        onView(withId(id)).check(matches(withText(containsString(containsText))));
    }


    /**检查控件文本
     * @param id
     * @param text
     */
    public static void check_id_text(int id, String text) {
        onView(withId(id)).check(matches(withText(text)));
    }

    /**验证控件是否可见
     * @param id
     */
    public static void check_id_displayed(int id,boolean isDisplayed) {
        if(isDisplayed){
            onView(withId(id)).check(matches(isDisplayed()));
        }else{
            //可同时调用多个action
            onView(withId(id)).check(matches(not(isDisplayed())));
        }

    }

    /**检查指定文本是否显示
     * @param text
     */
    public static void check_text_displayed(String text,boolean isDisplayed) {
        if(isDisplayed){
            onView(withText(text)).check(matches(isDisplayed()));
        }else{
            onView(withText(text)).check(matches(not(isDisplayed())));
        }
    }

    /**检查包含文字是否显示
     * @param text
     */
    public static void check_contains_text_displayed(String text,boolean isDisplayed) {
        if(isDisplayed){
            onView(withText(containsString(text))).check(matches(isDisplayed()));
        }else{
            onView(withText(containsString(text))).check(matches(not(isDisplayed())));
        }
    }
    /**检查指定文本不存在
     * @param text
     * @param text
     */
    public static void check_text_not_exist(String text) {
        onView(withText(text)).check(doesNotExist());
    }
    /**检查控件不存在
     * @param id
     */
    public static void check_id_not_exist(int id) {
        onView(withId(id)).check(doesNotExist());
    }

    /************************* check view state*********************************************/
    /**
     * 验证控件是否被选中
     */
    public static void check_id_checked(int id, boolean isChecked){
        assertThat(is_checked(id), is(isChecked));
    }
    /**
     * 验证控件是否可用
     */
    public static void check_id_enabled(int id, boolean isEnabled){
        assertThat(is_enable(id), is(isEnabled));
    }
    /**
     * 验证控件是否可点击
     */
    public static void check_id_clickable(int id, boolean isClickable){
        assertThat(is_clickable(id), is(isClickable));
    }
    /**
     * 验证控件是否可长按
     */
    public static void check_id_longclickable(int id, boolean islongclickable){
        assertThat(is_longclickable(id), is(islongclickable));
    }
    /************************* other*********************************************/



    /**
     * 验证EditTextView控件输入的类型
     */
    public static void check_input_type(int id,int inputType){
        onView(withId(id)).check(matches(withInputType(inputType)));
    }

    /**
     *下滑
     */
    public static void swipe_down(int id){
        onView(withId(id)).perform(swipeDown());
    }
    /**
     *上滑
     */
    public static void swipe_up(int id){
        onView(withId(id)).perform(swipeUp());
    }
    /**
     *左滑
     */
    public static void swipe_left(int id){
        onView(withId(id)).perform(swipeLeft());
    }
    /**
     *右滑
     */
    public static void swipe_right(int id){
        onView(withId(id)).perform(swipeRight());
    }
    /************************************Spinner**************************************************/

    /**检查Spinner是否选中了指定文本
     * @param id
     * @param text
     */
    public static void spinner_check_text(int id, String text) {
        onView(withId(id)).check(matches(withSpinnerText(containsString(text))));
    }
    /**Spinner选择了指定文本
     * @param id
     * @param text
     */
    public static void spinner_select_text(int id,String text) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(text))).perform(click());
    }
    /************************************Picker**************************************************/
    /**
     * 选择日期
     * @param id 控件id
     * @param year 年
     * @param monthOfYear 月
     * @param dayOfMonth 日
     */
    public static void picker_set_date(int id,int year,int  monthOfYear,  int dayOfMonth){
        onView(withId(id)).perform(setDate(year,monthOfYear,dayOfMonth));
    }

    /**
     * 选择时分
     * @param id 控件id
     * @param hours 时
     * @param minutes 分
     */
    public static void picker_set_time(int id,int hours, int minutes){
        onView(withId(id)).perform(setTime(hours,minutes));
    }
    /************************************不常用方法**************************************************/

    /**点击控件id名
     *@param resourceName 如main_layout_tv_name  就是去掉R.id
     */
    public static void click_resourceName(String resourceName){
        onView(withResourceName(resourceName)).perform(click());
    }
    /************************************私有方法**************************************************/
    /**
     * view是否Checked
     */
    public static boolean is_checked(int id){
        final boolean[] isChecked=new boolean[1];
        onView(withId(id)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                //识别所操作的对象类型
                return isAssignableFrom(CompoundButton.class);
            }

            @Override
            public String getDescription() {
                return "get CompoundButton is checked";
            }

            @Override
            public void perform(UiController uiController, View view) {
                isChecked[0]=((CompoundButton)view).isChecked();
            }
        });
        return  isChecked[0];
    }
    public static boolean is_enable(int id){
        final boolean[] isEnable=new boolean[1];
        onView(withId(id)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                //识别所操作的对象类型
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "get view is enable";
            }

            @Override
            public void perform(UiController uiController, View view) {
                isEnable[0]=view.isEnabled();
            }
        });
        return  isEnable[0];
    }
    public static boolean is_clickable(int id){
        final boolean[] isClickable=new boolean[1];
        onView(withId(id)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                //识别所操作的对象类型
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "get view  is clickable";
            }

            @Override
            public void perform(UiController uiController, View view) {
                isClickable[0]=view.isClickable();
            }
        });
        return  isClickable[0];
    }
    public static boolean is_longclickable(int id){
        final boolean[] isLongClickable=new boolean[1];
        onView(withId(id)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                //识别所操作的对象类型
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "get view  is clickable";
            }

            @Override
            public void perform(UiController uiController, View view) {
                isLongClickable[0]=view.isLongClickable();
            }
        });
        return  isLongClickable[0];
    }

    /**
     * 通过控件id获取控件文字
     */
    public static String get_id_text(int id){
        final String[] text=new String[1];
        onView(withId(id)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                //识别所操作的对象类型
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "get view  is clickable";
            }

            @Override
            public void perform(UiController uiController, View view) {
                text[0]=((TextView)view).getText().toString();
            }
        });
        return  text[0];
    }
}
