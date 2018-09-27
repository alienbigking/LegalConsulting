package com.gkzxhn.autoespresso.config;

import com.gkzxhn.autoespresso.util.TUtils;

import java.util.List;

/**
 * Created by Raleigh.Luo on 18/2/24.
 */

public class ClassConfig {

    public static final String TEST_CLASS_NAME="%sTest";//单元测试类名

    /**获取imports代码
     * @param packagename
     * @param classpageckname
     * @param classname
     * @return
     */
    public static final String  getImports(String packagename,String classpageckname,String classname){
        return String.format(IMPORTS,packagename,classpageckname,classname,packagename);
    }
    /*********************** import ************************/
    //params: packagename,classpageckname,classname
    private static String IMPORTS ="package %s.unit"+ Config.END_LINE+Config.WRAP//包名
            +"import %s.%s"+ Config.END_LINE//引入测试类
            +"import %s.R"+ Config.END_LINE//引入资源R
            +"import org.junit.runners.MethodSorters"+ Config.END_LINE
            +"import org.junit.runner.RunWith"+ Config.END_LINE
            +"import org.junit.*"+ Config.END_LINE
            +"import android.support.test.runner.AndroidJUnit4"+ Config.END_LINE
            +"import android.support.test.filters.LargeTest"+ Config.END_LINE
            +"import android.support.test.espresso.intent.rule.IntentsTestRule"+ Config.END_LINE
            +"import android.support.test.espresso.IdlingResource"+ Config.END_LINE
            +"import android.support.test.espresso.IdlingRegistry"+ Config.END_LINE
            +"import android.content.Intent"+ Config.END_LINE
            +"import android.app.Instrumentation"+ Config.END_LINE
            +"import android.os.Build"+ Config.END_LINE
            +"import android.support.test.InstrumentationRegistry.*"+ Config.END_LINE
            +"import com.gkzxhn.autoespresso.operate.*"+ Config.END_LINE
            +"import android.app.Activity"+ Config.END_LINE
            +"import android.text.InputType"+ Config.END_LINE
            +"import android.Manifest"+ Config.END_LINE
            +"import android.provider.MediaStore"+ Config.END_LINE
            +"import android.support.test.InstrumentationRegistry"+ Config.END_LINE
            +"import android.os.Environment"+ Config.END_LINE
            +"import android.view.Gravity"+Config.END_LINE
            +"import android.content.Context"+Config.END_LINE
            +"import android.content.SharedPreferences"+Config.END_LINE
            ;

    /*********************** header ************************/
    //params: modelname,currentTime
    private static String HEADERS = Config.WRAP+"/** %s"+ Config.WRAP
            +" * Created by Raleigh.Luo on %s."+ Config.WRAP
            +" */"+ Config.WRAP
            +"@RunWith(AndroidJUnit4::class)"+ Config.WRAP
            +"@FixMethodOrder(MethodSorters.NAME_ASCENDING)"+ Config.WRAP
            +"@LargeTest"+ Config.WRAP;
    //params: modelname,currentTime
    private static String HEADERS_JAVA = Config.WRAP+"/** %s"+ Config.WRAP
            +" * Created by Raleigh.Luo on %s."+ Config.WRAP
            +" */"+ Config.WRAP
            +"@RunWith(AndroidJUnit4.class)"+ Config.WRAP
            +"@FixMethodOrder(MethodSorters.NAME_ASCENDING)"+ Config.WRAP
            +"@LargeTest"+ Config.WRAP;

    /**获取头部代码，类说明＋注解引用
     * @param modelname
     * @return
     */
    public static final String  getHeaders(String modelname){
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA)){
            return String.format(HEADERS_JAVA,modelname, TUtils.getCurrentTimeFormate());
        }else{
            return String.format(HEADERS,modelname, TUtils.getCurrentTimeFormate());
        }
    }
    /*********************** 方法注释 ************************/
    //params: caseName
    private static String METHOD_ANNOTATION= Config.TABS_LINE+"/**"+ Config.WRAP
            + Config.TABS_LINE+" * %s"+ Config.WRAP
            + Config.TABS_LINE+" */"+ Config.WRAP;
    /**获取方法注释代码
     * @param caseName
     * @return
     */
    public static final String  getMethodAnnotation(String caseName){
        return String.format(METHOD_ANNOTATION,caseName);
    }
    /*********************** class ************************/
    //params: Testclassname,className,className,className,className
    private static String CLASS_MODULE ="class %s {"+ Config.WRAP
            + Config.TABS_LINE+"private var mIdlingResource: IdlingResource?=null"+ Config.END_LINE
            + Config.TABS_LINE+"@Rule"+ Config.WRAP
            + Config.TABS_LINE+"@JvmField"+ Config.WRAP
            + Config.TABS_LINE+"val mActivityTestRule: IntentsTestRule<%s> = object : IntentsTestRule<%s>(%s::class.java) {"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"override fun getActivityIntent(): Intent {"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+ Config.TABS_LINE+"val intent = Intent(getInstrumentation().targetContext,%s::class.java)"+ Config.END_LINE
            +"%s"
            + Config.TABS_LINE+ Config.TABS_LINE+ Config.TABS_LINE+"return intent"+ Config.END_LINE
            + Config.TABS_LINE+ Config.TABS_LINE+"}"+ Config.WRAP
            + Config.TABS_LINE+"}"+ Config.WRAP;
    //params: Testclassname,className,className,className,className
    private static String CLASS_MODULE_JAVA ="public class %s {"+ Config.WRAP
            + Config.TABS_LINE+"private IdlingResource mIdlingResource=null"+ Config.END_LINE
            + Config.TABS_LINE+"@Rule"+ Config.WRAP
            + Config.TABS_LINE+"public IntentsTestRule<%s> mActivityTestRule = new  IntentsTestRule<%s>(%s.class) {"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"@Override"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"protected Intent getActivityIntent(){"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+ Config.TABS_LINE+"Intent intent=new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),%s.class)"+ Config.END_LINE
            +"%s"
            + Config.TABS_LINE+ Config.TABS_LINE+ Config.TABS_LINE+"return intent"+ Config.END_LINE
            + Config.TABS_LINE+ Config.TABS_LINE+"}"+ Config.WRAP
            + Config.TABS_LINE+"}"+ Config.END_LINE;

    /**获取类名
     * @param testClassName 测试
     * @param className
     * @return
     */
    public static final String  getClassModule(String testClassName,String className,String intentExtras){

        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA)){
            return String.format(CLASS_MODULE_JAVA,testClassName,className,className,className,className,intentExtras);
        }else{
            return String.format(CLASS_MODULE,testClassName,className,className,className,className,intentExtras);
        }
    }

    /*********************** before Method ************************/
    //params: permissions
    public static final String BEFORE_METHOD = Config.TABS_LINE+"@Before"+ Config.WRAP
            + Config.TABS_LINE+"fun setUp() {"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"//从Activity中获取延迟操作对象"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"mIdlingResource=mActivityTestRule.activity.getIdlingResource()"+ Config.END_LINE
            + Config.TABS_LINE+ Config.TABS_LINE+"//注册空闲资源－便于网络请求等耗时操作阻塞线程，进行单元测试"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"if(mIdlingResource!=null)IdlingRegistry.getInstance().register(mIdlingResource)"+ Config.END_LINE
            +"%s"
            + Config.TABS_LINE+"}"+ Config.WRAP;
    public static final String BEFORE_METHOD_JAVA = Config.TABS_LINE+"@Before"+ Config.WRAP
            + Config.TABS_LINE+"public void setUp() {"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"//从Activity中获取延迟操作对象"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"mIdlingResource=mActivityTestRule.getActivity().getIdlingResource()"+ Config.END_LINE
            + Config.TABS_LINE+ Config.TABS_LINE+"//注册空闲资源－便于网络请求等耗时操作阻塞线程，进行单元测试"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"if(mIdlingResource!=null)IdlingRegistry.getInstance().register(mIdlingResource)"+ Config.END_LINE
            +"%s"
            + Config.TABS_LINE+"}"+ Config.WRAP;
    public static final String  getBeforeMethod(String premissions,String sharedPreferencesName,String sharedPreferences){
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA)){
            String db=sharedPreferencesName.isEmpty()?"":String.format(SHAREDPREFERENCES_JAVA,sharedPreferencesName,sharedPreferences);
            return String.format(BEFORE_METHOD_JAVA, premissions+db);
        }else{
            String db=sharedPreferencesName.isEmpty()?"":String.format(SHAREDPREFERENCES,sharedPreferencesName,sharedPreferences);
            return String.format(BEFORE_METHOD,premissions+db);
        }
    }

    public static final String SHAREDPREFERENCES = Config.TABS_LINE+ Config.TABS_LINE+"//本地存储"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"val edit=mActivityTestRule.getActivity().getSharedPreferences(\"%s\",Context.MODE_PRIVATE).edit()"+ Config.END_LINE
            +"%s"
            + Config.TABS_LINE+ Config.TABS_LINE+"edit.apply()"+Config.END_LINE;

    public static final String SHAREDPREFERENCES_JAVA = Config.TABS_LINE+ Config.TABS_LINE+"//本地存储"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"SharedPreferences.Editor edit=mActivityTestRule.getActivity().getSharedPreferences(\"%s\",Context.MODE_PRIVATE).edit()"+ Config.END_LINE
            +"%s"
            + Config.TABS_LINE+ Config.TABS_LINE+"edit.apply()"+Config.END_LINE;
    //params: others
    /*********************** after Method ************************/
    public  static String AFTER_METHOD = Config.TABS_LINE+"@After"+ Config.WRAP
            + Config.TABS_LINE+"fun unregisterIdlingResource(){"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"//注销延迟操作对象"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"if(mIdlingResource!=null)IdlingRegistry.getInstance().unregister(mIdlingResource)"+ Config.END_LINE
            + Config.TABS_LINE+"}"+ Config.WRAP;

    public  static String AFTER_METHOD_JAVA = Config.TABS_LINE+"@After"+ Config.WRAP
            + Config.TABS_LINE+"public void unregisterIdlingResource(){"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"//注销延迟操作对象"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"if(mIdlingResource!=null)IdlingRegistry.getInstance().unregister(mIdlingResource)"+ Config.END_LINE
            + Config.TABS_LINE+"}"+ Config.WRAP;

    public static final String  getAfterMethod(){
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA)){
            return AFTER_METHOD_JAVA;
        }else{
            return AFTER_METHOD;
        }
    }

    /*********************** Test Method ************************/
    //params: calssName, canseNumber,methodContent
    private static String TEST_METHOD=METHOD_ANNOTATION
            + Config.TABS_LINE+"@Test"+ Config.WRAP
            + Config.TABS_LINE+"fun %s() {"+ Config.WRAP
            + Config.TABS_LINE+ Config.TABS_LINE+"with(mActivityTestRule.activity){"+ Config.WRAP;
    private static String TEST_METHOD_JAVA=METHOD_ANNOTATION
            + Config.TABS_LINE+"@Test"+ Config.WRAP
            + Config.TABS_LINE+"public void %s() {"+ Config.WRAP;


    /**获取测试方法代码
     * @param className
     * @param canseNumber
     * @return
     */
    public static final String  getTestMethod(String className,String canseNumber){
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA)){
            return String.format(TEST_METHOD_JAVA,className,canseNumber);
        }else{
            return String.format(TEST_METHOD,className,canseNumber);
        }

    }
    /***********************Suite Test Method ************************/
    private static String SUITE_CLASS ="package %s.unit"+ Config.END_LINE//包名
            +"import org.junit.runner.RunWith"+ Config.END_LINE
            +"import org.junit.runners.Suite"+ Config.END_LINE
            +"@RunWith(Suite::class)"+ Config.WRAP
            +"@Suite.SuiteClasses(%s)"+ Config.WRAP
            +"class %s {}"+ Config.WRAP;
    private static String SUITE_CLASS_JAVA ="package %s.unit"+ Config.END_LINE//包名
            +"import org.junit.runner.RunWith"+ Config.END_LINE
            +"import org.junit.runners.Suite"+ Config.END_LINE
            +"@RunWith(Suite.class)"+ Config.WRAP
            +"@Suite.SuiteClasses(%s)"+ Config.WRAP
            +"public class %s {}"+ Config.WRAP;
    /**获取suite集成测试类代码
     */
    public static final String  getSuiteClass(String packageName,String className, List<String> unitTestClassNames){
        StringBuffer sb=new StringBuffer();
        for(String testClassname: unitTestClassNames){
            if(sb.length()>0)sb.append(",");
            if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA))
                sb.append(String.format("%s.class",testClassname));
            else
                sb.append(String.format("%s::class",testClassname));
        }
        if(Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA)){
            return  String.format(SUITE_CLASS_JAVA,packageName,sb.toString(),className);
        }else{
            return  String.format(SUITE_CLASS,packageName,sb.toString(),className);
        }
    }


}
