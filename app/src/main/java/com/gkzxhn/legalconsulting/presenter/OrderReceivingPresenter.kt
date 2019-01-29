package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.activity.OrderActivity
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.OrderMyInfo
import com.gkzxhn.legalconsulting.entity.OrderReceiving
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.model.iml.OrderModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.OrderReceivingView
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

/**
 * @classname：抢单
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：
 */
class OrderReceivingPresenter(context: Context, view: OrderReceivingView) : BasePresenter<IOrderModel, OrderReceivingView>(context, OrderModel(), view) {

    fun getOrderReceiving(page: String, mCompositeSubscription: CompositeSubscription?) {
        mContext?.let {
            mCompositeSubscription?.add(mModel.getOrderReceiving(it, page, "10")
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderReceiving>(it) {
                        override fun success(t: OrderReceiving) {
                            mView?.offLoadMore()
                            mView?.setLastPage(t.last, t.number)
                            mView?.showNullView(t.content!!.isEmpty())
                            mView?.updateData(t.first, t.content)
                        }

                        override fun onError(t: Throwable?) {
                            super.onError(t)
                            mView?.offLoadMore()
                        }
                    }))

        }
    }

    fun acceptRushOrder(id: String, mCompositeSubscription: CompositeSubscription?) {
        mContext?.let {
            mCompositeSubscription?.add(mModel.acceptRushOrder(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            if (t.status == Constants.ORDER_STATE_ACCEPTED) {
                                mContext?.showToast("接单成功")
                                RxBus.instance.post(RxBusBean.HomePoint(true))

                                getOrderReceiving("0",mCompositeSubscription)
                                val intent = Intent(it, OrderActivity::class.java)
                                intent.putExtra("orderId", id)
                                intent.putExtra("orderState", 2)
                                it.startActivity(intent)
                            }
                        }
                    }))

        }
    }

}
