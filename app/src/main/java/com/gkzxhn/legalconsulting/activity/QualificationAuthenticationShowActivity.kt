package com.gkzxhn.legalconsulting.activity

import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.QualificationAuthenticationEditPresenter
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationEditView
import kotlinx.android.synthetic.main.default_top.iv_default_top_back as back
import kotlinx.android.synthetic.main.default_top.tv_default_top_title as topTitle

/**
 * Explanation: 资格认证信息展示页面
 * @author LSX
 *    -----2018/9/7
 */

class QualificationAuthenticationShowActivity : BaseActivity(), QualificationAuthenticationEditView {

    var mPresenter: QualificationAuthenticationEditPresenter? = null


    override fun provideContentViewId(): Int {
        return R.layout.activity_qualification_authentication_show
    }

    override fun init() {
        mPresenter = QualificationAuthenticationEditPresenter(this, this)
        topTitle.text = "资格认证"
        back.setOnClickListener {
            finish()
        }
    }

}
