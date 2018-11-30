package com.gkzxhn.legalconsulting.activity

import android.content.Intent
import android.os.Handler
import android.view.WindowManager
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo

/**
 * @classname：SplashActivity
 * @author：liushaoxiang
 * @date：2018/10/22 10:28 AM
 * @description：
 */
class SplashActivity : BaseActivity() {

    override fun provideContentViewId(): Int {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return R.layout.activity_splash

    }

    override fun init() {

        handler.sendEmptyMessageDelayed(0, 1000)
    }



    val handler = Handler(Handler.Callback {
        if (App.SP.getString(Constants.SP_TOKEN, "")?.isNotBlank()!!) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
        finish()
        false
    })
}