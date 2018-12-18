package com.gkzxhn.legalconsulting.utils.download

import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.net.MySubscriber
import com.gkzxhn.legalconsulting.utils.download.DownloadResponseBody.DownloadProgressListener
import com.gkzxhn.legalconsulting.utils.download.entity.DownInfo
import com.gkzxhn.legalconsulting.utils.download.entity.DownState
import com.gkzxhn.legalconsulting.utils.logE


/**
 * @classname：
 * * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * @author：liushaoxiang
 * @date：2018/11/6 5:00 PM
 * @description：
 */
class ProgressDownSubscriber<T> : MySubscriber<T>, DownloadProgressListener {


    //弱引用结果回调
    private var mSubscriberOnNextListener: HttpProgressOnNextListener<DownInfo>

    private lateinit var downInfo: DownInfo

    constructor(/*下载数据*/downInfo: DownInfo) {
        this.downInfo = downInfo
        mSubscriberOnNextListener = downInfo.listener!!
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    override fun onCompleted() {
        mSubscriberOnNextListener.onComplete()
        downInfo.state = DownState.FINISH
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    override fun onStart() {
        super.onStart()
        "onStart onSubscribe-----".logE(this)
        mSubscriberOnNextListener.onStart()
        downInfo.state = DownState.START
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    override fun onError(e: Throwable) {
        /*停止下载*/
        HttpDownManager.getInstance().stopDown(downInfo)
        mSubscriberOnNextListener.onError(e)
        downInfo.state = (DownState.ERROR)
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    override fun onNext(t: T) {
        (mSubscriberOnNextListener as HttpProgressOnNextListener<T>)?.onNext(t)
    }

    override fun update(read: Long, count: Long, done: Boolean) {
        var read = read
        if (downInfo.countLength > count) {
            read += downInfo.countLength - count
        } else {
            downInfo.countLength = count
        }
        downInfo.readLength = read
        RxBus.instance.post(downInfo)
        /*接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力*/
//            Observable.just(read).observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({
        "read>>>>>>>>>>>>>>${downInfo.readLength}----${downInfo.countLength}".logE(this)
//                        mSubscriberOnNextListener.updateProgress(it, downInfo.countLength)
//                        /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
//                        if (downInfo.state == DownState.PAUSE || downInfo.state == DownState.STOP)
//                            downInfo.state = DownState.DOWN
//                    })
    }

}