package com.gkzxhn.legalconsulting.activity

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.presenter.WithdrawPresenter
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.StringUtils
import com.gkzxhn.legalconsulting.view.WithdrawView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_withdraw.*
import kotlinx.android.synthetic.main.default_top.*
import java.util.concurrent.TimeUnit

/**
 * @classname：WithdrawActivity
 * @author：liushaoxiang
 * @date：2018/10/16 2:10 PM
 * @description：提现
 */

class WithdrawFirstActivity : BaseActivity(), WithdrawView {



    lateinit var mPresenter: WithdrawPresenter
    var timeDisposable: Disposable? = null      //倒计时任务

    override fun init() {
        initTopTitle()
        ProjectUtils.addViewTouchChange(tv_withdraw_send)
        mPresenter = WithdrawPresenter(this, this)
        mPresenter.getAlipayInfo()
        et_withdraw_1_money.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    tv_withdraw_1_money_end.visibility = View.GONE
                } else {
                    tv_withdraw_1_money_end.visibility = View.VISIBLE
                    if (s?.startsWith(".")!!) {
                        tv_withdraw_1_money_end.text = "该输入不合法"
                        return
                    }
                    val money = s.toString().toDouble() * 0.7
                    if (s.toString().toDouble() < 1) {
                        tv_withdraw_1_money_end.text = "提现金额不能小于1"
                    } else {
                        val format = StringUtils.formatStringTwo(money)
                        tv_withdraw_1_money_end.text = "实际到账$format"
                    }
                }
            }
        })
    }

    override fun provideContentViewId(): Int {
        return R.layout.activity_withdraw
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "提现"
        iv_default_top_back.setOnClickListener {
            finish()
        }

        val phone = App.SP.getString(Constants.SP_PHONE, "")
        tv_withdraw_top_title.text = "提现需要短信确认，验证码已发送至手机：${StringUtils.phoneChange(phone)}，轻按提示操作。"


    }

    fun onClickWithdraw(view: View) {
        when (view.id) {
            R.id.tv_withdraw_send -> {
                mPresenter.withdraw()
            }
            R.id.tv_withdraw_get_code -> {
                mPresenter.sendCode()
            }
        }
    }

    override fun getName(): String {
        return tv_withdraw_1_name.text.trim().toString()
    }

    override fun getMoney(): String {
        return et_withdraw_1_money.text.trim().toString()
    }

    override fun setPayInfo(name: String, avatar: String?) {
        tv_withdraw_1_name.text = name
        Glide.with(this).load(avatar)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(120)))
                .into(iv_withdraw_pay_ic)
    }

    override fun getCode(): String {
        return et_withdraw_1_code.text.trim().toString()
    }

    override fun getPhone(): String {
        return App.SP.getString(Constants.SP_PHONE, "")
    }

    override fun onFinish() {
        finish()
    }

    /**
     * 开始倒计时
     */
    @SuppressLint("SetTextI18n")
    override fun startCountDown(seconds: Int) {
        timeDisposable = Observable.interval(0, 1L, TimeUnit.SECONDS)
                .take(seconds + 1L)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    runOnUiThread {
                        if (it == 60L) {
                            tv_withdraw_get_code.text = resources.getString(R.string.get_verify)
                            tv_withdraw_get_code.setTextColor(resources.getColor(R.color.dark_blue))
                            tv_withdraw_get_code.isClickable = true
                        } else {
                            tv_withdraw_get_code.text = "${60L - it}s"
                            tv_withdraw_get_code.setTextColor(resources.getColor(R.color.text_gray))
                            tv_withdraw_get_code.isClickable = false

                        }
                    }
                }, {
                    //                    it.message!!.logE(this)
                })
    }

    /**
     * 停止倒计时
     */
    override fun stopCountDown() {
        if (timeDisposable != null) {
            if (!timeDisposable!!.isDisposed) {
                timeDisposable!!.dispose()
            }
        }
        tv_withdraw_get_code.isClickable = true
        tv_withdraw_get_code.text = getString(R.string.get_verify)
        tv_withdraw_get_code.setTextColor(resources.getColor(R.color.dark_blue))
    }

}