package com.example.tuionf.vdhlayout;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author tuionf
 * @date 2017/12/13
 * @email 596019286@qq.com
 * @explain
 */

public class VDHlayout extends LinearLayout {

    private static final String TAG = "VDHlayout";

    private ViewDragHelper mDragHelper;
    private Point mPoint = new Point();

    public VDHlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mPoint.x = capturedChild.getLeft();
                mPoint.y = capturedChild.getTop();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.e(TAG, "clampViewPositionHorizontal: "+left+"---"+dx );
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.e(TAG, "clampViewPositionHorizontal: "+top+"---"+dy);
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                if (getChildAt(0) == releasedChild){
                    mDragHelper.flingCapturedView(getPaddingLeft(),getPaddingTop(),
                            getWidth()-getPaddingRight()-releasedChild.getWidth(),
                            getHeight()-getPaddingBottom()-releasedChild.getHeight());
                }else {
                    mDragHelper.settleCapturedViewAt(mPoint.x,mPoint.y);
                }

                invalidate();
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                mDragHelper.captureChildView(getChildAt(0),pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }
        });

        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)){
            invalidate();
        }
    }
}
