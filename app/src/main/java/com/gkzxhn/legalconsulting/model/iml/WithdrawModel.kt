package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.entity.AlipayInfo
import com.gkzxhn.legalconsulting.model.IWithdrawModel
import com.gkzxhn.legalconsulting.net.RetrofitClient
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

class WithdrawModel : BaseModel(), IWithdrawModel {


    override fun getCode(context: Context, phone: String): Observable<Response<Void>> {
        return RetrofitClientLogin.Companion.getInstance(context).mApi
                ?.getCode(phone)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>
    }

    override fun getAlipayInfo(context: Context): Observable<AlipayInfo> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getAlipayInfo()
                ?.subscribeOn(Schedulers.io()) as Observable<AlipayInfo>
    }

    override fun withdrawAli(context: Context, body: RequestBody): Observable<Response<Void>> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.withdrawAli(body)
                ?.subscribeOn(Schedulers.io()) as Observable<Response<Void>>
    }

}