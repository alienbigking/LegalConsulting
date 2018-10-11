package com.gkzxhn.legalconsulting.activity

import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants.ORDER_GET_STATE
import com.gkzxhn.legalconsulting.common.Constants.SP_ORDER_GET_STATE
import kotlinx.android.synthetic.main.activity_order_get_setting.*
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：OderGetSettingActivity
 * @author：liushaoxiang
 * @date：2018/10/11 9:32 AM
 * @description：  接单设置
 */

class OrderGetSettingActivity : BaseActivity() {
    var getOrderState = "1"

    override fun init() {
        initTopTitle()
        getOrderState = intent.getStringExtra(ORDER_GET_STATE)
        changeGetOrderState()
    }


    private fun initTopTitle() {
        tv_default_top_title.text = "接单设置"
        iv_default_top_back.setOnClickListener {
            finish()
        }
    }

    override fun provideContentViewId(): Int {
        return R.layout.activity_order_get_setting
    }

    fun onClickGetOrder(view: View) {
        when (view.id) {
        /****** 忙碌 ******/
            R.id.v_order_get_setting1_bg -> {
                getOrderState = "1"
                App.EDIT?.putString(SP_ORDER_GET_STATE, getOrderState)?.commit()
                changeGetOrderState()
            }
        /****** 接单 ******/
            R.id.v_order_get_setting2_bg -> {
                getOrderState = "2"
                App.EDIT?.putString(SP_ORDER_GET_STATE, getOrderState)?.commit()
                changeGetOrderState()
            }


        }
    }


    /**
     * @methodName： created by liushaoxiang on 2018/10/11 10:50 AM.
     * @description：根据状态值改变选择的状态
     */
    private fun changeGetOrderState() {
        if (getOrderState == "1") {
            iv_order_get_setting_select1.visibility = View.VISIBLE
            iv_order_get_setting_select2.visibility = View.GONE
        } else {
            iv_order_get_setting_select1.visibility = View.GONE
            iv_order_get_setting_select2.visibility = View.VISIBLE
        }
    }

}