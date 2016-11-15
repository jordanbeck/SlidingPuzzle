package com.twentyfivesquares.slidingpuzzle.view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.twentyfivesquares.slidingpuzzle.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectPuzzleView extends FrameLayout {

    @Bind(R.id.select_puzzle_size) TextView vSize;

    private int size;

    public SelectPuzzleView(Context context, int size) {
        super(context);
        this.size = size;

        inflate(context, R.layout.view_select_puzzle, this);
        ButterKnife.bind(this);

        vSize.setText(size + "x" + size);
    }

    public int getSize() {
        return size;
    }
}
