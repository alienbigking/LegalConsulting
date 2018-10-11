package com.gkzxhn.legalconsulting.activity

import com.gkzxhn.legalconsulting.R
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：SettingActivtiy
 * @author：liushaoxiang
 * @date：2018/10/11 3:52 PM
 * @description：设置
 */
class SettingActivity : BaseActivity() {
    override fun provideContentViewId(): Int {
        return R.layout.activity_setting
    }

    override fun init() {
        initTopTitle()
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "设置"
        iv_default_top_back.setOnClickListener {
            finish()
        }
    }
}

