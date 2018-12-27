package com.gkzxhn.legalconsulting.model

import android.content.Context
import com.gkzxhn.legalconsulting.entity.AlipayInfo
import okhttp3.RequestBody
import retrofit2.Response
import rx.Observable


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface IWithdrawModel : IBaseModel {

    fun getCode(context: Context, phone: String): Observable<Response<Void>>

    fun withdrawAli(context: Context, body: RequestBody): Observable<Response<Void>>

    fun getAlipayInfo(context: Context): Observable<AlipayInfo>
}