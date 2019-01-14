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

    override fun onFinish() {
        finish()
    }

    lateinit var mPresenter: QualificationAuthenticationPresenter

    override fun provideContentViewId(): Int {
        return R.layout.activity_qualification_authentication
    }

    override fun init() {
        mPresenter = QualificationAuthenticationPresenter(this, this)

        qualificationAuthentication.setOnClickListener {
            mPresenter.qualificationAuthentication()
        }

        initTitleTop()
        /****** 处理初始UI显示 ******/
        mPresenter.loadUISetting()
    }

    private fun initTitleTop() {
        back.setOnClickListener {
            finish()
        }
        top_title.text = "资格认证"
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/31 3:29 PM.
     * @description：改变文字说明
     */
    override fun changeMessage(string: String) {
        qualificationAuthenticationMessage.text = string
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/31 3:28 PM.
     * @description：改变下一步的按扭字符
     */
    override fun changeQualificationAuthentication(string: String) {
        qualificationAuthentication.text = string
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/31 3:28 PM.
     * @description：是否显示下一步的按扭
     */
    override fun changeQualificationAuthenticationVisibility(visibility: Int) {
        qualificationAuthentication.visibility = visibility
    }

}
