package com.gkzxhn.legalconsulting.net.error_exception

import android.content.Context
import android.util.Log
import com.gkzxhn.legalconsulting.utils.TsDialog
import com.gkzxhn.legalconsulting.utils.showToast
import retrofit2.adapter.rxjava.HttpException
import java.io.IOException
import java.net.ConnectException



 /**
 * Explanation: 
 * @author LSX
 *    -----2018/9/6
 */
object ApiErrorHelper {

    fun handleCommonError(context: Context, e: Throwable) {
        print("网络异常：" + e::javaClass)

        when (e) {
            is ConnectException -> context.TsDialog("没有网络，请检查你的网络", false)
            is HttpException -> context.TsDialog("网络异常请重试", false)
            is IOException -> context.TsDialog("数据加载失败，请检查您的网络", false)
        //后台返回的message
            is ApiException -> {
                context.TsDialog(e.message!!, false)
//                context.showToast(e.message!!)
                Log.e("ApiErrorHelper", e.message, e)
            }
            else -> {
//                context.TsDialog(e.message!!,false)
                context.showToast("数据异常")
                Log.e("ApiErrorHelper", e.message, e)
            }
        }
        //App.mContext?.showToast(e.message!!)
    }
}
