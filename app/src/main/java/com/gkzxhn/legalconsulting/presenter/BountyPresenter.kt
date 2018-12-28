package com.gkzxhn.legalconsulting.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.AuthTask
import com.gkzxhn.legalconsulting.activity.WithdrawFirstActivity
import com.gkzxhn.legalconsulting.entity.AlipaySign
import com.gkzxhn.legalconsulting.entity.AuthResult
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.model.IBountyModel
import com.gkzxhn.legalconsulting.model.iml.BountyModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.BountyView
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers

/**
 * Explanation: 赏金
 * @author LSX
 *    -----2018/1/22
 */

class BountyPresenter(context: Context, view: BountyView) : BasePresenter<IBountyModel, BountyView>(context, BountyModel(), view) {

    var isBind: Boolean = true
    /**
     * @methodName： created by liushaoxiang on 2018/12/27 9:30 AM.
     * @description：
     */
    fun getAlipaySign() {
        if (isBind) {
            unbingAlipay()
        } else {
            mContext?.let {
                mModel.getAlipaySign(it)
                        .unsubscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : HttpObserver<AlipaySign>(mContext!!) {
                            override fun success(t: AlipaySign) {
                                bindAlipay(t.sign!!)
                            }
                        })
            }
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/12/27 9:30 AM.
     * @description：绑定支付宝
     */
    fun bingAlipay(authCode: String) {
        mContext?.let {
            mModel.bingAlipay(it, authCode)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<Response<Void>>(mContext!!) {
                        override fun success(t: Response<Void>) {
                            if (t.code() == 204) {
                                mContext?.showToast("绑定成功")
                                getLawyersInfo()
                            } else {
                                mContext?.showToast("服务器异常 code:" + t.code())
                            }
                        }
                    })
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/12/27 9:30 AM.
     * @description：解绑支付宝
     */
    fun unbingAlipay() {
        mContext?.let {
            mModel.unbingAlipay(it)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<Response<Void>>(mContext!!) {
                        override fun success(t: Response<Void>) {
                            if (t.code() == 204) {
                                mContext?.showToast("解绑成功")
                                getLawyersInfo()
                            } else {
                                mContext?.showToast("服务器异常 code:" + t.code())
                            }
                        }
                    })
        }
    }


    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取律师信息
     */
    fun getLawyersInfo() {
        mContext?.let {
            mModel.getLawyersInfo(it)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<LawyersInfo>(mContext!!) {
                        override fun success(t: LawyersInfo) {
                            isBind = t.alipayBind!!
                            mView?.changeBingState(isBind)
                            mView?.setMoney(t.rewardAmount.toString())
                        }
                    })
        }
    }

    fun bindAlipay(authInfo: String) {
        val authRunnable = Runnable {
            // 构造AuthTask 对象
            val authTask = AuthTask(mView?.getActivity())
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = 100
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }

    fun getMoney() {
        if (isBind) {
            mView?.getActivity()?.startActivity(Intent(mView?.getActivity(), WithdrawFirstActivity::class.java))
        } else {
            mContext?.showToast("请先绑定支付宝")
        }
    }

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                100 -> {
                    val authResult = AuthResult(msg.obj as Map<String, String>, true)
                    val resultStatus = authResult.resultStatus
                    Log.e("xiaowu", authResult.toString())
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        bingAlipay(authResult.authCode)
                    } else {
                        // 其他状态值则为授权失败
                        mContext?.showToast("授权失败：$resultStatus")
                    }
                }
                else -> {

                }
            }
        }
    }
}