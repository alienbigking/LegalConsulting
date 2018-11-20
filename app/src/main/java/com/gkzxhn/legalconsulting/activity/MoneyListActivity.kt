package com.gkzxhn.legalconsulting.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.adapter.MoneyListAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.customview.PullToRefreshLayout
import com.gkzxhn.legalconsulting.entity.MoneyList
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.DisplayUtils
import com.gkzxhn.legalconsulting.utils.ItemDecorationHelper
import kotlinx.android.synthetic.main.activity_money_list.*
import kotlinx.android.synthetic.main.default_top.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @classname：AllOrderActivity
 * @author：liushaoxiang
 * @date：2018/10/12 11:55 AM
 * @description：交易明细
 */

class MoneyListActivity : BaseActivity() {

    lateinit var mAdapter: MoneyListAdapter

    var loadMore = false
    var page = 0

    override fun provideContentViewId(): Int {
        return R.layout.activity_money_list
    }

    override fun init() {
        initTopTitle()
        mAdapter = MoneyListAdapter(this)
        rcl_money_list.layoutManager = LinearLayoutManager(this, 1, false)
        rcl_money_list.adapter = mAdapter
        val decoration = DisplayUtils.dp2px(App.mContext, 15f)
        rcl_money_list.addItemDecoration(ItemDecorationHelper(decoration, decoration, decoration, 0, decoration))
        getMoneyList("0")


        //加载更多
        loading_more.setOnLoadMoreListener(object : com.gkzxhn.legalconsulting.customview.LoadMoreWrapper.OnLoadMoreListener {
            override fun onLoadMore() {
                if (loadMore) {
                    getMoneyList((page + 1).toString())
                } else {
                    offLoadMore()
                }
            }
        })

        //下啦刷新
        loading_refresh.setOnRefreshListener(object : PullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getMoneyList("0")
                loading_refresh?.finishRefreshing()
            }
        }, 1)
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "交易明细"
        iv_default_top_back.setOnClickListener {
            finish()
        }

    }

    fun getMoneyList(p: String) {
        RetrofitClient.Companion.getInstance(this).mApi
                ?.getTransaction(p, "10")
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<MoneyList>(this) {
                    override fun success(t: MoneyList) {
                        offLoadMore()
                        loadMore = !t.last
                        page = t.number
                        showNullView(t.content!!.isEmpty())
                        mAdapter.updateItems(t.first, t.content)
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        offLoadMore()
                    }
                })
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

}