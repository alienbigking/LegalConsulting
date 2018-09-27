package com.liushaoxiang.espressoautotest.idlingregistry

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * 自动测试使用
 * Created by Raleigh.Luo on 17/12/14.
 */
class SimpleIdlingResource : IdlingResource {
    @Volatile private var mCallback: IdlingResource.ResourceCallback? = null

    // Idleness is controlled with this boolean.
    private val mIsIdleNow = AtomicBoolean(true)

    /**
     * 必须返回代表idling resource的非空字符串
     * 用于日志显示的名字，可随意取
     */
    override fun getName(): String {
        return this::class.java.name;
    }

    /**是否是空闲状态
     * 返回当前idlingresource的idle状态。如果返回true，onTransitionToIdle()上注册的ResourceCallback必须必须在之前已经调用；
     */
    override fun isIdleNow(): Boolean {
        return mIsIdleNow.get()
    }

    /**注册变成空闲的回调
     * 通常此方法用于存储对回调的引用来通知idle状态的变化。
     */
    override fun registerIdleTransitionCallback(callBack: IdlingResource.ResourceCallback?) {
        mCallback = callBack
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the [ResourceCallback].
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    fun setIdleState(isIdleNow: Boolean) {
        mIsIdleNow.set(isIdleNow)
        if (isIdleNow && mCallback != null) {
            mCallback!!.onTransitionToIdle()
        }
    }
}