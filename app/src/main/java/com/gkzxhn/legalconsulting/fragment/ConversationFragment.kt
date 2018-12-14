package com.gkzxhn.legalconsulting.fragment

import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.view.ConversationView
import kotlinx.android.synthetic.main.conversation_fragment.*


/**
 * Explanation: 会话
 * @author LSX
 *    -----2018/9/7
 */
class ConversationFragment : BaseFragment(), ConversationView {



    override fun init() {
    }

    override fun initListener() {
        tv_user_title.setOnClickListener {
        }

    }

    override fun provideContentViewId(): Int {
        return R.layout.conversation_fragment
    }

}
