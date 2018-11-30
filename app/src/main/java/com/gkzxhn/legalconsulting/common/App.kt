package com.gkzxhn.legalconsulting.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.text.TextUtils
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.MainActivity
import com.gkzxhn.legalconsulting.net.ApiService
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.location.helper.MLocationProvider
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.uinfo.UserInfoProvider
import com.netease.nimlib.sdk.uinfo.model.UserInfo
import com.netease.nimlib.sdk.util.NIMUtil


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
class App : Application() {

    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        super.onCreate()
        mContext = this
        //初始化通用的SP&EDIT
        SP = getSharedPreferences("config", Context.MODE_PRIVATE)
        EDIT = SP.edit()
        mApi = RetrofitClient.getInstance(this).mApi!!

        /****** 崩溃日志初始化 ******/
        CrashHandler.instance.init(this)
        initWY()
    }

    private fun initWY() {
        NIMClient.init(this, loginInfo(), options(this))
        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {
            NimUIKit.init(this);
            // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
            NimUIKit.setLocationProvider(MLocationProvider())

        }

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
        /**
         * 初始化SP&EDIT
         */
        lateinit var SP: SharedPreferences
        lateinit var EDIT: SharedPreferences.Editor
        lateinit var mApi: ApiService
    }


    /**
     * 登录云信账号
     */
    private fun loginInfo(): LoginInfo? {
        // 从本地读取上次登录成功时保存的用户登录信息
        val account = App.SP.getString(Constants.SP_IM_ACCOUNT,"")
        val token =App.SP.getString(Constants.SP_IM_TOKEN,"")
        NimUIKit.setAccount(account)

        return if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            LoginInfo(account, token)
        } else {
            null
        }
    }


    // 如果返回值为 null，则全部使用默认参数。
    private fun options(context: Context): SDKOptions {
        val options = SDKOptions()

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        val config = StatusBarNotificationConfig()
        config.notificationEntrance = MainActivity::class.java // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.notification_icon_background
        // 呼吸灯配置
        config.ledARGB = Color.GREEN
        config.ledOnMs = 1000
        config.ledOffMs = 1500
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg"
        options.statusBarNotificationConfig = config

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        options.sdkStorageRootPath = Environment.getExternalStorageDirectory().toString() + "/" + context.packageName + "/nim"

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = ScreenUtil.screenWidth / 2

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = object : UserInfoProvider {
            override fun getAvatarForMessageNotifier(p0: SessionTypeEnum?, p1: String?): Bitmap? {
                return null

            }

            override fun getUserInfo(account: String): UserInfo? {
                return null
            }

            fun getTeamIcon(tid: String): Bitmap? {
                return null
            }

            fun getAvatarForMessageNotifier(account: String): Bitmap? {
                return null
            }

            override fun getDisplayNameForMessageNotifier(account: String, sessionId: String, sessionType: SessionTypeEnum): String? {
                return null
            }
        }
        return options
    }



}