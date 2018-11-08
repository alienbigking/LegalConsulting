package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.OrderMyInfo
import com.gkzxhn.legalconsulting.entity.OrderReceiving
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.model.iml.OrderModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.OrderReceivingView
import rx.android.schedulers.AndroidSchedulers

/**
 * @classname：抢单
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：
 */
class OrderReceivingPresenter(context: Context, view: OrderReceivingView) : BasePresenter<IOrderModel, OrderReceivingView>(context, OrderModel(), view) {

    fun getOrderReceiving() {
        mContext?.let {
            mModel.getOrderReceiving(it)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderReceiving>(it) {
                        override fun success(t: OrderReceiving) {
                            if (t.content!!.isNotEmpty()) {
                                mView?.updateData(t.content)
                            }
                        }
                    })
        }
    }

    fun acceptRushOrder(id:String) {
        mContext?.let {
            mModel.acceptRushOrder(it,id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            if (t.status == Constants.ORDER_STATE_PROCESSING) {
                                mContext?.showToast("接单成功")
                                getOrderReceiving()
                            }
                        }
                    })
        }

    }

}