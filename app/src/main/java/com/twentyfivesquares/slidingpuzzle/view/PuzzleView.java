package com.twentyfivesquares.slidingpuzzle.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;
import com.twentyfivesquares.slidingpuzzle.store.PuzzleStore;

import java.util.Map;

public class PuzzleView extends ViewGroup {

    public interface PuzzleViewListener {
        void onMoveCompleted(int totalMoves);
        void onSolved();
    }

    private final int SIZE = 3;

    private PuzzleStore store;
    private PuzzleViewListener listener;
    private boolean locked;

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
        locked = false;

        store = new PuzzleStore(SIZE);
        store.shufflePuzzle(1);

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
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int childSpec = MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.EXACTLY);
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View child = getChildAt(i);
            child.measure(childSpec, childSpec);
        }

        // Overwrite the height to match the width
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

    public void setPuzzleListener(PuzzleViewListener listener) {
        this.listener = listener;
    }

    public void showHint() {
        final PuzzlePoint hintPoint = store.getSolution().peek();

        // TODO: Come up with better way to get PuzzleView based off of PuzzlePoint
        for (int i = 0, size = getChildCount(); i < size; i++) {
            final View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView tileView = (TileView) child;
                if (tileView.getPosition().equals(hintPoint)) {
                    ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(tileView, "alpha", 0.5f, 1.0f);
                    alphaAnim.setDuration(350);
                    alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                    alphaAnim.start();
                }
            }
        }
    }

    private void moveTile(TileView tileView) {
        if (locked) {
            return;
        }

        locked = true;
        // Run a scaling animation if the tile can not move
        if (!store.canMove(tileView.position)) {
            // Simple scale animation
            ObjectAnimator animX = ObjectAnimator.ofFloat(tileView, "scaleX", 0.95f, 1.0f);
            ObjectAnimator animY = ObjectAnimator.ofFloat(tileView, "scaleY", 0.95f, 1.0f);
            AnimatorSet set = new AnimatorSet();
            set.setInterpolator(new BounceInterpolator());
            set.playTogether(animX, animY);
            // Add listener to unlock view when animation is finished
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    locked = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    locked = false;
                }
            });
            set.start();
            return;
        }

        final PuzzlePoint emptyTile = store.getEmptyPoint();
        PuzzlePoint.Direction direction = tileView.getPosition().relativeTo(emptyTile);
        final float tx = tileView.getTranslationX();
        final float ty = tileView.getTranslationY();
        switch (direction) {
            case LEFT:
                animateTileX(tileView, tx - tileView.getMeasuredWidth());
                break;
            case RIGHT:
                animateTileX(tileView, tx + tileView.getMeasuredWidth());
                break;
            case ABOVE:
                animateTileY(tileView, ty - tileView.getMeasuredHeight());
                break;
            case BELOW:
                animateTileY(tileView, ty + tileView.getMeasuredHeight());
                break;
        }
    }

    private void animateTileX(final TileView tileView, float translation) {
        ViewPropertyAnimator moveAnimator = buildMoveAnimator(tileView);
        moveAnimator.translationX(translation);
        moveAnimator.start();
    }

    private void animateTileY(final TileView tileView, float translation) {
        ViewPropertyAnimator moveAnimator = buildMoveAnimator(tileView);
        moveAnimator.translationY(translation);
        moveAnimator.start();
    }

    private ViewPropertyAnimator buildMoveAnimator(final TileView tileView) {
        return tileView.animate()
                .setDuration(250)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        locked = false;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        locked = false;
                        moveCompleted(tileView);
                    }
                });
    }

    private void moveCompleted(TileView tileView) {
        // Execute the move to update the puzzle
        final PuzzlePoint oldEmptyPoint = store.getEmptyPoint();
        store.move(tileView.position);
        tileView.setPosition(oldEmptyPoint);

        if (listener != null) {
            listener.onMoveCompleted(store.getMoveCount());
        }

        if (store.isSolved()) {
            // Re-lock the board
            locked = true;
            // Inform listener that the puzzle has been solved
            if (listener != null) {
                listener.onSolved();
            }
        }
    }

    protected class TileView extends TextView {
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

    protected class EmptyView extends FrameLayout {
        public EmptyView(Context context) {
            super(context);
            setVisibility(INVISIBLE);
        }
    }
}
