package com.gkzxhn.legalconsulting.net


import android.app.Dialog
import android.content.Context
import com.gkzxhn.legalconsulting.net.error_exception.ApiErrorHelper
import com.gkzxhn.legalconsulting.utils.loadDialog

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

abstract class HttpObserver<T>(context: Context) : MySubscriber<T>(), OnRequestListener<T> {
    var loadDialog: Dialog? = null
    var contexts: Context? = null

    init {
        contexts = context
        loadDialog = context.loadDialog("加载中", false)
        loadDialog?.show()
    }

    override fun onCompleted() {
        loadDialog?.dismiss()
    }

    override fun onNext(t: T) {
        success(t)
    }

    override fun onError(t: Throwable?) {
        loadDialog?.dismiss()
        ApiErrorHelper.handleCommonError(contexts!!, t!!)
    }

}