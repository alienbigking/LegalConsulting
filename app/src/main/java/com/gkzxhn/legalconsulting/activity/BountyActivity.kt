package com.gkzxhn.legalconsulting.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.alipay.sdk.app.AuthTask
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.AuthResult
import com.gkzxhn.legalconsulting.utils.showToast
import kotlinx.android.synthetic.main.activity_bounty.*
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：BountyActivity
 * @author：liushaoxiang
 * @date：2018/10/11 1:39 PM
 * @description：我的赏金
 */
class BountyActivity : BaseActivity() {

    override fun provideContentViewId(): Int {
        return R.layout.activity_bounty
    }

    override fun init() {
        initTopTitle()
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "我的赏金"
        tv_bounty_money.text = App.SP.getString(Constants.SP_REWARDAMOUNT, "").toString()

        iv_default_top_back.setOnClickListener {
            finish()
        }
    }

    fun onClickBounty(view: View) {
        when (view.id) {
            R.id.v_bounty_get_money -> {
                startActivity(Intent(this, WithdrawFirstActivity::class.java))
                finish()
            }
            R.id.v_bounty_money_list -> {
                startActivity(Intent(this, MoneyListActivity::class.java))
            }
            R.id.v_bounty_get_alipay -> {
                bindAlipay()
            }
        }
    }

    private fun bindAlipay() {
        val authInfo = "ZqboZsfW079b5RU%2BaLyo0QVMGWCj6EIzz0vnrVemPuHbRGesPefRL1l2rMfdwG2YvVNw7%2B%2FgwCzpXbznHvUmms05EkCf3VQ%2Bw5SA5SIPp9E49eKzoaK1vYISkIrXr5FNcTm7w%2FV4Mf2TTT3yI5n%2BNxUvkbI8cRSQgeyS1atdLfw%3D"
        val authRunnable = Runnable {
            // 构造AuthTask 对象
            val authTask = AuthTask(this)
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
                        showToast("OK")
                    } else {
                        // 其他状态值则为授权失败
                        showToast("error")
                    }
                }
                else -> {
                }
            }
        }
    }

}