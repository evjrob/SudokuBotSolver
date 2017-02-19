package com.everettsprojects.sudokubotsolver;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;

public class ManualEntryFragment extends Fragment {

    // Keep an array of the TextViews to populate out board
    TextView[] sudokuCellViews;

    // Our sudoku game bord will be based on GridLayout
    GridLayout sudokuBoard;

    // Selected cell tracks which cell of the game is currently focused.
    View selectedCell;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_manual_entry, container, false);
        sudokuBoard = (GridLayout) rootView.findViewById(R.id.sudoku_board);

        // Instantiate the sudokuCellViews array with enough spce for all the cells in the game board
        int columnCount = sudokuBoard.getColumnCount();
        int rowCount = sudokuBoard.getRowCount();
        sudokuCellViews = new TextView[rowCount * columnCount];

        // Create each cell and add it to the sudokuBoard GridLayout view then attach a click listener
        for (int yIndex = 0; yIndex < rowCount; yIndex++) {
            for (int xIndex = 0; xIndex < columnCount; xIndex++) {
                TextView sudokuCellView = new TextView(this.getActivity());
                sudokuCellViews[yIndex * columnCount + xIndex] = sudokuCellView;
                sudokuBoard.addView(sudokuCellView);

                sudokuCellView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleSelectedCell(view);
                        //return true;
                    }
                });
            }
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        /* This approach of adding a global layout listener and running onGlobalLayout() once before removing
         * the listener appears to be the best way to achieve the resizing of the sudokuBoard's child cells.
         * I tried to create a custom layout that extended GridLayout and do the resizing in the onLayout()
         * method, but that led to a huge number of warnings about performance issues from a recursive calling
         * of the requestLayout function during a layout pass. Setting LayoutParams on the TextViews must
         * cause requestLayout() to be called at some point.
         */
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
                                TextView cellView = sudokuCellViews[yIndex * columnCount + xIndex];

                                // Set some of the formatting on the sudokuCellView
                                cellView.setGravity(Gravity.CENTER);
                                cellView.setText(Integer.toString(yIndex * columnCount + xIndex));
                                cellView.setBackgroundColor(Color.WHITE);

                                // Adjust the layout param of the view including the height and width, and the margins.
                                GridLayout.LayoutParams params =
                                        (GridLayout.LayoutParams) sudokuCellViews[yIndex * columnCount + xIndex].getLayoutParams();

                                params.setGravity(Gravity.START);
                                params.columnSpec = GridLayout.spec(xIndex);
                                params.rowSpec = GridLayout.spec(yIndex);

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
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            sudokuBoard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            sudokuBoard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        sudokuBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.clearFocus();
                view.requestFocus();
            }
        });

        sudokuBoard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                View viewSelected = getSelectedCell();
                // if the selected view exists and it lost focus
                if (viewSelected != null && !viewSelected.hasFocus()) {
                    // remove it
                    removeSelectedCell();
                }
            }
        });
    }

    // toggleSelectedCell allows for a user to select and deselect a given cell of the sudoku game board,
    // or simultaneously deselect one cell and select a new one
    public void toggleSelectedCell(View cell) {

        // If the user has clicked on the already selected cell then we just want to deselect it.
        boolean deselectOnly = false;
        if (selectedCell == cell) {
            deselectOnly = true;
        }

        removeSelectedCell();

        if (!deselectOnly) {
            setSelectedCell(cell);
        }
    }

    public void setSelectedCell(View cell) {
        selectedCell = cell;
        if (selectedCell != null) {
            // shade the selectedCell to show it has focus.
            selectedCell.setBackgroundColor(Color.LTGRAY);
        }
    }

    public View getSelectedCell() {
        if (selectedCell != null) {
            return selectedCell;
        }
        return null;
    }

    public void removeSelectedCell() {
        if (selectedCell != null) {
            // restore the background of a deslected cell to white
            selectedCell.setBackgroundColor(Color.WHITE);
            selectedCell = null;
        }

        // clear and reset the focus on the sudoku board
        sudokuBoard.clearFocus();
        sudokuBoard.requestFocus();
    }
}
