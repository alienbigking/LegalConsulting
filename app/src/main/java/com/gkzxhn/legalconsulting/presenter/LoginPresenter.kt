package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.MainActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.model.ILoginModel
import com.gkzxhn.legalconsulting.model.iml.LoginModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.StringUtils
import com.gkzxhn.legalconsulting.utils.TsDialog
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.LoginView
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Explanation: 登录
 * @author LSX
 *    -----2018/1/22
 */

class LoginPresenter(context: Context, view: LoginView) : BasePresenter<ILoginModel, LoginView>(context, LoginModel(), view) {

    fun login() {
        if (mView?.getCode()?.isEmpty()!! || mView?.getPhone()?.isEmpty()!!) {
            mContext?.showToast("请填写完成后操作！")
        } else if (!StringUtils.isMobileNO(mView?.getPhone())) {
            mContext?.showToast("手机号格式不正确")
        } else {
            requestLogin()
        }
    }


    fun sendCode() {
        if (!StringUtils.isMobileNO(mView?.getPhone())) {
            mContext?.showToast("手机号格式不正确")
        } else {
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

        }

    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/19 11:50 AM.
     * @description：登录
     */
    private fun requestLogin() {
        var map = LinkedHashMap<String, String>()
        map["phoneNumber"] = mView?.getPhone().toString()
        map["verificationCode"] = mView?.getCode().toString()
        map["name"] = mView?.getPhone().toString()
        map["group"] = "LAWYER"
        var body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(map))
        mModel.login(mContext!!, body)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<Response<Void>>(mContext!!) {
                    override fun success(t: Response<Void>) {
                        when (t.code()) {
                            201 -> {
                                //注册成功 sms.verification-code.NotMatched user.Existed
                                getToken(map["phoneNumber"].toString(), map["verificationCode"].toString())
                            }
                            400 -> {
                                try {
                                    val errorBody = t.errorBody().string()
                                    when (JSONObject(errorBody).getString("code")) {
                                        "sms.verification-code.NotMatched" -> {
                                            mContext?.TsDialog(mContext?.getString(R.string.verify_number_error).toString(), false)

                                        }
                                        "user.Existed" -> {
                                            getToken(map["phoneNumber"].toString(), map["verificationCode"].toString())

                                        }
                                    //user.password.NotMatched=账号密码不匹配。
                                        "user.password.NotMatched" -> {
                                            mContext?.TsDialog(mContext?.getString(R.string.password_error).toString(), false)

                                        }
                                        else -> {

                                        }
                                    }
                                } catch (e: Exception) {
                                }
                            }
                            else -> {

                            }
                        }
                    }
                }
                )
    }


    /**
     * @methodName： created by liushaoxiang on 2018/10/19 11:51 AM.
     * @description：获取Token
     */
    private fun getToken(phoneNumber: String, code: String) {
        mContext?.let {
            mModel.getToken(it, phoneNumber, code)?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<ResponseBody>(it) {
                        override fun success(t: ResponseBody) {
                            val string = t.string()
                            if (!TextUtils.isEmpty(string)) {
                                var token: String? = null
                                var refreshToken: String? = null
                                try {
                                    token = JSONObject(string).getString("access_token")
                                    refreshToken = JSONObject(string).getString("refresh_token")
                                } catch (e: Exception) {

                                }
                                App.EDIT.putString(Constants.SP_TOKEN, token)?.commit()
                                App.EDIT.putString("refreshToken", refreshToken)?.commit()
                                getLawyersInfo()

                            }
                        }
                    })
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取律师信息
     */
    private fun getLawyersInfo() {
        mContext?.let {
            mModel.getLawyersInfo(it)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<LawyersInfo>(mContext!!) {
                        override fun success(date: LawyersInfo) {
                            App.EDIT.putString(Constants.SP_CERTIFICATIONSTATUS, date.certificationStatus)?.commit()
                            App.EDIT.putString(Constants.SP_PHONE, date.phoneNumber)?.commit()
                            App.EDIT.putString(Constants.SP_NAME, date.name)?.commit()
                            App.EDIT.putString(Constants.SP_LAWOFFICE, date.lawOffice)?.commit()

                            mContext?.startActivity(Intent(mContext, MainActivity::class.java))
                            mView?.onFinish()
                        }

                        override fun onError(t: Throwable?) {
                            super.onError(t)
                            mContext?.startActivity(Intent(mContext, MainActivity::class.java))
                            mView?.onFinish()
                        }
                    })
        }

    }

}