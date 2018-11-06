package com.gkzxhn.legalconsulting.utils.download

import com.gkzxhn.legalconsulting.utils.download.DownloadResponseBody.DownloadProgressListener
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by 方 on 2018/4/13.
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