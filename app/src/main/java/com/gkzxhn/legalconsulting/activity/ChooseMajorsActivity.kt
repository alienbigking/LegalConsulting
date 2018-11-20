package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.utils.showToast
import kotlinx.android.synthetic.main.activity_choose_majors.*
import kotlinx.android.synthetic.main.default_top.*

/**
 * @classname：ChooseMajorsActivity
 * @author：liushaoxiang
 * @date：2018/10/15 3:51 PM
 * @description：专业领域 选择
 */

class ChooseMajorsActivity : BaseActivity() {

    var selectNumber = 0
    var selectString: ArrayList<String>? = arrayListOf()
    override fun init() {
        initTopTitle()
        initSelect()
    }

    private fun initSelect() {
        val intentSelectString = intent.getStringArrayListExtra(Constants.INTENT_SELECTSTRING) ?: return
        for (str: String in intentSelectString) {
            initSelect(str)
        }
    }

    override fun provideContentViewId(): Int {
        return R.layout.activity_choose_majors
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "选择专业领域"
        tv_default_top_end.visibility = View.VISIBLE
        tv_default_top_end.text = "确定"
        iv_default_top_back.setOnClickListener {
            onBackPressed()
        }
        tv_default_top_end.setOnClickListener {
            onBackPressed()
        }
    }

    fun onClickChooseMajors(view: View) {
        when (view.id) {
            R.id.v_choose_majors_bg_1 -> {
                changeViewVisibility(iv_choose_majors_1, 1)
            }
            R.id.v_choose_majors_bg_2 -> {
                changeViewVisibility(iv_choose_majors_2, 2)
            }
            R.id.v_choose_majors_bg_3 -> {
                changeViewVisibility(iv_choose_majors_3, 3)
            }
            R.id.v_choose_majors_bg_4 -> {
                changeViewVisibility(iv_choose_majors_4, 4)
            }
            R.id.v_choose_majors_bg_5 -> {
                changeViewVisibility(iv_choose_majors_5, 5)
            }
            R.id.v_choose_majors_bg_6 -> {
                changeViewVisibility(iv_choose_majors_6, 6)
            }
            R.id.v_choose_majors_bg_7 -> {
                changeViewVisibility(iv_choose_majors_7, 7)
            }
            R.id.v_choose_majors_bg_8 -> {
                changeViewVisibility(iv_choose_majors_8, 8)
            }
        }
    }

    private fun changeViewVisibility(view: View, number: Int) {

        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
            selectString?.remove(findMajors(number))
            selectNumber--
        } else if (selectNumber < 3) {
            view.visibility = View.VISIBLE
            selectString?.add(findMajors(number))
            selectNumber++
        } else {
            showToast("最多选择三个")
        }
    }

    private fun findMajors(number: Int): String {

        return when (number) {
            1 -> "财产纠纷"
            2 -> "婚姻家庭"
            3 -> "交通事故"
            4 -> "工伤赔偿"
            5 -> "合同纠纷"
            6 -> "刑事辩护"
            7 -> "房产纠纷"
            8 -> "劳动就业"
            else -> {
                "未知"
            }
        }
    }

    private fun initSelect(string: String) {
        when (string) {
            "财产纠纷" -> {
                changeViewVisibility(iv_choose_majors_1, 1)
            }
            "婚姻家庭" -> {
                changeViewVisibility(iv_choose_majors_2, 2)
            }
            "交通事故" -> {
                changeViewVisibility(iv_choose_majors_3, 3)
            }
            "工伤赔偿" -> {
                changeViewVisibility(iv_choose_majors_4, 4)
            }
            "合同纠纷" -> {
                changeViewVisibility(iv_choose_majors_5, 5)
            }
            "刑事辩护" -> {
                changeViewVisibility(iv_choose_majors_6, 6)
            }
            "房产纠纷" -> {
                changeViewVisibility(iv_choose_majors_7, 7)
            }
            "劳动就业" -> {
                changeViewVisibility(iv_choose_majors_8, 8)
            }
        }
    }

    override fun onBackPressed() {
        var intent = Intent()
        intent.putStringArrayListExtra(Constants.RESULT_CHOOSE_MAJORS, selectString)
        setResult(Constants.RESULTCODE_CHOOSE_MAJORS, intent)
        finish()
    }

}