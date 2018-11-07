package com.gkzxhn.legalconsulting.model

import android.content.Context
import com.gkzxhn.legalconsulting.entity.OrderReceiving
import rx.Observable


/**
 * @classname：
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：抢单 我的
 */

interface IOrderModel : IBaseModel {


    fun getOrderReceiving(context: Context): Observable<OrderReceiving>

}