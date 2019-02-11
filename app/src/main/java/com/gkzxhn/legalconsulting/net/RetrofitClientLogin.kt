package com.gkzxhn.legalconsulting.net

import android.content.Context
import android.util.Log
import com.gkzxhn.legalconsulting.net.error_exception.MyGsonConverterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit


 /**
 * Explanation: 登录相关
 * @author LSX
 *    -----2018/9/6
 */

class RetrofitClientLogin private constructor(context: Context, baseUrl: String){

    var httpCacheDirectory : File? = null
    val mContext : Context = context
    var cache : Cache? = null
    var okHttpClient : OkHttpClient? = null
    var retrofit : Retrofit? = null
    val DEFAULT_TIMEOUT : Long = 6
    val url = baseUrl
    var mApi : ApiService? = null

    init {
        //缓存地址
        if (httpCacheDirectory == null) {
            httpCacheDirectory = File(mContext.cacheDir, "app_cache")
        }
        try {
            if (cache == null) {
                cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)
            }
        } catch (e: Exception) {
            Log.e("OKHttp", "Could not create http cache", e)
        }

        mApi = provideHotApi()

    }

    fun provideHotApi(): ApiService? {
        //okhttp创建了
        okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(CacheInterceptorLogin(mContext))
                .addNetworkInterceptor(CacheInterceptorLogin(mContext))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()
        //retrofit创建了
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build()
        return retrofit?.create(ApiService::class.java)
    }

    companion object {
        var instance: RetrofitClientLogin? = null
        fun getInstance(context: Context) : RetrofitClientLogin {
            if (instance == null) {
                synchronized(RetrofitClientLogin::class){
                    if (instance == null) {
                        instance = RetrofitClientLogin(context, NetWorkCodeInfo.BASE_URL)
                    }
                }
            }
            return instance!!
        }
    }

}