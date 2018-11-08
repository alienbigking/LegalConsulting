package com.gkzxhn.legalconsulting.model

import android.content.Context
import com.gkzxhn.legalconsulting.entity.OrderDispose
import com.gkzxhn.legalconsulting.entity.OrderMyInfo
import com.gkzxhn.legalconsulting.entity.OrderReceiving
import com.gkzxhn.legalconsulting.entity.OrderRushInfo
import rx.Observable


/**
 * @classname：
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：抢单 我的
 */

interface IOrderModel : IBaseModel {

    fun getOrderReceiving(context: Context): Observable<OrderReceiving>
    fun getOrderDispose(context: Context): Observable<OrderDispose>
    fun getOrderRushInfo(context: Context, id: String): Observable<OrderRushInfo>
    fun getOrderMyInfo(context: Context, id: String): Observable<OrderMyInfo>
    fun rejectMyOrder(context: Context, id: String): Observable<OrderMyInfo>
    fun acceptRushOrder(context: Context, id: String): Observable<OrderMyInfo>
    fun acceptMyOrder(context: Context, id: String): Observable<OrderMyInfo>
}