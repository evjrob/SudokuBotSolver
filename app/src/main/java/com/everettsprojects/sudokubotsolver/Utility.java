package com.everettsprojects.sudokubotsolver;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import go.sudokuBotDlx.Solver;
import go.sudokuBotDlx.SudokuBotDlx;

class Utility {

    private static final String TAG = "GoDLX";

    private static String convertGridToString(ArrayList<String> unsolvedPuzzle) {

        String puzzleString = "";

        for (int i = 0; i < unsolvedPuzzle.size(); i++ ) {
            String cell = unsolvedPuzzle.get(i);
            if (cell != null) {
                if (!cell.equals("")) {
                    puzzleString = puzzleString + cell;
                } else {
                    puzzleString = puzzleString + "0";
                }
                if (i != unsolvedPuzzle.size() - 1) {
                    puzzleString = puzzleString + ",";
                }
            }
        }

        return puzzleString;
    }

    private static ArrayList<String> convertStringToArray(String solvedPuzzle) {

        return new ArrayList<>(Arrays.asList(solvedPuzzle.split(",")));
    }

    static ArrayList<String> solvePuzzle(ArrayList<String> unsolvedPuzzle) {

        String puzzleString = convertGridToString(unsolvedPuzzle);

        Solver solver = SudokuBotDlx.newSolver(puzzleString, 3, 3, ",");

        String solution;

        ArrayList<String> solvedPuzzle = new ArrayList<>();

        try {
            solution = solver.solve();
        } catch (Exception e) {
            Log.e(TAG, "The Go sudokuBotDlx code returned an exception: assuming it was the InvalidInput error for incorrect dimensions.");
            solution = "";
        }

        if (!solution.equals("")) {
            solvedPuzzle = convertStringToArray(solution);
        }

        return solvedPuzzle;
    }
}
