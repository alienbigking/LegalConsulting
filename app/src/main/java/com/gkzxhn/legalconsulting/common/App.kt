package com.gkzxhn.legalconsulting.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.gkzxhn.legalconsulting.net.ApiService
import com.gkzxhn.legalconsulting.net.RetrofitClient


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
class App : Application() {

    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        super.onCreate()
        mContext = this
        //初始化通用的SP&EDIT
        SP = getSharedPreferences("config", Context.MODE_PRIVATE)
        EDIT = SP.edit()
        mApi = RetrofitClient.getInstance(this).mApi!!

        /****** 崩溃日志初始化 ******/
        CrashHandler.instance.init(this)

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
        /**
         * 初始化SP&EDIT
         */
        lateinit var SP: SharedPreferences
        lateinit var EDIT: SharedPreferences.Editor
        lateinit var mApi: ApiService
    }

}