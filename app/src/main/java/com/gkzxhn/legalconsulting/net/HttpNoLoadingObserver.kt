package com.gkzxhn.legalconsulting.net

import android.content.Context
import com.gkzxhn.legalconsulting.net.error_exception.ApiErrorHelper


/**
 * Explanation: 没有Loading
 * @author LSX
 *    -----2018/2/5
 */
abstract class HttpNoLoadingObserver<T>(context: Context) : MySubscriber<T>(), OnRequestListener<T> {
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
        ApiErrorHelper.handleCommonError(contexts!!, t!!)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun unsubscribe() {
        super.unsubscribe()
    }
}