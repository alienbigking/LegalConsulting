package com.gkzxhn.legalconsulting.net.error_exception

import android.content.Context
import android.content.Intent
import android.util.Log
import com.gkzxhn.legalconsulting.activity.LoginActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.utils.TsClickDialog
import com.gkzxhn.legalconsulting.utils.TsDialog
import com.gkzxhn.legalconsulting.utils.showToast
import kotlinx.android.synthetic.main.dialog_ts.*
import org.json.JSONObject
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
            is ConnectException -> context.TsDialog("服务器异常，请重试", false)
            is HttpException -> {
                when (e.code()) {
                    401 -> context.TsClickDialog("登录已过期", false).dialog_save.setOnClickListener {
                        App.EDIT.putString(Constants.SP_TOKEN, "")?.commit()
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }
                    400 -> {
                        val errorBody = e.response().errorBody().string()
                        val code = JSONObject(errorBody).getString("code")
                        val message = JSONObject(errorBody).getString("message")
                        when (code) {
                            "lawyer.CanNotRushToAcceptIsBusy" -> {
                                context.TsDialog("您有订单未处理，请处理", false)
                            }
                            else -> {
                                context.TsDialog(message, false)
                            }
                        }
                    }
                    else -> {
                        context.TsDialog("服务器异常，请重试", false)
                    }
                }
            }
            is IOException -> {
                context.showToast("网络连接超时，请重试")
//                context.TsDialog("网络连接超时，请重试", false)
            }
        //后台返回的message
            is ApiException -> {
                context.TsDialog(e.message!!, false)
                Log.e("ApiErrorHelper", e.message, e)
            }
            else -> {
                context.showToast("数据异常")
                Log.e("ApiErrorHelper", e.message, e)
            }
        }
    }

}
