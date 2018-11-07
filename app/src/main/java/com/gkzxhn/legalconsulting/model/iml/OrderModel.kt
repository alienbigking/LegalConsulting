package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.entity.OrderReceiving
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

    override fun getOrderReceiving(context: Context): Observable<OrderReceiving> {
        return RetrofitClient.Companion.getInstance(context).mApi
                ?.getOrderReceiving()
                ?.subscribeOn(Schedulers.io()) as Observable<OrderReceiving>

    }

}