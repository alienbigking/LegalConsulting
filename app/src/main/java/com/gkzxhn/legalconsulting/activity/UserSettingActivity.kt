package com.gkzxhn.legalconsulting.activity

import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：UserSettingActivity
 * @author：liushaoxiang
 * @date：2018/10/12 3:52 PM
 * @description：个人账号设置
 */
class UserSettingActivity : BaseActivity() {
    override fun provideContentViewId(): Int {
        return R.layout.activity_user_setting
    }

    override fun init() {
        initTopTitle()
        ProjectUtils.addViewTouchChange(tv_user_setting_change_phone)
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "个人账号"
        iv_default_top_back.setOnClickListener {
            finish()
        }


    }


    fun onClickUserSetting(view: View) {
        when (view.id) {

        /****** 姓名 ******/
            R.id.v_user_setting_name_bg -> {

            }
        /****** 个人头像 ******/
            R.id.v_user_setting_photo_bg -> {

            }
        /****** 手机号 ******/
            R.id.v_user_setting_phone_bg -> {

            }
        /****** 更换手机号 ******/
            R.id.tv_user_setting_change_phone -> {

            }

        }
    }
}

