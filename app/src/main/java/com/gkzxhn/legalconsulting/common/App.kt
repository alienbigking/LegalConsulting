package com.gkzxhn.legalconsulting.common

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

    override fun onCreate() {
        super.onCreate()
        mContext = this
        //初始化通用的SP&EDIT
        SP = getSharedPreferences("config", Context.MODE_PRIVATE)
        EDIT = SP?.edit()
        mApi = RetrofitClient.getInstance(this).mApi

    }

    companion object {
        var mContext: Context? = null
        /**
         * 初始化SP&EDIT
         */
        var SP: SharedPreferences? = null
        var EDIT: SharedPreferences.Editor? = null
        var mApi: ApiService? = null
    }

}