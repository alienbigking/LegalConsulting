package com.gkzxhn.autoespresso.operate;

import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;

import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.model.Atoms.getCurrentUrl;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static org.hamcrest.Matchers.containsString;
/**
 * Created by Raleigh.Luo on 18/6/4.
 *   onWebView()
 *   .withElement(Atom)
 *   .perform(Atom)
 *   .check(WebAssertion);
 */

public class TWebView {
    /**
     * afterActivityLaunched  调用
     */
    public static void forceJavascriptEnabled()   {
        onWebView().forceJavascriptEnabled();
    }

    /**
     * 点击
     * @param type eg:"id"
     * @param value et:"myId"
     */
    public static void click(String type,String value){
        onWebView()
                // Find the input element by ID
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                .perform(DriverAtoms.webClick());
    }

    /**
     * 两级div
     * @param type eg:"id"
     */
    public static void click_sub(String type,String value,String subType,String subValue){
        onWebView()
                //父级div ID
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                //子级div ID
                .withContextualElement(DriverAtoms.findElement(getLocator(subType), subValue))
                .perform(DriverAtoms.webClick());
    }

    /**
     * 清除文本
     * @param type eg:"id"
     * @param value
     */
    public static void clear_text(String type,String value){
        onWebView()
                // Find the input element by ID
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                .perform(DriverAtoms.clearElement());
    }
    /**
     * 清除文本
     * @param type eg:"id"
     * @param value
     */
    public static void clear_text_sub(String type,String value,String subType,String subValue){
        onWebView()
                //父级div ID
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                //子级div ID
                .withContextualElement(DriverAtoms.findElement(getLocator(subType), subValue))
                .perform(DriverAtoms.clearElement());
    }

    /**
     * 输入文本
     * @param type
     * @param value
     * @param inputText
     */
    public static void input_text(String type,String value,String inputText){
        onWebView()
                // Find the input element by ID
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                //Enter text into the input element
                .perform(DriverAtoms.webKeys(inputText));
    }
    /**
     * 输入文本
     * @param type
     * @param value
     * @param inputText
     */
    public static void input_text_sub(String type,String value,String subType,String subValue,String inputText){
        onWebView()
                // Find the input element by ID
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                //子级div ID
                .withContextualElement(DriverAtoms.findElement(getLocator(subType), subValue))
                //Enter text into the input element
                .perform(DriverAtoms.webKeys(inputText));
    }
    /**
     * 验证文本
     * @param type
     * @param value
     * @param matcherText
     */
    public static void check_text(String type,String value,String matcherText){
        onWebView()
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                // Verify that the text is displayed
                .check(webMatches(DriverAtoms.getText(), containsString(matcherText)));
    }
    /**
     * 验证文本
     * @param type
     * @param value
     * @param matcherText
     */
    public static void check_text_sub(String type,String value,String subType,String subValue,String matcherText){
        onWebView()
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                //子级div ID
                .withContextualElement(DriverAtoms.findElement(getLocator(subType), subValue))
                // Verify that the text is displayed
                .check(webMatches(DriverAtoms.getText(), containsString(matcherText)));
    }

    /**
     * 验证跳转的URL
     * @param type
     * @param value
     * @param matcherUrl
     */
    public static void check_url(String type,String value,String subType,String subValue,String matcherUrl){
        onWebView()
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                //子级div ID
                .withContextualElement(DriverAtoms.findElement(getLocator(subType), subValue))
                // Verify that the text is displayed
                .check(webMatches(getCurrentUrl(), containsString(matcherUrl)));
    }
    /**
     * 验证跳转的URL
     * @param type
     * @param value
     * @param matcherUrl
     */
    public static void check_url_sub(String type,String value,String matcherUrl){
        onWebView()
                .withElement(DriverAtoms.findElement(getLocator(type), value))
                .perform(DriverAtoms.webClick())
                // Verify that the text is displayed
                .check(webMatches(getCurrentUrl(), containsString(matcherUrl)));
    }


    static Locator getLocator(String type) {
        if (Locator.CLASS_NAME.getType().equals(type)) {
            return Locator.CLASS_NAME;
        }
        if (Locator.CSS_SELECTOR.getType().equals(type)) {
            return Locator.CSS_SELECTOR;
        }
        if (Locator.ID.getType().equals(type)) {
            return Locator.ID;
        }
        if (Locator.LINK_TEXT.getType().equals(type)) {
            return Locator.LINK_TEXT;
        }
        if (Locator.NAME.getType().equals(type)) {
            return Locator.NAME;
        }
        if (Locator.PARTIAL_LINK_TEXT.getType().equals(type)) {
            return Locator.PARTIAL_LINK_TEXT;
        }
        if (Locator.TAG_NAME.getType().equals(type)) {
            return Locator.TAG_NAME;
        }
        if (Locator.XPATH.getType().equals(type)) {
            return Locator.XPATH;
        }
        return null;
    }
}
