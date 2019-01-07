package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.adapter.MainAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.App.Companion.mContext
import com.gkzxhn.legalconsulting.entity.UpdateInfo
import com.gkzxhn.legalconsulting.fragment.BaseFragment
import com.gkzxhn.legalconsulting.fragment.ConversationFragment
import com.gkzxhn.legalconsulting.fragment.MainFragment
import com.gkzxhn.legalconsulting.fragment.UserFragment
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.ObtainVersion
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nimlib.sdk.NimIntent
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import kotlinx.android.synthetic.main.activity_main.tv_main_conversation as mainConversation
import kotlinx.android.synthetic.main.activity_main.tv_main_home as mainHome
import kotlinx.android.synthetic.main.activity_main.tv_main_my as mainMy
import kotlinx.android.synthetic.main.activity_main.vp_main as vpMain

/**
 * Explanation: 主页面
 * @author LSX
 *    -----2018/9/11
 */
class MainActivity : BaseActivity() {

    var tbList: MutableList<BaseFragment>? = null
    private var mainAdapter: MainAdapter? = null


    override fun provideContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun init() {

        tbList = ArrayList()

        tbList?.add(MainFragment())
        tbList?.add(ConversationFragment())
        tbList?.add(UserFragment())
        mainAdapter = MainAdapter(supportFragmentManager, tbList)
        vpMain.adapter = mainAdapter
        vpMain.offscreenPageLimit = 3

        updateApp()
        onParseIntent()

    }

    /**
     * Explanation: 首页的点击方法
     * @author LSX
     *    -----2018/9/11
     */
    fun onClickGoHome(view: View) {
        vpMain.currentItem = 0
        mainHome.setDrawable(resources.getDrawable(R.mipmap.ic_home_purple))
        mainConversation.setDrawable(resources.getDrawable(R.mipmap.ic_conversation_black))
        mainMy.setDrawable(resources.getDrawable(R.mipmap.ic_my_black))
        resources?.getColor(R.color.main_bottom_purple)?.let { it1 -> mainHome.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainConversation.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainMy.setTextColor(it1) }

    }

    /**
     * Explanation: 会话页面的点击方法
     * @author LSX
     *    -----2018/9/11
     */
    fun onClickConversation(view: View) {
        vpMain.currentItem = 1
        mainHome.setDrawable(resources.getDrawable(R.mipmap.ic_home_black))
        mainConversation.setDrawable(resources.getDrawable(R.mipmap.ic_conversation_purple))
        mainMy.setDrawable(resources.getDrawable(R.mipmap.ic_my_black))
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainHome.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_purple)?.let { it1 -> mainConversation.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainMy.setTextColor(it1) }

    }

    /**
     * Explanation: 我的页面的点击方法
     * @author LSX
     *    -----2018/9/11
     */
    fun onClickGoUser(view: View) {
        vpMain.currentItem = 2
        mainHome.setDrawable(resources.getDrawable(R.mipmap.ic_home_black))
        mainConversation.setDrawable(resources.getDrawable(R.mipmap.ic_conversation_black))
        mainMy.setDrawable(resources.getDrawable(R.mipmap.ic_my_purple))
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainHome.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainConversation.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_purple)?.let { it1 -> mainMy.setTextColor(it1) }

    }


    /**
     * @methodName： created by liushaoxiang on 2018/11/6 4:09 PM.
     * @description：检查更新
     */
    private fun updateApp() {
        RetrofitClient.getInstance(this).mApi?.updateApp()
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
                })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onParseIntent()
    }


    fun onParseIntent() {
        Log.e("xiaowu", "onParseIntent")
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            Log.e("xiaowu", "onParseIntent2")
            var message = intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT) as IMMessage
            Log.e("xiaowu", "sessionType:" + message.sessionType)
            when (message.sessionType) {
                SessionTypeEnum.P2P -> {
                    Log.e("xiaowu", "3333333")
                    NimUIKit.startP2PSession(mContext, message.sessionId)
                }
                else -> {

                }
            }
        }
    }


}
