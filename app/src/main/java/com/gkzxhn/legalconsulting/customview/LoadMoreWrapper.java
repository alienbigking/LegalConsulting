package com.gkzxhn.legalconsulting.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.gkzxhn.legalconsulting.R;


/**
 * @classname：LoadMoreWrapper
 * @author：PrivateXiaoWu
 * @date：2018/9/10 17:17
 * @description： 上拉自动加载更多
 */

public class LoadMoreWrapper extends LinearLayout implements NestedScrollingParent,
        NestedScrollingChild, PullToRefreshLayout.OnChildScrollUpCallback {
    private static final String TAG = LoadMoreWrapper.class.getSimpleName();

    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    private View mFooterView;
    private View mTarget;

    private boolean mLoading;
    private OnLoadMoreListener mListener;

    public LoadMoreWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        mFooterView = LayoutInflater.from(context).inflate(R.layout.common_refresh_load_more, null);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTarget = getChildAt(0);
        if (mTarget instanceof AbsListView) {
            ((AbsListView) mTarget).setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (view.getLastVisiblePosition() == view.getAdapter().getCount() - 1
                            && !mLoading
                            && (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING)) {
                        Log.e(TAG, "onScrollStateChanged: " + scrollState);
                        loadMore();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                     int totalItemCount) {
                }
            });
        } else if (mTarget instanceof RecyclerView) {
            ((RecyclerView) mTarget).addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                    if (lm instanceof LinearLayoutManager) {
                        LinearLayoutManager llm = (LinearLayoutManager) lm;
                        if (llm.findLastCompletelyVisibleItemPosition() == llm.getItemCount() - 1
                                && !mLoading
                                && (newState == RecyclerView.SCROLL_STATE_IDLE
                                || newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                            Log.e(TAG, "onScrollStateChanged: " + newState);
                            loadMore();
                        }
                    } else if (lm instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
                        int lastPosition = getLastPosition(sglm);
                        if (lastPosition == sglm.getItemCount() - 1
                                && !mLoading
                                && (newState == RecyclerView.SCROLL_STATE_IDLE
                                || newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                            Log.e(TAG, "onScrollStateChanged: " + newState);
                            loadMore();
                        }
                    }
                }
            });
        }
    }

    private int getLastPosition(StaggeredGridLayoutManager sglm) {
        int lastPosition = 0;
        int spanCount = sglm.getSpanCount();
        int[] lastPositions = new int[spanCount];
        sglm.findLastCompletelyVisibleItemPositions(lastPositions);
        for (int i = 0; i < spanCount; i++) {
            lastPosition = Math.max(lastPosition, lastPositions[i]);
        }
        return lastPosition;
    }

    private void loadMore() {
        setLoading(true);
        if (mListener != null) {
            mListener.onLoadMore();
        }
    }

    public void setLoading(boolean loading) {
        if (loading != mLoading) {
            mLoading = loading;
            if (mLoading) {
                addView(mFooterView, getChildCount());
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mTarget instanceof AbsListView) {
                            AbsListView absListView = (AbsListView) mTarget;
                            absListView.setSelection(absListView.getCount());
                        } else if (mTarget instanceof RecyclerView) {
                            RecyclerView recyclerView = (RecyclerView) mTarget;
                            recyclerView.smoothScrollToPosition(recyclerView.getLayoutManager().getItemCount());
                        }
                    }
                }, 10);
            } else {
                removeView(mFooterView);
            }
        }
    }

    public void finishLoading() {
        setLoading(false);
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mListener = listener;
    }

    @Override
    public boolean canChildScrollUp(PullToRefreshLayout parent, @Nullable View child) {
        return canChildScrollUp();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0
                        || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // Re-dispatch up the tree by default
        dispatchNestedPreScroll(dx, dy, consumed, null);
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Re-dispatch up the tree by default
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null);
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
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
}
