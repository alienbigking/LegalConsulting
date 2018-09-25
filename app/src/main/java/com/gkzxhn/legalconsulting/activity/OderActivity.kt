package com.gkzxhn.legalconsulting.activity

import com.gkzxhn.legalconsulting.R
import kotlinx.android.synthetic.main.activity_oder.*


/**
 * Explanation：
 * @author LSX
 * Created on 2018/9/25.
 */

class OderActivity : BaseActivity() {
    override fun init() {
        iv_oder_back.setOnClickListener { finish() }

    }

    override fun provideContentViewId(): Int {

        return R.layout.activity_oder
    }

}