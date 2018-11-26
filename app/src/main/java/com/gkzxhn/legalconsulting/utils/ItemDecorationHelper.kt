package com.gkzxhn.legalconsulting.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView


/**
 * Explanation：适配器间距帮助类
 * @author LSX
 * Created on 2018/9/10.
 */


class ItemDecorationHelper(left: Int, top: Int, right: Int, bottom: Int, firstTop: Int) : RecyclerView.ItemDecoration() {


    private var left: Int = left
    private var right: Int = right
    private var bottom: Int = bottom
    private var top: Int = top
    private var firstTop: Int = firstTop

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        super.getItemOffsets(outRect, itemPosition, parent)
        outRect.left = left
        outRect.right = right
        outRect.bottom = bottom
        if (itemPosition == 0) {
            outRect.top = firstTop
        } else {
            outRect.top = top
        }
    }
}