package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.text.TextUtils
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.model.ILoginModel
import com.gkzxhn.legalconsulting.model.iml.LoginModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.RetrofitClientLogin
import com.gkzxhn.legalconsulting.utils.ObtainVersion
import com.gkzxhn.legalconsulting.utils.TsDialog
import com.gkzxhn.legalconsulting.utils.getRequestMap
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.LoginView
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Explanation: 登录
 * @author LSX
 *    -----2018/1/22
 */

class LoginPresenter(context: Context, view: LoginView) : BasePresenter<ILoginModel, LoginView>(context, LoginModel(), view) {

    var time: String? = null
    var versionName: String? = null

    //构造方法
    init {
        versionName = ObtainVersion.getVersionName(App.mContext)
    }

    fun login() {
//        if (mView?.getCode()?.isEmpty()!! || mView?.getPhone()?.isEmpty()!!) {
//            mContext?.showToast("请填写完成后操作！")
//        } else if (!StringUtils.isMobileNO(mView?.getPhone())) {
//            mContext?.showToast("手机号格式不正确")
//        } else {
//            mContext?.showToast("登录中，请稍候。。")
//            var intent = Intent(mContext, QualificationAuthenticationActivity::class.java)
//            mContext?.startActivity(intent)
//        }
        requestLogin()


    }


    fun sendCode() {
//        mContext?.showToast("验证码已发送")
//        var intent = Intent(mContext, MainActivity::class.java)
//        mContext?.startActivity(intent)

        mContext?.let {
            RetrofitClientLogin.Companion.getInstance(it).mApi
                    ?.getCode(mView?.getPhone()!!)
                    ?.subscribeOn(Schedulers.io())
                    ?.unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<Response<Void>>(it) {
                        override fun success(t: Response<Void>) {
                            if (t.code() == 201) {
                                it.showToast(it.getString(R.string.have_send).toString())
                            } else {
                                it.TsDialog(it.getString(R.string.send_failed).toString(), false)
                            }
                        }

                        override fun onError(t: Throwable?) {

                        }
                    })
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
        getRequestMap(mContext!!, map)
        var create = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(map))
        RetrofitClientLogin.Companion.getInstance(mContext!!).mApi
                ?.login(create)
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
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
                                //注册失败
                                var errorString: String? = null
                                try {
                                    errorString = JSONObject(t.errorBody().string()).getString("message")
                                } catch (e: Exception) {
                                }
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
        var map = LinkedHashMap<String, String>()
        getRequestMap(mContext!!, map)
        RetrofitClientLogin.Companion.getInstance(mContext!!)
                .mApi?.getToken("password", phoneNumber, code)
                ?.subscribeOn(Schedulers.io())
//                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<ResponseBody>(mContext!!) {
                    override fun success(t: ResponseBody) {
                        var string = t.string()
                        if (!TextUtils.isEmpty(string)) {
                            var token: String? = null
                            var refreshToken: String? = null
                            try {
                                token = JSONObject(string).getString("access_token")
                                refreshToken = JSONObject(string).getString("refresh_token")
                            } catch (e: Exception) {

                            }
                            App.EDIT?.putString(Constants.SP_TOKEN, token)?.commit()
                            App.EDIT?.putString("refreshToken", refreshToken)?.commit()
                            getLawyersInfo()

                            mContext?.showToast(token.toString())
                        }
                    }


                    override fun onError(t: Throwable?) {

                    }
                })
    }


    private fun getLawyersInfo() {
        RetrofitClient.getInstance(mContext!!).mApi?.getLawyersInfo()
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<LawyersInfo>(mContext!!) {
                    override fun success(date: LawyersInfo) {

                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                    }
                })

    }


}