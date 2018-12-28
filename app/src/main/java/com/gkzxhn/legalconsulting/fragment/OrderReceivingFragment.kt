package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.OrderActivity
import com.gkzxhn.legalconsulting.adapter.OrderReceivingAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.customview.PullToRefreshLayout
import com.gkzxhn.legalconsulting.entity.OrderReceivingContent
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.presenter.OrderReceivingPresenter
import com.gkzxhn.legalconsulting.utils.*
import com.gkzxhn.legalconsulting.view.OrderReceivingView
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.order_receiving_fragment.*
import rx.android.schedulers.AndroidSchedulers

/**
 * Explanation：抢单
 * @author LSX
 * Created on 2018/9/10.
 */

class OrderReceivingFragment : BaseFragment(), OrderReceivingView {

    private var mAdapter: OrderReceivingAdapter? = null

    var mPresenter: OrderReceivingPresenter? = null


    var loadMore = false
    var page = 0

    override fun setLastPage(lastPage: Boolean, page: Int) {
        this.loadMore = !lastPage
        this.page = page
    }

    override fun provideContentViewId(): Int {
        return R.layout.order_receiving_fragment
    }

    override fun init() {
        mPresenter = OrderReceivingPresenter(context!!, this)
        mAdapter = context?.let { OrderReceivingAdapter(it) }
        rcl_order_receiving.layoutManager = LinearLayoutManager(context, 1, false)
        rcl_order_receiving.adapter = mAdapter
        val decoration = DisplayUtils.dp2px(App.mContext, 15f)
        rcl_order_receiving.addItemDecoration(ItemDecorationHelper(decoration, decoration, decoration, 0, decoration))
        mPresenter?.getOrderReceiving("0")


        /****** 收到抢单成功的消息 ******/
        RxBus.instance.toObserverable(RxBusBean.HomePoint::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (ProjectUtils.certificationStatus()) {
                        mPresenter?.getOrderReceiving("0")
                    }
                }, {
                    it.message.toString().logE(this)
                })
    }

    override fun initListener() {
        //加载更多
        loading_more.setOnLoadMoreListener(object : com.gkzxhn.legalconsulting.customview.LoadMoreWrapper.OnLoadMoreListener {
            override fun onLoadMore() {
                if (loadMore) {
                    mPresenter?.getOrderReceiving((page + 1).toString())
                } else {
                    offLoadMore()
                }
            }
        })

        //下啦刷新
        loading_refresh.setOnRefreshListener(object : PullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                mPresenter?.getOrderReceiving("0")
                loading_refresh?.finishRefreshing()
            }
        }, 1)

        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                /****** 认证通过才能进入  ******/
                if (ProjectUtils.certificationStatus()) {
                    val intent = Intent(context, OrderActivity::class.java)
                    val data = mAdapter!!.getCurrentItem()
                    intent.putExtra("orderId", data.id)
                    intent.putExtra("orderState", 1)
                    startActivity(intent)
                } else {
                    context?.showToast("您认证尚未通过，不能进行此操作！")
                }
            }
        })

//        抢单事件的回调监听
        mAdapter?.setOnItemRushListener(object : OrderReceivingAdapter.ItemRushListener {
            override fun onRushListener() {
                if (ProjectUtils.certificationStatus()) {
                    mPresenter?.acceptRushOrder(mAdapter!!.getCurrentItem().id!!)
                } else {
                    context?.showToast("您认证尚未通过，不能进行此操作！")
                }
            }
        })
    }

    override fun updateData(clear: Boolean, data: List<OrderReceivingContent>?) {
        mAdapter?.updateItems(clear, data)
    }

    override fun offLoadMore() {
        if (loading_more!!.isLoading) {
            loading_more?.finishLoading()
        }
    }

    override fun showNullView(show: Boolean) {
        tv_item_order_receiving_bull.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {

            mPresenter?.getOrderReceiving("0")
        }

    }

}