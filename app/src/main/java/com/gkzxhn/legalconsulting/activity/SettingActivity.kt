package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.SystemUtil
import com.gkzxhn.legalconsulting.utils.selectDialog
import com.gkzxhn.legalconsulting.utils.showToast
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

        tv_setting_clear_size.text = SystemUtil.getTotalCacheSize(this)

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
                clearDialog()
            }
        /****** 版本更新 ******/
            R.id.v_setting_update_bg -> {
                initDialog()
            }
        /****** 退出账号 ******/
            R.id.tv_setting_exit -> {
                exit()
            }
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/12 4:33 PM.
     * @description：清理缓存的dialog处理
     */
    private fun clearDialog() {
        val selectDialog = selectDialog("确认清除吗？", false)
        selectDialog.findViewById<TextView>(R.id.dialog_save).setOnClickListener {
            SystemUtil.clearAllCache(this)
            tv_setting_clear_size.text = SystemUtil.getTotalCacheSize(this)
            showToast("清除完成")
            selectDialog.dismiss()
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/12 3:58 PM.
     * @description：加载更新的Dialog
     */
    private fun initDialog() {
        val progressDialog = android.app.Dialog(this, R.style.progress_dialog)
        progressDialog.setContentView(R.layout.dialog_seeting_update)
        progressDialog.setCancelable(true)
        progressDialog.setCanceledOnTouchOutside(true)
        progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val cancel = progressDialog.findViewById(R.id.tv_update_dialog_cancel) as TextView
        val update = progressDialog.findViewById(R.id.tv_update_dialog_update) as TextView
        cancel.setOnClickListener { _ ->
            progressDialog.dismiss()
        }
        update.setOnClickListener { _ ->
            progressDialog.dismiss()
        }
        progressDialog.show()
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:10 PM.
     * @description：退出账号
     */
    private fun exit() {
        val selectDialog = selectDialog("确认退出账号吗？", false)
        selectDialog.findViewById<TextView>(R.id.dialog_save).setOnClickListener {
            App.EDIT?.putString(Constants.SP_TOKEN,"")?.commit()

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            selectDialog.dismiss()
        }
    }

}

