package com.gkzxhn.legalconsulting.activity

import com.gkzxhn.legalconsulting.R
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：AllOrderActivity
 * @author：liushaoxiang
 * @date：2018/10/12 11:55 AM
 * @description：所有订单
 */
class AllOrderActivity : BaseActivity() {
    override fun provideContentViewId(): Int {
        return R.layout.activity_all_order
    }

    override fun init() {
        initTopTitle()
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "所有订单"
        iv_default_top_back.setOnClickListener {
            finish()
        }

    }

}