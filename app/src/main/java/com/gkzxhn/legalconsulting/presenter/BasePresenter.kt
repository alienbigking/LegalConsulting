package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import com.gkzxhn.legalconsulting.model.IBaseModel
import com.gkzxhn.legalconsulting.view.BaseView
import java.lang.ref.WeakReference

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */
open class BasePresenter<M : IBaseModel, V : BaseView>(context: Context, protected val mModel: M, view: V)  {

    protected var mWeakContext: WeakReference<Context>? = null //弱引用Context
    protected val UNAUTHCODE = "401"
    protected val PAGE_SIZE = 100//分页 每页数量
    protected val FIRST_PAGE = 1//分页 起始页
    protected var currentPage = FIRST_PAGE //分页 当前页面
    protected var mWeakView: WeakReference<V>? = null//弱引用 view

    init {
            mWeakContext = WeakReference(context)
            mWeakView = WeakReference(view)
    }

    var mView: V? = null
        get() =  mWeakView?.get()

    var mContext: Context?=null
        get() = mWeakContext?.get()

}