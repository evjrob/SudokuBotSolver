package com.everettsprojects.sudokubotsolver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import go.sudokuBotDlx.Solver;
import go.sudokuBotDlx.SudokuBotDlx;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GoDLX";

    Button solveButton;
    EditText inputPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final TextView s = (TextView) findViewById(R.id.solution_textview);
        final TextView o = (TextView) findViewById(R.id.original_textview);

        solveButton = (Button)findViewById(R.id.solve_button);
        inputPuzzle   = (EditText)findViewById(R.id.input_textview);

        solveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                s.setText("");
                o.setText("");

                String puzzle = inputPuzzle.getText().toString();

                Solver solver = SudokuBotDlx.newSolver(puzzle, 3, 3, "");

                String solution;

                try {
                    solution = solver.solve();
                } catch (Exception e) {
                    Log.e(TAG, "The Go sudokuBotDlx code returned an exception: assuming it was the InvalidInput error for incorrect dimensions.");
                    solution = "Invalid Input Puzzle";
                }
                s.setText(solution);
                o.setText(puzzle);
                inputPuzzle.setText("");

            }
        });
    }
}
