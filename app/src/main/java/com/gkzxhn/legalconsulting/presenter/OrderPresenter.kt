package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.ImInfo
import com.gkzxhn.legalconsulting.entity.OrderMyInfo
import com.gkzxhn.legalconsulting.entity.OrderRushInfo
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.model.iml.OrderModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.ImageUtils
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.StringUtils
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.OrderView
import com.netease.nim.uikit.api.NimUIKit
import rx.android.schedulers.AndroidSchedulers


/**
 * @classname：接单详情
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：
 */
class OrderPresenter(context: Context, view: OrderView) : BasePresenter<IOrderModel, OrderView>(context, OrderModel(), view) {

    /****** 客户的网易ID ******/
    var userName = ""

    /****** 1，获取 抢单的明细 ******/
    fun getOrderRushInfo(id: String) {
        mContext?.let {
            mModel.getOrderRushInfo(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderRushInfo>(it) {
                        override fun success(t: OrderRushInfo) {
                            mView?.setDescription(t.description!!)
                            mView?.setName(t.customer?.name!!)
                            mView?.setReward("￥" + t.reward)
                            mView?.setTime(StringUtils.parseDate(t.createdTime))
                            mView?.setOrderNumber(t.number.toString())
                            mView?.setNextText("抢单")
                            mView?.setOrderImage(t.customer?.avatarThumb)
                            mView?.setOrderState("已支付")
//                            mView?.setAllbgColor(App.mContext.resources.getColor(R.color.main_gary_bg))

                            if (t.attachments!!.isNotEmpty() && t.attachments != null) {
                                ImageUtils.base64ToBitmap("order_image1" + ".jpg", t.attachments!![0].thumb!!.toString())?.let { it1 -> mView?.setImage1(it1) }
                            }

                            if (t.attachments!!.size > 1 && t.attachments != null) {
                                ImageUtils.base64ToBitmap("order_image2" + ".jpg", t.attachments!![1].thumb!!.toString())?.let { it1 -> mView?.setImage2(it1) }
                            }
                            val str1 = ProjectUtils.categoriesConversion(t.category!!)
                            mView?.setOrderType(str1)
                        }
                    })
        }
    }

    /****** 2，获取 指定订单的明细 ******/
    fun getOrderMyInfo(id: String) {
        mContext?.let {
            mModel.getOrderMyInfo(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            initOrderInfo(t)
                        }
                    })
        }
    }

    private fun initOrderInfo(t: OrderMyInfo) {
        mView?.setDescription(t.description!!)
        mView?.setName(t.customer?.name!!)
        mView?.setReward("￥" + t.reward)
        mView?.setOrderState("已支付")
        mView?.setTime(StringUtils.parseDate(t.createdTime))
        mView?.setOrderNumber(t.number.toString())
        mView?.setOrderImage(t.customer?.avatarThumb)

        userName = t.customer?.username!!
        if (t.attachments!!.isNotEmpty() && t.attachments != null) {
            ImageUtils.base64ToBitmap("order_image1" + ".jpg", t.attachments!![0].thumb!!.toString())?.let { it1 -> mView?.setImage1(it1) }
        }
        if (t.attachments!!.size > 1 && t.attachments != null) {
            ImageUtils.base64ToBitmap("order_image2" + ".jpg", t.attachments!![1].thumb!!.toString())?.let { it1 -> mView?.setImage2(it1) }
        }

        if (t.type == Constants.ASSIGN) {
            /****** 指定单 ******/
            mView?.setReward("")
        }

        val str1 = ProjectUtils.categoriesConversion(t.category!!)
        mView?.setOrderType(str1)
        when (t.status) {
        /****** 待接单 ******/
            Constants.ORDER_STATE_PENDING_RECEIVING -> {
                mView?.setOrderState("待接单")
                mView?.setShowOrderInfo(View.GONE, "", "")
                mView?.setBottomSelectVisibility(View.VISIBLE)
            }
        /****** 已接单 ******/
            Constants.ORDER_STATE_ACCEPTED -> {
                mView?.setShowOrderInfo(View.VISIBLE, "接单时间：" + StringUtils.parseDate(t.acceptedTime), "已接单（" + t.lawyer?.name!! + "律师）")
                mView?.setBottomSelectVisibility(View.GONE)
                mView?.setNextText(App.mContext.resources.getString(R.string.send_message))
            }
        /****** 已完成 ******/
            Constants.ORDER_STATE_COMPLETE -> {
                mView?.setShowOrderInfo(View.GONE, "", "")
                mView?.setOrderState("已完成")
            }
        /******  已拒绝 ******/
            Constants.ORDER_STATE_REFUSED -> {
                mView?.setOrderState("已拒绝")
                mView?.setBottomSelectVisibility(View.GONE)
                mView?.setOrderStateColor(App.mContext.resources.getColor(R.color.order_red))
                mView?.setShowOrderInfo(View.GONE, "拒单时间" + StringUtils.parseDate(t.acceptedTime), "拒接单(" + t.lawyer?.name!!+ "律师）")
            }
        /******  已取消 ******/
            Constants.ORDER_STATE_CANCELLED -> {
                mView?.setOrderState("已取消")
                mView?.setShowOrderInfo(View.GONE, "", "")
                mView?.setOrderStateColor(App.mContext.resources.getColor(R.color.order_red))
            }
        /******  待付款 ******/
            Constants.ORDER_STATE_PENDING_PAYMENT -> {
                mView?.setOrderState("待付款")
                mView?.setBottomSelectVisibility(View.GONE)
                mView?.setShowOrderInfo(View.GONE, "", "")
                mView?.setOrderStateColor(App.mContext.resources.getColor(R.color.order_red))
            }
        /******  待审核 ******/
            Constants.ORDER_STATE_PENDING_APPROVAL -> {
                mView?.setOrderState("待审核")
                mView?.setShowOrderInfo(View.GONE, "", "")
                mView?.setOrderStateColor(App.mContext.resources.getColor(R.color.order_red))
            }
        }
    }

    /****** 抢单 ******/
    fun acceptRushOrder(id: String) {
        mContext?.let {
            mModel.acceptRushOrder(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            if (t.status == Constants.ORDER_STATE_ACCEPTED) {
                                mContext?.showToast("抢单成功")
                                RxBus.instance.post(RxBusBean.HomePoint(true))
                                initOrderInfo(t)
                            } else {
                                mContext?.showToast("抢单失败")
                            }
                        }
                    })
        }
    }

    /****** 接受订单 ******/
    fun acceptMyOrder(id: String) {
        val orderMoeny = mView?.getOrderMoeny()
        if (orderMoeny?.isEmpty()!!) {
            mContext?.showToast("请输入金额再操作")
            return
        }

        if (orderMoeny.startsWith(".")) {
            mContext?.showToast("该金额输入不合法")
            return
        }

        if (orderMoeny.toDouble() < 20) {
            mContext?.showToast("金额不能少于20")
            return
        }

        mContext?.let {
            mModel.acceptMyOrder(it, id,orderMoeny)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            if (t.status == Constants.ORDER_STATE_PENDING_PAYMENT) {
                                mContext?.showToast("接单成功")
                                RxBus.instance.post(RxBusBean.AcceptOrder(true))
                                initOrderInfo(t)
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
                                initOrderInfo(t)
                                RxBus.instance.post(RxBusBean.AcceptOrder(false))
                                mContext?.showToast("订单拒绝成功")
                            }
                        }
                    })
        }
    }

    fun sendMessage() {
        getImAccount(userName)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取网易信息
     */
    private fun getImAccount(userName: String) {
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

}