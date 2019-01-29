package com.gkzxhn.legalconsulting.model

import android.content.Context
import com.gkzxhn.legalconsulting.entity.*
import rx.Observable


/**
 * @classname：
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：抢单 我的
 */

interface IOrderModel : IBaseModel {
    fun getOrderDispose(context: Context ,page: String, size: String): Observable<OrderDispose>
    fun getOrderRushInfo(context: Context, id: String): Observable<OrderRushInfo>
    fun getOrderMyInfo(context: Context, id: String): Observable<OrderMyInfo>
    fun rejectMyOrder(context: Context, id: String): Observable<OrderMyInfo>
    fun acceptRushOrder(context: Context, id: String): Observable<OrderMyInfo>
    fun acceptMyOrder(context: Context, id: String,reward:String): Observable<OrderMyInfo>
    fun getOrderReceiving(context: Context, page: String, size: String): Observable<OrderReceiving>
    fun getImAccount(context: Context, account: String): Observable<ImInfo>
    fun getOrderComment(context: Context, id: String): Observable<OrderComment>
    fun getVideoDuration(context: Context, id: String): Observable<VideoDuration>
}