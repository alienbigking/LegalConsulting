package com.gkzxhn.legalconsulting.customview


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.IntDef
import android.support.v4.view.*
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.Transformation
import android.widget.*
import com.gkzxhn.legalconsulting.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.text.SimpleDateFormat
import java.util.*


/**
 * Explanation: 下拉刷新
 * @author LSX
 *    -----2018/9/12
 */
class PullToRefreshLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), NestedScrollingParent, NestedScrollingChild {


    private val mHeaderWrapper: View

    private val mDecelerateInterpolator: DecelerateInterpolator
    private val mTvRefreshTip: TextView

    /**
     * 下拉刷新的回调接口
     */
    private var mListener: OnRefreshListener? = null

    private val mPreferences: SharedPreferences

    /**
     * 下拉头
     */
    private lateinit var mHeader: View

    /**
     * 刷新时显示的进度条
     */
    private val mProgressBar: ProgressBar

    /**
     * 指示下拉和释放的箭头
     */
    private val mIvArrow: ImageView

    /**
     * 上次更新时间的文字描述
     */
    private val mTvLastUpdateTime: TextView

    /**
     * 下拉头的布局参数
     */
    private var mHeaderLayoutParams: ViewGroup.MarginLayoutParams? = null

    /**
     * 防止不同界面的下拉刷新在上次更新时间上混淆
     */
    private var mId = -1

    /**
     * 下拉头隐藏时的TopMargin
     */
    private var mHiddenHeaderTopMargin: Int = 0

    @State
    private var mCurrState = REFRESH_FINISHED

    private var mLastState = mCurrState

    /**
     * 是否已执行下拉头初始隐藏
     */
    private var mFirstHidden: Boolean = false

    private var mReturning: Boolean = false
    private var mNestedScrollInProgress: Boolean = false
    private var mActivePointerId = INVALID_POINTER
    private var mIsBeingDragged: Boolean = false
    private var mInitialDownY: Float = 0.toFloat()
    private var mTarget: View? = null
    private var mInitialMotionY: Float = 0.toFloat()
    private val mTouchSlop: Float
    private var mTotalUnconsumed: Float = 0.toFloat()
    private val mParentScrollConsumed = IntArray(2)
    private val mParentOffsetInWindow = IntArray(2)
    private val mNestedScrollingParentHelper: NestedScrollingParentHelper
    private val mNestedScrollingChildHelper: NestedScrollingChildHelper
    private var mFrom: Int = 0

    private var mChildScrollUpCallback: OnChildScrollUpCallback? = null

    private val mRefershingAnimationListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            mReturning = true
        }

        override fun onAnimationEnd(animation: Animation) {
            mReturning = false
            if (mListener != null) {
                mListener!!.onRefresh()
            } else {
                postDelayed({ finishHeader() }, 1000)
            }
        }

        override fun onAnimationRepeat(animation: Animation) {}
    }
    private val mHiddenAnimationListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            mReturning = true
        }

        override fun onAnimationEnd(animation: Animation) {
            mReturning = false
        }

        override fun onAnimationRepeat(animation: Animation) {}
    }

    private val mAnimateToHiddenPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            mHeaderLayoutParams!!.topMargin = mFrom + ((mHiddenHeaderTopMargin - mFrom) * interpolatedTime).toInt()
            mHeader.layoutParams = mHeaderLayoutParams
        }
    }

    private val mAnimateToRefreshingPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            mHeaderLayoutParams!!.topMargin = mFrom + ((0 - mFrom) * interpolatedTime).toInt()
            mHeader.layoutParams = mHeaderLayoutParams
        }
    }

    @IntDef(DRAGGING_NOT_REFRESHABLE, DRAGGING_REFRESHABLE, REFRESHING, REFRESH_FINISHED)
    @Retention(RetentionPolicy.SOURCE)
    annotation class State


    init {
        orientation = LinearLayout.VERTICAL

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
        mNestedScrollingParentHelper = NestedScrollingParentHelper(this)
        mNestedScrollingChildHelper = NestedScrollingChildHelper(this)
        mDecelerateInterpolator = DecelerateInterpolator(2f)

        mHeaderWrapper = LayoutInflater.from(context).inflate(R.layout.common_pull_to_refresh, null)
        mHeader = mHeaderWrapper.findViewById(R.id.rl_header)
        mProgressBar = mHeader.findViewById<View>(R.id.progress_bar) as ProgressBar
        mIvArrow = mHeader.findViewById<View>(R.id.iv_arrow) as ImageView
        mTvRefreshTip = mHeader.findViewById<View>(R.id.tv_refresh_tip) as TextView
        mTvLastUpdateTime = mHeader.findViewById<View>(R.id.tv_last_update_time) as TextView
        addView(mHeaderWrapper, 0)

        refreshLastUpdateTimeDesc()

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed && !mFirstHidden) {
            mHiddenHeaderTopMargin = -mHeader.height
            mHeaderLayoutParams = mHeader.layoutParams as ViewGroup.MarginLayoutParams
            mHeaderLayoutParams!!.topMargin = mHiddenHeaderTopMargin
            mHeader.layoutParams = mHeaderLayoutParams
            mFirstHidden = true
        }
    }


    fun setOnRefreshListener(listener: OnRefreshListener, id: Int) {
        mListener = listener
        mId = id
    }

    /**
     * Set a callback to override [PullToRefreshLayout.canChildScrollUp] method. Non-null
     * callback will return the value provided by the callback and ignore all internal logic.
     * @param callback Callback that should be called when canChildScrollUp() is called.
     */
    fun setOnChildScrollUpCallback(callback: OnChildScrollUpCallback?) {
        mChildScrollUpCallback = callback
    }

    fun finishRefreshing() {
        mPreferences.edit().putLong(LAST_UPDATE_TIME + mId, System.currentTimeMillis()).apply()
        finishHeader()
    }

    private fun updateHeaderView() {
        if (mLastState != mCurrState) {
            if (mCurrState == DRAGGING_NOT_REFRESHABLE) {
                mTvRefreshTip.text = resources.getString(R.string.pull_to_refresh)
                mProgressBar.visibility = View.GONE
                mIvArrow.visibility = View.VISIBLE
                rotateArrow()
            } else if (mCurrState == DRAGGING_REFRESHABLE) {
                mTvRefreshTip.text = resources.getString(R.string.release_to_refresh)
                mProgressBar.visibility = View.GONE
                mIvArrow.visibility = View.VISIBLE
                rotateArrow()
            } else if (mCurrState == REFRESHING) {
                mTvRefreshTip.text = resources.getString(R.string.refreshing)
                mProgressBar.visibility = View.VISIBLE
                mIvArrow.clearAnimation()
                mIvArrow.visibility = View.GONE
            }
            mLastState = mCurrState
            refreshLastUpdateTimeDesc()
        }
    }

    private fun rotateArrow() {
        val pivotX = mIvArrow.width / 2f
        val pivotY = mIvArrow.height / 2f
        var fromDegrees = 0f
        var toDegrees = 0f
        if (mLastState != REFRESH_FINISHED && mCurrState == DRAGGING_NOT_REFRESHABLE) {
            fromDegrees = 180f
            toDegrees = 360f
        } else if (mCurrState == DRAGGING_REFRESHABLE) {
            fromDegrees = 0f
            toDegrees = 180f
        }
        val animation = RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY)
        animation.duration = 100
        animation.fillAfter = true
        mIvArrow.startAnimation(animation)
    }


    /**
     * 刷新下拉头中上次更新时间的文字描述。
     */
    private fun refreshLastUpdateTimeDesc() {
        val lastUpdateTime = mPreferences.getLong(LAST_UPDATE_TIME + mId, -1)
        val currentTime = System.currentTimeMillis()
        val timePassed = currentTime - lastUpdateTime
        val timeDesc: String
        when {
            lastUpdateTime == -1L -> timeDesc = resources.getString(R.string.not_updated_yet)
            timePassed < 0 -> timeDesc = resources.getString(R.string.time_error)
            else -> {
                val sdfDate = SimpleDateFormat("yyyy-MM-dd")
                val sdfTime = SimpleDateFormat("HH:mm")
                val strLastDate = sdfDate.format(Date(lastUpdateTime))
                val strLastTime = sdfTime.format(Date(lastUpdateTime))
                val replace: String
                if (TextUtils.equals(sdfDate.format(Date(currentTime)), strLastDate)) {
                    replace = "今天$strLastTime"
                } else if (TextUtils.equals(sdfDate.format(Date(currentTime - ONE_DAY)),
                                strLastDate)) {
                    replace = "昨天$strLastTime"
                } else {
                    replace = "$strLastDate $strLastTime"
                }
                timeDesc = resources.getString(R.string.updated_at, replace)
            }
        }
        mTvLastUpdateTime.text = timeDesc
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        ensureTarget()

        val action = MotionEventCompat.getActionMasked(ev)
        val pointerIndex: Int

        if (mReturning && action == MotionEvent.ACTION_DOWN) {
            mReturning = false
        }

        if (!isEnabled || mReturning || canChildScrollUp()
                || mCurrState == REFRESHING || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0)
                mIsBeingDragged = false

                pointerIndex = ev.findPointerIndex(mActivePointerId)
                if (pointerIndex < 0) {
                    return false
                }
                mInitialDownY = ev.getY(pointerIndex)
            }

            MotionEvent.ACTION_MOVE -> {
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(TAG, "Got ACTION_MOVE event but don't have an active pointer id.")
                    return false
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId)
                if (pointerIndex < 0) {
                    return false
                }
                val y = ev.getY(pointerIndex)
                startDragging(y)
            }

            MotionEventCompat.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
            }
        }

        return mIsBeingDragged
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        var pointerIndex = -1

        if (mReturning && action == MotionEvent.ACTION_DOWN) {
            mReturning = false
        }

        if (!isEnabled || mReturning || canChildScrollUp()
                || mCurrState == REFRESHING || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0)
                mIsBeingDragged = false
            }

            MotionEvent.ACTION_MOVE -> {
                pointerIndex = ev.findPointerIndex(mActivePointerId)
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id.")
                    return false
                }

                val y = ev.getY(pointerIndex)
                startDragging(y)

                if (mIsBeingDragged) {
                    val overscrollTop = (y - mInitialMotionY) * DRAG_RATE
                    if (overscrollTop > 0) {
                        moveHeader(overscrollTop)
                    } else {
                        return false
                    }
                }
            }
            MotionEventCompat.ACTION_POINTER_DOWN -> {
                pointerIndex = MotionEventCompat.getActionIndex(ev)
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.")
                    return false
                }
                mActivePointerId = ev.getPointerId(pointerIndex)
            }

            MotionEventCompat.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)

            MotionEvent.ACTION_UP -> {
                pointerIndex = ev.findPointerIndex(mActivePointerId)
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.")
                    return false
                }

                if (mIsBeingDragged) {
                    mIsBeingDragged = false
                    finishHeader()
                }
                mActivePointerId = INVALID_POINTER
                return false
            }
            MotionEvent.ACTION_CANCEL -> return false
        }
        return true
    }

    private fun animateToHiddenPosition(from: Int, listener: Animation.AnimationListener?) {
        mFrom = from
        mAnimateToHiddenPosition.reset()
        mAnimateToHiddenPosition.duration = 200
        mAnimateToHiddenPosition.interpolator = mDecelerateInterpolator
        if (listener != null) {
            mAnimateToHiddenPosition.setAnimationListener(listener)
        }
        mHeader.clearAnimation()
        mHeader.startAnimation(mAnimateToHiddenPosition)
    }

    private fun animateToRefreshingPosition(from: Int, listener: Animation.AnimationListener?) {
        mFrom = from
        mAnimateToRefreshingPosition.reset()
        mAnimateToRefreshingPosition.duration = 100
        mAnimateToRefreshingPosition.interpolator = mDecelerateInterpolator
        if (listener != null) {
            mAnimateToRefreshingPosition.setAnimationListener(listener)
        }
        mHeader.clearAnimation()
        mHeader.startAnimation(mAnimateToRefreshingPosition)
    }

    private fun finishHeader() {
        if (mCurrState == DRAGGING_REFRESHABLE) {
            animateToRefreshingPosition(mHeaderLayoutParams!!.topMargin, mRefershingAnimationListener)
            mCurrState = REFRESHING
            updateHeaderView()
        } else if (mCurrState == DRAGGING_NOT_REFRESHABLE || mCurrState == REFRESHING) {
            animateToHiddenPosition(mHeaderLayoutParams!!.topMargin, mHiddenAnimationListener)
            mCurrState = REFRESH_FINISHED
        }
    }


    private fun moveHeader(overscrollTop: Float) {
        if (mCurrState != REFRESHING) {
            // 修改下拉头的topMargin实现下拉效果
            mHeaderLayoutParams!!.topMargin = (overscrollTop + mHiddenHeaderTopMargin).toInt()
            mHeader.layoutParams = mHeaderLayoutParams
            if (mHeaderLayoutParams!!.topMargin > 0) {
                mCurrState = DRAGGING_REFRESHABLE
            } else {
                mCurrState = DRAGGING_NOT_REFRESHABLE
            }
            updateHeaderView()
        }
    }

    private fun ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child != mHeaderWrapper) {
                    mTarget = child
                    break
                }
            }
        }
        if (mTarget != null && mTarget is OnChildScrollUpCallback
                && mChildScrollUpCallback == null) {
            setOnChildScrollUpCallback(mTarget as OnChildScrollUpCallback?)
        }
    }

    // 判断能不能下拉
    fun canChildScrollUp(): Boolean {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback!!.canChildScrollUp(this, mTarget)
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget is AbsListView) {
                val absListView = mTarget as AbsListView?
                return absListView!!.childCount > 0 && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(0)
                        .top < absListView.paddingTop)
            } else {
                return ViewCompat.canScrollVertically(mTarget!!, -1) || mTarget!!.scrollY > 0
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget!!, -1)
        }
    }

    private fun startDragging(y: Float) {
        val yDiff = y - mInitialDownY
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop
            mIsBeingDragged = true
        }
    }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = MotionEventCompat.getActionIndex(ev)
        val pointerId = ev.getPointerId(pointerIndex)
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mActivePointerId = ev.getPointerId(newPointerIndex)
        }
    }

    // NestedScrollingParent

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return (isEnabled && !mReturning && mCurrState != REFRESHING
                && nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes)
        // Dispatch up to the nested parent
        startNestedScroll(axes and ViewCompat.SCROLL_AXIS_VERTICAL)
        mTotalUnconsumed = 0f
        mNestedScrollInProgress = true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - mTotalUnconsumed.toInt()
                mTotalUnconsumed = 0f
            } else {
                mTotalUnconsumed -= dy.toFloat()
                consumed[1] = dy
            }
            moveHeader(mTotalUnconsumed * DRAG_RATE)
        }

        // Now let our nested parent consume the leftovers
        val parentConsumed = mParentScrollConsumed
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0]
            consumed[1] += parentConsumed[1]
        }
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int,
                                dxUnconsumed: Int, dyUnconsumed: Int) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow)

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        val dy = dyUnconsumed + mParentOffsetInWindow[1]
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy).toFloat()
            moveHeader(mTotalUnconsumed * DRAG_RATE)
        }
    }

    override fun getNestedScrollAxes(): Int {
        return mNestedScrollingParentHelper.nestedScrollAxes
    }

    override fun onStopNestedScroll(target: View) {
        mNestedScrollingParentHelper.onStopNestedScroll(target)
        mNestedScrollInProgress = false
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishHeader()
            mTotalUnconsumed = 0f
        }
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

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        fun onRefresh()
    }

    /**
     * Classes that wish to override [PullToRefreshLayout.canChildScrollUp] method
     * behavior should implement this interface.
     */
    interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when [PullToRefreshLayout.canChildScrollUp] method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent PullToRefreshLayout that this callback is overriding.
         * @param child The child view of PullToRefreshLayout.
         *
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        fun canChildScrollUp(parent: PullToRefreshLayout, child: View?): Boolean
    }

    companion object {

        private val TAG = PullToRefreshLayout::class.java.simpleName

        /**
         * 下拉-释放不可刷新状态
         */
        const val DRAGGING_NOT_REFRESHABLE = 0

        /**
         * 下拉-释放可刷新状态
         */
        const val DRAGGING_REFRESHABLE = 1

        /**
         * 正在刷新状态
         */
        const val REFRESHING = 2

        /**
         * 刷新完成或未刷新状态
         */
        const val REFRESH_FINISHED = 3

        /**
         * 一天的毫秒值
         */
        val ONE_DAY = (24 * 60 * 60 * 1000).toLong()

        /**
         * SharedPreferences中保存的上次更新时间的key前缀
         */
        private val LAST_UPDATE_TIME = "last_update_time_"


        private val DRAG_RATE = .5f

        val INVALID_POINTER = -1
    }
}
