package com.gkzxhn.legalconsulting.customview

import android.content.Context
import android.support.v4.view.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.LinearLayout
import com.gkzxhn.legalconsulting.R


/**
 * @classname：LoadMoreWrapper
 * @author：PrivateXiaoWu
 * @date：2018/9/10 17:17
 * @description： 上拉自动加载更多
 */

class LoadMoreWrapper(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), NestedScrollingParent, NestedScrollingChild, PullToRefreshLayout.OnChildScrollUpCallback {

    private val mNestedScrollingParentHelper: NestedScrollingParentHelper
    private val mNestedScrollingChildHelper: NestedScrollingChildHelper

    private val mFooterView: View
    private var mTarget: View? = null

    var isLoading: Boolean = false
        set(loading) {
            if (loading != isLoading) {
                field = loading
                if (isLoading) {
                    addView(mFooterView, childCount)
                    postDelayed({
                        if (mTarget is AbsListView) {
                            val absListView = mTarget as AbsListView?
                            absListView!!.setSelection(absListView.count)
                        } else if (mTarget is RecyclerView) {
                            val recyclerView = mTarget as RecyclerView?
                            recyclerView!!.smoothScrollToPosition(recyclerView.layoutManager?.itemCount!!)
                        }
                    }, 10)
                } else {
                    removeView(mFooterView)
                }
            }
        }
    private var mListener: OnLoadMoreListener? = null

    init {
        orientation = LinearLayout.VERTICAL
        mFooterView = LayoutInflater.from(context).inflate(R.layout.common_refresh_load_more, null)
        mNestedScrollingParentHelper = NestedScrollingParentHelper(this)
        mNestedScrollingChildHelper = NestedScrollingChildHelper(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTarget = getChildAt(0)
        if (mTarget is AbsListView) {
            (mTarget as AbsListView).setOnScrollListener(object : AbsListView.OnScrollListener {
                override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                    if (view.lastVisiblePosition == view.adapter.count - 1
                            && !isLoading
                            && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)) {
                        loadMore()
                    }
                }

                override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int,
                                      totalItemCount: Int) {
                }
            })
        } else if (mTarget is RecyclerView) {
            (mTarget as RecyclerView).addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val lm = recyclerView.layoutManager
                    if (lm is LinearLayoutManager) {
                        if (lm.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1
                                && !isLoading
                                && (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                            loadMore()
                        }
                    } else if (lm is StaggeredGridLayoutManager) {
                        val lastPosition = getLastPosition(lm)
                        if (lastPosition == lm.itemCount - 1
                                && !isLoading
                                && (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                            loadMore()
                        }
                    }
                }
            })
        }
    }

    private fun getLastPosition(sglm: StaggeredGridLayoutManager): Int {
        var lastPosition = 0
        val spanCount = sglm.spanCount
        val lastPositions = IntArray(spanCount)
        sglm.findLastCompletelyVisibleItemPositions(lastPositions)
        for (i in 0 until spanCount) {
            lastPosition = Math.max(lastPosition, lastPositions[i])
        }
        return lastPosition
    }

    private fun loadMore() {
        isLoading = true
        if (mListener != null) {
            mListener!!.onLoadMore()
        }
    }

    fun finishLoading() {
        isLoading = false
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        mListener = listener
    }

    override fun canChildScrollUp(parent: PullToRefreshLayout, child: View?): Boolean {
        return canChildScrollUp()
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    fun canChildScrollUp(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget is AbsListView) {
                val absListView = mTarget as AbsListView?
                absListView!!.childCount > 0 && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(0).top < absListView.paddingTop)
            } else {
                ViewCompat.canScrollVertically(mTarget!!, -1) || mTarget!!.scrollY > 0
            }
        } else {
            ViewCompat.canScrollVertically(mTarget!!, -1)
        }
    }

    // NestedScrollingParent

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return isEnabled && nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes)
        // Dispatch up to the nested parent
        startNestedScroll(axes and ViewCompat.SCROLL_AXIS_VERTICAL)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // Re-dispatch up the tree by default
        dispatchNestedPreScroll(dx, dy, consumed, null)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int,
                                dxUnconsumed: Int, dyUnconsumed: Int) {
        // Re-dispatch up the tree by default
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
    }

    override fun getNestedScrollAxes(): Int {
        return mNestedScrollingParentHelper.nestedScrollAxes
    }

    override fun onStopNestedScroll(target: View) {
        mNestedScrollingParentHelper.onStopNestedScroll(target)
        // Dispatch up our nested parent
        stopNestedScroll()
    }

    override fun onNestedPreFling(target: View, velocityX: Float,
                                  velocityY: Float): Boolean {
        return dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float,
                               consumed: Boolean): Boolean {
        return dispatchNestedFling(velocityX, velocityY, consumed)
    }

    // NestedScrollingChild

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mNestedScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mNestedScrollingChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mNestedScrollingChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                      dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    companion object {
        private val TAG = LoadMoreWrapper::class.java.simpleName
    }
}
