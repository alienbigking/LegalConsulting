package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationEditActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.model.IQualificationAuthenticationModel
import com.gkzxhn.legalconsulting.model.iml.QualificationAuthenticationModel
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationView

/**
 * Explanation: 认证页面状态显示界面
 * @author LSX
 *    -----2018/1/22
 */

class QualificationAuthenticationPresenter(context: Context, view: QualificationAuthenticationView) : BasePresenter<IQualificationAuthenticationModel, QualificationAuthenticationView>(context, QualificationAuthenticationModel(), view) {

    /**
     * @methodName： created by PrivateXiaoWu on 2018/9/7 9:58.
     * @description：开始认证或者重新认证
     */
    fun qualificationAuthentication() {
        var intent = Intent(mContext, QualificationAuthenticationEditActivity::class.java)
        when (App.SP.getString(Constants.SP_CERTIFICATIONSTATUS, "")) {
        /****** 待认证 ******/
            Constants.PENDING_CERTIFIED -> {
                intent.putExtra("again_Authentication",false)
            }
        /****** 审核失败 ******/
            Constants.APPROVAL_FAILURE -> {
                intent.putExtra("again_Authentication",true)
            }
            else -> {
                intent.putExtra("again_Authentication",false)
            }
        }
        mContext?.startActivity(intent)
        mView?.onFinish()
    }

    fun loadUISetting() {
        when (App.SP.getString(Constants.SP_CERTIFICATIONSTATUS, "")) {
        /****** 待认证 ******/
            Constants.PENDING_CERTIFIED -> {
                mView?.changeMessage(mContext?.getString(R.string.certified_not).toString())
                mView?.changeQualificationAuthentication(mContext?.getString(R.string.certified_not_send).toString())
            }
        /****** 待审核 ******/
            Constants.PENDING_APPROVAL -> {
                mView?.changeMessage(mContext?.getString(R.string.certified_wait).toString())
                mView?.changeQualificationAuthenticationVisibility(View.GONE)
            }
        /****** 审核失败 ******/
            Constants.APPROVAL_FAILURE -> {
                mView?.changeMessage(mContext?.getString(R.string.certified_fail).toString())
                mView?.changeQualificationAuthentication(mContext?.getString(R.string.certified_fail_send).toString())
            }
        }

    }


}