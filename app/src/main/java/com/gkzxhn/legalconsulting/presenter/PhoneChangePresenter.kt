package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.ChangePhoneSecondActivity
import com.gkzxhn.legalconsulting.model.IPhoneChangeModel
import com.gkzxhn.legalconsulting.model.iml.PhoneChangeModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.StringUtils
import com.gkzxhn.legalconsulting.utils.TsDialog
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.PhoneChangeView
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Explanation: 更换电话号码
 * @author LSX
 *    -----2018/1/22
 */

class PhoneChangePresenter(context: Context, view: PhoneChangeView) : BasePresenter<IPhoneChangeModel, PhoneChangeView>(context, PhoneChangeModel(), view) {


    fun login() {
        if (mView?.getCode()?.isEmpty()!!) {
            mContext?.showToast("请填写完成后操作！")
        } else if (!StringUtils.isMobileNO(mView?.getPhone()!!)) {
            mContext?.showToast("手机号格式不正确")
        } else {
            requestLogin()
        }
    }


    fun sendCode() {
        if (!StringUtils.isMobileNO(mView?.getPhone()!!)) {
            mContext?.showToast("手机号格式不正确")
            return
        }
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
                            }
                            400 -> {
                                try {
                                    val errorBody = t.errorBody().string()
                                    when (JSONObject(errorBody).getString("code")) {
                                        "sms.verification-code.NotMatched" -> {
                                            mContext?.TsDialog(mContext?.getString(R.string.verify_number_error).toString(), false)

                                        }
                                        "user.Existed" -> {
                                            /****** 已存在账号  表示验证通过  跳转下一步 ******/
                                            mContext?.startActivity(Intent(mContext, ChangePhoneSecondActivity::class.java))
                                            mView?.onFinish()
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
     * @methodName： created by liushaoxiang on 2018/10/29 10:57 AM.
     * @description：修改手机号
     */
    fun updatePhoneNumber() {
        if (mView?.getCode()?.isEmpty()!!) {
            mContext?.showToast("请填写完成后操作！")
            return
        } else if (!StringUtils.isMobileNO(mView?.getPhone()!!)) {
            mContext?.showToast("手机号格式不正确")
            return
        }
        var map = LinkedHashMap<String, String>()
        map["phoneNumber"] = mView?.getPhone().toString()
        map["verificationCode"] = mView?.getCode().toString()
        var body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(map))
        mModel.updatePhoneNumber(mContext!!, body)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<Response<Void>>(mContext!!) {
                    override fun success(t: Response<Void>) {
                        when (t.code()) {
                            204 -> {
                                mContext?.showToast("更换成功")
                                mView?.onFinish()
                            }
                            else -> {
                                mContext?.showToast(t.code().toString()+t.errorBody())
                            }
                        }
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        mContext?.showToast(t?.message.toString())
                    }
                }
                )

    }


}