package com.gkzxhn.legalconsulting.utils

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor


/**
 * Created by 方 on 2017/8/9.
 */

class RxBus private constructor() {

    private lateinit var mBus: FlowableProcessor<Any>

    init {
        // toSerialized method made bus thread safe
        mBus = PublishProcessor.create<Any>().toSerialized()
    }

    companion object {
        val instance: RxBus by lazy { Holder.BUS }
    }

    fun post(obj: Any) {
        mBus.onNext(obj)
    }

    fun <T> toFlowable(tClass: Class<T>): Flowable<T> {
        return mBus.ofType(tClass)
    }

    fun toFlowable(): Flowable<Any> {
        return mBus
    }

    fun hasSubscribers(): Boolean {
        return mBus.hasSubscribers()
    }

    fun unregisterAll() {
        //会将所有由mBus生成的Flowable都置completed状态后续的所有消息都收不到了
        mBus.onComplete()
    }

    private object Holder {
        val BUS = RxBus()
    }
}