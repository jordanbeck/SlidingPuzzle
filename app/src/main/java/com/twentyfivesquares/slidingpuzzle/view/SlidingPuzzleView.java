package com.twentyfivesquares.slidingpuzzle.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;
import com.twentyfivesquares.slidingpuzzle.store.PuzzleStore;

import java.util.Map;

public class SlidingPuzzleView extends ViewGroup {

    private final int SIZE = 3;

    private PuzzleStore store;

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
        store = new PuzzleStore(SIZE);
        store.shufflePuzzle(10);

        final Map<PuzzlePoint, Integer> puzzleMap = store.getPuzzleMap();
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                final Integer label = puzzleMap.get(new PuzzlePoint(x, y));
                if (label == PuzzleStore.EMPTY) {
                    addView(new EmptyView(context));
                } else {
                    final TileView tileView = new TileView(context);
                    tileView.setLabel(label);
                    tileView.setBackgroundResource(label % 2 == 0 ? R.color.colorAccent : R.color.colorAccentDark);
                    addView(tileView);
                }
            }
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

        public void setLabel(Integer label) {
            setText(label.toString());
        }
    }

    public class EmptyView extends FrameLayout {
        public EmptyView(Context context) {
            super(context);
            setVisibility(INVISIBLE);
        }
    }

}
