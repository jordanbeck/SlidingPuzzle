package com.twentyfivesquares.slidingpuzzle.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        for (int i = 0; i < 8; i++) {
            final TileView tileView = new TileView(context);
            tileView.setText(Integer.toString(i + 1));
            addView(tileView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int spec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() / 3, MeasureSpec.EXACTLY);
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View child = getChildAt(i);
            child.measure(spec, spec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        int left = 0;
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View child = getChildAt(i);
            child.setBackgroundResource(i % 2 == 0 ? R.color.colorAccent : R.color.colorAccentDark);

            if (i != 0 && i % 3 == 0) {
                top += child.getMeasuredHeight();
                left = 0;
            }
            child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
            left += child.getMeasuredWidth();
        }
    }

    public class TileView extends TextView {
        public TileView(Context context) {
            super(context);
            setGravity(Gravity.CENTER);
            // Set text appearance
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextAppearance(R.style.AppTheme_TextAppearance_PuzzlePiece);
            } else {
                setTextAppearance(context, R.style.AppTheme_TextAppearance_PuzzlePiece);
            }
        }
    }
}
