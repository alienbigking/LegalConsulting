package com.gkzxhn.legalconsulting.model.iml

import android.content.Context
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.model.IConversationModel
import com.gkzxhn.legalconsulting.net.RetrofitClient
import rx.Observable
import rx.schedulers.Schedulers


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

class ConversationModel : BaseModel(), IConversationModel {

    override fun getLawyersInfo(context: Context): Observable<LawyersInfo> {
        return RetrofitClient.getInstance(context).mApi?.getLawyersInfo()?.subscribeOn(Schedulers.io()) as Observable<LawyersInfo>
    }


}