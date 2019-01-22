package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.entity.QualificationAuthentication
import com.gkzxhn.legalconsulting.entity.UploadFile
import com.gkzxhn.legalconsulting.model.IQualificationAuthenticationModel
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.RetrofitClientLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import rx.Observable
import rx.schedulers.Schedulers

/**
 * @classname：QualificationAuthenticationModel
 * @author：liushaoxiang
 * @date：2018/10/31 4:28 PM
 * @description：
 */

class QualificationAuthenticationModel : BaseModel(), IQualificationAuthenticationModel {

    override fun getCertification(context: Context): Observable<QualificationAuthentication> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getCertification()
                ?.subscribeOn(Schedulers.io()) as Observable<QualificationAuthentication>
    }

    override fun certification(context: Context, body: RequestBody): Observable<Response<Void>> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.certification(body)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>
    }

    override fun uploadFiles(context: Context, body: MultipartBody.Part): Observable<UploadFile> {
        return RetrofitClientLogin.getInstance(context).mApi?.uploadFiles(body,"PROTECTED")
                ?.subscribeOn(Schedulers.io()) as Observable<UploadFile>
    }

}