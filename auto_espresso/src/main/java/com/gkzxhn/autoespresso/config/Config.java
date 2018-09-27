package com.gkzxhn.autoespresso.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raleigh.Luo on 18/2/25.
 */

public class Config {
    /***********************常量**************************/
    public static final String TEST_CLASS_SUFFIX_KOTLIN=".kt";
    public static final String TEST_CLASS_SUFFIX_JAVA=".java";
    public static final String WRAP="\n";//换行符
    public static final String END_LINE_KOTLIN=WRAP;//kotlin end
    public static final String END_LINE_JAVA=";"+WRAP;//java end

    public static final String TABS_LINE="\t";//制表符
    public static final String TAKE_SCREENSHOT="Environment.getExternalStorageDirectory().getPath()+\"/takeScreenshot/\"";
    public static final int MODULE_FIRST_ROW =2;//模块名称英文头部第一行,英文行
    public static final String UNIT_PACKAGE_NAME="/unit";
    /***********************过滤 配置 sheet名**************************/
    public static final String[] FILTER_SHEET_NAMES={"config","params","demo"};
    /***********************单元测试名**************************/
    public static final List<String> MODULE_NAMES=new ArrayList<String>();

    /***********************变量**************************/
    public static boolean IS_DEBUG=false;
    //文件后缀
    public static String TEST_CLASS_SUFFIX=TEST_CLASS_SUFFIX_JAVA;
    public static String END_LINE=END_LINE_JAVA;//换行符
}
