package com.twentyfivesquares.slidingpuzzle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twentyfivesquares.slidingpuzzle.controller.PuzzleController;

public class PuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PuzzleController controller = new PuzzleController(this);
        setContentView(controller.getView());
    }
}
