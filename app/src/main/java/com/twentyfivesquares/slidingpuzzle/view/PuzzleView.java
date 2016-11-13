package com.twentyfivesquares.slidingpuzzle.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;
import com.twentyfivesquares.slidingpuzzle.store.PuzzleStore;

import java.util.Map;

public class PuzzleView extends ViewGroup {

    private final int SIZE = 3;

    private PuzzleStore store;

    public PuzzleView(Context context) {
        super(context);
        init(context);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        store = new PuzzleStore(SIZE);
        store.shufflePuzzle(10);

        final Map<PuzzlePoint, Integer> puzzleMap = store.getPuzzleMap();
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                final PuzzlePoint position = new PuzzlePoint(x, y);
                final Integer label = puzzleMap.get(position);
                if (label == PuzzleStore.EMPTY) {
                    addView(new EmptyView(context));
                } else {
                    final TileView tileView = new TileView(context, position);
                    tileView.setLabel(label);
                    tileView.setBackgroundResource(label % 2 == 0 ? R.color.colorAccent : R.color.colorAccentDark);
                    tileView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            moveTile((TileView) view);
                        }
                    });
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

    private void moveTile(TileView tileView) {
        if (!store.canMove(tileView.position)) {
            // Make the object width 50%
            ObjectAnimator animX = ObjectAnimator.ofFloat(tileView, "scaleX", 0.95f, 1.0f);
            ObjectAnimator animY = ObjectAnimator.ofFloat(tileView, "scaleY", 0.95f, 1.0f);
            AnimatorSet set = new AnimatorSet();
            set.setInterpolator(new BounceInterpolator());
            set.playTogether(animX, animY);
            set.start();
        }
    }

    public class TileView extends TextView {
        private PuzzlePoint position;
        private Integer label;

        public TileView(Context context, PuzzlePoint position) {
            super(context);
            this.position = position;

            setGravity(Gravity.CENTER);
            // Set text appearance
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextAppearance(R.style.AppTheme_TextAppearance_PuzzlePiece);
            } else {
                setTextAppearance(context, R.style.AppTheme_TextAppearance_PuzzlePiece);
            }
        }

        public Integer getLabel() {
            return label;
        }

        public void setLabel(Integer label) {
            this.label = label;
            setText(label.toString());
        }

        public PuzzlePoint getPosition() {
            return position;
        }

        public void setPosition(PuzzlePoint position) {
            this.position = position;
        }
    }

    public class EmptyView extends FrameLayout {
        public EmptyView(Context context) {
            super(context);
            setVisibility(INVISIBLE);
        }
    }

}
