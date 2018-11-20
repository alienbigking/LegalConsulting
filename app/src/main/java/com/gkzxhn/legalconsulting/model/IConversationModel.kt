package com.gkzxhn.legalconsulting.model

import android.content.Context
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import rx.Observable


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface IConversationModel : IBaseModel {


    fun getLawyersInfo(context: Context): Observable<LawyersInfo>

}