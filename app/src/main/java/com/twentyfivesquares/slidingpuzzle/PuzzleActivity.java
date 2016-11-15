package com.twentyfivesquares.slidingpuzzle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twentyfivesquares.slidingpuzzle.controller.PuzzleController;

public class PuzzleActivity extends AppCompatActivity {

    public static final String EXTRA_PUZZLE_SIZE = PuzzleActivity.class.getName() + ".PUZZLE_SIZE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PuzzleController controller = new PuzzleController(this);
        if (getIntent().hasExtra(EXTRA_PUZZLE_SIZE)) {
            controller.setPuzzleSize(getIntent().getIntExtra(EXTRA_PUZZLE_SIZE, PuzzleController.DEFAULT_SIZE));
        }
        setContentView(controller.getView());
    }
}
