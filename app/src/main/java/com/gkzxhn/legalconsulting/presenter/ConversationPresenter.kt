package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.content.Intent
import com.gkzxhn.legalconsulting.activity.MainActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.model.IConversationModel
import com.gkzxhn.legalconsulting.model.iml.ConversationModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.view.ConversationView
import rx.android.schedulers.AndroidSchedulers

/**
 * Explanation: 登录
 * @author LSX
 *    -----2018/1/22
 */

class ConversationPresenter(context: Context, view: ConversationView) : BasePresenter<IConversationModel, ConversationView>(context, ConversationModel(), view) {

    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取律师信息
     */
    private fun getLawyersInfo() {
        mContext?.let {
            mModel.getLawyersInfo(it)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<LawyersInfo>(mContext!!) {
                        override fun success(date: LawyersInfo) {
                            App.EDIT.putString(Constants.SP_CERTIFICATIONSTATUS, date.certificationStatus)?.commit()
                            App.EDIT.putString(Constants.SP_PHONE, date.phoneNumber)?.commit()
                            App.EDIT.putString(Constants.SP_NAME, date.name)?.commit()
                            App.EDIT.putString(Constants.SP_LAWOFFICE, date.lawOffice)?.commit()

                            mContext?.startActivity(Intent(mContext, MainActivity::class.java))
                        }

                        override fun onError(t: Throwable?) {
                            super.onError(t)
                            mContext?.startActivity(Intent(mContext, MainActivity::class.java))
                        }
                    })
        }

    }

}