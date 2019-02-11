package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.entity.ImInfo
import com.gkzxhn.legalconsulting.entity.OrderDispose
import com.gkzxhn.legalconsulting.entity.VideoDuration
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.model.iml.OrderModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.TsDialog
import com.gkzxhn.legalconsulting.view.OrderDisposeView
import com.netease.nim.uikit.api.NimUIKit
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

/**
 * @classname：我的订单
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：
 */
class OrderDisposePresenter(context: Context, view: OrderDisposeView) : BasePresenter<IOrderModel, OrderDisposeView>(context, OrderModel(), view) {

    fun getOrderDispose(page: String, mCompositeSubscription: CompositeSubscription?) {
        mContext?.let {
            mCompositeSubscription?.add(mModel.getOrderDispose(it, page, "10")
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderDispose>(it) {
                        override fun success(t: OrderDispose) {
                            mView?.offLoadMore()
                            mView?.setLastPage(t.last, t.number)
                            mView?.updateData(t.first, t.content)

                            mView?.showNullView(t.content!!.isEmpty(), "您还没有咨询订单")

                            if (t.content!!.isNotEmpty()) {
                                App.EDIT.putString("OrderId", t.content!![0].id).commit()
                            }

                        }

                        override fun onError(t: Throwable?) {
                            super.onError(t)
                            mView?.offLoadMore()
                        }
                    }))
        }
    }


    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取网易信息
     */
    fun getImAccount(userName: String) {
        mContext?.let {
            mModel.getImAccount(it, userName)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<ImInfo>(mContext!!) {
                        override fun success(t: ImInfo) {
                            NimUIKit.startP2PSession(mContext, t.account)
                            NimUIKit.setMsgForwardFilter { false }
                            NimUIKit.setMsgRevokeFilter { false }
                        }
                    })
        }

    }

    fun getVideoDuration(id:String,userName: String) {
        mContext?.let {
            mModel.getVideoDuration(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<VideoDuration>(mContext!!) {
                        override fun success(t: VideoDuration) {
                            if (t.videoDuration!! <= 0) {
                                mContext?.TsDialog("视频通话时长已用完", false)
                            }else{
                                getImAccount(userName)
                            }

                        }
                    })
        }
    }

}