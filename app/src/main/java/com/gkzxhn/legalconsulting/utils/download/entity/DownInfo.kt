package com.gkzxhn.legalconsulting.utils.download.entity

import com.gkzxhn.legalconsulting.net.ApiService
import com.gkzxhn.legalconsulting.utils.download.HttpProgressOnNextListener


/**
 * @classname：
 * @author：liushaoxiang
 * @date：2018/11/6 5:00 PM
 * @description：
 */
class DownInfo {
    /*存储位置*/
    var savePath: String? = null
    /*下载url*/
    var url: String? = null
    /*基础url*/
    var baseUrl: String? = null
    /*文件总长度*/
    var countLength: Long = 0
    /*下载长度*/
    var readLength: Long = 0
    /*下载唯一的HttpService*/
    var service: ApiService? = null
    /*回调监听*/
    var listener: HttpProgressOnNextListener<DownInfo>? = null
    /*超时设置*/
    var DEFAULT_TIMEOUT = 6
    /*下载状态*/
    var state: DownState? = null
    /* 连接超时时间, 秒*/
    val connectionTimeout: Long = 20
}

enum class DownState {
    START,
    DOWN,
    PAUSE,
    STOP,
    ERROR,
    FINISH
}