package com.gkzxhn.legalconsulting.net


import android.content.Context
import com.gkzxhn.legalconsulting.net.error_exception.ApiErrorHelper



 /**
 * Explanation:没有加载框的
 * @author LSX
 *    -----2018/9/6
 */
abstract class HttpObserverNoDialog<T>(context: Context) : MySubscriber<T>(), OnRequestListener<T> {
    var contexts : Context?  = null

    init {
        contexts = context
    }

    override fun onCompleted() {
    }

    override fun onNext(t: T) {
        success(t)
    }

    override fun onError(t: Throwable?) {
        ApiErrorHelper.handleCommonError(contexts!!,t!!)
    }
}