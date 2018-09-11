package com.gkzxhn.legalconsulting.customview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.gkzxhn.legalconsulting.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 下拉刷新
 *
 * @author liufang
 */
public class PullToRefreshLayout extends LinearLayout implements NestedScrollingParent, NestedScrollingChild {

    private static final String TAG = PullToRefreshLayout.class.getSimpleName();

    /**
     * 下拉-释放不可刷新状态
     */
    public static final int DRAGGING_NOT_REFRESHABLE = 0;

    /**
     * 下拉-释放可刷新状态
     */
    public static final int DRAGGING_REFRESHABLE = 1;

    /**
     * 正在刷新状态
     */
    public static final int REFRESHING = 2;

    /**
     * 刷新完成或未刷新状态
     */
    public static final int REFRESH_FINISHED = 3;

    /**
     * 一天的毫秒值
     */
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * SharedPreferences中保存的上次更新时间的key前缀
     */
    private static final String LAST_UPDATE_TIME = "last_update_time_";


    private final View mHeaderWrapper;

    private final DecelerateInterpolator mDecelerateInterpolator;
    private final TextView mTvRefreshTip;

    /**
     * 下拉刷新的回调接口
     */
    private OnRefreshListener mListener;

    private SharedPreferences mPreferences;

    /**
     * 下拉头
     */
    private View mHeader;

    /**
     * 刷新时显示的进度条
     */
    private ProgressBar mProgressBar;

    /**
     * 指示下拉和释放的箭头
     */
    private ImageView mIvArrow;

    /**
     * 上次更新时间的文字描述
     */
    private TextView mTvLastUpdateTime;

    /**
     * 下拉头的布局参数
     */
    private MarginLayoutParams mHeaderLayoutParams;

    /**
     * 防止不同界面的下拉刷新在上次更新时间上混淆
     */
    private int mId = -1;

    /**
     * 下拉头隐藏时的TopMargin
     */
    private int mHiddenHeaderTopMargin;

    @State
    private int mCurrState = REFRESH_FINISHED;

    private int mLastState = mCurrState;

    /**
     * 是否已执行下拉头初始隐藏
     */
    private boolean mFirstHidden;

    @IntDef({DRAGGING_NOT_REFRESHABLE, DRAGGING_REFRESHABLE, REFRESHING, REFRESH_FINISHED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}


    private static final float DRAG_RATE = .5f;

    public static final int INVALID_POINTER = -1;

    private boolean mReturning;
    private boolean mNestedScrollInProgress;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsBeingDragged;
    private float mInitialDownY;
    private View mTarget;
    private float mInitialMotionY;
    private float mTouchSlop;
    private float mTotalUnconsumed;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private int mFrom;

    private OnChildScrollUpCallback mChildScrollUpCallback;

    private Animation.AnimationListener mRefershingAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mReturning = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mReturning = false;
            if (mListener != null) {
                mListener.onRefresh();
            } else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishHeader();
                    }
                }, 1000);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };
    private Animation.AnimationListener mHiddenAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mReturning = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mReturning = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };


    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        mDecelerateInterpolator = new DecelerateInterpolator(2f);

        mHeaderWrapper = LayoutInflater.from(context).inflate(R.layout.common_pull_to_refresh, null);
        mHeader = mHeaderWrapper.findViewById(R.id.rl_header);
        mProgressBar = (ProgressBar) mHeader.findViewById(R.id.progress_bar);
        mIvArrow = (ImageView) mHeader.findViewById(R.id.iv_arrow);
        mTvRefreshTip = (TextView) mHeader.findViewById(R.id.tv_refresh_tip);
        mTvLastUpdateTime = (TextView) mHeader.findViewById(R.id.tv_last_update_time);
        addView(mHeaderWrapper, 0);

        refreshLastUpdateTimeDesc();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !mFirstHidden) {
            mHiddenHeaderTopMargin = -mHeader.getHeight();
            mHeaderLayoutParams = (MarginLayoutParams) mHeader.getLayoutParams();
            mHeaderLayoutParams.topMargin = mHiddenHeaderTopMargin;
            mHeader.setLayoutParams(mHeaderLayoutParams);
            mFirstHidden = true;
        }
    }


    public void setOnRefreshListener(OnRefreshListener listener, int id) {
        mListener = listener;
        mId = id;
    }

    /**
     * Set a callback to override {@link PullToRefreshLayout#canChildScrollUp()} method. Non-null
     * callback will return the value provided by the callback and ignore all internal logic.
     * @param callback Callback that should be called when canChildScrollUp() is called.
     */
    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    public void finishRefreshing() {
        mPreferences.edit().putLong(LAST_UPDATE_TIME + mId, System.currentTimeMillis()).apply();
        finishHeader();
    }

    private void updateHeaderView() {
        if (mLastState != mCurrState) {
            if (mCurrState == DRAGGING_NOT_REFRESHABLE) {
                mTvRefreshTip.setText(getResources().getString(R.string.pull_to_refresh));
                mProgressBar.setVisibility(View.GONE);
                mIvArrow.setVisibility(View.VISIBLE);
                rotateArrow();
            } else if (mCurrState == DRAGGING_REFRESHABLE) {
                mTvRefreshTip.setText(getResources().getString(R.string.release_to_refresh));
                mProgressBar.setVisibility(View.GONE);
                mIvArrow.setVisibility(View.VISIBLE);
                rotateArrow();
            } else if (mCurrState == REFRESHING) {
                mTvRefreshTip.setText(getResources().getString(R.string.refreshing));
                mProgressBar.setVisibility(View.VISIBLE);
                mIvArrow.clearAnimation();
                mIvArrow.setVisibility(View.GONE);
            }
            mLastState = mCurrState;
            refreshLastUpdateTimeDesc();
        }
    }

    private void rotateArrow() {
        float pivotX = mIvArrow.getWidth() / 2f;
        float pivotY = mIvArrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (mLastState != REFRESH_FINISHED && mCurrState == DRAGGING_NOT_REFRESHABLE) {
            fromDegrees = 180f;
            toDegrees = 360f;
        } else if (mCurrState == DRAGGING_REFRESHABLE) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(100);
        animation.setFillAfter(true);
        mIvArrow.startAnimation(animation);
    }


    /**
     * 刷新下拉头中上次更新时间的文字描述。
     */
    private void refreshLastUpdateTimeDesc() {
        long lastUpdateTime = mPreferences.getLong(LAST_UPDATE_TIME + mId, -1);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        String timeDesc;
        if (lastUpdateTime == -1) {
            timeDesc = getResources().getString(R.string.not_updated_yet);
        } else if (timePassed < 0) {
            timeDesc = getResources().getString(R.string.time_error);
        } else {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            String strLastDate = sdfDate.format(new Date(lastUpdateTime));
            String strLastTime = sdfTime.format(new Date(lastUpdateTime));
            String replace;
            if (TextUtils.equals(sdfDate.format(new Date(currentTime)), strLastDate)) {
                replace = "今天" + strLastTime;
            } else if (TextUtils.equals(sdfDate.format(new Date(currentTime - ONE_DAY)),
                    strLastDate)) {
                replace = "昨天" + strLastTime;
            } else {
                replace = strLastDate + " " + strLastTime;
            }
            timeDesc = getResources().getString(R.string.updated_at, replace);
        }
        mTvLastUpdateTime.setText(timeDesc);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex;

        if (mReturning && action == MotionEvent.ACTION_DOWN) {
            mReturning = false;
        }

        if (!isEnabled() || mReturning || canChildScrollUp()
                || mCurrState == REFRESHING || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex = -1;

        if (mReturning && action == MotionEvent.ACTION_DOWN) {
            mReturning = false;
        }

        if (!isEnabled() || mReturning || canChildScrollUp()
                || mCurrState == REFRESHING || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                startDragging(y);

                if (mIsBeingDragged) {
                    final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    if (overscrollTop > 0) {
                        moveHeader(overscrollTop);
                    } else {
                        return false;
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                pointerIndex = MotionEventCompat.getActionIndex(ev);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    finishHeader();
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return true;
    }

    private final Animation mAnimateToHiddenPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            mHeaderLayoutParams.topMargin = mFrom + (int) ((mHiddenHeaderTopMargin - mFrom) *
                    interpolatedTime);
            mHeader.setLayoutParams(mHeaderLayoutParams);
        }
    };

    private void animateToHiddenPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToHiddenPosition.reset();
        mAnimateToHiddenPosition.setDuration(200);
        mAnimateToHiddenPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mAnimateToHiddenPosition.setAnimationListener(listener);
        }
        mHeader.clearAnimation();
        mHeader.startAnimation(mAnimateToHiddenPosition);
    }

    private final Animation mAnimateToRefreshingPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            mHeaderLayoutParams.topMargin = mFrom + (int) ((0 - mFrom) * interpolatedTime);
            mHeader.setLayoutParams(mHeaderLayoutParams);
        }
    };

    private void animateToRefreshingPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToRefreshingPosition.reset();
        mAnimateToRefreshingPosition.setDuration(100);
        mAnimateToRefreshingPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mAnimateToRefreshingPosition.setAnimationListener(listener);
        }
        mHeader.clearAnimation();
        mHeader.startAnimation(mAnimateToRefreshingPosition);
    }

    private void finishHeader() {
        if (mCurrState == DRAGGING_REFRESHABLE) {
            animateToRefreshingPosition(mHeaderLayoutParams.topMargin, mRefershingAnimationListener);
            mCurrState = REFRESHING;
            updateHeaderView();
        } else if (mCurrState == DRAGGING_NOT_REFRESHABLE || mCurrState == REFRESHING) {
            animateToHiddenPosition(mHeaderLayoutParams.topMargin, mHiddenAnimationListener);
            mCurrState = REFRESH_FINISHED;
        }
    }


    private void moveHeader(float overscrollTop) {
        if (mCurrState != REFRESHING) {
            // 修改下拉头的topMargin实现下拉效果
            mHeaderLayoutParams.topMargin = (int) (overscrollTop + mHiddenHeaderTopMargin);
            mHeader.setLayoutParams(mHeaderLayoutParams);
            if (mHeaderLayoutParams.topMargin > 0) {
                mCurrState = DRAGGING_REFRESHABLE;
            } else {
                mCurrState = DRAGGING_NOT_REFRESHABLE;
            }
            updateHeaderView();
        }
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mHeaderWrapper)) {
                    mTarget = child;
                    break;
                }
            }
        }
        if (mTarget != null && mTarget instanceof OnChildScrollUpCallback
                && mChildScrollUpCallback == null) {
            setOnChildScrollUpCallback(((OnChildScrollUpCallback) mTarget));
        }
    }

    // 判断能不能下拉
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mReturning && mCurrState != REFRESHING
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveHeader(mTotalUnconsumed * DRAG_RATE);
        }

        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
            moveHeader(mTotalUnconsumed * DRAG_RATE);
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishHeader();
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();
    }

    /**
     * Classes that wish to override {@link PullToRefreshLayout#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link PullToRefreshLayout#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent PullToRefreshLayout that this callback is overriding.
         * @param child The child view of PullToRefreshLayout.
         *
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(PullToRefreshLayout parent, @Nullable View child);
    }
}
