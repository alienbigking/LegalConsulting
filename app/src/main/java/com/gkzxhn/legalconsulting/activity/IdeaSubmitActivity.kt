package com.gkzxhn.legalconsulting.activity

import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.showToast
import kotlinx.android.synthetic.main.activity_idea_submit.*

/**
 * @classname：IdeaSubmitActivity
 * @author：liushaoxiang
 * @date：2018/10/12 11:55 AM
 * @description：意见 反馈
 */
class IdeaSubmitActivity : BaseActivity() {
    override fun provideContentViewId(): Int {
        return R.layout.activity_idea_submit
    }

    override fun init() {
        ProjectUtils.addViewTouchChange(tv_idea_submit_send)
    }

    fun onClickIdeaSubmit(view: View) {
        when (view.id) {
            R.id.iv_idea_submit_back -> {
                finish()
            }
            R.id.tv_idea_submit_send -> {
                showToast(et_idea_submit_title.text.trim().toString()+"__"+et_idea_submit_content.text.trim().toString())
            }
        }
    }
}