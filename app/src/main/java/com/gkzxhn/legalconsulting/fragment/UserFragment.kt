package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.*
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.RetrofitClientLogin
import com.gkzxhn.legalconsulting.net.error_exception.ApiException
import com.gkzxhn.legalconsulting.utils.*
import kotlinx.android.synthetic.main.user_fragment.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.adapter.rxjava.HttpException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.net.ConnectException


/**
 * Explanation: 个人中心
 * @author LSX
 *    -----2018/9/7
 */

class UserFragment : BaseFragment(), View.OnClickListener {
    var lawyersInfo: LawyersInfo? = null

    override fun provideContentViewId(): Int {
        return R.layout.user_fragment
    }

    override fun init() {
        getLawyersInfo()

        /****** 接受控件小红点的消息 ******/
        RxBus.instance.toObserverable(RxBusBean.HomeTopRedPoint::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getLawyersInfo()
                }, {
                    it.message.toString().logE(this)
                })
    }

    override fun initListener() {
               v_user_my_money_bg.setOnClickListener(this)
        v_user_rz_bg.setOnClickListener(this)
        v_user_all_order_bg.setOnClickListener(this)
        v_user_set_bg.setOnClickListener(this)
        v_user_top_bg.setOnClickListener(this)
        /****** 给个人信息栏设置背影触摸变化 ******/
        ProjectUtils.addViewTouchChange(v_user_top_bg)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.v_user_my_money_bg -> {
                context?.startActivity(Intent(context, BountyActivity::class.java))
            }
            R.id.v_user_rz_bg -> {
                when (App.SP.getString(Constants.SP_CERTIFICATIONSTATUS, "")) {
                /****** 已认证 ******/
                    Constants.CERTIFIED -> {
                        context?.startActivity(Intent(context, QualificationAuthenticationShowActivity::class.java))
                    }
                    else -> {
                        context?.startActivity(Intent(context, QualificationAuthenticationActivity::class.java))
                    }
                }
            }
//            所有订单
            R.id.v_user_all_order_bg -> {
                context?.startActivity(Intent(context, AllOrderActivity::class.java))
            }
//            设置
            R.id.v_user_set_bg -> {
                context?.startActivity(Intent(context, SettingActivity::class.java))
            }
//            个人信息栏
            R.id.v_user_top_bg -> {
                val intent = Intent(context, UserSettingActivity::class.java)
                intent.putExtra("name", if (lawyersInfo != null) lawyersInfo?.name else "")
                intent.putExtra("phoneNumber", if (lawyersInfo != null) lawyersInfo?.phoneNumber else "")
                context?.startActivity(intent)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        init()
    }

    /****** 刷新新的token ******/
    private fun getRefreshToken(refresh_token: String) {
        context?.let {
            RetrofitClientLogin.Companion.getInstance(it)
                    .mApi?.getToken("refresh_token", refreshToken = refresh_token)
                    ?.subscribeOn(Schedulers.io())
                    ?.unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<Response<ResponseBody>>(it) {
                        override fun success(t: Response<ResponseBody>) {
                            if (t.code() == 200) {
                                val string = t.body().string()
                                if (!TextUtils.isEmpty(string)) {
                                    var token: String? = null
                                    var refreshToken: String? = null
                                    try {
                                        token = JSONObject(string).getString("access_token")
                                        refreshToken = JSONObject(string).getString("refresh_token")
                                    } catch (e: Exception) {

                                    }
                                    App.EDIT.putString(Constants.SP_TOKEN, token)?.commit()
                                    App.EDIT.putString(Constants.SP_REFRESH_TOKEN, refreshToken)?.commit()
                                    getLawyersInfo()
                                }
                            }
                        }

                    })
        }
    }

       /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取律师信息
     */
    private fun getLawyersInfo() {
        context?.let {
            RetrofitClient.getInstance(it).mApi?.getLawyersInfo()
                    ?.subscribeOn(Schedulers.io())
                    ?.unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<LawyersInfo>(it) {
                        override fun success(t: LawyersInfo) {
                            App.EDIT.putString(Constants.SP_CERTIFICATIONSTATUS, t.certificationStatus)?.commit()
                            lawyersInfo = t
                            loadUI(t)
                        }

                        override fun onError(e: Throwable?) {
                            loadDialog?.dismiss()
                            when (e) {
                                is ConnectException -> context?.TsDialog("服务器异常", false)
                                is HttpException -> {
                                    if (e.code() == 401) {
                                        getRefreshToken(App.SP.getString(Constants.SP_REFRESH_TOKEN, ""))
                                    } else {
                                        context?.TsDialog("服务器异常，请重试", false)
                                    }
                                }
                                is IOException -> context?.TsDialog("数据加载失败，请检查您的网络", false)
                            //后台返回的message
                                is ApiException -> {
                                    context?.TsDialog(e.message!!, false)
                                    Log.e("ApiErrorHelper", e.message, e)
                                }
                                else -> {
                                    context?.showToast("数据异常")
                                    Log.e("ApiErrorHelper", e?.message, e)
                                }
                            }
                        }

                    })
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/26 3:46 PM.
     * @description：处理UI数据
     */
    private fun loadUI(date: LawyersInfo) {
        tv_user_phone.text = StringUtils.phoneChange(date.phoneNumber!!)
        tv_user_name.text = date.name
        tv_user_money.text = "￥" + date.rewardAmount
        App.EDIT.putString(Constants.SP_PHONE, date.phoneNumber)?.commit()
        App.EDIT.putString(Constants.SP_NAME, date.name)?.commit()
        App.EDIT.putString(Constants.SP_LAWOFFICE, date.lawOffice)?.commit()

        ProjectUtils.loadMyIcon(context,iv_user_icon)

        RxBus.instance.post(RxBusBean.HomeUserInfo(date))

    }

}
