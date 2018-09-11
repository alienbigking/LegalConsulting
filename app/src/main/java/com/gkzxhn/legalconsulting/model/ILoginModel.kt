package com.gkzxhn.legalconsulting.model

import android.content.Context
import com.gkzxhn.legalconsulting.entity.Login
import com.gkzxhn.legalconsulting.net.BaseResponseEntity
import rx.Observable


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface ILoginModel : IBaseModel {
    fun sendMessage(context: Context, map: Map<String, String>): Observable<BaseResponseEntity<String>>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun login(context: Context, map: Map<String, String>): Observable<BaseResponseEntity<Login>>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}