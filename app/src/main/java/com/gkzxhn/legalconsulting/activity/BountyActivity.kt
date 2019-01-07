package com.gkzxhn.legalconsulting.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.presenter.BountyPresenter
import com.gkzxhn.legalconsulting.view.BountyView
import kotlinx.android.synthetic.main.activity_bounty.*
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：BountyActivity
 * @author：liushaoxiang
 * @date：2018/10/11 1:39 PM
 * @description：我的赏金
 */
class BountyActivity : BaseActivity(), BountyView {

    override fun finishActivity() {
        finish()
    }

    lateinit var mPresenter: BountyPresenter

    override fun provideContentViewId(): Int {
        return R.layout.activity_bounty
    }

    override fun init() {
        initTopTitle()
        mPresenter = BountyPresenter(this, this)
        mPresenter.getLawyersInfo()
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "我的赏金"

        iv_default_top_back.setOnClickListener {
            finish()
        }
    }

    fun onClickBounty(view: View) {
        when (view.id) {
        /****** 提现 ******/
            R.id.v_bounty_get_money -> {
                mPresenter.getMoney()

            }
            R.id.v_bounty_money_list -> {
                startActivity(Intent(this, MoneyListActivity::class.java))
            }
        /****** 绑定支付宝 ******/
            R.id.v_bounty_get_alipay -> {
                mPresenter.bindOrUnbind()
            }
        }
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun setMoney(money: String) {
        tv_bounty_money.text = money
    }

    override fun setBindState(bindState: String) {
        tv_bounty_bind_state.text = bindState
    }

    override fun changeBingState(bindState: Boolean) {
        if (bindState) {
            tv_bounty_bind_state.text = "已绑定"
            tv_bounty_bind_state.setTextColor(resources.getColor(R.color.bind_blue))
        } else {
            tv_bounty_bind_state.text = "未绑定"
            tv_bounty_bind_state.setTextColor(resources.getColor(R.color.bind_gary))
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getLawyersInfo()
    }
}