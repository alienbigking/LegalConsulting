package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.entity.ImInfo
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.model.ILoginModel
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.RetrofitClientLogin
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import rx.Observable
import rx.schedulers.Schedulers


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

class LoginModel : BaseModel(), ILoginModel {
    override fun login(context: Context, body: RequestBody): Observable<Response<Void>> {
        return RetrofitClientLogin.Companion.getInstance(context).mApi
                ?.login(body)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>

    }

    override fun getCode(context: Context, phone: String): Observable<Response<Void>> {
        return RetrofitClientLogin.Companion.getInstance(context).mApi
                ?.getCode(phone)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>
    }

    override fun getLawyersInfo(context: Context): Observable<LawyersInfo> {
        return RetrofitClient.getInstance(context).mApi?.getLawyersInfo()?.subscribeOn(Schedulers.io()) as Observable<LawyersInfo>
    }

    override fun getToken(context: Context, phoneNumber: String, code: String): Observable<Response<ResponseBody>>? {
        return RetrofitClientLogin.Companion.getInstance(context)
                .mApi?.getToken("password", phoneNumber, code)
                ?.subscribeOn(Schedulers.io())
    }

    override fun uploadCrash(context: Context, body: RequestBody): Observable<Response<Void>> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.uploadCrash(body)
                ?.subscribeOn(Schedulers.io())
                as Observable<Response<Void>>
    }

    override fun getIMInfo(context: Context): Observable<ImInfo> {
        return RetrofitClientLogin.Companion.getInstance(context).mApi
                ?.getImInfo()
                ?.subscribeOn(Schedulers.io())
                as Observable<ImInfo>
    }

}