package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.activity.OrderActivity
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.OrderDispose
import com.gkzxhn.legalconsulting.entity.OrderMyInfo
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.model.iml.OrderModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.OrderDisposeView
import rx.android.schedulers.AndroidSchedulers

/**
 * @classname：抢单
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：
 */
class OrderDisposePresenter(context: Context, view: OrderDisposeView) : BasePresenter<IOrderModel, OrderDisposeView>(context, OrderModel(), view) {

    fun getOrderDispose(page: String) {
        mContext?.let {
            mModel.getOrderDispose(it, page, "10")
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderDispose>(it) {
                        override fun success(t: OrderDispose) {
                            mView?.offLoadMore()
                            mView?.setLastPage(t.last, t.number)
                            if (t.content!!.isNotEmpty()) {
                                mView?.updateData(t.first, t.content)
                            }
                            mView?.showNullView(t.content!!.isEmpty(),"您还没有咨询订单")
                        }

                        override fun onError(t: Throwable?) {
                            super.onError(t)
                            mView?.offLoadMore()
                        }
                    })
        }
    }


    /****** 接受订单 ******/
    fun acceptMyOrder(id: String) {
        mContext?.let {
            mModel.acceptMyOrder(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            if (t.status == Constants.ORDER_STATE_ACCEPTED) {
                                mContext?.showToast("接单成功")
                                getOrderDispose("0")
                                val intent = Intent(it, OrderActivity::class.java)
                                intent.putExtra("orderId",id)
                                intent.putExtra("orderState", 2)
                                it.startActivity(intent)
                            }
                        }
                    })
        }
    }

    /****** 拒绝订单 ******/
    fun rejectMyOrder(id: String) {
        mContext?.let {
            mModel.rejectMyOrder(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            if (t.status == Constants.ORDER_STATE_REFUSED) {
                                mContext?.showToast("订单拒绝成功")
                                getOrderDispose("0")
                            }
                        }
                    })
        }
    }

}