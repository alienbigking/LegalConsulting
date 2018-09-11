package com.gkzxhn.legalconsulting.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rx.subscriptions.CompositeSubscription


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
abstract class BaseFragment : Fragment() {

    var mCompositeSubscription: CompositeSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCompositeSubscription = CompositeSubscription()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(provideContentViewId(), container, false)
        initView(rootView)
        return rootView
    }


    abstract fun provideContentViewId(): Int


    /**
     * Explanation: 需要可以重写
     * @author LSX
     *    -----2018/9/11
     */
    open fun initView(rootView: View) {}


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initListener()
    }

    /**
     * Explanation: 统一管理监听事件
     * @author LSX
     *    -----2018/9/11
     */
    open fun initListener() {}

    abstract fun init()

    override fun onDestroy() {
        super.onDestroy()
        when {
            mCompositeSubscription?.hasSubscriptions()!! -> mCompositeSubscription?.unsubscribe()
        }
    }

}