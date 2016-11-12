package com.twentyfivesquares.slidingpuzzle.controller;


import android.content.Context;

import com.twentyfivesquares.slidingpuzzle.R;

public class MainController extends TinyController {

    public MainController(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.controller_main;
    }
}
