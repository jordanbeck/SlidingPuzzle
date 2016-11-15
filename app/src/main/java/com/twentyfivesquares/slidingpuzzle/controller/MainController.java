package com.twentyfivesquares.slidingpuzzle.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.twentyfivesquares.slidingpuzzle.PuzzleActivity;
import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.util.RecordUtils;
import com.twentyfivesquares.slidingpuzzle.view.SelectPuzzleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainController extends TinyController {

    // TODO: Switch to a RecyclerView with grid adapter.
    @Bind(R.id.main_select_puzzle_2) SelectPuzzleView vPuzzle2;
    @Bind(R.id.main_select_puzzle_3) SelectPuzzleView vPuzzle3;
    @Bind(R.id.main_select_puzzle_4) SelectPuzzleView vPuzzle4;
    @Bind(R.id.main_select_puzzle_5) SelectPuzzleView vPuzzle5;

    private View.OnClickListener selectPuzzleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!(view instanceof SelectPuzzleView)) {
                throw new IllegalStateException("This OnClickListener can not be used on other views.");
            }

            puzzleSelected(((SelectPuzzleView) view).getSize());
        }
    };

    public MainController(Context context) {
        super(context);

        ButterKnife.bind(this, getView());

        vPuzzle2.setOnClickListener(selectPuzzleClickListener);
        vPuzzle3.setOnClickListener(selectPuzzleClickListener);
        vPuzzle4.setOnClickListener(selectPuzzleClickListener);
        vPuzzle5.setOnClickListener(selectPuzzleClickListener);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.controller_main;
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO: Make this dynamically respond to changes in the record
        vPuzzle2.setRecord(RecordUtils.fetchRecord(getContext(), 2));
        vPuzzle3.setRecord(RecordUtils.fetchRecord(getContext(), 3));
        vPuzzle4.setRecord(RecordUtils.fetchRecord(getContext(), 4));
        vPuzzle5.setRecord(RecordUtils.fetchRecord(getContext(), 5));
    }

    private void puzzleSelected(int size) {
        Intent i = new Intent(getContext(), PuzzleActivity.class);
        i.putExtra(PuzzleActivity.EXTRA_PUZZLE_SIZE, size);
        getContext().startActivity(i);
    }
}
