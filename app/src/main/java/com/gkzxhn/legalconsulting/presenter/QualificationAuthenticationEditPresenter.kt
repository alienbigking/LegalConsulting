package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.activity.MainActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.model.iml.BaseModel
import com.gkzxhn.legalconsulting.utils.ObtainVersion
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationEditView

/**
 * Explanation: 登录
 * @author LSX
 *    -----2018/1/22
 */

class QualificationAuthenticationEditPresenter(context: Context, view: QualificationAuthenticationEditView) : BasePresenter<BaseModel, QualificationAuthenticationEditView>(context, BaseModel(), view) {

    var time: String? = null
    var versionName: String? = null

    //构造方法
    init {
        versionName = ObtainVersion.getVersionName(App.mContext)
    }


    fun send() {
        var intent = Intent(mContext, MainActivity::class.java)
        mContext?.startActivity(intent)

    }


}