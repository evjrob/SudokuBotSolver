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
import android.widget.TextView;

public class SudokuBoardFragment extends Fragment {

    TextView[] sudokuCellViews;

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

        int columnCount = sudokuBoard.getColumnCount();
        int rowCount = sudokuBoard.getRowCount();
        sudokuCellViews = new SudokuCellView[rowCount * columnCount];
        for (int yIndex = 0; yIndex < rowCount; yIndex++) {
            for (int xIndex = 0; xIndex < columnCount; xIndex++) {
                SudokuCellView cellView = new SudokuCellView(this.getActivity(), xIndex, yIndex);
                sudokuCellViews[yIndex * columnCount + xIndex] = cellView;
                sudokuBoard.addView(cellView);
                cellView.setGravity(Gravity.CENTER);
                cellView.setText(Integer.toString(yIndex * columnCount + xIndex));
                cellView.setBackgroundColor(Color.WHITE);

            }
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sudokuBoard.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener(){
                @Override
                public void onGlobalLayout() {
                    final int MARGIN = 2;

                    int pWidth = sudokuBoard.getWidth();
                    int pHeight = sudokuBoard.getHeight();
                    int colCount = sudokuBoard.getColumnCount();
                    int rowCount = sudokuBoard.getRowCount();
                    int w = pWidth / colCount;
                    int h = pHeight / rowCount;

                    for (int yIndex = 0; yIndex < rowCount; yIndex++) {
                        for (int xIndex = 0; xIndex < colCount; xIndex++) {
                            GridLayout.LayoutParams params =
                                    (GridLayout.LayoutParams) sudokuCellViews[yIndex * colCount + xIndex].getLayoutParams();

                            params.setGravity(Gravity.LEFT);

                            int topMargin = MARGIN;
                            int bottomMargin = MARGIN;
                            int leftMargin = MARGIN;
                            int rightMargin = MARGIN;

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
                            sudokuCellViews[yIndex * colCount + xIndex].setLayoutParams(params);
                        }
                    }
                    // Remove this layout listener once the cells have been resized.
                   sudokuBoard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
    }
}
