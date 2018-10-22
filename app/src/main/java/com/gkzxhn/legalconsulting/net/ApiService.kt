package com.gkzxhn.legalconsulting.net

import com.gkzxhn.legalconsulting.entity.LawyersInfo
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

    companion object {
        val BASE_URL: String
            get() = NetWorkCodeInfo.BASE_URL_PROJECT
    }

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
    fun updatePhoneNumber(@HeaderMap headers: Map<String, String>,
                          @Body requestBody: RequestBody): Observable<Response<Void>>


//    @GET("lawyers")
//    fun getLawyersInfo(@HeaderMap headers: Map<String, String>,@Query("username") username: String):Observable<LawyersInfo>

    @GET("lawyer/profiles")
    fun getLawyersInfo(): Observable<LawyersInfo>


    /****** 设置接单状态 ******/
    @POST("lawyer/profiles/service-status")
    @Headers("Content-Type:application/json;charset=utf-8")
    fun setOrderState(@Body map: RequestBody): Observable<Response<Void>>

    /****** 意见反馈 ******/
    @POST("lawyers/feedback")
    @Headers("Content-Type:application/json;charset=utf-8")
    fun feedback(@Body map: RequestBody): Observable<Response<Void>>


    /**
     * 获取token
     */
    @POST("oauth/token")
    @FormUrlEncoded
    fun getToken(@Field("grant_type") grant_type: String,
                 @Field("username") username: String? = null,
                 @Field("password") password: String? = null,
                 @Field("refresh_token") refreshToken: String? = null): Observable<ResponseBody>

//    /*****  上传头像  */
//    @POST(NetWorkCodeInfo.BASE_START + NetWorkCodeInfo.UPLOAD_IMAGE)
//    @Multipart
//    fun uploadImages(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>): Observable<BaseResponseEntity<UploadImage>>

}