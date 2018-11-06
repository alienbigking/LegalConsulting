package com.gkzxhn.legalconsulting.net

import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.entity.QualificationAuthentication
import com.gkzxhn.legalconsulting.entity.UpdateInfo
import com.gkzxhn.legalconsulting.entity.UploadFile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import rx.Observable


/**
 * Explanation: 接口
 * @author LSX
 *    -----2018/1/29
 */

interface ApiService {

    // 获取验证码
    @POST("/users/{phoneNumber}/verification-codes/login")
    fun getCode(@Path("phoneNumber") string: String): Observable<Response<Void>>

    //    注册 登录
    @POST("users/of-mobile")
    @Headers("Content-Type:application/json;charset=utf-8")
    fun login(@Body map: RequestBody): Observable<Response<Void>>

    /**
     * 修改我的手机号码
     */
    @PUT("users/me/phone-number")
    fun updatePhoneNumber(@Body requestBody: RequestBody): Observable<Response<Void>>

    /**
     *获取律师详情
     */
    @GET("lawyer/profiles")
    fun getLawyersInfo(): Observable<LawyersInfo>

    /**
     *获取android最新版本
     */
    @GET("lawyer/app-version/android/latest")
    fun updateApp(): Observable<UpdateInfo>

    /****** 设置接单状态 ******/
    @POST("lawyer/profiles/service-status")
    @Headers("Content-Type:application/json;charset=utf-8")
    fun setOrderState(@Body map: RequestBody): Observable<Response<Void>>

    /****** 意见反馈 ******/
    @POST("lawyer/feedback")
    @Headers("Content-Type:application/json;charset=utf-8")
    fun feedback(@Body map: RequestBody): Observable<Response<Void>>

    /****** 添加或更新律师认证 ******/
    @POST("lawyer/certification")
    @Headers("Content-Type:application/json;charset=utf-8")
    fun certification(@Body map: RequestBody): Observable<Response<Void>>

   /****** 添加或更新律师认证 ******/
    @GET("lawyer/certification")
    @Headers("Content-Type:application/json;charset=utf-8")
    fun getCertification(): Observable<QualificationAuthentication>

    /**
     * 获取token
     */
    @POST("oauth/token")
    @FormUrlEncoded
    fun getToken(@Field("grant_type") grant_type: String,
                 @Field("username") username: String? = null,
                 @Field("password") password: String? = null,
                 @Field("refresh_token") refreshToken: String? = null): Observable<ResponseBody>

    /*****  崩溃日志上传  */
    @POST("lawyer/app-crash-log")
    fun uploadCrash(@Body map: RequestBody): Observable<Response<Void>>

    /*****  上传头像  */
    @POST("lawyer/profiles/avatar")
    fun uploadAvatar(@Body map: RequestBody): Observable<Response<Void>>

    /*****  上传文件  */
    @POST("/files")
    @Multipart
    fun uploadFiles(@Part file: MultipartBody.Part): Observable<UploadFile>

    // 下载文件
//    @Streaming
    @GET("/files/{id}")
    fun downloadFile(@Header("Range") range: String,@Path("id") id: String): Observable<ResponseBody>


}