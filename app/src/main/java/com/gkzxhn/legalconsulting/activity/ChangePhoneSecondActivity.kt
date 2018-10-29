package com.gkzxhn.legalconsulting.activity

import android.annotation.SuppressLint
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.PhoneChangePresenter
import com.gkzxhn.legalconsulting.view.PhoneChangeView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_phone_2.*
import java.util.concurrent.TimeUnit

/**
 * @classname：ChangePhoneFirstActivity
 * @author：liushaoxiang
 * @date：2018/10/22 5:36 PM
 * @description：
 */

class ChangePhoneSecondActivity : BaseActivity(), PhoneChangeView {


    var timeDisposable: Disposable? = null      //倒计时任务
    private var sendClick: Boolean = false  //是否已经点击发送验证码
    lateinit var mPresenter: PhoneChangePresenter

    override fun init() {
        mPresenter = PhoneChangePresenter(this, this)
    }

    override fun provideContentViewId(): Int {
        return R.layout.activity_change_phone_2
    }

    override fun getPhone(): String {
        return et_change_phone_number.text.trim().toString()
    }

    override fun getCode(): String {
        return et_change_phone.text.trim().toString()
    }

    override fun onFinish() {
        finish()
    }

    fun onClickChangPhone(view: View) {
        when (view.id) {
        /****** 返回 ******/
            R.id.iv_change_phone_back -> {
                finish()
            }
        /****** 下一步 ******/
            R.id.tv_change_phone_next -> {
                mPresenter.updatePhoneNumber()
            }
        /****** 发送验证码 ******/
            R.id.tv_change_phone_code_send -> {
                mPresenter.sendCode()
            }

        }
    }

    /**
     * 开始倒计时
     */
    @SuppressLint("SetTextI18n")
    override fun startCountDown(seconds: Int) {
        sendClick = true
        timeDisposable = Observable.interval(0, 1L, TimeUnit.SECONDS)
                .take(seconds + 1L)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    runOnUiThread {
                        if (it == 60L) {
                            tv_change_phone_code_send.text = resources.getString(R.string.get_verify)
                            tv_change_phone_code_send.setTextColor(resources.getColor(R.color.dark_blue))
                            tv_change_phone_code_send.isClickable = true
                        } else {
                            tv_change_phone_code_send.text = "${60L - it}s"
                            tv_change_phone_code_send.setTextColor(resources.getColor(R.color.text_gray))
                            tv_change_phone_code_send.isClickable = false

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
        sendClick = false
        if (timeDisposable != null) {
            if (!timeDisposable!!.isDisposed) {
                timeDisposable!!.dispose()
            }
        }
        tv_change_phone_code_send.isClickable = true
        tv_change_phone_code_send.text = getString(R.string.get_verify)
        tv_change_phone_code_send.setTextColor(resources.getColor(R.color.dark_blue))
    }

}