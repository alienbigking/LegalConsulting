package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.QualificationAuthenticationShowPresenter
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationShowView
import kotlinx.android.synthetic.main.activity_qualification_authentication_show.*
import kotlinx.android.synthetic.main.default_top.iv_default_top_back as back
import kotlinx.android.synthetic.main.default_top.tv_default_top_title as topTitle

/**
 * Explanation: 资格认证信息展示页面
 * @author LSX
 *    -----2018/9/7
 */

class QualificationAuthenticationShowActivity : BaseActivity(), QualificationAuthenticationShowView {

    lateinit var mPresenter: QualificationAuthenticationShowPresenter


    override fun provideContentViewId(): Int {
        return R.layout.activity_qualification_authentication_show
    }

    override fun init() {
        mPresenter = QualificationAuthenticationShowPresenter(this, this)
        topTitle.text = "资格认证"
        ProjectUtils.addViewTouchChange(tv_qualification_authentication_show_send)
        back.setOnClickListener {
            finish()
        }

        tv_qualification_authentication_show_send.setOnClickListener {
            var intent = Intent(this, QualificationAuthenticationEditActivity::class.java)
            intent.putExtra("again_Authentication", true)
            startActivity(intent)
            finish()
        }

        mPresenter.getCertification()
    }


    override fun setName(string: String) {
        tv_qa_show_name.text = string
    }

    override fun setGender(string: String) {
        tv_qa_show_sex.text = if (string == "MALE") "男" else "女"
    }

    override fun setDescription(string: String) {
        tv_qualification_authentication_personal_profile_context.text = string
    }

    override fun setLawOffice(string: String) {
        tv_qa_show_lawOffice.text = string
    }

    override fun setAddress(string: String) {
        tv_qa_show_address.text = string
    }

    override fun setProfessional(string: String) {
        tv_qa_show_professional.text = string
    }

    override fun setYear(string: String) {
        tv_qa_show_year.text = string
    }

    override fun setImage1(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this, decodeFile, iv_qa_show_image1)
    }

    override fun setImage2(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this, decodeFile, iv_qa_show_image2)
    }

    override fun setImage3(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this, decodeFile, iv_qa_show_image3)
    }

    override fun setImage4(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this, decodeFile, iv_qa_show_image4)
    }


}
