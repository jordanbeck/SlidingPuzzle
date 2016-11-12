package com.twentyfivesquares.slidingpuzzle.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.twentyfivesquares.slidingpuzzle.R;

public class SlidingPuzzleView extends ViewGroup {

    public SlidingPuzzleView(Context context) {
        super(context);
        init(context);
    }

    public SlidingPuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlidingPuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingPuzzleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        for (int i = 0; i < 9; i++) {
            addView(new View(context));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int spec = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View child = getChildAt(i);
            child.measure(spec, spec);
        }

        final int parentSpec = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
        super.onMeasure(parentSpec, parentSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        int left = 0;
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View child = getChildAt(i);
            child.setBackgroundResource(R.color.colorAccent);
            child.setAlpha(1.0f - (i * 0.1f));

            if (i != 0 && i % 3 == 0) {
                top += child.getMeasuredHeight();
                left = 0;
            }
            child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
            left += child.getMeasuredWidth();
        }
    }
}
