package com.example.tuionf.vdhlayout;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author tuionf
 * @date 2017/12/12
 * @email 596019286@qq.com
 * @explain
 */

public class MyLayout extends LinearLayout {

    private ViewDragHelper viewDragHelper;

    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;
    private Point mAutoBackOriginPos = new Point();

    public MyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
//            返回ture则表示可以捕获该view
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                return child == mDragView || child == mAutoBackView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView){
                    // 将捕获的视图放置到给定的位置
                    viewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x,mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                viewDragHelper.captureChildView(mEdgeTrackerView,pointerId);
            }
        });

        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true))
        {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
    }
}
