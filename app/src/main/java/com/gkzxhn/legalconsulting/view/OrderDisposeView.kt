package com.gkzxhn.legalconsulting.view

import com.gkzxhn.legalconsulting.entity.OrderDispose

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface OrderDisposeView : BaseView {

    fun offLoadMore()

    fun updateData(clear: Boolean,data: List<OrderDispose.ContentBean>?)

    fun setLastPage(lastPage: Boolean,page:Int)

    fun showNullView(show:Boolean,string: String)

}