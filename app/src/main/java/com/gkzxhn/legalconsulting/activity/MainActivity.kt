package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.adapter.MainAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.entity.UpdateInfo
import com.gkzxhn.legalconsulting.fragment.BaseFragment
import com.gkzxhn.legalconsulting.fragment.MainFragment
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.error_exception.ApiException
import com.gkzxhn.legalconsulting.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_ts.*
import kotlinx.android.synthetic.main.layout_user_info.*
import retrofit2.adapter.rxjava.HttpException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.net.ConnectException
import java.util.*
import kotlinx.android.synthetic.main.activity_main.vp_main as vpMain

/**
 * Explanation: 主页面
 * @author LSX
 *    -----2018/9/11
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    var tbList: MutableList<BaseFragment>? = null
    private var mainAdapter: MainAdapter? = null
    var lawyersInfo: LawyersInfo? = null


    override fun provideContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun init() {

        tbList = ArrayList()

        tbList?.add(MainFragment())
//        tbList?.add(ConversationFragment())
//        tbList?.add(UserFragment())
        mainAdapter = MainAdapter(supportFragmentManager, tbList)
        vpMain.adapter = mainAdapter
//        vpMain.offscreenPageLimit = 3

        updateApp()
        getLawyersInfo()

        RxBus.instance.toObserverable(RxBusBean.ShowMenu::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    openLeftLayout()
                }, {
                    it.message.toString().logE(this)
                })

        v_user_rz_bg.setOnClickListener(this)
        v_user_my_money_bg.setOnClickListener(this)
        v_user_all_order_bg.setOnClickListener(this)
        v_user_set_bg.setOnClickListener(this)
        v_user_top_bg.setOnClickListener(this)
        /****** 给个人信息栏设置背影触摸变化 ******/
        ProjectUtils.addViewTouchChange(v_user_top_bg)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.v_user_my_money_bg -> {
                startActivity(Intent(this, BountyActivity::class.java))
            }
            R.id.v_user_rz_bg -> {
                when (App.SP.getString(Constants.SP_CERTIFICATIONSTATUS, "")) {
                /****** 已认证 ******/
                    Constants.CERTIFIED -> {
                        startActivity(Intent(this, QualificationAuthenticationShowActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this, QualificationAuthenticationActivity::class.java))
                    }
                }
            }
//            所有订单
            R.id.v_user_all_order_bg -> {
                startActivity(Intent(this, AllOrderActivity::class.java))
            }
//            设置
            R.id.v_user_set_bg -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
//            个人信息栏
            R.id.v_user_top_bg -> {
                val intent = Intent(this, UserSettingActivity::class.java)
                intent.putExtra("name", if (lawyersInfo != null) lawyersInfo?.name else "")
                intent.putExtra("phoneNumber", if (lawyersInfo != null) lawyersInfo?.phoneNumber else "")
                startActivity(intent)
            }
        }
        openLeftLayout()
    }


    fun openLeftLayout() {
        if (drawerLayout.isDrawerOpen(main_left_drawer_layout)) {
            drawerLayout.closeDrawer(main_left_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_left_drawer_layout);
        }
    }

    /**
     * Explanation: 首页的点击方法
     * @author LSX
     *    -----2018/9/11
     */
//    fun onClickGoHome(view: View) {
//        vpMain.currentItem = 0
//        mainHome.setDrawable(resources.getDrawable(R.mipmap.ic_home_purple))
//        mainConversation.setDrawable(resources.getDrawable(R.mipmap.ic_conversation_black))
//        mainMy.setDrawable(resources.getDrawable(R.mipmap.ic_my_black))
//        resources?.getColor(R.color.main_bottom_purple)?.let { it1 -> mainHome.setTextColor(it1) }
//        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainConversation.setTextColor(it1) }
//        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainMy.setTextColor(it1) }
//
//    }

    /**
     * Explanation: 会话页面的点击方法
     * @author LSX
     *    -----2018/9/11
     */
//    fun onClickConversation(view: View) {
//        vpMain.currentItem = 1
//        mainHome.setDrawable(resources.getDrawable(R.mipmap.ic_home_black))
//        mainConversation.setDrawable(resources.getDrawable(R.mipmap.ic_conversation_purple))
//        mainMy.setDrawable(resources.getDrawable(R.mipmap.ic_my_black))
//        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainHome.setTextColor(it1) }
//        resources?.getColor(R.color.main_bottom_purple)?.let { it1 -> mainConversation.setTextColor(it1) }
//        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainMy.setTextColor(it1) }
//
//    }

    /**
     * Explanation: 我的页面的点击方法
     * @author LSX
     *    -----2018/9/11
     */
//    fun onClickGoUser(view: View) {
//        vpMain.currentItem = 2
//        mainHome.setDrawable(resources.getDrawable(R.mipmap.ic_home_black))
//        mainConversation.setDrawable(resources.getDrawable(R.mipmap.ic_conversation_black))
//        mainMy.setDrawable(resources.getDrawable(R.mipmap.ic_my_purple))
//        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainHome.setTextColor(it1) }
//        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainConversation.setTextColor(it1) }
//        resources?.getColor(R.color.main_bottom_purple)?.let { it1 -> mainMy.setTextColor(it1) }
//
//    }


    /**
     * @methodName： created by liushaoxiang on 2018/11/6 4:09 PM.
     * @description：检查更新
     */
    private fun updateApp() {
        mCompositeSubscription?.add(RetrofitClient.getInstance(this).mApi?.updateApp()
                ?.subscribeOn(rx.schedulers.Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<UpdateInfo>(this) {
                    override fun success(t: UpdateInfo) {
                        val versionCode = ObtainVersion.getVersionCode(App.mContext)
                        if (t.number!! > versionCode) {
                            showDownloadDialog(t)
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
                                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                    }
                                    e.code() == 404 -> {
                                        /****** 不处理 ******/
                                    }
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

                }))
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取律师信息
     */
    private fun getLawyersInfo() {
        mCompositeSubscription?.add(RetrofitClient.getInstance(this).mApi?.getLawyersInfo()
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<LawyersInfo>(this) {
                    override fun success(data: LawyersInfo) {
                        App.EDIT.putString(Constants.SP_CERTIFICATIONSTATUS, data.certificationStatus)?.commit()
                        lawyersInfo = data
                        tv_user_phone.text = StringUtils.phoneChange(data.phoneNumber!!)
                        tv_user_name.text = data.name
                        tv_user_money.text = "￥" + data.rewardAmount

                        App.EDIT.putString(Constants.SP_PHONE, data.phoneNumber)?.commit()
                        App.EDIT.putString(Constants.SP_NAME, data.name)?.commit()
                        App.EDIT.putString(Constants.SP_LAWOFFICE, data.lawOffice)?.commit()


                        RxBus.instance.post(RxBusBean.HomeUserInfo(data))

                    }
                }))
    }

    override fun onResume() {
        super.onResume()
        getLawyersInfo()
        /****** 刷新订单数据 ******/
        RxBus.instance.post(RxBusBean.RefreshOrder(true))

    }

}
