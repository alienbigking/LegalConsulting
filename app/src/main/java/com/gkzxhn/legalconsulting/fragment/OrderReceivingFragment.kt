package com.gkzxhn.legalconsulting.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.adapter.OrderReceivingAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.utils.DisplayUtils
import com.gkzxhn.legalconsulting.utils.ItemDecorationHelper
import com.gkzxhn.legalconsulting.utils.showToast
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.conversation_fragment.*


/**
 * Explanation：待接单
 * @author LSX
 * Created on 2018/9/10.
 */

class OrderReceivingFragment : BaseFragment() {

    private var mAdapter: OrderReceivingAdapter? = null

    private var cont: Int = 0
    private var list: MutableList<String>? = null

    override fun provideContentViewId(): Int {
        return R.layout.order_receiving_fragment
    }


    override fun init() {
        list = ArrayList()
        mAdapter = context?.let { OrderReceivingAdapter(it, list) }
        rcl_conversation.layoutManager = LinearLayoutManager(activity, 1, false)
        rcl_conversation.adapter = mAdapter
        rcl_conversation.addItemDecoration(App.mContext?.let { DisplayUtils.dp2px(it, 15f) }?.let { ItemDecorationHelper(it, it, it, 0, it) })
        getData()
    }

    override fun initListener() {
        //加载更多
        loading_more?.setOnLoadMoreListener {
            cont++
            getData()
        }
        //下啦刷新
        loading_refresh?.setOnRefreshListener({
            list?.clear()
            cont = 0
            getData()
            loading_refresh?.finishRefreshing()
        }, 1)

        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                context?.showToast("点击了接单条目：" + position.toString())
            }

        })

    }


    private fun getData() {
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
            activity?.runOnUiThread(java.lang.Runnable {
                //加载更多取消
                if (loading_more!!.isLoading) {
                    loading_more?.finishLoading()
                }
                mAdapter?.updateItems(list)
            })
        }).start()

    }

}