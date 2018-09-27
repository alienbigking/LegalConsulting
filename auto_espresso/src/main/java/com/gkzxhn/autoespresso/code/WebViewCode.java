package com.gkzxhn.autoespresso.code;

/**
 * Created by Raleigh.Luo on 18/6/5.
 */

public class WebViewCode extends BaseCode {
    public static String forceJavascriptEnabled(String procedureName) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.forceJavascriptEnabled()"+END_LINE;
        return String.format(format,procedureName);
    }
    /**
     * 点击
     * @param procedureName
     * @return
     */
    public static String click(String procedureName,String type,String value) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.click(\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value);
    }
    /**
     * 点击
     * @param procedureName
     * @return
     */
    public static String click_sub(String procedureName,String type,String value,String subType,String subValue) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.click_sub(\"%s\",\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,subType,subValue);
    }
    /**
     * 清除文本
     * @param procedureName
     * @return
     */
    public static String clear_text(String procedureName,String type,String value) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.clear_text(\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value);
    }
    /**
     * 清除文本
     * @param procedureName
     * @return
     */
    public static String clear_text_sub(String procedureName,String type,String value,String subType,String subValue) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.clear_text_sub(\"%s\",\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,subType,subValue);
    }

    /**
     * 输入文本
     * @param procedureName
     * @return
     */
    public static String input_text(String procedureName,String type,String value,String inputText) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.input_text(\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,inputText);
    }

    /**
     * 输入文本
     * @param procedureName
     * @return
     */
    public static String input_text_sub(String procedureName,String type,String value,String subType,String subValue,String inputText) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.input_text_sub(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,subType,subValue,inputText);
    }

    /**
     * 验证文本
     * @param procedureName
     * @return
     */
    public static String check_text(String procedureName,String type,String value,String matcherText) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.check_text(\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,matcherText);
    }

    /**
     * 验证文本
     * @param procedureName
     * @return
     */
    public static String check_text_sub(String procedureName,String type,String value,String subType,String subValue,String matcherText) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.check_text_sub(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,subType,subValue,matcherText);
    }
    /**
     *  验证跳转的URL
     * @param procedureName
     * @return
     */
    public static String check_url(String procedureName,String type,String value,String matcherUrl) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.check_url(\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,matcherUrl);
    }

    /**
     *  验证跳转的URL
     * @param procedureName
     * @return
     */
    public static String check_url_sub(String procedureName,String type,String value,String subType,String subValue,String matcherUrl) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TWebView.check_url_sub(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")"+END_LINE;
        return String.format(format,procedureName,type,value,subType,subValue,matcherUrl);
    }
}
