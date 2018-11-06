package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.model.IPhoneChangeModel
import com.gkzxhn.legalconsulting.net.RetrofitClientLogin
import okhttp3.RequestBody
import retrofit2.Response
import rx.Observable
import rx.schedulers.Schedulers


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

class PhoneChangeModel : BaseModel(), IPhoneChangeModel {

    override fun login(context: Context, body: RequestBody): Observable<Response<Void>> {
        return RetrofitClientLogin.Companion.getInstance(context).mApi
                ?.login(body)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>

    }

    override fun updatePhoneNumber(context: Context, body: RequestBody): Observable<Response<Void>> {
        return RetrofitClientLogin.Companion.getInstance(context).mApi
                ?.updatePhoneNumber(body)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>

    }

    override fun getCode(context: Context, phone: String): Observable<Response<Void>> {
        return RetrofitClientLogin.Companion.getInstance(context).mApi
                ?.getCode(phone)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>
    }

}