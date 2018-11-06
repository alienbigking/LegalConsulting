package com.gkzxhn.legalconsulting.net

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @classname：DownLoadService
 * @author：liushaoxiang
 * @date：2018/11/5 4:27 PM
 * @description：
 */

interface DownLoadService{
    /**
     * 下载
     */
    @Streaming
    @GET
    fun downloadFile(@Header("Range") range: String, @Url url: String): Observable<ResponseBody>


}