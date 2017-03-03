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

    // findPuzzleContradictions returns a boolean ArrayList of the same length as unsolvedPuzzle with a true or a false
    // at the same index for each clue in unsolvedPuzzle indicating if there was a contradiction with another clue.
    static ArrayList<Boolean> findPuzzleContradictions(ArrayList<String> unsolvedPuzzle) {

        int blockXDim = 3;
        int blockYDim = 3;
        int peerGroupSize = blockXDim * blockYDim;
        ArrayList<Boolean> contradictions = new ArrayList<>();

        // For every clue in the puzzle
        for (int i = 0; i < unsolvedPuzzle.size(); i++ ) {

            String thisClue = unsolvedPuzzle.get(i);
            contradictions.add(i, false);

            // If the clue is not a zero
            if (!thisClue.equals("0")) {

                ArrayList<String> peers = new ArrayList<>();

                int cellRow = i / peerGroupSize;
                int cellColumn = i % peerGroupSize;
                int blockStartRow = (cellRow / blockYDim) * blockYDim;
                int blockStartColumn = (cellColumn / blockXDim) * blockXDim;

                for (int j = 0; j < peerGroupSize; j++) {

                    // Add the peers in the same row
                    int rowPeerIndex = (cellRow * peerGroupSize) + j;
                    if (rowPeerIndex != i) {
                        peers.add(unsolvedPuzzle.get(rowPeerIndex));
                    }

                    // Add the peers in the same column
                    int columnPeerIndex = (j * peerGroupSize) + cellColumn;
                    if (columnPeerIndex != i) {
                        peers.add(unsolvedPuzzle.get(columnPeerIndex));
                    }

                    // Add the peers in the same block
                    int blockPeerIndex = (blockStartRow + (j / blockXDim)) * peerGroupSize + (blockStartColumn + (j % blockYDim));
                    if (blockPeerIndex != i) {
                        peers.add(unsolvedPuzzle.get(blockPeerIndex));
                    }
                }

                // For the above peers, check if any are the same value as unsolvedPuzzle[i] and flag as a contradiction if so.
                for (String peer: peers) {
                    if (peer.equals(thisClue)) {
                        contradictions.set(i, true);
                    }
                }
            }
        }

        return contradictions;
    }
}
