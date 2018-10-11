package com.gkzxhn.legalconsulting.activity

import com.gkzxhn.legalconsulting.R
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：BountyActivity
 * @author：liushaoxiang
 * @date：2018/10/11 1:39 PM
 * @description：我的赏金
 */
class BountyActivity : BaseActivity() {


    override fun provideContentViewId(): Int {
        return R.layout.activity_bounty
    }

    override fun init() {
        initTopTitle()

    }

    private fun initTopTitle() {
        tv_default_top_title.text = "我的赏金"
        iv_default_top_back.setOnClickListener {
            finish()
        }
    }
}