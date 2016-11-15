package com.twentyfivesquares.slidingpuzzle.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.view.PuzzleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainController extends TinyController {

    @Bind(R.id.main_puzzle) PuzzleView vPuzzle;
    @Bind(R.id.main_hint_button) Button vHintButton;
    @Bind(R.id.main_move_count) TextView vMoveCount;

    public MainController(Context context) {
        super(context);
        ButterKnife.bind(this, getView());

        // I prefer explicit click listeners to using Butterknife @OnClick annotation :)
        vHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vPuzzle.showHint();
            }
        });

        vPuzzle.setPuzzleListener(new PuzzleView.PuzzleViewListener() {
            @Override
            public void onMoveCompleted(int totalMoves) {
                vMoveCount.setText(getContext().getString(R.string.msg_move_count, totalMoves));
            }

            @Override
            public void onSolved() {
                Toast.makeText(getContext(), R.string.msg_congratulations, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.controller_main;
    }
}
