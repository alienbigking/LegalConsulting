package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.OrderActivity
import com.gkzxhn.legalconsulting.adapter.OrderDisposeAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.customview.PullToRefreshLayout
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.entity.OrderDispose
import com.gkzxhn.legalconsulting.presenter.OrderDisposePresenter
import com.gkzxhn.legalconsulting.utils.DisplayUtils
import com.gkzxhn.legalconsulting.utils.ItemDecorationHelper
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.logE
import com.gkzxhn.legalconsulting.view.OrderDisposeView
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.order_disposer_fragment.*
import rx.android.schedulers.AndroidSchedulers

/**
 * Explanation：我的咨询里面（指定单）
 * @author LSX
 * Created on 2018/9/10.
 */

class OrderDisposeFragment : BaseFragment(), OrderDisposeView {

    private var mAdapter: OrderDisposeAdapter? = null

    lateinit var mPresenter: OrderDisposePresenter

    var loadMore = false
    var page = 0

    override fun setLastPage(lastPage: Boolean, page: Int) {
        this.loadMore = !lastPage
        this.page = page
    }

    override fun provideContentViewId(): Int {
        return R.layout.order_disposer_fragment
    }

    override fun init() {
        mPresenter = OrderDisposePresenter(context!!, this)

        /****** 认证未通过时显示空状态  ******/
        if (!ProjectUtils.certificationStatus()) {
            loading_refresh.visibility = View.GONE
            tv_order_disposer_null_2.visibility = View.VISIBLE
        } else {
            mAdapter = context?.let { OrderDisposeAdapter(it, null) }
            rcl_order_disposer.layoutManager = LinearLayoutManager(activity, 1, false)
            rcl_order_disposer.adapter = mAdapter
            val decoration = DisplayUtils.dp2px(App.mContext, 15f)
            rcl_order_disposer.addItemDecoration(ItemDecorationHelper(decoration, decoration, decoration, 0, decoration))
            mPresenter.getOrderDispose("0")
        }

        /****** 接受更新的律师信息 ******/
        RxBus.instance.toObserverable(LawyersInfo::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    /****** 认证未通过时显示空状态  ******/
                    if (!ProjectUtils.certificationStatus()) {
                        loading_refresh.visibility = View.GONE
                        tv_order_disposer_null_2.visibility = View.VISIBLE
                    } else {
                        loading_refresh.visibility = View.VISIBLE
                        tv_order_disposer_null_2.visibility = View.GONE
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
                    mPresenter.getOrderDispose((page + 1).toString())
                } else {
                    offLoadMore()
                }
            }
        })

        //下啦刷新
        loading_refresh.setOnRefreshListener(object : PullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {

                mPresenter.getOrderDispose("0")
                loading_refresh?.finishRefreshing()
            }
        }, 1)

        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val intent = Intent(context, OrderActivity::class.java)
                val data = mAdapter!!.getCurrentItem()
                intent.putExtra("orderId", data.id)
                intent.putExtra("orderState", 2)
                startActivity(intent)
            }
        })

        mAdapter?.setOnItemOrderListener(object : OrderDisposeAdapter.ItemOrderListener {
            override fun onRefusedListener() {
                val data = mAdapter!!.getCurrentItem()
                mPresenter.rejectMyOrder(data.id!!)
            }

            override fun onAcceptListener() {
                val data = mAdapter!!.getCurrentItem()
                mPresenter.acceptMyOrder(data.id!!)
            }
        })
    }

    override fun offLoadMore() {
        //加载更多取消
        if (loading_more!!.isLoading) {
            loading_more?.finishLoading()
        }
    }

    override fun updateData(clear: Boolean, data: List<OrderDispose.ContentBean>?) {
        mAdapter?.updateItems(clear, data)
    }

    override fun showNullView(show: Boolean, string: String) {
        if (show) {
            tv_order_disposer_null.text = string
            tv_order_disposer_null.visibility = View.VISIBLE
        } else {
            tv_order_disposer_null.visibility = View.GONE
        }

    }


}