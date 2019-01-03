package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.WithdrawThirdActivity
import com.gkzxhn.legalconsulting.entity.AlipayInfo
import com.gkzxhn.legalconsulting.model.IWithdrawModel
import com.gkzxhn.legalconsulting.model.iml.WithdrawModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.TsDialog
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.WithdrawView
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Explanation:
 * @author LSX
 *    -----2018/1/22
 */

class WithdrawPresenter(context: Context, view: WithdrawView) : BasePresenter<IWithdrawModel, WithdrawView>(context, WithdrawModel(), view) {

    fun sendCode() {
        if (mView?.getMoney()!!.isNotEmpty()) {
            mContext?.let {
                mModel.getCode(it, mView?.getPhone()!!)
                        .unsubscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : HttpObserver<Response<Void>>(it) {
                            override fun success(t: Response<Void>) {
                                mView?.startCountDown(60)
                                it.showToast(it.getString(R.string.have_send).toString())
                            }

                            override fun onError(t: Throwable?) {
                                mView?.stopCountDown()
                            }
                        })
            }
        } else {
            mContext?.showToast("请填写完成后操作！")
        }
    }

    fun getAlipayInfo() {
            mContext?.let {
                mModel.getAlipayInfo(it)
                        .unsubscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : HttpObserver<AlipayInfo>(it) {
                            override fun success(t: AlipayInfo) {
                                mView?.setPayInfo(t.nickName!!,t.avatar)
                            }

                        })
            }
    }

    fun withdraw() {
        if (mView?.getMoney()!!.isNotEmpty() && mView?.getCode()!!.isNotEmpty()) {
            /****** 服务器没做限制  会报错 所以这里加一个限制 保险 ******/
            if (mView?.getMoney()!!.toDouble() < 1) {
                mContext?.showToast("提现金额不能小于1")
                return
            }

            var map = LinkedHashMap<String, Any>()
            map["amount"] = mView?.getMoney()!!.toDouble()
            map["verificationCode"] = mView?.getCode().toString()
            var body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    Gson().toJson(map))
            mContext?.let {
                mModel.withdrawAli(it, body)
                        .unsubscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : HttpObserver<Response<Void>>(it) {
                            override fun success(t: Response<Void>) {
                                if (t.code() == 200) {
                                    it.showToast("提现成功")
                                    val intent = Intent(mContext, WithdrawThirdActivity::class.java)
                                    intent.putExtra("pay_type", 1)
                                    intent.putExtra("pay_Account", mView?.getName().toString())
                                    intent.putExtra("money", mView?.getMoney()!!.toDouble().toString())
                                    mContext?.startActivity(intent)
                                    mView?.onFinish()

                                } else if (t.code() == 400) {
                                    val errorBody = t.errorBody().string()
                                    val codeStr = JSONObject(errorBody).getString("code")
                                    when (codeStr) {
                                        "lawyer.CanNotWithdrawalBalanceInsufficient" -> {
                                            mContext?.TsDialog("余额不足，不能提现。", false)
                                        }
                                        "sms.verification-code.Error" -> {
                                            mContext?.TsDialog("验证码错误！", false)
                                        }
                                        else -> {
                                            mContext?.showToast("服务异常")
                                        }
                                    }
                                } else {
                                    mContext?.showToast(t.code().toString() + t.message())

                                }
                            }

                        })
            }
        } else {
            mContext?.showToast("请填写完成后操作！")
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/12 5:34 PM.
     * @description：
     */
    fun showDialog() {
        var dialog = android.app.Dialog(mContext)//可以在style中设定dialog的样式
        dialog.setContentView(R.layout.dialog_pay_type)
        var lp = dialog.window.attributes
        lp.gravity = Gravity.BOTTOM
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window.attributes = lp
        //设置该属性，dialog可以铺满屏幕
        dialog.window.setBackgroundDrawable(null)
        dialog.show()
        slideToUp(dialog.window.findViewById(R.id.cl_pay_type_dialog_all))

        val tvOne = dialog.findViewById<TextView>(R.id.tv_pay_type_dialog_one)
        val tvTwo = dialog.findViewById<TextView>(R.id.tv_pay_type_dialog_two)

        val ivBack = dialog.findViewById<ImageView>(R.id.iv_dialog_back)
        val ivOne = dialog.findViewById<ImageView>(R.id.iv_pay_type_one)
        val ivTwo = dialog.findViewById<ImageView>(R.id.iv_pay_type_two)


        val tvVerify = dialog.findViewById<TextView>(R.id.tv_pay_type_verify)
        tvOne.setOnClickListener {
            checkSelect(ivOne, ivTwo, 1)
            dialog.dismiss()
        }
        tvTwo.setOnClickListener {
            mContext!!.showToast("暂不支持微信,敬请期待")
            dialog.dismiss()
//            checkSelect(ivOne, ivTwo, 2)
        }
        tvVerify.setOnClickListener {
            dialog.dismiss()
        }
        ivBack.setOnClickListener {
            dialog.dismiss()
        }
    }

    var payType = 1

    private fun checkSelect(ivOne: ImageView, ivTwo: ImageView, i: Int) {
        ivOne.setImageResource(R.mipmap.ic_checked_false)
        ivTwo.setImageResource(R.mipmap.ic_checked_false)
        when (i) {
            1 -> {
                ivOne.setImageResource(R.mipmap.ic_checked_true)
                payType = 1
            }

            2 -> {
                ivTwo.setImageResource(R.mipmap.ic_checked_true)
                payType = 2
            }
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/12 5:34 PM.
     * @description：弹窗的动画
     */
    private fun slideToUp(view: View) {
        var slide = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f)

        slide.duration = 200
        slide.fillAfter = true
        slide.isFillEnabled = true
        view.startAnimation(slide)
    }


}