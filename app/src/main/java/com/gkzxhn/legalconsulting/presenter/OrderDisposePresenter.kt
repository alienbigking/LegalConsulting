package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import com.gkzxhn.legalconsulting.entity.OrderReceiving
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.model.iml.OrderModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.view.OrderDisposeView
import rx.android.schedulers.AndroidSchedulers

/**
 * @classname：抢单
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：
 */
class OrderDisposePresenter(context: Context, view: OrderDisposeView) : BasePresenter<IOrderModel, OrderDisposeView>(context, OrderModel(), view) {

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


}