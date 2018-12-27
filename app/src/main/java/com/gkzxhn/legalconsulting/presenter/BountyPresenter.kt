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
        if (mView?.getsign()!!.isEmpty()) {
            mContext?.showToast("别为空")
            return
        }
        bindAlipay(mView?.getsign()!!)
//        mContext?.let {
//            mModel.getAlipaySign(it)
//                    .unsubscribeOn(AndroidSchedulers.mainThread())
//                    ?.observeOn(AndroidSchedulers.mainThread())
//                    ?.subscribe(object : HttpObserver<AlipaySign>(mContext!!) {
//                        override fun success(t: AlipaySign) {
////                            mContext?.showToast("sign:"+t.sign)
////                            bindAlipay(t.sign!!)
//                            bindAlipay("apiname=com.alipay.account.auth&;app_id=2018122562681680&;app_name=%E6%B3%95%E5%BE%8B%E5%92%A8%E8%AF%A2&;auth_type=LOGIN&;biz_type=%E6%94%AF%E4%BB%98%E5%AE%9D%E7%BB%91%E5%AE%9A&;method=alipay.open.auth.sdk.code.get&;pid=2088121417397335&;product_id=APP_FAST_LOGIN&;scope=auth_user&;sign=K8a14WkhEa0KeMMo4vBWnYafTTHRIfLSSDZ1zGDJdH5jB1PkmglQHYvc1Be7ehBIH6yFPxU6yDGmo%2FMLE57I6KWSPi4KLNPyIgrMAkdW1ASJVtg2KwNNRn4b2MFPbPXdq2VDs08xhM3CzKAv7C3rfEyuXPTS%2FpKXXjDKgHBGYZOqquuCGWw7Ydpqz4qVtiXsbKhOKDFFjWV2b%2FtpInS%2FXdonTp60uNF6BzhXx8m0aQhweMXKvOwQBEKftUuM8WjkQHsVdPSiNwxadSqTeRG9DroQxD3uZNUpXWAGmhldyegEuYt2YD%2FqdLjMtFfKDJ%2FSnt7C8qnaddlsT5S4voVhxg%3D%3D&;sign_type=RSA&;target_id=989989bd73a74850a10646045231b428")
//                        }
//                    })
//        }
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
                    val resultStatus = authResult.getResultStatus()
                    Log.e("xiaowu", authResult.toString() + "__" + resultStatus)
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        mContext?.showToast("成功" + authResult.authCode)
                        bingAlipay(authResult.authCode)
                    } else {
                        // 其他状态值则为授权失败
                        mContext?.showToast("失败代码：" + resultStatus)
                        bingAlipay(resultStatus)
                    }
                }
                else -> {

                }
            }
        }
    }


}