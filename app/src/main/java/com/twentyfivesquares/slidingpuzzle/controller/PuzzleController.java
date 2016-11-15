package com.twentyfivesquares.slidingpuzzle.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.object.Record;
import com.twentyfivesquares.slidingpuzzle.store.PuzzleStore;
import com.twentyfivesquares.slidingpuzzle.util.DateUtils;
import com.twentyfivesquares.slidingpuzzle.util.RecordUtils;
import com.twentyfivesquares.slidingpuzzle.view.PuzzleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PuzzleController extends TinyController {

    public static final int DEFAULT_SIZE = 3;

    @Bind(R.id.puzzle_puzzle) PuzzleView vPuzzle;
    @Bind(R.id.puzzle_hint_button) Button vHintButton;
    @Bind(R.id.puzzle_solve_button) Button vSolveButton;
    @Bind(R.id.puzzle_move_count) TextView vMoveCount;
    @Bind(R.id.puzzle_success_container) View vSuccessContainer;
    @Bind(R.id.puzzle_success_banner) View vSuccessBanner;
    @Bind(R.id.puzzle_star_far_left) View vStarFarLeft;
    @Bind(R.id.puzzle_star_close_left) View vStarCloseLeft;
    @Bind(R.id.puzzle_star_far_right) View vStarFarRight;
    @Bind(R.id.puzzle_star_close_right) View vStarCloseRight;
    @Bind(R.id.puzzle_success_stats) TextView vStats;
    @Bind(R.id.puzzle_success_reset_button) Button vResetButton;

    private PuzzleStore store;

    public PuzzleController(Context context, int size) {
        super(context);
        ButterKnife.bind(this, getView());

        // I prefer explicit click listeners to using Butterknife @OnClick annotation :)
        vHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vPuzzle.showHint();
            }
        });
        vSolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vPuzzle.solve();
            }
        });
        vResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store.resetPuzzle();
                vPuzzle.resetPuzzle();
                hideSuccessBanner();
            }
        });

        store = new PuzzleStore(size);
        store.shufflePuzzle(store.getSize() * store.getSize());
        vPuzzle.initialize(store);
        vPuzzle.setPuzzleListener(new PuzzleView.PuzzleViewListener() {
            @Override
            public void onMoveCompleted(int totalMoves) {
                vMoveCount.setText(getContext().getString(R.string.msg_move_count, totalMoves));
            }

            @Override
            public void onSolved() {
                puzzleSolved();
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.controller_puzzle;
    }

    private void puzzleSolved() {
        // Update the records with new information
        Record record = RecordUtils.fetchRecord(getContext(), store.getSize());
        record.totalWins += 1;
        if (record.leastMoves == 0 || store.getMoveCount() < record.leastMoves) {
            record.leastMoves = store.getMoveCount();
        }
        if (record.bestTimeMillis == 0 || store.getDuration() < record.bestTimeMillis) {
            record.bestTimeMillis = store.getDuration();
        }

        // Save the record
        RecordUtils.updateRecord(getContext(), store.getSize(), record);

        // Set the stats on the success screen
        final String stats = getContext().getString(
                R.string.msg_success_stats, store.getMoveCount(), DateUtils.formatTime(store.getDuration()));
        vStats.setText(stats);

        /**
         * Animation playlist:
         *  - Fade in the success background
         *  - Scale up the success banner (both x and y)
         *  - Scale up the close stars
         *  - Scale up the far stars
         */
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(vSuccessContainer, "alpha", 0.0f, 1.0f);
        alphaAnimator.setDuration(250);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                vSuccessContainer.setVisibility(View.VISIBLE);

                // Make sure all of the scaling is reset
                vSuccessBanner.setScaleX(0.0f);
                vSuccessBanner.setScaleY(0.0f);

                vStarCloseLeft.setScaleX(0.0f);
                vStarCloseLeft.setScaleY(0.0f);
                vStarCloseRight.setScaleX(0.0f);
                vStarCloseRight.setScaleY(0.0f);

                vStarFarLeft.setScaleX(0.0f);
                vStarFarLeft.setScaleY(0.0f);
                vStarFarRight.setScaleX(0.0f);
                vStarFarRight.setScaleY(0.0f);
            }
        });

        AnimatorSet scaleSet = buildScaleAnimation(vSuccessBanner, 1);

        AnimatorSet scaleCloseLeftStar = buildScaleAnimation(vStarCloseLeft, 2);
        AnimatorSet scaleCloseRightStar = buildScaleAnimation(vStarCloseRight, 2);
        AnimatorSet scaleCloseStars = new AnimatorSet();
        scaleCloseStars.playTogether(scaleCloseLeftStar, scaleCloseRightStar);

        AnimatorSet scaleFarLeftStar = buildScaleAnimation(vStarFarLeft, 3);
        AnimatorSet scaleFarRightStar = buildScaleAnimation(vStarFarRight, 3);
        AnimatorSet scaleFarStars = new AnimatorSet();
        scaleFarStars.playTogether(scaleFarLeftStar, scaleFarRightStar);

        AnimatorSet solvedAnimSet = new AnimatorSet();
        solvedAnimSet.playTogether(alphaAnimator, scaleSet, scaleCloseStars, scaleFarStars);
        solvedAnimSet.start();
    }

    private AnimatorSet buildScaleAnimation(View view, int order) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.0f);
        AnimatorSet scaleSet = new AnimatorSet();
        scaleSet.setStartDelay(order * 150);
        scaleSet.setInterpolator(new AnticipateOvershootInterpolator());
        scaleSet.setDuration(500);
        scaleSet.playTogether(scaleX, scaleY);
        return scaleSet;
    }

    private void hideSuccessBanner() {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(vSuccessContainer, "alpha", 1.0f, 0.0f);
        alphaAnimator.setDuration(250);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                vSuccessContainer.setVisibility(View.GONE);
            }
        });
        alphaAnimator.start();
    }
}
