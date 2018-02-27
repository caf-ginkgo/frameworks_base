/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.android.systemui.qs;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.LinearLayout;

/**
 * Quick setting scroll view containing the brightness slider and the QS tiles.
 *
 * <p>Call {@link #shouldIntercept(MotionEvent)} from parent views'
 * {@link #onInterceptTouchEvent(MotionEvent)} method to determine whether this view should
 * consume the touch event.
 */
public class QSScrollLayout extends NestedScrollView {
    private final int mTouchSlop;
    private int mLastMotionY;
    private Rect mHitRect = new Rect();

    public QSScrollLayout(Context context, View... children) {
        super(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (View view : children) {
            linearLayout.addView(view);
        }
        addView(linearLayout);
    }

    public boolean shouldIntercept(MotionEvent ev) {
        getHitRect(mHitRect);
        if (!mHitRect.contains((int) ev.getX(), (int) ev.getY())) {
            // Do not intercept touches that are not within this view's bounds.
            return false;
        }
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mLastMotionY = (int) ev.getY();
        } else if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
            // Do not allow NotificationPanelView to intercept touch events when this
            // view can be scrolled down.
            if (mLastMotionY >= 0 && Math.abs(ev.getY() - mLastMotionY) > mTouchSlop
                    && canScrollVertically(1)) {
                requestParentDisallowInterceptTouchEvent(true);
                mLastMotionY = (int) ev.getY();
                return true;
            }
        } else if (ev.getActionMasked() == MotionEvent.ACTION_CANCEL
            || ev.getActionMasked() == MotionEvent.ACTION_UP) {
            mLastMotionY = -1;
            requestParentDisallowInterceptTouchEvent(false);
        }
        return false;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }
}
