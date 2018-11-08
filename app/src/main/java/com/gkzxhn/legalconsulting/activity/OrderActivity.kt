package com.gkzxhn.legalconsulting.activity

import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.OrderPresenter
import com.gkzxhn.legalconsulting.view.OrderView
import kotlinx.android.synthetic.main.activity_oder.*
import kotlinx.android.synthetic.main.default_top.*


/**
 * Explanation：订单详情
 * @author LSX
 * Created on 2018/9/25.
 */

class OrderActivity : BaseActivity(), OrderView {

    lateinit var mPresenter: OrderPresenter

    override fun provideContentViewId(): Int {
        return R.layout.activity_oder
    }


    override fun init() {
        mPresenter = OrderPresenter(this, this)
        iv_default_top_back.setOnClickListener { finish() }
        tv_default_top_title.text = "订单详情"

        val orderID = intent.getStringExtra("orderId")
        val orderState = intent.getIntExtra("orderState", 0)

        when (orderState) {
        /****** 1，获取 抢单的明细 ******/
            1 -> mPresenter.getOrderRushInfo(orderID)
        /****** 2，获取 指定订单的明细 ******/
            2 -> mPresenter.getOrderMyInfo(orderID)
        }

        tv_order_next.setOnClickListener {
            if (tv_order_next.text.trim().toString() == resources.getString(R.string.send_message)) {
                /****** 发消息 ******/
                mPresenter.sendMessage()
            }else{
                /****** 抢单 ******/
                mPresenter.acceptRushOrder(orderID)
            }
        }

        /****** 拒绝订单 ******/
        tv_order_reject.setOnClickListener {
            mPresenter.rejectMyOrder(orderID)
        }
        /****** 接受订单 ******/
        tv_order_accept.setOnClickListener {
            mPresenter.acceptMyOrder(orderID)

        }

    }

    override fun onFinish() {
        finish()
    }

    override fun setDescription(description: String) {
        tv_order_context.text = description
    }

    override fun setName(name: String) {
        tv_order_name.text = name
    }

    override fun setTime(time: String) {
        tv_order_time.text = time
    }

    override fun setReward(reward: String) {
        tv_order_price.text = reward
    }

    override fun setNextText(str: String) {
        tv_order_next.visibility = View.VISIBLE
        tv_order_next.text = str
    }

    override fun setOrderState(str: String) {
        tv_order_state.text = str
    }

    override fun setAllbgColor(color: Int) {
        ll_order_all.setBackgroundColor(color)
    }

    override fun setOrderStateColor(color: Int) {
        tv_order_state.setTextColor(color)
    }

    override fun setBottomSelectVisibility(visibility: Int) {
        cl_order_bottom_select.visibility = visibility
    }

    override fun setShowOrderInfo(visibility: Int, time: String, name: String) {
        tv_order_get_time.visibility = visibility
        tv_order_get_time.text=time
        tv_order_state_name.visibility = visibility
        tv_order_state_name.text=name
        v_order_state_name.visibility = visibility

    }


}