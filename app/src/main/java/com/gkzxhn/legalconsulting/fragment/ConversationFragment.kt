package com.gkzxhn.legalconsulting.fragment

import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.presenter.ConversationPresenter
import com.gkzxhn.legalconsulting.view.ConversationView
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
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
//            loginNim("106ce735677e44f382e955bc9fe02968","9a7dc1a29268455db5d75b5f4d94ca47")
//            NimUIKit.startP2PSession(context, "106ce735677e44f382e955bc9fe02968");
//            NimUIKit.startP2PSession(context, "b56d5db58b824fe2b2a51c1956a51b63");
//            NimUIKit.setMsgForwardFilter { false }
//            NimUIKit.setMsgRevokeFilter { false }
        }

    }

    /**
     * 登录云信
     * @param account
     * @param pwd
     */
    private fun loginNim(account: String, pwd: String) {

        val loginInfo = LoginInfo(account, pwd)

        NIMClient.getService(AuthService::class.java).login(loginInfo).setCallback(object : RequestCallback<LoginInfo> {
            override fun onSuccess(param: LoginInfo) {
                App.EDIT.putString(Constants.SP_IM_ACCOUNT,param.account).commit()
                App.EDIT.putString(Constants.SP_IM_TOKEN,param.token).commit()
                NimUIKit.setAccount(account)
            }

            override fun onFailed(code: Int) {
            }

            override fun onException(exception: Throwable) {

            }
        })
    }





    override fun provideContentViewId(): Int {
        return R.layout.conversation_fragment
    }

}
