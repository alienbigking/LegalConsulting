package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationEditActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.model.iml.BaseModel
import com.gkzxhn.legalconsulting.utils.ObtainVersion
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationView

/**
 * Explanation: 登录
 * @author LSX
 *    -----2018/1/22
 */

class QualificationAuthenticationPresenter (context: Context, view: QualificationAuthenticationView) : BasePresenter<BaseModel, QualificationAuthenticationView>(context, BaseModel(), view) {

    var time: String? = null
    var versionName: String? = null

    //构造方法
    init {
        versionName = ObtainVersion.getVersionName(App.mContext)
    }




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