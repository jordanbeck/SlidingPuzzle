package com.twentyfivesquares.slidingpuzzle.controller;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.twentyfivesquares.slidingpuzzle.PuzzleActivity;
import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.view.SelectPuzzleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainController extends TinyController {

    @Bind(R.id.main_container) ViewGroup vContainer;

    public MainController(Context context) {
        super(context);

        ButterKnife.bind(this, getView());

        // Add boards from 2x2 tpo 5x5
        for (int i = 2; i <= 5; i++) {
            SelectPuzzleView vSelectPuzzle = new SelectPuzzleView(context, i);
            vSelectPuzzle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    puzzleSelected(((SelectPuzzleView) view).getSize());
                }
            });
            vContainer.addView(vSelectPuzzle);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.controller_main;
    }

    private void puzzleSelected(int size) {
        Intent i = new Intent(getContext(), PuzzleActivity.class);
        i.putExtra(PuzzleActivity.EXTRA_PUZZLE_SIZE, size);
        getContext().startActivity(i);
    }
}
