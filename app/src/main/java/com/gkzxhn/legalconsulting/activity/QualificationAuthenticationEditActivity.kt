package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.Constants.REQUESTCODE_CHOOSE_MAJORS
import com.gkzxhn.legalconsulting.presenter.QualificationAuthenticationEditPresenter
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationEditView
import kotlinx.android.synthetic.main.activity_qualification_authentication_edit.*
import kotlinx.android.synthetic.main.activity_qualification_authentication_edit.tv_qualification_authentication_edit_send as send
import kotlinx.android.synthetic.main.default_top.iv_default_top_back as back
import kotlinx.android.synthetic.main.default_top.tv_default_top_title as topTitle

/**
 * Explanation: 资格认证信息填写页面
 * @author LSX
 *    -----2018/9/7
 */

class QualificationAuthenticationEditActivity : BaseActivity(), QualificationAuthenticationEditView {


    var mPresenter: QualificationAuthenticationEditPresenter? = null
    var selectString: ArrayList<String>? = arrayListOf()

    override fun provideContentViewId(): Int {
        return R.layout.activity_qualification_authentication_edit
    }

    override fun init() {
        mPresenter = QualificationAuthenticationEditPresenter(this, this)
        topTitle.text = "资格认证"
        back.setOnClickListener {
            finish()
        }

        send.setOnClickListener {

            mPresenter?.send()
        }
    }

    fun onClickQualificationAuthentication(view: View) {
        when (view.id) {
        /****** 专业领域 ******/
            R.id.v_qualification_authentication_professional_field_bg -> {
                val intent = Intent(this, ChooseMajorsActivity::class.java)
                intent.putStringArrayListExtra(Constants.INTENT_SELECTSTRING,selectString )
                startActivityForResult(intent, REQUESTCODE_CHOOSE_MAJORS)
            }
        /****** 律所地址 ******/
            R.id.v_qualification_authentication_address_bg -> {

            }
        /****** 地域 ******/
            R.id.v_qualification_authentication_territory_bg -> {

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUESTCODE_CHOOSE_MAJORS && resultCode == Constants.RESULTCODE_CHOOSE_MAJORS) {
            selectString = data?.getStringArrayListExtra(Constants.RESULT_CHOOSE_MAJORS)
            var professionalList = ""
            if (selectString != null && (selectString as java.util.ArrayList<String>).isNotEmpty()) {
                for (str: String in selectString as java.util.ArrayList<String>) {
                    professionalList = "$professionalList、$str"
                }
                tv_qualification_authentication_professional_list.text = professionalList.substring(1, professionalList.length)
            }
        }


    }

}
