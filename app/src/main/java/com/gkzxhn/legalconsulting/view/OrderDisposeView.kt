package com.gkzxhn.legalconsulting.view

import com.gkzxhn.legalconsulting.entity.OrderDispose

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface OrderDisposeView : BaseView {

    fun offLoadMore()

    fun updateData(data: List<OrderDispose.ContentBean>?)
}