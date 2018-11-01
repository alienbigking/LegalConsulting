package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
import kotlinx.android.synthetic.main.activity_edit_address.*

/**
 * @classname：地址填写
 * @author：liushaoxiang
 * @date：2018/10/28 5:36 PM
 * @description：
 */

class EditAddressActivity : BaseActivity() {

    override fun init() {

        tv_edit_save.setOnClickListener {
            var intent = Intent()
            intent.putExtra(Constants.RESULT_EDIT_ADDRESS, et_edit_address.text.trim().toString())
            setResult(Constants.RESULTCODE_EDIT_ADDRESS, intent)
            finish()
        }
        iv_edit_address_back.setOnClickListener { onBackPressed() }
    }

    override fun provideContentViewId(): Int {
        return R.layout.activity_edit_address
    }

}
