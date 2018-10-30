package com.gkzxhn.legalconsulting.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.idlingregistry.SimpleIdlingResource
import com.tbruyelle.rxpermissions2.RxPermissions
import rx.subscriptions.CompositeSubscription


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
        init()
        //初始化权限管理
        rxPermissions = RxPermissions(this)
        mCompositeSubscription = CompositeSubscription()
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


    //自动化测试使用
    private var mIdlingResource: SimpleIdlingResource? = null

    /**
     * Espresso 自动化测试延迟操作
     * @param isIdleNow 是否为空闲，false则阻塞测试线程
     */
    fun setIdleNow(isIdleNow: Boolean) {
        if (mIdlingResource?.isIdleNow != isIdleNow) {
            if (isIdleNow) {
                //耗时操作结束，设置空闲状态为true，放开测试线程
                mIdlingResource?.setIdleState(true)
            } else {
                //耗时操作开始，设置空闲状态为false，阻塞测试线程
                mIdlingResource?.setIdleState(false)
            }
        }
    }

    /**
     *
     * 自动化测试使用
     * Only called from test, creates and returns a new [SimpleIdlingResource].
     */
    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource()
        }
        return mIdlingResource!!
    }
}


