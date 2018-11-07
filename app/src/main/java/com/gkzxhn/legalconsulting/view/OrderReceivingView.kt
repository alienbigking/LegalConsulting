package com.gkzxhn.legalconsulting.view

import com.gkzxhn.legalconsulting.entity.OrderReceivingContent

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface OrderReceivingView : BaseView {

    fun offLoadMore()

    fun updateData(data: List<OrderReceivingContent>?)
}