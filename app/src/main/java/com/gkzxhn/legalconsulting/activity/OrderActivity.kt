package com.gkzxhn.legalconsulting.activity

import android.graphics.Bitmap
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.OrderPresenter
import com.gkzxhn.legalconsulting.utils.ProjectUtils
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
    var bitmap1: Bitmap? = null
    var bitmap2: Bitmap? = null
    var bitmap3: Bitmap? = null
    var bitmap4: Bitmap? = null

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

        /****** 拒绝订单 ******/
        tv_order_reject.setOnClickListener {
            mPresenter.rejectMyOrder(orderID)
        }
        /****** 接受订单 ******/
        tv_order_accept.setOnClickListener {
            mPresenter.acceptMyOrder(orderID)
        }

        iv_oder_image1.setOnClickListener {
            iv_order_big.visibility = View.VISIBLE
            iv_order_big.setImageBitmap(bitmap1)
        }

        iv_oder_image2.setOnClickListener {
            iv_order_big.visibility = View.VISIBLE
            iv_order_big.setImageBitmap(bitmap2)
        }
        iv_oder_image3.setOnClickListener {
            iv_order_big.visibility = View.VISIBLE
            iv_order_big.setImageBitmap(bitmap3)
        }
        iv_oder_image4.setOnClickListener {
            iv_order_big.visibility = View.VISIBLE
            iv_order_big.setImageBitmap(bitmap4)
        }

        /****** 大图 ******/
        iv_order_big.setOnClickListener {
            iv_order_big.visibility = View.GONE
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

    override fun setOrderImage(avatarThumb: String?) {
        if (avatarThumb != null) {
            ProjectUtils.loadImage(this, avatarThumb, iv_oder_icon)
        }
    }


    override fun setTime(time: String) {
        tv_order_time.text = time
    }

    override fun getOrderMoeny(): String {
        return et_order_money.text.trim().toString()
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

    override fun setNextText(str: String) {
        tv_order_next.visibility = View.VISIBLE
        tv_order_next.text = str
    }

    override fun setOrderState(str: String) {
        tv_order_state.text = str
    }

    override fun setImage1(bitmap: Bitmap) {
        iv_oder_image1.visibility = View.VISIBLE
        bitmap1 = bitmap
        iv_oder_image1.setImageBitmap(bitmap)
    }

    override fun setImage2(bitmap: Bitmap) {
        iv_oder_image2.visibility = View.VISIBLE
        bitmap2 = bitmap
        iv_oder_image2.setImageBitmap(bitmap)
    }
    override fun setImage3(bitmap: Bitmap) {
        iv_oder_image3.visibility = View.VISIBLE
        bitmap3 = bitmap
        iv_oder_image3.setImageBitmap(bitmap)
    }
    override fun setImage4(bitmap: Bitmap) {
        iv_oder_image4.visibility = View.VISIBLE
        bitmap4 = bitmap
        iv_oder_image4.setImageBitmap(bitmap)
    }

    override fun setAllbgColor(color: Int) {
        ll_order_all.setBackgroundColor(color)
    }

    override fun setOrderStateColor(color: Int) {
        tv_order_state.setTextColor(color)
    }

    override fun setBottomSelectVisibility(visibility: Int) {
        cl_order_bottom_select.visibility = visibility
        v_order_money_white_bg.visibility = visibility
        et_order_money.visibility = visibility
        tv_order_money_title3.visibility = visibility
        tv_order_money_title2.visibility = visibility
        v_order_money_title.visibility = visibility
        tv_order_money_title.visibility = visibility
    }

    override fun setShowOrderInfo(visibility: Int, time: String, name: String) {
        tv_order_get_time.visibility = visibility
        tv_order_get_time.text = time
        tv_order_state_name.visibility = visibility
        tv_order_state_name.text = name
        v_order_state_name.visibility = visibility
        v_order_white_bg.visibility = visibility
    }

    override fun onBackPressed() {
        if (iv_order_big.visibility == View.GONE) {
            super.onBackPressed()
        }else{
            iv_order_big.visibility = View.GONE
        }
    }

}