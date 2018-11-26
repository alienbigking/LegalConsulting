package com.gkzxhn.legalconsulting.fragment

import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.presenter.ConversationPresenter
import com.gkzxhn.legalconsulting.view.ConversationView
import com.netease.nim.uikit.api.NimUIKit
import kotlinx.android.synthetic.main.conversation_fragment.*


/**
 * Explanation: 会话
 * @author LSX
 *    -----2018/9/7
 */
class ConversationFragment : BaseFragment(), ConversationView {


    lateinit var mPresenter: ConversationPresenter

    override fun init() {
        mPresenter = ConversationPresenter(App.mContext, this)
    }

    override fun initListener() {
        tv_user_title.setOnClickListener {
            NimUIKit.startP2PSession(context, "gkzxhn001");
        }

    }




    override fun provideContentViewId(): Int {
        return R.layout.conversation_fragment
    }

}
