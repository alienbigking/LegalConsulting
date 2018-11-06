package com.gkzxhn.legalconsulting.common

import android.content.Context
import android.os.Build
import android.util.Log
import com.gkzxhn.legalconsulting.entity.CrashLogger
import com.gkzxhn.legalconsulting.model.iml.LoginModel
import com.gkzxhn.legalconsulting.utils.ObtainVersion
import com.gkzxhn.legalconsulting.utils.logE
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*

/**
 * @classname：崩溃日志处理类
 * @author：liushaoxiang
 * @date：2018/10/29 3:39 PM
 * @description：
 */
class CrashHandler
/** 保证只有一个CrashHandler实例  */
private constructor() : Thread.UncaughtExceptionHandler {
    //系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    //程序的Context对象
    private var mContext: Context? = null
    //用来存储设备信息和异常信息
    private val infos = HashMap<String, String>()

    //用于格式化日期,作为日志文件名的一部分
    private val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
    //是否已发送log日志
    private var flag = false
    lateinit var model: LoginModel
    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context) {
        model = LoginModel()
        mContext = context
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(50)
            } catch (e: InterruptedException) {
            }

            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        if (flag) {
            return false
        }

        val crashLogger = getRequestBody(ex)
        ("handleException: body === ${crashLogger.toString()}").logE(this)
        /**
         *发送给服务器
         */
        uploadCrash(crashLogger)
        return true
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/29 2:47 PM.
     * @description：上传崩溃日志
     */
    private fun uploadCrash(crashLogger: CrashLogger) {
        var map = LinkedHashMap<String, String>()
        map.put("content", crashLogger.contents)
        map.put("deviceName", crashLogger.deviceName)
        map.put("sysVersion", crashLogger.sysVersion)
        map.put("deviceType", crashLogger.deviceType)
        map.put("appVersion", crashLogger.appVersion)
        var body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(map))
        model.uploadCrash(App.mContext, body)
                .subscribe({
                    it.code()
                    Log.v("okhttp",it.code().toString())
                    flag = true
                }, {
                    Log.v("okhttp",it.message.toString())
                    flag = true
                })
    }

    /**
     * 获取log请求体
     * @return
     * @param ex
     */
    private fun getRequestBody(ex: Throwable): CrashLogger {
        val crashLogger = CrashLogger()
        crashLogger.appVersion = ObtainVersion.getVersionName(App.mContext)
        crashLogger.contents = ex.message.toString()
        crashLogger.deviceName = Build.BRAND + "_" + Build.MODEL
        crashLogger.deviceType = "Android"
        crashLogger.sysVersion = Build.VERSION.SDK_INT.toString()
        crashLogger.phone = App.SP.getString(Constants.SP_PHONE, "unkown")
        return crashLogger
    }

    companion object {
        /** 获取CrashHandler实例 ,单例模式 CrashHandler实例  */
        val instance = CrashHandler()
    }
}