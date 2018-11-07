package com.gkzxhn.legalconsulting.utils.download

import com.gkzxhn.legalconsulting.utils.download.DownloadResponseBody.DownloadProgressListener
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * @classname：
 * @author：liushaoxiang
 * @date：2018/11/6 5:34 PM
 * @description：
 */
class DownloadInterceptor(private val listener: DownloadProgressListener) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        return originalResponse.newBuilder()
                .body(DownloadResponseBody(originalResponse.body()!!, listener))
                .build()
    }
}