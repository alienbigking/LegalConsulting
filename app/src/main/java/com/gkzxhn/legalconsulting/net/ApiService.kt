package com.gkzxhn.legalconsulting.net

import com.gkzxhn.legalconsulting.entity.*
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

    //    获取网易云信的账号
    @GET("im/users/me")
    fun getImInfo(): Observable<ImInfo>

    //    获取网易云信的账号
    @GET("im/users/{username}/account")
    fun getImAccount(@Path("username") username: String): Observable<ImInfo>

    //    获取指定法律咨询所剩视频时长
    @GET("/lawyer/my/legal-advice/{id}/video-duration")
    fun getVideoDuration(@Path("id") id: String): Observable<VideoDuration>

    /**
     * 修改我的手机号码
     */
    @PUT("users/me/phone-number")
    fun updatePhoneNumber(@Body requestBody: RequestBody): Observable<Response<Void>>

    /**
     * 支付宝提现
     */
    @POST("/lawyer/withdrawal/alipay")
    fun withdrawAli(@Body requestBody: RequestBody): Observable<Response<Void>>

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
                 @Field("refresh_token") refreshToken: String? = null): Observable<Response<ResponseBody>>

    /*****  崩溃日志上传  */
    @POST("lawyer/app-crash-log")
    fun uploadCrash(@Body map: RequestBody): Observable<Response<Void>>

    /*****  上传头像  */
    @POST("lawyer/profiles/avatar")
    fun uploadAvatar(@Body map: RequestBody): Observable<Response<Void>>

    /*****  修改头像  */
    @PUT("users/me/avatar")
    @Multipart
    fun modifyAvatar(@Part file: MultipartBody.Part): Observable<Response<Void>>

    /*****  上传文件  */
    @POST("/files")
    @Multipart
    fun uploadFiles(@Part file: MultipartBody.Part, @Query("type") page: String): Observable<UploadFile>

    // 下载文件
    @GET("/files/{id}")
    fun downloadFile(@Header("Range") range: String, @Path("id") id: String): Observable<ResponseBody>

    // 下载文件
    @GET("/files/{id}")
    fun downloadImage(@Path("id") id: String): Observable<ResponseBody>

    //    获取抢单列表
    @GET("lawyer/rush/legal-advice")
    fun getOrderReceiving(@Query("page") page: String, @Query("size") size: String): Observable<OrderReceiving>

    //    获取交易明细列表
    @GET("/lawyer/transaction-logs")
    fun getTransaction(@Query("page") page: String, @Query("size") size: String): Observable<MoneyList>

    //    获取抢单的明细
    @GET("/lawyer/rush/legal-advice/{id}")
    fun getOrderRushInfo(@Path("id") id: String): Observable<OrderRushInfo>

    //    获取指定法律咨询评论明细
    @GET("/lawyer/my/legal-advice/{id}/comment")
    fun getOrderComment(@Path("id") id: String): Observable<OrderComment>

    //    接单
    @POST("lawyer/rush/legal-advice/{id}/accepted")
    fun acceptRushOrder(@Path("id") id: String): Observable<OrderMyInfo>

    //    获取我的咨询列表
    @GET("lawyer/my/legal-advice")
    fun getOrderDispose(@Query("page") page: String, @Query("size") size: String): Observable<OrderDispose>

    //    获取我的咨询列表（所有订单）
    @GET("lawyer/my/legal-advice")
    fun getAllOrderDispose(@Query("page") page: String, @Query("size") size: String): Observable<OrderDispose>

    //    获取我的咨询的明细
    @GET("/lawyer/my/legal-advice/{id}")
    fun getOrderMyInfo(@Path("id") id: String): Observable<OrderMyInfo>

    //  接单
    @POST("lawyer/my/legal-advice/{id}/accepted")
    fun acceptMyOrder(@Path("id") id: String, @Query("reward") reward: String): Observable<OrderMyInfo>

    //  拒绝单
    @POST("lawyer/my/legal-advice/{id}/refused")
    fun rejectMyOrder(@Path("id") id: String): Observable<OrderMyInfo>

    //  查询我的支付宝绑定
    @GET("/lawyer/alipay")
    fun getAlipayInfo(): Observable<AlipayInfo>

    //  获取支付宝授权参数信息的签名
    @GET("/lawyer/alipay/auth/sign")
    fun getAlipaySign(): Observable<AlipaySign>

    // 绑定支付宝
    @POST("/lawyer/alipay/bind")
    fun bingAlipay(@Query("authCode") authCode: String): Observable<Response<Void>>

    // 绑定支付宝
    @POST("/lawyer/alipay/unbind")
    fun unbingAlipay(): Observable<Response<Void>>

}