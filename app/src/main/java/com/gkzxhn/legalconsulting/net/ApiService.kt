package com.gkzxhn.legalconsulting.net


/**
 * Explanation: 接口
 * @author LSX
 *    -----2018/1/29
 */

interface ApiService {

    companion object {
        val BASE_URL: String
            get() = NetWorkCodeInfo.BASE_URL
    }

//   ogin(@FieldMap map: Map<String, String>): Observable<BaseResponseEntity<Login>>
    /***** 登录 *****/
//    @POST(NetWorkCodeInfo.BASE_START + NetWorkCodeInfo.LOGIN)
//    @FormUrlEncoded
//    fun l
//    /*****  上传头像  */
//    @POST(NetWorkCodeInfo.BASE_START + NetWorkCodeInfo.UPLOAD_IMAGE)
//    @Multipart
//    fun uploadImages(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>): Observable<BaseResponseEntity<UploadImage>>

}