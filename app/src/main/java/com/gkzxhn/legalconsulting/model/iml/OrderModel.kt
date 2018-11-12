package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.entity.OrderDispose
import com.gkzxhn.legalconsulting.entity.OrderMyInfo
import com.gkzxhn.legalconsulting.entity.OrderReceiving
import com.gkzxhn.legalconsulting.entity.OrderRushInfo
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.net.RetrofitClient
import rx.Observable
import rx.schedulers.Schedulers


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

class OrderModel : BaseModel(), IOrderModel {

    override fun getOrderMyInfo(context: Context, id: String): Observable<OrderMyInfo> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getOrderMyInfo(id)
                ?.subscribeOn(Schedulers.io()) as Observable<OrderMyInfo>

    }

    override fun getOrderRushInfo(context: Context, id: String): Observable<OrderRushInfo> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getOrderRushInfo(id)
                ?.subscribeOn(Schedulers.io()) as Observable<OrderRushInfo>
    }

    override fun getOrderReceiving(context: Context,page:String,size:String): Observable<OrderReceiving> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getOrderReceiving(page,size)
                ?.subscribeOn(Schedulers.io()) as Observable<OrderReceiving>

    }

    override fun getOrderDispose(context: Context, page: String, size: String): Observable<OrderDispose> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getOrderDispose(page,size)
                ?.subscribeOn(Schedulers.io()) as Observable<OrderDispose>

    }

    override fun acceptMyOrder(context: Context, id: String): Observable<OrderMyInfo> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.acceptMyOrder(id)
                ?.subscribeOn(Schedulers.io()) as Observable<OrderMyInfo>

    }

    override fun rejectMyOrder(context: Context, id: String): Observable<OrderMyInfo> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.rejectMyOrder(id)
                ?.subscribeOn(Schedulers.io()) as Observable<OrderMyInfo>

    }

    override fun acceptRushOrder(context: Context, id: String): Observable<OrderMyInfo> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.acceptRushOrder(id)
                ?.subscribeOn(Schedulers.io()) as Observable<OrderMyInfo>

    }

}