package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import kotlinx.android.synthetic.main.activity_bounty.*
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
        tv_bounty_money.text = App.SP.getString(Constants.SP_REWARDAMOUNT,"").toString()

        iv_default_top_back.setOnClickListener {
            finish()
        }
    }

    fun onClickBounty(view: View) {
        when (view.id) {
            R.id.v_bounty_get_money
            -> {
                startActivity(Intent(this, WithdrawFirstActivity::class.java))
                finish()
            }
            R.id.v_bounty_money_list
            -> {
                startActivity(Intent(this, MoneyListActivity::class.java))
            }
        }
    }
}