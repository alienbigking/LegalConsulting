package com.gkzxhn.legalconsulting.entity

/**
 * @author：liushaoxiang
 * @date：2018/10/29 2:07 PM
 * @description：崩溃日志
 * <p>
 */
data class CrashLogger(

        var phone: String = "",
        var contents: String = "",
        var deviceName: String = "",
        var sysVersion: String = "",
        var deviceType: String = "",
        var appVersion: String = ""
)
