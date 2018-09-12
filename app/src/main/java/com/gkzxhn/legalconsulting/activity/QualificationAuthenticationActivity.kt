package com.gkzxhn.legalconsulting.activity

import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.QualificationAuthenticationPresenter
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationView
import kotlinx.android.synthetic.main.activity_qualification_authentication.tv_qualification_authentication_go as qualificationAuthentication
import kotlinx.android.synthetic.main.activity_qualification_authentication.tv_qualification_authentication_message as qualificationAuthenticationMessage
import kotlinx.android.synthetic.main.default_top.iv_default_top_back as back

/**
 * Explanation: 资格认证
 * @author LSX
 *    -----2018/9/6
 */

class QualificationAuthenticationActivity : BaseActivity(), QualificationAuthenticationView {


    var mPresenter: QualificationAuthenticationPresenter? = null


    override fun provideContentViewId(): Int {
        return R.layout.activity_qualification_authentication
    }

    override fun init() {
        mPresenter = QualificationAuthenticationPresenter(this, this)
        qualificationAuthentication.setOnClickListener {
            mPresenter?.qualificationAuthentication()
        }

        back.setOnClickListener {
            finish()
        }
    }

    override fun changeMessage(message: String) {
        qualificationAuthenticationMessage.text = message
    }

    override fun changeQualificationAuthentication(message: String) {
        qualificationAuthentication.text = message
    }

    override fun changeQualificationAuthenticationVisibility(visibility: Int) {
        qualificationAuthentication.visibility = visibility
    }

}
