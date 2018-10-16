package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import kotlinx.android.synthetic.main.activity_withdraw_2.*
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：WithdrawActivity
 * @author：liushaoxiang
 * @date：2018/10/16 2:10 PM
 * @description：提现
 */
class WithdrawSecondActivity : BaseActivity() {


    override fun init() {
        initTopTitle()
        ProjectUtils.addViewTouchChange(tv_withdraw_2_send)
    }

    override fun provideContentViewId(): Int {
        return R.layout.activity_withdraw_2
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "提现"
        iv_default_top_back.setOnClickListener {
            finish()
        }

    }

    fun onClickWithdraw(view: View) {
        when (view.id) {R.id.tv_withdraw_2_send -> {
            startActivity(Intent(this, WithdrawThirdActivity::class.java))

        }
        }
    }
}