package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.adapter.AllOrderAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.customview.PullToRefreshLayout
import com.gkzxhn.legalconsulting.entity.OrderDispose
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.DisplayUtils
import com.gkzxhn.legalconsulting.utils.ItemDecorationHelper
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.activity_all_order.*
import kotlinx.android.synthetic.main.default_top.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @classname：AllOrderActivity
 * @author：liushaoxiang
 * @date：2018/10/12 11:55 AM
 * @description：所有订单
 */

class AllOrderActivity : BaseActivity() {

    private var mAdapter: AllOrderAdapter? = null

    var loadMore = false
    var page = 0

    override fun provideContentViewId(): Int {
        return R.layout.activity_all_order
    }

    override fun init() {
        initTopTitle()

        mAdapter = AllOrderAdapter(this)
        rcl_all_order.layoutManager = LinearLayoutManager(this, 1, false)
        rcl_all_order.adapter = mAdapter
        val decoration = DisplayUtils.dp2px(App.mContext, 15f)
        rcl_all_order.addItemDecoration(ItemDecorationHelper(0, decoration, 0, 0, decoration))
        getOrderDispose("0")

        initListener()
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "所有订单"
        iv_default_top_back.setOnClickListener {
            finish()
        }

    }

    fun setLastPage(lastPage: Boolean, page: Int) {
        this.loadMore = !lastPage
        this.page = page
    }


    fun initListener() {
        //加载更多
        loading_more.setOnLoadMoreListener(object : com.gkzxhn.legalconsulting.customview.LoadMoreWrapper.OnLoadMoreListener {
            override fun onLoadMore() {
                if (loadMore) {
                    getOrderDispose((page + 1).toString())
                } else {
                    offLoadMore()
                }
            }
        })

        //下啦刷新
        loading_refresh.setOnRefreshListener(object : PullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getOrderDispose("0")
                loading_refresh?.finishRefreshing()
            }
        }, 1)

        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val intent = Intent(this@AllOrderActivity, OrderActivity::class.java)
                val data = mAdapter!!.getCurrentItem()
                intent.putExtra("orderId", data.id)
                intent.putExtra("orderState", 2)
                startActivity(intent)
            }
        })

    }

    fun offLoadMore() {
        //加载更多取消
        if (loading_more!!.isLoading) {
            loading_more?.finishLoading()
        }
    }

    fun updateData(clear: Boolean, data: List<OrderDispose.ContentBean>?) {
        mAdapter?.updateItems(clear, data)
    }

    fun showNullView(show: Boolean) {
        if (show) {
            tv_item_order_receiving_bull.visibility = View.VISIBLE
        } else {
            tv_item_order_receiving_bull.visibility = View.GONE
        }
    }

    fun getOrderDispose(page: String) {
        RetrofitClient.Companion.getInstance(this).mApi
                ?.getAllOrderDispose(page, "10")
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<OrderDispose>(this) {
                    override fun success(t: OrderDispose) {
                        offLoadMore()
                        setLastPage(t.last, t.number)
                        if (t.content!!.isNotEmpty()) {
                            updateData(t.first, t.content)
                        } else {
                            showNullView(true)
                        }
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        offLoadMore()
                    }
                })
    }
}