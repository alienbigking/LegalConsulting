package com.gkzxhn.legalconsulting.net

import android.content.Context
import android.util.Log
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

class CacheInterceptor(context: Context) : Interceptor {

    val context = context

    override fun intercept(chain: Interceptor.Chain?): Response? {

        var request = chain?.request()
        if (NetworkUtils.isNetConneted(context)) {

            val addHeader = request?.newBuilder()

            val token = App.SP.getString(Constants.SP_TOKEN, "")
            if (token != null) {
                if (token.isNotEmpty()) {
                    val mtoken = "Bearer $token"
                    addHeader?.addHeader("Authorization", mtoken)
                }
            }

            val method = addHeader?.method(request!!.method(), request.body())

            return chain?.proceed(method?.build())

//            val credentials = "lawyer.app" + ":" + "506a7b6dfc5d42fe857ea9494bb24014"
//            val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
//
//            val method = request?.newBuilder()?.header("Authorization", basic)
//                    ?.header("Accept", "applicaton/json")
//                    ?.method(request.method(), request.body())
//
//            return chain?.proceed(method?.build())
//            val response = chain?.proceed(request)
//             read from cache for 60 s
//            val maxAge = 60
//            val cacheControl = request?.cacheControl().toString()
//            Log.e("CacheInterceptor", "6s load cahe" + cacheControl)
//
//            val token = App.SP?.getString("token", "")
//            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aCIsImxlZ2FsIl0sInVzZXJfbmFtZSI6IjIzZGZlNzBjOTUyOTQ5ZmI4NTdlN2I3Yjc5MDVhOGNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTUzOTk5ODg0NiwianRpIjoiOGRjNGIyNGYtMWE5NS00M2Y2LTllZjMtMmVkNWE5ZDcwOGI4IiwidGVuYW50IjpudWxsLCJjbGllbnRfaWQiOiJsYXd5ZXIuYXBwIn0.rb8GLrs7exgQh-heMsA1JCzF74kqLR2JWh7M-zTfHZw"

//            val mtoken = "Bearer $token"

//            return response?.newBuilder()
//                    ?.removeHeader("Pragma")?.removeHeader("Cache-Control")
//                    ?.removeHeader("Authorization")
//                    ?.header("Cache-Control", "public, max-age=" + maxAge)
//                    ?.addHeader("Authorization", mtoken)
//                    ?.addHeader("Accept","application/json;version=1.2")

//                    ?.build()

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
