package com.gkzxhn.legalconsulting.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.UpdateInfo
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.error_exception.ApiException
import com.gkzxhn.legalconsulting.presenter.LoginPresenter
import com.gkzxhn.legalconsulting.utils.*
import com.gkzxhn.legalconsulting.view.LoginView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_ts.*
import retrofit2.adapter.rxjava.HttpException
import rx.android.schedulers.AndroidSchedulers
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.activity_login.et_login_code as code
import kotlinx.android.synthetic.main.activity_login.et_login_phone as loginPhone
import kotlinx.android.synthetic.main.activity_login.tv_login_get_code as sendCode
import kotlinx.android.synthetic.main.activity_login.tv_login_login as login

/**
 * Explanation: 登录页
 * @author LSX
 *    -----2018/9/6
 */

class LoginActivity : BaseActivity(), LoginView {

    override fun onFinish() {
        finish()
    }


    var timeDisposable: Disposable? = null      //倒计时任务
    private var sendClick: Boolean = false  //是否已经点击发送验证码


    var mPresenter: LoginPresenter? = null

    override fun getPhone(): String {
        return loginPhone.text.toString().trim()
    }

    override fun getCode(): String {
        return code.text.toString().trim()
    }

    override fun getRememberState(): Boolean {
        return cb_remember.isChecked
    }


    override fun provideContentViewId(): Int {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return R.layout.activity_login
    }


    override fun init() {
        mPresenter = LoginPresenter(this, this)

        val phone = App.SP.getString(Constants.SP_REMEMBER_PHONE, "")
        Log.e("xiaowu", "phone" + phone)

        if (phone.isNotEmpty()) {
            loginPhone.setText(phone)
            /****** 获取光标到验证码一栏 ******/
            code.requestFocus()
        }

        updateApp()
        login.setOnClickListener {
            mPresenter?.login()
        }

        sendCode.setOnClickListener {
            mPresenter?.sendCode()
        }
        ProjectUtils.addViewTouchChange(login)
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
                            sendCode.text = resources.getString(R.string.get_verify)
                            sendCode.setTextColor(resources.getColor(R.color.dark_blue))
                            sendCode.isClickable = true
                        } else {
                            sendCode.text = "${60L - it}s"
                            sendCode.setTextColor(resources.getColor(R.color.text_gray))
                            sendCode.isClickable = false

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
        sendCode.isClickable = true
        sendCode.text = getString(R.string.get_verify)
        sendCode.setTextColor(resources.getColor(R.color.dark_blue))
    }


    /**
     * @methodName： created by liushaoxiang on 2018/11/6 4:09 PM.
     * @description：检查更新
     */
    private fun updateApp() {
        RetrofitClient.getInstance(this).mApi?.updateApp()
                ?.subscribeOn(rx.schedulers.Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<UpdateInfo>(this) {
                    override fun success(t: UpdateInfo) {
                        val versionCode = ObtainVersion.getVersionCode(App.mContext)
                        if (t.number!! > versionCode) {
                            showDownloadDialog(t)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        loadDialog?.dismiss()
                        when (e) {
                            is ConnectException -> TsDialog("服务器异常，请重试", false)
                            is HttpException -> {
                                when {
                                    e.code() == 401 -> TsClickDialog("登录已过期", false).dialog_save.setOnClickListener {
                                        App.EDIT.putString(Constants.SP_TOKEN, "")?.commit()
                                        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                    }
                                    e.code() == 404 -> {
                                        /****** 不处理 ******/
                                    }
                                    else -> TsDialog("服务器异常，请重试", false)
                                }
                            }
                            is IOException -> TsDialog("数据加载失败，请检查您的网络", false)
                        //后台返回的message
                            is ApiException -> {
                                TsDialog(e.message!!, false)
                                Log.e("ApiErrorHelper", e.message, e)
                            }
                            else -> {
                                showToast("数据异常")
                                Log.e("ApiErrorHelper", e?.message, e)
                            }
                        }
                    }
                })
    }

}
