package com.gkzxhn.legalconsulting.common

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Environment
import android.support.multidex.MultiDex
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.util.Log
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.MainActivity
import com.gkzxhn.legalconsulting.activity.NotificationActivity
import com.gkzxhn.legalconsulting.activity.SplashActivity
import com.gkzxhn.legalconsulting.entity.NotificationInfo
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.greendao.dao.GreenDaoManager
import com.gkzxhn.legalconsulting.net.ApiService
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.LogHelper
import com.gkzxhn.legalconsulting.utils.location.helper.MLocationProvider
import com.netease.nim.avchatkit.AVChatKit
import com.netease.nim.avchatkit.config.AVChatOptions
import com.netease.nim.avchatkit.model.ITeamDataProvider
import com.netease.nim.avchatkit.model.IUserInfoProvider
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nim.uikit.business.team.helper.TeamHelper
import com.netease.nim.uikit.business.uinfo.UserInfoHelper
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import com.netease.nim.uikit.custom.CustomAttachParser
import com.netease.nim.uikit.custom.MsgViewHolderMySafe
import com.netease.nim.uikit.custom.MySafeAttachment
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.model.CustomNotification
import com.netease.nimlib.sdk.uinfo.model.UserInfo
import com.netease.nimlib.sdk.util.NIMUtil
import org.json.JSONObject

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

//        初始化数据库
        //解决greendao android5.0一下版本就会报错的问题
        MultiDex.install(this)
        GreenDaoManager.getInstance()

        /****** 崩溃日志初始化 ******/
        CrashHandler.instance.init(this)
        initWY()
    }

    private fun initWY() {
//        NIMClient.init(this, loginInfo(), options(this))
        NIMClient.init(this, loginInfo(), null)
        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {

            NimUIKit.init(this)
            // 初始化音视频模块
            initAVChatKit()
            /****** 注册自定义消息 ******/
            NIMClient.getService(MsgService::class.java).registerCustomAttachmentParser(CustomAttachParser()) // 监听的注册，必须在主进程中。
            NimUIKit.registerMsgItemViewHolder(MySafeAttachment::class.java, MsgViewHolderMySafe::class.java)
//            Log.e("xiaowu", "注册自定义消息")
            // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
            NimUIKit.setLocationProvider(MLocationProvider())

            // 如果有自定义通知是作用于全局的，不依赖某个特定的 Activity，那么这段代码应该在 Application 的 onCreate 中就调用
            NIMClient.getService(MsgServiceObserve::class.java).observeCustomNotification({ p0 ->

                val json = p0.content
                val type = JSONObject(json).getString("type")
                val ext = JSONObject(json).getString("ext")
                val content = JSONObject(json).getString("content")
                initNotification(p0!!,content)

                when (type) {
                /****** 普通通知 ******/
                    "NOTIFICATION" -> {
                        /****** 保存数据到数据库 ******/
                        GreenDaoManager.getInstance().newSession.notificationInfoDao.insert(NotificationInfo(null, p0.sessionId, p0.fromAccount, p0.time, content))
                        RxBus.instance.post(RxBusBean.HomeTopRedPoint(true))
                    }
                /****** 法律咨询通知 ******/
                    "NOTIFICATION_LEGAL_ADVICE" -> {

                    }
                /****** 刷新抢单页 ******/
                    "RUSH_PAGE_REFRESH" -> {
                        RxBus.instance.post(RxBusBean.RefreshGrabOrder(true))
                    }
                }
            }, true)

            NIMClient.getService(AuthServiceObserver::class.java)
                    .observeOnlineStatus({ statusCode ->
                        Log.e("xiaowu", statusCode.toString() + "")
                        if (statusCode.wontAutoLogin()) {
                            RxBus.instance.post(RxBusBean.LoginOut(true))
                        }
                    }, true)
        }

    }


    /**
     * @methodName： created by liushaoxiang on 2018/12/4 4:01 PM.
     * @description：显示系统通知
     */
    private fun initNotification(data: CustomNotification,content: String) {
        val mNotificationManager = getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager

        val intent = PendingIntent.getActivity(this, 0, Intent(this, NotificationActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(App.mContext)
                .setContentTitle("通知")
                .setContentText(content)
                .setContentIntent(intent) //设置通知栏点击意图
                .setTicker(data.fromAccount) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.logo);//设置通知小ICON
        mNotificationManager.notify(0, notification.build())
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
        val account = App.SP.getString(Constants.SP_IM_ACCOUNT, "")
        val token = App.SP.getString(Constants.SP_IM_TOKEN, "")
        NimUIKit.setAccount(account)
        AVChatKit.setAccount(account)
        Log.e("xiaowu", "account:$account")

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

//        config.notificationSmallIconId = R.drawable.notification_icon_background
        // 呼吸灯配置
        config.ledARGB = Color.GREEN
        config.ledOnMs = 1000
        config.ledOffMs = 1500
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg"
        config.ring = false

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

        return options
    }


    private fun initAVChatKit() {
        val avChatOptions = object : AVChatOptions() {
            override fun logout(context: Context) {
                /****** 登出 ******/
//                MainActivity.logout(context, true)
            }
        }
        avChatOptions.entranceActivity = SplashActivity::class.java
        avChatOptions.notificationIconRes = R.mipmap.logo
        AVChatKit.init(avChatOptions)
        AVChatKit.setContext(this)
        // 初始化日志系统
        LogHelper.init()
        // 设置用户相关资料提供者
        AVChatKit.setUserInfoProvider(object : IUserInfoProvider() {
            override fun getUserInfo(account: String): UserInfo {
                return NimUIKit.getUserInfoProvider().getUserInfo(account)
            }

            override fun getUserDisplayName(account: String): String {
                return UserInfoHelper.getUserDisplayName(account)
            }
        })
        // 设置群组数据提供者
        AVChatKit.setTeamDataProvider(object : ITeamDataProvider() {
            override fun getDisplayNameWithoutMe(teamId: String, account: String): String {
                return TeamHelper.getDisplayNameWithoutMe(teamId, account)
            }

            override fun getTeamMemberDisplayName(teamId: String, account: String): String {
                return TeamHelper.getTeamMemberDisplayName(teamId, account)
            }
        })
    }

}