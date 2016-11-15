package com.twentyfivesquares.slidingpuzzle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.object.Record;
import com.twentyfivesquares.slidingpuzzle.util.DateUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectPuzzleView extends FrameLayout {

    @Bind(R.id.select_puzzle_size) TextView vSize;
    @Bind(R.id.select_puzzle_difficulty) TextView vDifficulty;
    @Bind(R.id.select_puzzle_total_wins) TextView vTotalWins;
    @Bind(R.id.select_puzzle_best_moves) TextView vBestMoves;
    @Bind(R.id.select_puzzle_best_time) TextView vBestTime;

    private int size;

    public SelectPuzzleView(Context context) {
        this(context, null);
    }

    public SelectPuzzleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectPuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectPuzzleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_select_puzzle, this);
        ButterKnife.bind(this);

        // Search for size and difficulty label in attributes
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SelectPuzzle, 0, 0);
        String difficulty = a.getString(R.styleable.SelectPuzzle_selectPuzzleDifficulty);
        int background = a.getColor(R.styleable.SelectPuzzle_selectPuzzleBackground,
                context.getResources().getColor(R.color.colorAccent));
        size = a.getInt(R.styleable.SelectPuzzle_selectPuzzleSize, 3);
        a.recycle();

        // Set initial values
        setBackgroundColor(background);
        vSize.setText(size + "x" + size);
        vDifficulty.setText(difficulty);
    }

    public void setRecord(Record record) {
        vTotalWins.setText(Integer.toString(record.totalWins));
        vBestMoves.setText(record.leastMoves == 0 ? "--" : Integer.toString(record.leastMoves));
        vBestTime.setText(record.bestTimeMillis == 0 ? "--:--" : DateUtils.formatTime(record.bestTimeMillis));
    }

    public int getSize() {
        return size;
    }
}
