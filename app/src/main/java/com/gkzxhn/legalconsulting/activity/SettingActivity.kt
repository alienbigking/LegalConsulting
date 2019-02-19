package com.gkzxhn.legalconsulting.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.UpdateInfo
import com.gkzxhn.legalconsulting.greendao.dao.GreenDaoManager
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.error_exception.ApiException
import com.gkzxhn.legalconsulting.utils.*
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.default_top.*
import kotlinx.android.synthetic.main.dialog_ts.*
import retrofit2.adapter.rxjava.HttpException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.net.ConnectException

/**
 * @classname：SettingActivtiy
 * @author：liushaoxiang
 * @date：2018/10/11 3:52 PM
 * @description：设置
 */
class SettingActivity : BaseActivity() {
    override fun provideContentViewId(): Int {
        return R.layout.activity_setting
    }

    @SuppressLint("SetTextI18n")
    override fun init() {
        initTopTitle()
        ProjectUtils.addViewTouchChange(tv_setting_exit)
        tv_setting_clear_size.text = SystemUtil.getTotalCacheSize(this)
        tv_setting_version.text = "V_" + ObtainVersion.getVersionName(App.mContext)
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "设置"
        iv_default_top_back.setOnClickListener {
            finish()
        }

    }

    fun onClickSetting(view: View) {
        when (view.id) {

        /****** 意见反馈 ******/
            R.id.v_setting_idea_bg -> {
                startActivity(Intent(this, IdeaSubmitActivity::class.java))
            }
        /****** 清除缓存 ******/
            R.id.v_setting_clear_bg -> {
                clearDialog()
            }
        /****** 版本更新 ******/
            R.id.v_setting_update_bg -> {
                updateApp()
            }
        /****** 退出账号 ******/
            R.id.tv_setting_exit -> {
                exit()
            }
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/12 4:33 PM.
     * @description：清理缓存的dialog处理
     */
    private fun clearDialog() {
        val selectDialog = selectDialog("确认清除吗？", false)
        selectDialog.findViewById<TextView>(R.id.dialog_save).setOnClickListener {
            SystemUtil.clearAllCache(this)
            tv_setting_clear_size.text = SystemUtil.getTotalCacheSize(this)
            showToast("清除完成")
            selectDialog.dismiss()
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:10 PM.
     * @description：退出账号
     */
    private fun exit() {
        val selectDialog = selectDialog("确认退出账号吗？", false)
        selectDialog.findViewById<TextView>(R.id.dialog_save).setOnClickListener {
            NIMClient.getService(AuthService::class.java).logout()
            App.EDIT.putString(Constants.SP_TOKEN, "")?.commit()
            App.EDIT.putString(Constants.SP_NAME, "")?.commit()
            App.EDIT.putString(Constants.SP_LAWOFFICE, "")?.commit()
            App.EDIT.putString(Constants.SP_CERTIFICATIONSTATUS, "")?.commit()

            /****** 清空数消息数据库 ******/
            GreenDaoManager.getInstance().newSession.notificationInfoDao.deleteAll()
            /****** 清除缓存 ******/
            SystemUtil.clearAllCache(this)

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            selectDialog.dismiss()
        }
    }

    private fun updateApp() {
        RetrofitClient.getInstance(this).mApi?.updateApp()
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<UpdateInfo>(this) {
                    override fun success(t: UpdateInfo) {
                        val versionCode = ObtainVersion.getVersionCode(App.mContext)
                        if (t.number!! > versionCode) {
                            showDownloadDialog(t)
                        } else {
                            TsDialog("已是最新版本", true)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        loadDialog?.dismiss()
                        when (e) {
                            is ConnectException -> TsDialog("服务器异常，请重试", false)
                            is HttpException -> {
                                when {
                                    e.code() == 401 -> TsClickDialog("登录已过期", false).dialog_save.setOnClickListener {
                                        App.EDIT.putString(Constants.SP_TOKEN, "")?.commit()
                                        val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                    }
                                    e.code() == 404 ->
                                        /****** 不处理 ******/
                                        TsDialog("已是最新版本", false)
                                    else -> TsDialog("服务器异常，请重试", false)
                                }
                            }
                            is IOException -> showToast("网络连接超时，请重试")

                        //后台返回的message
                            is ApiException -> {
                                TsDialog(e.message!!, false)
                                Log.e("ApiErrorHelper", e.message, e)
                            }
                            else -> {
                                showToast("数据异常")
                                Log.e("ApiErrorHelper", e?.message, e)
                            }
                        }
                    }

                })
    }
}

