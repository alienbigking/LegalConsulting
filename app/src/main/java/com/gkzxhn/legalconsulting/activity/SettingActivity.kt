package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import kotlinx.android.synthetic.main.activity_setting.*
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
        ProjectUtils.addViewTouchChange(tv_setting_exit)
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "设置"
        iv_default_top_back.setOnClickListener {
            finish()
        }

    }


    fun onClickSetting(view: View) {
        when (view.id) {

        /****** 意见反馈 ******/
            R.id.v_setting_idea_bg -> {
                startActivity(Intent(this, IdeaSubmitActivity::class.java))
            }
        /****** 清除缓存 ******/
            R.id.v_setting_clear_bg -> {

            }
        /****** 版本更新 ******/
            R.id.v_setting_update_bg -> {
                initDialog()
            }
        /****** 退出账号 ******/
            R.id.tv_setting_exit -> {

            }

        }
    }

    fun initDialog() {
        val progressDialog = android.app.Dialog(this, R.style.progress_dialog)
        progressDialog.setContentView(R.layout.dialog_seeting_update)
        progressDialog.setCancelable(true)
        progressDialog.setCanceledOnTouchOutside(true)
        progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val msg = progressDialog.findViewById<TextView>(R.id.id_tv_loadingmsg) as TextView
        val save = progressDialog.findViewById<TextView>(R.id.dialog_save) as TextView
        val title = progressDialog.findViewById<TextView>(R.id.dialog_title) as TextView
        title.text = "发现新版"
        save.text = "更新"
        save.setOnClickListener { _ ->
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
//            val content_url = Uri.parse(url)
//            intent.data = content_url
//            context.startActivity(intent)
            progressDialog.dismiss()
        }
//        msg.text = message
        progressDialog.show()
    }

}

