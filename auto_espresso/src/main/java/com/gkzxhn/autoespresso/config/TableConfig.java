package com.gkzxhn.autoespresso.config;

/**
 * Created by Raleigh.Luo on 18/2/24.
 */

public interface TableConfig {//表头
    String MODULE_NUMBER="ModuleNumer";//模块编号
    String MODULE_NAME="ModuleName";//模块名称
    String IS_CREATE="isCreate";//是否创建
    String CLASS_NAME="ClassName";//测试类名
    String CLASS_PACKAGE_NAME="ClassPackageName";//所在包名
    String INTENT_EXTRA="Intent";//Intent值
    String SHAREDPREFERENCES_NAME="SharedPreferencesName";//本地存储名称
    String SHAREDPREFERENCES="SharedPreferences";//本地存储
    String PREMISSIONS="Premissions";//所需权限
    String CASE_NUMBER="CaseNumber";//用例编号
    String CASE_NAME="CaseName";//用例名称
    String IS_EXECUTE ="isExecute";//是否执行
    String TESTING_PROCEDURE="TestingProcedure";//测试步骤
    String VIEW_TYPE="ViewType";//操作类型
    String ACTION ="Action";//操作类型
    String PARAM ="param";//参数  参数index为 ［action+1,param］
    //module头部名称
    String[] MODULE_HEADERS={MODULE_NUMBER,MODULE_NAME,IS_CREATE,CLASS_NAME,CLASS_PACKAGE_NAME,INTENT_EXTRA,SHAREDPREFERENCES_NAME,SHAREDPREFERENCES,PREMISSIONS};
    //测试用例头部名称
    String[] CASE_HEADERS ={CASE_NUMBER
    ,CASE_NAME, IS_EXECUTE,TESTING_PROCEDURE,VIEW_TYPE, ACTION,
            PARAM};
    String PUT_STRING="putString";
    String PUT_INT="putInt";
    String PUT_BOOLEAN="putBoolean";
    String PUT_FLOAT="putFloat";
    String PUT_LONG="putLong";
    //数据存储／传值格式
    String[] EXTRA_FORMAT ={PUT_STRING,PUT_INT,PUT_BOOLEAN,PUT_FLOAT,PUT_LONG};
}
