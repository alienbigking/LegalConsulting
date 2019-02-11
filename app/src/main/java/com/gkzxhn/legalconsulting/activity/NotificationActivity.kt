package com.gkzxhn.legalconsulting.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.adapter.NotificationInfoAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.customview.PullToRefreshLayout
import com.gkzxhn.legalconsulting.entity.NotificationInfo
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.greendao.dao.GreenDaoManager
import com.gkzxhn.legalconsulting.utils.DisplayUtils
import com.gkzxhn.legalconsulting.utils.ItemDecorationHelper
import kotlinx.android.synthetic.main.activity_money_list.*
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：消息
 * @author：liushaoxiang
 * @date：2018/12/4 5:52 PM
 * @description：
 */

class NotificationActivity : BaseActivity() {

    lateinit var mAdapter: NotificationInfoAdapter
    var notificationList: List<NotificationInfo>?=null
    override fun provideContentViewId(): Int {
        return R.layout.activity_notification_list
    }

    override fun init() {
        initTopTitle()
        mAdapter = NotificationInfoAdapter(this)
        val linearLayoutManager = LinearLayoutManager(this, 1, false)
        linearLayoutManager.stackFromEnd = true;//列表再底部开始展示，反转后由上面开始展示
        linearLayoutManager.reverseLayout = true;//列表翻转
        rcl_money_list.layoutManager = linearLayoutManager
        rcl_money_list.adapter = mAdapter
        val decoration = DisplayUtils.dp2px(App.mContext, 0f)
        rcl_money_list.addItemDecoration(ItemDecorationHelper(decoration, decoration, decoration, 0, decoration))
        getData()


        //加载更多
        loading_more.setOnLoadMoreListener(object : com.gkzxhn.legalconsulting.customview.LoadMoreWrapper.OnLoadMoreListener {
            override fun onLoadMore() {
                    offLoadMore()
            }
        })

        //下啦刷新
        loading_refresh.setOnRefreshListener(object : PullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getData()
                loading_refresh?.finishRefreshing()
            }
        }, 1)
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "消息"
        iv_default_top_back.setOnClickListener {
            finish()
        }
    }

    fun getData() {
        notificationList = GreenDaoManager.getInstance().newSession.notificationInfoDao.loadAll()
        showNullView(notificationList?.isEmpty()!!)
        mAdapter.updateItems(true,notificationList)
    }


    fun offLoadMore() {
        //加载更多取消
        if (loading_more!!.isLoading) {
            loading_more?.finishLoading()
        }
    }

    fun showNullView(show: Boolean) {
        if (show) {
            tv_null.visibility = View.VISIBLE
        } else {
            tv_null.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.instance.post(RxBusBean.HomeTopRedPoint(false))

    }

}