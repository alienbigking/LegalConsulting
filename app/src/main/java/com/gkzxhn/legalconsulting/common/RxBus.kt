package com.gkzxhn.legalconsulting.common

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subjects.Subject

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
class RxBus {

    private val bus: Subject<Any, Any>

    init {
        bus = SerializedSubject(PublishSubject.create())
    }


    /**
     * 发送消息
     *
     * @param object
     */
    fun post(`object`: Any) {

        bus.onNext(`object`)
    }

    /**
     * 接收消息
     *
     * @param eventType
     * @param <T>
     * @return
    </T> */
    fun <T> toObserverable(eventType: Class<T>): Observable<T> {
        return bus.ofType(eventType)
    }

    companion object {

        @Volatile
        private var mInstance: RxBus? = null

        /**
         * 单例模式RxBus
         *
         * @return
         */
        val instance: RxBus
            get() {

                var rxBus2 = mInstance
                if (mInstance == null) {
                    synchronized(RxBus::class.java) {
                        rxBus2 = mInstance
                        if (mInstance == null) {
                            rxBus2 = RxBus()
                            mInstance = rxBus2
                        }
                    }
                }

                return rxBus2!!
            }
    }
}

