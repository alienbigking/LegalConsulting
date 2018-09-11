package com.gkzxhn.legalconsulting.activity

import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.adapter.MainAdapter
import com.gkzxhn.legalconsulting.fragment.BaseFragment
import com.gkzxhn.legalconsulting.fragment.ConversationFragment
import com.gkzxhn.legalconsulting.fragment.MainFragment
import com.gkzxhn.legalconsulting.fragment.UserFragment
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

    }

    /**
     * Explanation: 首页的点击方法
     * @author LSX
     *    -----2018/9/11
     */
    fun onClickGoHome(view: View) {
        vpMain.currentItem = 0
        mainHome.setDrawable(resources?.getDrawable(R.mipmap.ic_home_purple))
        mainConversation.setDrawable(resources?.getDrawable(R.mipmap.ic_conversation_black))
        mainMy.setDrawable(resources?.getDrawable(R.mipmap.ic_my_black))
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
        mainHome.setDrawable(resources?.getDrawable(R.mipmap.ic_home_black))
        mainConversation.setDrawable(resources?.getDrawable(R.mipmap.ic_conversation_purple))
        mainMy.setDrawable(resources?.getDrawable(R.mipmap.ic_my_black))
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
        mainHome.setDrawable(resources?.getDrawable(R.mipmap.ic_home_black))
        mainConversation.setDrawable(resources?.getDrawable(R.mipmap.ic_conversation_black))
        mainMy.setDrawable(resources?.getDrawable(R.mipmap.ic_my_purple))
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainHome.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_black)?.let { it1 -> mainConversation.setTextColor(it1) }
        resources?.getColor(R.color.main_bottom_purple)?.let { it1 -> mainMy.setTextColor(it1) }

    }


}
