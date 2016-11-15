package com.twentyfivesquares.slidingpuzzle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.twentyfivesquares.slidingpuzzle.controller.MainController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MainController controller = new MainController(this);
        setContentView(controller.getView());
    }
}
