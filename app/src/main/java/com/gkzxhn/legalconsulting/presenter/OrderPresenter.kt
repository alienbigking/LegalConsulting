package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.OrderMyInfo
import com.gkzxhn.legalconsulting.entity.OrderRushInfo
import com.gkzxhn.legalconsulting.model.IOrderModel
import com.gkzxhn.legalconsulting.model.iml.OrderModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.ImageUtils
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.StringUtils
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.OrderView
import rx.android.schedulers.AndroidSchedulers


/**
 * @classname：接单详情
 * @author：liushaoxiang
 * @date：2018/11/7 2:46 PM
 * @description：
 */
class OrderPresenter(context: Context, view: OrderView) : BasePresenter<IOrderModel, OrderView>(context, OrderModel(), view) {

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
                            mView?.setNextText("抢单")

                            mView?.setOrderState("已支付")
                            mView?.setAllbgColor(App.mContext.resources.getColor(R.color.main_gary_bg))


                            if (t.attachments!!.isNotEmpty() &&t.attachments!=null) {
                                ImageUtils.base64ToBitmap("order_image1" + ".jpg", t.attachments!![0].thumb!!.toString())?.let { it1 -> mView?.setImage1(it1) }
                            }

                            if (t.attachments!!.size > 1&&t.attachments!=null) {
                                ImageUtils.base64ToBitmap("order_image2" + ".jpg", t.attachments!![1].thumb!!.toString())?.let { it1 -> mView?.setImage2(it1) }
                            }
                            val str1 = ProjectUtils.categoriesConversion(t.categories!![0])
                            val str2 = ProjectUtils.categoriesConversion(if (t.categories!!.size > 1) t.categories!![1] else "")
                            val str3 = ProjectUtils.categoriesConversion(if (t.categories!!.size > 2) t.categories!![2] else "")
                            mView?.setOrderType(str1, str2, str3)
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

        if (t.attachments!!.isNotEmpty() &&t.attachments!=null) {
            ImageUtils.base64ToBitmap("order_image1" + ".jpg", t.attachments!![0].thumb!!.toString())?.let { it1 -> mView?.setImage1(it1) }
        }
        if (t.attachments!!.size > 1&&t.attachments!=null) {
            ImageUtils.base64ToBitmap("order_image2" + ".jpg", t.attachments!![1].thumb!!.toString())?.let { it1 -> mView?.setImage2(it1) }
        }

        val str1 = ProjectUtils.categoriesConversion(t.categories!![0])
        val str2 = ProjectUtils.categoriesConversion(if (t.categories!!.size > 1) t.categories!![1] else "")
        val str3 = ProjectUtils.categoriesConversion(if (t.categories!!.size > 2) t.categories!![2] else "")
        mView?.setOrderType(str1, str2, str3)
        when (t.status) {
        /****** 待接单 ******/
            Constants.ORDER_STATE_PENDING_RECEIVING -> {
                mView?.setShowOrderInfo(View.GONE, "", "")
                mView?.setBottomSelectVisibility(View.VISIBLE)
            }
        /****** 已接单 ******/
            Constants.ORDER_STATE_ACCEPTED -> {
                mView?.setShowOrderInfo(View.VISIBLE, "接单时间：" + StringUtils.parseDate(t.acceptedTime), "已接单（" + t.lawyer?.name!! + "律师）")
                mView?.setNextText(App.mContext.resources.getString(R.string.send_message))
            }
        /****** 已完成 ******/
            Constants.ORDER_STATE_COMPLETE -> {
                mView?.setShowOrderInfo(View.GONE, "", "")
                mView?.setOrderState("已完成")
            }
        /******  已拒绝 ******/
            Constants.ORDER_STATE_REFUSED -> {
                mView?.setOrderState("已退款")
                mView?.setOrderStateColor(App.mContext.resources.getColor(R.color.order_red))
                mView?.setShowOrderInfo(View.VISIBLE, "拒单时间" + StringUtils.parseDate(t.acceptedTime), "拒接单" + t.lawyer?.name!!)

            }
        /******  已取消 ******/
            Constants.ORDER_STATE_CANCELLED -> {
                mView?.setOrderState("已取消")
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
        mContext?.let {
            mModel.acceptMyOrder(it, id)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<OrderMyInfo>(it) {
                        override fun success(t: OrderMyInfo) {
                            if (t.status == Constants.ORDER_STATE_ACCEPTED) {
                                mContext?.showToast("接单成功")
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
                                mContext?.showToast("订单拒绝成功")
                            }
                        }
                    })
        }
    }

    fun sendMessage() {
        mContext?.showToast("限时通讯开发中")
    }

}