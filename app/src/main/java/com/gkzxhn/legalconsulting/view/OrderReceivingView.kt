package com.gkzxhn.legalconsulting.view

import com.gkzxhn.legalconsulting.entity.OrderReceivingContent

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface OrderReceivingView : BaseView {

    fun offLoadMore()

    fun setLastPage(lastPage: Boolean,page:Int)

    fun updateData(clear: Boolean, data: List<OrderReceivingContent>?)

    fun showNullView(show: Boolean)
}