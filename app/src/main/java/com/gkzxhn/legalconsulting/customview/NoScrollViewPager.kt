package com.gkzxhn.legalconsulting.customview

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


/**
 * @classname：NoScrollViewPager
 * @author：PrivateXiaoWu
 * @date：2018/9/7 16:06
 * @description： 不可划动
 */

class NoScrollViewPager : ViewPager {


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    // 事件拦截
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false// 不拦截子控件的事件
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // 重写此方法, 触摸时什么都不做, 从而实现对滑动事件的禁用
        return true
    }

}
