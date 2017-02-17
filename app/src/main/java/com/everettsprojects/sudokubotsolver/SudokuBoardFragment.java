package com.everettsprojects.sudokubotsolver;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;

public class SudokuBoardFragment extends Fragment {

    // Keep an array of the TextView based SudokuCellViews to populate out board
    SudokuCellView[] sudokuCellViews;

    // Our sudoku game bord will be based on GridLayout
    GridLayout sudokuBoard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sudoku_board, container, false);
        sudokuBoard = (GridLayout) rootView.findViewById(R.id.sudoku_board);

        // Instantiate the sudokuCellViews array with enough spce for all the cells in the game board
        int columnCount = sudokuBoard.getColumnCount();
        int rowCount = sudokuBoard.getRowCount();
        sudokuCellViews = new SudokuCellView[rowCount * columnCount];

        // Create each cell and add it to the sudokuBoard GridLayout view
        for (int yIndex = 0; yIndex < rowCount; yIndex++) {
            for (int xIndex = 0; xIndex < columnCount; xIndex++) {
                SudokuCellView cellView = new SudokuCellView(this.getActivity(), xIndex, yIndex);
                sudokuCellViews[yIndex * columnCount + xIndex] = cellView;
                sudokuBoard.addView(cellView);
            }
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // This approach of adding a global layout listener and running onGlobalLayout() once before removing
        // the listener feels hackish to me, but it was recommended in a couple stack overflow answers as
        // the best way to resize elements in a GridLayout and it appears to get the job done.
        sudokuBoard.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener(){
                @Override
                public void onGlobalLayout() {
                    final int MARGIN = 2;

                    int pWidth = sudokuBoard.getWidth();
                    int pHeight = sudokuBoard.getHeight();
                    int columnCount = sudokuBoard.getColumnCount();
                    int rowCount = sudokuBoard.getRowCount();
                    int w = pWidth / columnCount;
                    int h = pHeight / rowCount;

                    for (int yIndex = 0; yIndex < rowCount; yIndex++) {
                        for (int xIndex = 0; xIndex < columnCount; xIndex++) {

                            // The currently indexed cell of the sudokuBoard
                            SudokuCellView cellView = sudokuCellViews[yIndex * columnCount + xIndex];

                            // Set some of the formatting on the sudokuCellView
                            cellView.setGravity(Gravity.CENTER);
                            cellView.setText(Integer.toString(yIndex * columnCount + xIndex));
                            cellView.setBackgroundColor(Color.WHITE);

                            // Adjust the layout param of the view including the height and width, and the margins.
                            GridLayout.LayoutParams params =
                                    (GridLayout.LayoutParams) sudokuCellViews[yIndex * columnCount + xIndex].getLayoutParams();

                            params.setGravity(Gravity.LEFT);

                            int topMargin = MARGIN;
                            int bottomMargin = MARGIN;
                            int leftMargin = MARGIN;
                            int rightMargin = MARGIN;

                            // The margins are used to draw the lines of the gameboard by exposing the dark background of the GridLayout
                            // All of this conditional logic down below is used to ensure that the thick lines used to separate one of the
                            // blocks of 9 from another block are drawn correctly.
                            if (xIndex == 2 || xIndex == 3 || xIndex == 5 || xIndex == 6) {
                                params.width = w - 3 * MARGIN;
                                if (xIndex == 2 || xIndex == 5) {
                                    rightMargin = 2 * MARGIN;
                                } else {
                                    leftMargin = 2 * MARGIN;
                                }
                            } else {
                                params.width = w - 2 * MARGIN;
                            }
                            if (yIndex == 2 || yIndex == 3 || yIndex == 5 || yIndex == 6) {
                                params.height = h - 3 * MARGIN;
                                if (yIndex == 2 || yIndex == 5) {
                                    bottomMargin = 2 * MARGIN;
                                } else {
                                    topMargin = 2 * MARGIN;
                                }
                            } else {
                                params.height = h - 2 * MARGIN;
                            }

                            params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                            sudokuCellViews[yIndex * columnCount + xIndex].setLayoutParams(params);
                        }
                    }

                    // Remove this layout listener once the cells have been resized.
                   sudokuBoard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
    }
}
