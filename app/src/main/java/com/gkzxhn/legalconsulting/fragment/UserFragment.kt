package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.OrderGetSettingActivity
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationShowActivity
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants.ORDER_GET_STATE
import com.gkzxhn.legalconsulting.common.Constants.SP_ORDER_GET_STATE
import com.gkzxhn.legalconsulting.utils.showToast
import kotlinx.android.synthetic.main.user_fragment.*


/**
 * Explanation: 个人中心
 * @author LSX
 *    -----2018/9/7
 */
class UserFragment : BaseFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.v_user_get_order_bg -> {
                val intent = Intent(context, OrderGetSettingActivity::class.java)
                val state = if (tv_user_fragment_get_order_state.text == "忙碌") "1" else "2"
                intent.putExtra(ORDER_GET_STATE, state)
                context?.startActivity(intent)
            }
            R.id.v_user_my_money_bg -> {
                context?.showToast("我的赏金")
            }
            R.id.v_user_rz_bg -> {
                context?.startActivity(Intent(context, QualificationAuthenticationShowActivity::class.java))
            }
            R.id.v_user_all_order_bg -> {
                context?.showToast("所有订单")
            }
            R.id.v_user_set_bg -> {
                context?.showToast("设置")
            }
        }
    }

    override fun init() {
        tv_user_fragment_get_order_state.text = if (App.SP?.getString(SP_ORDER_GET_STATE, "") == "1") "忙碌" else "接单"
    }

    override fun initListener() {
        v_user_get_order_bg.setOnClickListener(this)
        v_user_my_money_bg.setOnClickListener(this)
        v_user_rz_bg.setOnClickListener(this)
        v_user_all_order_bg.setOnClickListener(this)
        v_user_set_bg.setOnClickListener(this)

    }

    override fun provideContentViewId(): Int {
        return R.layout.user_fragment
    }

    override fun onResume() {
        init()
        super.onResume()
    }

}
