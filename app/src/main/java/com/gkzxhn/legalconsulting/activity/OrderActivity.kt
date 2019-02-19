package com.gkzxhn.legalconsulting.activity

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.OrderPresenter
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.dp2px
import com.gkzxhn.legalconsulting.utils.showToast
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
            } else {
                if (ProjectUtils.certificationStatus()) {
                    /****** 抢单 ******/
                    mPresenter.acceptRushOrder(orderID)
                } else {
                    showToast("您认证尚未通过，不能进行此操作！")
                }
            }
        }

    }

    override fun onFinish() {
        finish()
    }

    override fun setName(name: String) {
        tv_order_name.text = name
    }

    override fun setOrderImage(userName: String?) {
        if (userName != null && userName.isNotEmpty()) {
            ProjectUtils.loadRoundImageByUserName(this, userName, iv_oder_icon)
        }
    }


    override fun setTime(time: String) {
        tv_order_time.text = time
    }

    override fun setOrderNumber(time: String) {
        tv_order_number.text = "编号：$time"
    }

    override fun setOrderType(str1: String) {
        tv_order_type1.text = str1
    }

    override fun setReward(reward: String) {
        tv_order_price.text = reward
    }

    override fun setNextText(visibility :Int,str: String) {
        tv_order_next.visibility = visibility
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

    override fun setOrderStateNameColor(color: Int) {
        tv_order_state_name.setTextColor(color)
    }


    /****** 订单状态栏的显示 ******/
    override fun setShowOrderState(visibility: Int, stateName: String, getTime: String, completeTime: String) {
        v_order_explain.visibility = visibility
        tv_order_state_name.visibility = visibility
        tv_order_get_time.visibility = visibility
        tv_order_complete_time.visibility = visibility
        tv_order_state_name.text = stateName
        tv_order_get_time.text = getTime
        tv_order_complete_time.text = completeTime
        if (completeTime.isEmpty()) {
            tv_order_complete_time.visibility = View.GONE
        }
    }

    /****** 到账栏的显示 ******/
    override fun setShowGetMoney(visibility: Int, getMoney: String, getMoneyTime: String) {
        v_order_complete_time.visibility = visibility
        tv_order_get_money.visibility = visibility
        tv_order_get_money_time.visibility = visibility
        tv_order_get_money.text = getMoney
        tv_order_get_money_time.text = getMoneyTime
    }

    /****** 评价栏的显示 ******/
    override fun setShowEvaluation(visibility: Int, ServiceSesults: String, ServiceInfo: String, star: Int) {
        v_order_get_money_time.visibility = visibility
        tv_order_server.visibility = visibility
        tv_order_server_end.visibility = visibility
        tv_order_evaluation.visibility = visibility
        ll_order_server_star.visibility = visibility
        tv_order_server_end.text = ServiceSesults
        tv_order_evaluation.text = ServiceInfo

        ll_order_server_star.removeAllViews()
        for (a in 1..star) {
            var image = ImageView(this)
            image.setImageResource(R.mipmap.ic_star)
            val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, dp2px(5f), 0)
            image.layoutParams = layoutParams
            ll_order_server_star.addView(image)
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }

}