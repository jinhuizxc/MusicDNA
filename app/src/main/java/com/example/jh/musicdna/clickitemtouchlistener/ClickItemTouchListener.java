package com.example.jh.musicdna.clickitemtouchlistener;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public abstract class ClickItemTouchListener implements RecyclerView.OnItemTouchListener {

    private static final String TAG = "ClickItemTouchListener";
    private GestureDetector mGestureDetector;

    public ClickItemTouchListener(RecyclerView hostView) {
        mGestureDetector = new ItemClickGestureDetector(hostView.getContext(),
                new ItemClickGestureListener(hostView));
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (!isAttachedToWindow(rv) || !hasAdapter(rv)) {
            return false;
        }

        mGestureDetector.onTouchEvent(e);
        return false;
    }

    private boolean hasAdapter(RecyclerView hostView) {
        return (hostView.getAdapter() != null);
    }

    private boolean isAttachedToWindow(RecyclerView hostView) {
        if (Build.VERSION.SDK_INT >= 19) {
            return hostView.isAttachedToWindow();
        } else {
            return (hostView.getHandler() != null);
        }
    }


    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        // We can silently track tap and and long presses by silently
        // intercepting touch events in the host RecyclerView.
    }

    // 项目里面这个方法没有，但是实现接口必须得实现这个方法，
    // 但写了抽象方法后这个方法就可以注释掉，不必实现，这或许就是继承吧。
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract boolean onClick(RecyclerView parent, View view, int position, long id);

    public abstract boolean onLongClick(RecyclerView parent, View view, int position, long id);

    private class ItemClickGestureDetector extends GestureDetector {
        private final ItemClickGestureListener mGestureListener;

        public ItemClickGestureDetector(Context context, ItemClickGestureListener listener) {
            super(context, listener);
            mGestureListener = listener;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            final boolean handled = super.onTouchEvent(event);

            final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
            if (action == MotionEvent.ACTION_UP) {
                mGestureListener.dispatchSingleTapUpIfNeeded(event);
            }

            return handled;
        }
    }

    // 内部接口类
    private class ItemClickGestureListener extends GestureDetector.SimpleOnGestureListener {
        private final RecyclerView mHostView;
        private View mTargetChild;

        public ItemClickGestureListener(RecyclerView hostView) {
            mHostView = hostView;
        }

        public void dispatchSingleTapUpIfNeeded(MotionEvent event) {
            // When the long press hook is called but the long press listener
            // returns false, the target child will be left around to be
            // handled later. In this case, we should still treat the gesture
            // as potential item click.
            if (mTargetChild != null) {
                onSingleTapUp(event);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            mTargetChild = mHostView.findChildViewUnder(x, y);
            return (mTargetChild != null);
        }

        @Override
        public void onShowPress(MotionEvent event) {
            if (mTargetChild != null) {
                mTargetChild.setPressed(true);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            boolean handled = false;

            if (mTargetChild != null) {
                mTargetChild.setPressed(false);

                final int position = mHostView.getChildPosition(mTargetChild);
                final long id = mHostView.getAdapter().getItemId(position);
                handled = onClick(mHostView, mTargetChild, position, id);

                mTargetChild = null;
            }

            return handled;
        }

        @Override
        public boolean onScroll(MotionEvent event, MotionEvent event2, float v, float v2) {
            if (mTargetChild != null) {
                mTargetChild.setPressed(false);
                mTargetChild = null;

                return true;
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if (mTargetChild == null) {
                return;
            }

            final int position = mHostView.getChildPosition(mTargetChild);
            final long id = mHostView.getAdapter().getItemId(position);
            final boolean handled = onLongClick(mHostView, mTargetChild, position, id);

            if (handled) {
                mTargetChild.setPressed(false);
                mTargetChild = null;
            }
        }
    }

}
