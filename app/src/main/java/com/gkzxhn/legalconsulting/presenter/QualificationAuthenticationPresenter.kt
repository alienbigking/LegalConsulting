package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationEditActivity
import com.gkzxhn.legalconsulting.model.iml.BaseModel
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationView

/**
 * Explanation: 认证页面状态显示界面
 * @author LSX
 *    -----2018/1/22
 */

class QualificationAuthenticationPresenter (context: Context, view: QualificationAuthenticationView) : BasePresenter<BaseModel, QualificationAuthenticationView>(context, BaseModel(), view) {

    /**
     * @methodName： created by PrivateXiaoWu on 2018/9/7 9:58.
     * @description：开始认证或者重新认证
     */
    fun qualificationAuthentication() {
        mContext?.showToast("开始认证或者重新认证")
        var intent = Intent(mContext, QualificationAuthenticationEditActivity::class.java)
        mContext?.startActivity(intent)
    }


}