package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.OrderActivity
import com.gkzxhn.legalconsulting.adapter.OrderReceivingAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.customview.PullToRefreshLayout
import com.gkzxhn.legalconsulting.entity.OrderReceivingContent
import com.gkzxhn.legalconsulting.presenter.OrderReceivingPresenter
import com.gkzxhn.legalconsulting.utils.DisplayUtils
import com.gkzxhn.legalconsulting.utils.ItemDecorationHelper
import com.gkzxhn.legalconsulting.view.OrderReceivingView
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.order_receiving_fragment.*


/**
 * Explanation：抢单
 * @author LSX
 * Created on 2018/9/10.
 */

class OrderReceivingFragment : BaseFragment(), OrderReceivingView {

    private var mAdapter: OrderReceivingAdapter? = null

    private var cont: Int = 0
    private var list: MutableList<String>? = null
    lateinit var mPresenter: OrderReceivingPresenter

    override fun provideContentViewId(): Int {
        return R.layout.order_receiving_fragment
    }

    override fun init() {
        mPresenter = OrderReceivingPresenter(context!!, this)
        list = ArrayList()
        mAdapter = context?.let { OrderReceivingAdapter(it, null) }
        rcl_order_receiving.layoutManager = LinearLayoutManager(context, 1, false)
        rcl_order_receiving.adapter = mAdapter
        val decoration = DisplayUtils.dp2px(App.mContext, 15f)
        rcl_order_receiving.addItemDecoration(ItemDecorationHelper(decoration, decoration, decoration, 0, decoration))
        mPresenter.getOrderReceiving()
    }

    override fun initListener() {
        //加载更多
        loading_more.setOnLoadMoreListener(object : com.gkzxhn.legalconsulting.customview.LoadMoreWrapper.OnLoadMoreListener {
            override fun onLoadMore() {
                cont++
//                mPresenter.getOrderReceiving()
                loading_more.finishLoading()
            }

        })

        //下啦刷新
        loading_refresh.setOnRefreshListener(object : PullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                list?.clear()
                cont = 0
                mPresenter.getOrderReceiving()
                loading_refresh?.finishRefreshing()
            }
        }, 1)

        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val intent = Intent(context, OrderActivity::class.java)
                val data = mAdapter!!.getCurrentItem()
                intent.putExtra("orderId", data.id)
                intent.putExtra("orderState", 1)
                startActivity(intent)
            }

        })

//        抢单事件的回调监听
        mAdapter?.setOnItemRushListener(object : OrderReceivingAdapter.ItemRushListener{
            override fun onRushListener() {
                mPresenter.acceptRushOrder(mAdapter!!.getCurrentItem().id!!)
            }

        })

    }

    override fun updateData(data: List<OrderReceivingContent>?) {
        mAdapter?.updateItems(data)
    }

    override fun offLoadMore() {
        if (loading_more!!.isLoading) {
            loading_more?.finishLoading()
        }
    }

}