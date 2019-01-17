package com.gkzxhn.legalconsulting.utils

import com.netease.nim.avchatkit.AVChatKit
import com.netease.nim.avchatkit.common.log.ILogUtil
import com.netease.nim.uikit.common.util.log.LogUtil

/**
 * Created by winnie on 2017/12/21.
 */

object LogHelper {

    fun init() {
        AVChatKit.setiLogUtil(object : ILogUtil {
            override fun ui(msg: String) {
                LogUtil.ui(msg)
            }

            override fun e(tag: String, msg: String) {
                LogUtil.e(tag, msg)
            }

            override fun i(tag: String, msg: String) {
                LogUtil.i(tag, msg)
            }

            override fun d(tag: String, msg: String) {
                LogUtil.d(tag, msg)
            }
        })
    }
}
