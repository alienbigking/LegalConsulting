package com.gkzxhn.legalconsulting.activity

import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.QualificationAuthenticationPresenter
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationView
import kotlinx.android.synthetic.main.activity_qualification_authentication.tv_qualification_authentication_go as qualificationAuthentication
import kotlinx.android.synthetic.main.activity_qualification_authentication.tv_qualification_authentication_message as qualificationAuthenticationMessage
import kotlinx.android.synthetic.main.default_top.iv_default_top_back as back
import kotlinx.android.synthetic.main.default_top.tv_default_top_title as top_title

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

        initTitleTop()

    }

    private fun initTitleTop() {
        back.setOnClickListener {
            finish()
        }
        top_title.text = "资格认证"

    }

    override fun changeMessage(string: String) {
        qualificationAuthenticationMessage.text = string
    }

    override fun changeQualificationAuthentication(string: String) {
        qualificationAuthentication.text = string
    }

    override fun changeQualificationAuthenticationVisibility(visibility: Int) {
        qualificationAuthentication.visibility = visibility

    }

}
