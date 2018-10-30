package com.gkzxhn.legalconsulting.net

import android.content.Context
import android.util.Base64
import android.util.Log
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Explanation: 登录相关
 * @author LSX
 *    -----2018/9/6
 */
class CacheInterceptorLogin(context: Context) : Interceptor {
    val context = context
    override fun intercept(chain: Interceptor.Chain?): Response? {

        var request = chain?.request()
        if (NetworkUtils.isNetConneted(context)) {
            val credentials = "lawyer.app" + ":" + "506a7b6dfc5d42fe857ea9494bb24014"
            val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            val addHeader = request?.newBuilder()?.addHeader("Authorization", basic)

            val token = App.SP.getString(Constants.SP_TOKEN, "")
            if (token != null) {
                if (token.isNotEmpty()) {
                    val mtoken = "Bearer $token"
                    addHeader?.addHeader("Authorization", mtoken)
                }
            }
            val method = addHeader?.method(request!!.method(), request.body())
            return chain?.proceed(method?.build())
        } else {
            Log.e("CacheInterceptor", " no network load cahe")
            request = request?.newBuilder()?.cacheControl(CacheControl.FORCE_CACHE)?.build()
            val response = chain?.proceed(request)
            //set cahe times is 3 days
            val maxStale = 60 * 60 * 24 * 3
            return response?.newBuilder()?.removeHeader("Pragma")?.removeHeader("Cache-Control")?.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)?.build()
        }
    }
}
