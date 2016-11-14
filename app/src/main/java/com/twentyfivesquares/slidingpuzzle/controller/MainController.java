package com.twentyfivesquares.slidingpuzzle.controller;

import android.content.Context;
import android.widget.Toast;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.view.PuzzleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainController extends TinyController {

    @Bind(R.id.main_puzzle) PuzzleView vPuzzle;

    public MainController(Context context) {
        super(context);
        ButterKnife.bind(this, getView());
        vPuzzle.setPuzzleListener(new PuzzleView.PuzzleViewListener() {
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
