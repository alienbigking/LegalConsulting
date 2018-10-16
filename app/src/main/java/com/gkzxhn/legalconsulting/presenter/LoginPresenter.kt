package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.activity.MainActivity
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.model.ILoginModel
import com.gkzxhn.legalconsulting.model.iml.LoginModel
import com.gkzxhn.legalconsulting.utils.ObtainVersion
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.LoginView

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
            var intent = Intent(mContext, QualificationAuthenticationActivity::class.java)
            mContext?.startActivity(intent)
//        }
    }

    fun sendCode() {
        mContext?.showToast("验证码已发送")
        var intent = Intent(mContext, MainActivity::class.java)
        mContext?.startActivity(intent)
    }

}