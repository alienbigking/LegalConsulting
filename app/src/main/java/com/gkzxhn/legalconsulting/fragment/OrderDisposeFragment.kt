package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.MainActivity
import com.gkzxhn.legalconsulting.activity.OrderActivity
import com.gkzxhn.legalconsulting.adapter.OrderDisposeAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.customview.PullToRefreshLayout
import com.gkzxhn.legalconsulting.entity.OrderReceivingContent
import com.gkzxhn.legalconsulting.presenter.OrderDisposePresenter
import com.gkzxhn.legalconsulting.utils.DisplayUtils
import com.gkzxhn.legalconsulting.utils.ItemDecorationHelper
import com.gkzxhn.legalconsulting.view.OrderDisposeView
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.order_disposer_fragment.*


/**
 * Explanation：我的咨询里面
 * @author LSX
 * Created on 2018/9/10.
 */

class OrderDisposeFragment : BaseFragment(), OrderDisposeView {


    override fun offLoadMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateData(data: List<OrderReceivingContent>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mAdapter: OrderDisposeAdapter? = null

    private var cont: Int = 0
    private var list: MutableList<String>? = null

    lateinit var mPresenter: OrderDisposePresenter


    override fun init() {
        mPresenter = OrderDisposePresenter(context!!, this)
        list = ArrayList()
        mAdapter = context?.let { OrderDisposeAdapter(it, list) }
        rcl_order_disposer.layoutManager = LinearLayoutManager(activity, 1, false)
        rcl_order_disposer.adapter = mAdapter
        val decoration = DisplayUtils.dp2px(App.mContext, 15f)
        rcl_order_disposer.addItemDecoration(ItemDecorationHelper(decoration, decoration, decoration, 0, decoration))
        getData()
    }

    override fun initListener() {
        //加载更多
        loading_more.setOnLoadMoreListener(object : com.gkzxhn.legalconsulting.customview.LoadMoreWrapper.OnLoadMoreListener {
            override fun onLoadMore() {
                cont++
                getData()
            }

        })

        //下啦刷新
        loading_refresh.setOnRefreshListener(object : PullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                list?.clear()
                cont = 0
                getData()
                loading_refresh?.finishRefreshing()
            }

        }, 1)

        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                startActivity(Intent(context, OrderActivity::class.java))
            }

        })

    }


    private fun getData() {
        var activity = activity as MainActivity

        Thread(Runnable //	开启一个线程处理逻辑，然后在线程中在开启一个UI线程，当子线程中的逻辑完成之后，
        //	就会执行UI线程中的操作，将结果反馈到UI界面。
        {
            // 模拟耗时的操作，在子线程中进行。
            try {
                for (i in 0..10) {
                    list?.add("会话：")
                }
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            // 更新主线程ＵＩ，跑在主线程。
            activity.runOnUiThread(java.lang.Runnable {
                //加载更多取消
                if (loading_more!!.isLoading) {
                    loading_more?.finishLoading()
                }
                mAdapter?.updateItems(list)
            })
        }).start()

    }


    override fun provideContentViewId(): Int {
        return R.layout.order_disposer_fragment
    }


}