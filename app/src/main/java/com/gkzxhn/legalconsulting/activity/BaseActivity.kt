package com.gkzxhn.legalconsulting.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.entity.UpdateInfo
import com.gkzxhn.legalconsulting.greendao.dao.GreenDaoManager
import com.gkzxhn.legalconsulting.net.NetWorkCodeInfo
import com.gkzxhn.legalconsulting.utils.SystemUtil
import com.gkzxhn.legalconsulting.utils.TsClickDialog
import com.gkzxhn.legalconsulting.utils.download.HttpDownManager
import com.gkzxhn.legalconsulting.utils.download.HttpProgressOnNextListener
import com.gkzxhn.legalconsulting.utils.download.entity.DownInfo
import com.gkzxhn.legalconsulting.utils.download.entity.DownState
import com.gkzxhn.legalconsulting.utils.logE
import com.gkzxhn.legalconsulting.utils.showToast
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.dialog_ts.*
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.io.File


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
abstract class BaseActivity : AppCompatActivity() {
    var open = 0x00001
    var rxPermissions: RxPermissions? = null
    var mCompositeSubscription: CompositeSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //设置竖屏锁死
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initBefore()
        setContentView(provideContentViewId())
        mCompositeSubscription = CompositeSubscription()
        init()
        //初始化权限管理
        rxPermissions = RxPermissions(this)

        RxBus.instance.toObserverable(DownInfo::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateProgress(it)
                }, {
                    it.message.toString().logE(this)
                })

        /******  在其它地方登录 ******/
        RxBus.instance.toObserverable(RxBusBean.LoginOut::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    App.EDIT.putString(Constants.SP_TOKEN, "")?.commit()
                    NIMClient.getService(AuthService::class.java).logout()
                    if (javaClass.simpleName == "LoginActivity" || javaClass.simpleName == "SplashActivity") {
                        /****** 已经在登录 闪屏 页面就不要弹出来了 ******/
                    } else {
                        TsClickDialog("您的账号已经在其它地方登录", false).dialog_save.setOnClickListener {
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
                        }
                    }

                }, {
                    it.message.toString().logE(this)
                })

    }

    /**
     * Explanation: 在加载布局前的方法，需要使用可以重写
     * @author LSX
     *    -----2018/9/11
     */
    open fun initBefore() {}

    /**
     * @methodName： created by PrivateXiaoWu on 2018/9/11 9:12.
     * @description：
     */
    abstract fun init()

    /**
     * Explanation: 返回布局文件
     * @author LSX
     *    -----2018/9/11
     */
    abstract fun provideContentViewId(): Int


    /**
     * Explanation: 单个权限申请
     * @author LSX
     *    -----2018/9/11
     */
    open fun singlePermissions(permission: String) {
        rxPermissions?.requestEach(permission)
                ?.subscribe { permission ->
                    when (permission.granted) {
                    // 用户已经同意该权限
                        true -> consent()
                    // 用户拒绝了该权限，并且选中『不再询问』
                        permission.shouldShowRequestPermissionRationale -> Dialog()
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        else -> showToast(permission.name)
                    }
                }
    }

    /**
     * Explanation: 当用户权限被调用，且同意，所要走的方法 子类需要自己去实现
     * @author LSX
     *    -----2018/9/11
     */
    open fun consent() {}

    //一次获取所有权限,
    open fun multiPermissions() {
        open = 0x00001
        //常用权限,定位，相机，SD卡读写，日历
        rxPermissions?.requestEach(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        )
                ?.subscribe { permission ->
                    when (permission.granted) {
                    // 用户已经同意该权限
                        true -> {
                            println("同意了" + permission.name)
                        }
                    // 用户拒绝了该权限，并且选中『不再询问』
                        permission.shouldShowRequestPermissionRationale -> judge()
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        else -> multiPermissions()
                    }
                }
    }

    private fun judge() {
        when (open) {
            0x00001 -> Dialog()
        }
    }

    private fun Dialog() {
        open = 0x0002
        val builder = AlertDialog.Builder(this)
        builder.setMessage("权限未开启，暂无法使用此功能")
                .setCancelable(false)
                .setPositiveButton("取消", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                .setNegativeButton("前去设置", DialogInterface.OnClickListener { dialog, id -> getAppDetailSettingIntent() }).show()
        val alert = builder.create()
    }

    //当权限被拒绝前往设置界面
    @SuppressLint("ObsoleteSdkInt")
    private fun getAppDetailSettingIntent() {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT > 16) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", packageName, null)
        } else if (Build.VERSION.SDK_INT <= 16) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", packageName)
        }
        startActivity(localIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        when {
            mCompositeSubscription?.hasSubscriptions()!! -> mCompositeSubscription?.unsubscribe()
        }

    }


    var downloadDialog: MaterialDialog? = null
    lateinit var mpbProgress: ProgressBar
    lateinit var tvCancel: TextView
    lateinit var tvConfirm: TextView
    lateinit var v_line: View
    var downloadInfo: DownInfo? = null

    /**
     * 显示更新弹窗
     */
    @SuppressLint("SetTextI18n")
    fun showDownloadDialog(updateInfo: UpdateInfo) {
        downloadDialog = downloadDialog ?: MaterialDialog.Builder(this)
                .customView(R.layout.dialog_seeting_update, false)
                .cancelable(!updateInfo.force!!)
                .show()

        if (!downloadDialog?.isShowing!!) {
            downloadDialog?.show()
        }
        val tvVersionNumber = downloadDialog?.customView!!.findViewById<TextView>(R.id.tv_update_dialog_version)
        val tvDesc = downloadDialog?.customView!!.findViewById<TextView>(R.id.tv_update_dialog_context)
        val desc: CharSequence = updateInfo.description ?: ""
        if (TextUtils.isEmpty(desc)) {
            tvDesc.visibility = View.GONE
        } else {
            tvDesc.visibility = View.VISIBLE
            tvDesc.text = desc.toString().replace("\\n", "\n")
        }
        mpbProgress = downloadDialog?.customView!!.findViewById(R.id.pb_update_dialog_progress_bar)
        tvCancel = downloadDialog?.customView!!.findViewById<TextView>(R.id.tv_update_dialog_cancel)
        tvConfirm = downloadDialog?.customView!!.findViewById<TextView>(R.id.tv_update_dialog_update)
        v_line = downloadDialog?.customView!!.findViewById<TextView>(R.id.v_dialog_update)
        tvCancel.visibility = if (updateInfo.force!!) {
            v_line.visibility = View.GONE
            View.GONE
        } else View.VISIBLE

        val filters = HttpDownManager.getInstance().downInfos.filter { updateInfo.fileId == it.url }
        if (filters.isNotEmpty()) {
            downloadInfo = filters[0]
        }
        if (downloadInfo != null) {
            var status = getString(R.string.update_now)
            when (downloadInfo?.state) {
                DownState.DOWN -> {
                    //停止状态
                    status = getString(R.string.go_on)
                }
                DownState.START -> {
                    status = getString(R.string.pause)
                }
                DownState.FINISH -> {
                    status = getString(R.string.install)
                }
                DownState.PAUSE, DownState.STOP -> {
                    //暂停, 点击继续下载
                    status = getString(R.string.go_on)
                }
                DownState.ERROR -> {
                    status = getString(R.string.retry)
                }
                else -> {
                }
            }
            tvConfirm.text = status
        }

        tvVersionNumber.text = "V${updateInfo.code}"
        setDownloadDialogClick(tvCancel, tvConfirm, updateInfo)
    }

    private fun setDownloadDialogClick(tvCancel: TextView, tvConfirm: TextView, updateInfo: UpdateInfo?) {
        tvCancel.setOnClickListener {
            downloadDialog?.dismiss()
        }

        tvConfirm.setOnClickListener {
            downloadInfo?.state.toString().logE(this@BaseActivity)
            tvCancel.text = getString(R.string.download_background)
            if (downloadInfo == null) {
                downloadInfo = DownInfo()

                val externalFilesDir = getExternalFilesDir("download")
                if (!externalFilesDir.exists()) {
                    externalFilesDir.mkdirs()
                }

                downloadInfo?.baseUrl = NetWorkCodeInfo.BASE_URL
                downloadInfo?.url = updateInfo?.fileId
                downloadInfo?.savePath = File(externalFilesDir, Constants.APK_ADRESS).absolutePath

                downloadInfo?.listener = object : HttpProgressOnNextListener<DownInfo>() {
                    override fun onNext(t: DownInfo) {
                        "download on next ---- ${t.state?.toString()}".logE(this)
                    }

                    override fun onStart() {
                        "download onStart ----".logE(this)
                        tvConfirm.text = getString(R.string.pause)
                    }

                    override fun onComplete() {
                        "download onComplete ----".logE(this)
                        tvConfirm.text = getString(R.string.install)
                        installApk()
                    }

                    override fun updateProgress(readLength: Long, countLength: Long) {
                        "download updateProgress ---- $readLength".logE(this)
                        mpbProgress.max = countLength.toInt()
                        mpbProgress.progress = readLength.toInt()
                    }
                }

                HttpDownManager.getInstance().startDown(downloadInfo)
                tvConfirm.setTextColor(ContextCompat.getColor(App.mContext, R.color.text_gray))
            } else {
                "download updateProgress ---- ${downloadInfo?.readLength}>>>>${downloadInfo?.countLength}".logE(this)
                when (downloadInfo?.state) {
                    DownState.DOWN -> {
                        //停止状态, 点击继续下载
                        HttpDownManager.getInstance().startDown(downloadInfo)
                        /****** 暂时不做断点下载  所以文字方面先不显示 功能 未完成 等待下版本更新 ******/
//                        tvConfirm.text = getString(R.string.pause)
                    }
                    DownState.START -> {
                        //正在下载中, 暂停下载
                        HttpDownManager.getInstance().pause(downloadInfo)
//                        tvConfirm.text = getString(R.string.go_on)
                    }
                    DownState.FINISH -> {
                        installApk()
                    }
                    DownState.PAUSE, DownState.STOP -> {
                        //暂停, 点击继续下载
                        HttpDownManager.getInstance().startDown(downloadInfo)
//                        tvConfirm.text = getString(R.string.pause)
                    }
                    DownState.ERROR -> {
                        HttpDownManager.getInstance().startDown(downloadInfo)
//                        tvConfirm.text = getString(R.string.pause)
                    }
                    else -> {
                        HttpDownManager.getInstance().startDown(downloadInfo)
//                        tvConfirm.text = getString(R.string.pause)
                    }
                }
            }
        }
    }

    private fun installApk() {
        val intent = Intent("android.intent.action.VIEW")
        val externalFilesDir = getExternalFilesDir("download")
        val file = File(externalFilesDir, Constants.APK_ADRESS)
        if (file.exists() && file.length() == downloadInfo?.countLength) {
            var uri: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(App.mContext, "$packageName.fileprovider",
                        file)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                uri = Uri.fromFile(file)
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            startActivity(intent)
        } else {
            showToast("文件损坏,重新下载")
            HttpDownManager.getInstance().let {
                it.subMap.remove(downloadInfo?.url)
                it.downInfos.remove(downloadInfo)
                downloadInfo?.state = DownState.STOP
                downloadInfo?.readLength = 0
//                tvConfirm.text = "暂停"
                it.startDown(downloadInfo)
            }
        }
    }

    fun updateProgress(it: DownInfo) {
        "reading>>>>${it.countLength}>>>>>>>>>>${it.readLength}----".logE(this)
        mpbProgress.max = it.countLength.toInt()
        mpbProgress.progress = it.readLength.toInt()
    }

}


