package com.twentyfivesquares.slidingpuzzle.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twentyfivesquares.slidingpuzzle.R;
import com.twentyfivesquares.slidingpuzzle.object.Record;
import com.twentyfivesquares.slidingpuzzle.store.PuzzleStore;
import com.twentyfivesquares.slidingpuzzle.util.RecordUtils;
import com.twentyfivesquares.slidingpuzzle.view.PuzzleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PuzzleController extends TinyController {

    public static final int DEFAULT_SIZE = 3;

    @Bind(R.id.puzzle_puzzle) PuzzleView vPuzzle;
    @Bind(R.id.puzzle_hint_button) Button vHintButton;
    @Bind(R.id.puzzle_solve_button) Button vSolveButton;
    @Bind(R.id.puzzle_move_count) TextView vMoveCount;

    private PuzzleStore store;

    public PuzzleController(Context context, int size) {
        super(context);
        ButterKnife.bind(this, getView());

        // I prefer explicit click listeners to using Butterknife @OnClick annotation :)
        vHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vPuzzle.showHint();
            }
        });
        vSolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vPuzzle.solve();
            }
        });

        store = new PuzzleStore(size);
        store.shufflePuzzle(store.getSize() * store.getSize());
        vPuzzle.initialize(store);
        vPuzzle.setPuzzleListener(new PuzzleView.PuzzleViewListener() {
            @Override
            public void onMoveCompleted(int totalMoves) {
                vMoveCount.setText(getContext().getString(R.string.msg_move_count, totalMoves));
            }

            @Override
            public void onSolved() {
                puzzleSolved();
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.controller_puzzle;
    }

    private void puzzleSolved() {
        Toast.makeText(getContext(), R.string.msg_congratulations, Toast.LENGTH_SHORT).show();

        // Update the records with new information
        Record record = RecordUtils.fetchRecord(getContext(), store.getSize());
        record.totalWins += 1;
        if (record.leastMoves == 0 || store.getMoveCount() < record.leastMoves) {
            record.leastMoves = store.getMoveCount();
        }
        if (record.bestTimeMillis == 0 || store.getDuration() < record.bestTimeMillis) {
            record.bestTimeMillis = store.getDuration();
        }

        // Save the record
        RecordUtils.updateRecord(getContext(), store.getSize(), record);
    }
}
