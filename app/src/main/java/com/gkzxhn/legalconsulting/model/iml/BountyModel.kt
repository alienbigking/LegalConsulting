package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.entity.AlipaySign
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.model.IBountyModel
import com.gkzxhn.legalconsulting.net.RetrofitClient
import retrofit2.Response
import rx.Observable
import rx.schedulers.Schedulers


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

class BountyModel : BaseModel(), IBountyModel {

    override fun getAlipaySign(context: Context): Observable<AlipaySign> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getAlipaySign()
                ?.subscribeOn(Schedulers.io())
                as Observable<AlipaySign>
    }

    override fun bingAlipay(context: Context, authCode: String): Observable<Response<Void>> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.bingAlipay(authCode)
                ?.subscribeOn(Schedulers.io())
                as Observable<Response<Void>>
    }

    override fun unbingAlipay(context: Context): Observable<Response<Void>> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.unbingAlipay()
                ?.subscribeOn(Schedulers.io())
                as Observable<Response<Void>>
    }

    override fun getLawyersInfo(context: Context): Observable<LawyersInfo> {
        return RetrofitClient.getInstance(context).mApi?.getLawyersInfo()
                ?.subscribeOn(Schedulers.io()) as Observable<LawyersInfo>
    }


}