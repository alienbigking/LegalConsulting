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
//            NimUIKit.startP2PSession(mContext, "9f9469948f76456d850b9f3bed1ddc10")
        }

    }

    override fun provideContentViewId(): Int {
        return R.layout.conversation_fragment
    }

}
