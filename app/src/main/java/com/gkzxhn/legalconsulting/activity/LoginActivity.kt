package com.gkzxhn.legalconsulting.activity

import android.view.WindowManager
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.LoginPresenter
import com.gkzxhn.legalconsulting.view.LoginView
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

    var mPresenter: LoginPresenter? = null

    override fun getPhone(): String {
        return loginPhone.text.toString().trim()
    }

    override fun getCode(): String {
        return code.text.toString().trim()
    }

    override fun provideContentViewId(): Int {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return R.layout.activity_login
    }


    override fun init() {
        mPresenter = LoginPresenter(this, this)

        login.setOnClickListener {
            mPresenter?.login()
        }

        sendCode.setOnClickListener {

            mPresenter?.sendCode()
        }
    }


}
