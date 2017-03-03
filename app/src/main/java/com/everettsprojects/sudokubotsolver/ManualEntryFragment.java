package com.everettsprojects.sudokubotsolver;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ManualEntryFragment extends Fragment implements View.OnClickListener {

    // Keep an array list of the TextViews to populate out board
    ArrayList<SudokuCellView> sudokuCellViews;

    // Keep track of the original puzzle and solution to facilitate switching between them
    ArrayList<String> unsolvedPuzzle;
    boolean displaySolved = false;

    // Our sudoku game bord will be based on GridLayout
    SudokuLayout sudokuBoard;

    // Selected cell tracks which cell of the game is currently focused.
    View selectedCell;

    // Keypad buttons
    Button mKeyPad1;
    Button mKeyPad2;
    Button mKeyPad3;
    Button mKeyPad4;
    Button mKeyPad5;
    Button mKeyPad6;
    Button mKeyPad7;
    Button mKeyPad8;
    Button mKeyPad9;
    Button mKeyPadClearAll;
    Button mKeyPadClear;
    Button mKeyPadSolve;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_manual_entry, container, false);

        // Instantiate the sudokuCellViews array with enough spce for all the cells in the game board
        sudokuBoard = (SudokuLayout) rootView.findViewById(R.id.sudoku_board);
        int columnCount = sudokuBoard.getColumnCount();
        int rowCount = sudokuBoard.getRowCount();

        sudokuCellViews = new ArrayList<>();
        unsolvedPuzzle = new ArrayList<>();
        int selectedCellIndex = -1;
        boolean useSavedState = false;

        // Restore the savedInstanceState if there is one
        if (savedInstanceState != null) {
            unsolvedPuzzle = savedInstanceState.getStringArrayList("unsolvedPuzzle");
            displaySolved = savedInstanceState.getBoolean("displaySolved");
            selectedCellIndex = savedInstanceState.getInt("selectedCellIndex");
            useSavedState = true;
        }

        // Create each cell and add it to the sudokuBoard GridLayout with the right formatting and a clickListener
        for (int yIndex = 0; yIndex < rowCount; yIndex++) {
            for (int xIndex = 0; xIndex < columnCount; xIndex++) {

                // flattenedIndex is used to index the sudoku cells and strings in one dimensional
                // array lists like sudokuCellViews and un
                int flattenedIndex = yIndex * columnCount + xIndex;

                SudokuCellView sudokuCellView = new SudokuCellView(this.getActivity());
                sudokuCellViews.add(flattenedIndex, sudokuCellView);
                sudokuBoard.addView(sudokuCellView);

                // Set the default cell and text colour. Let it be changed again below if the game is
                // in the displaySolved State.
                sudokuCellView.setBackgroundColor(Color.WHITE);
                sudokuCellView.setTextColor(Color.DKGRAY);
                sudokuCellView.setGravity(Gravity.CENTER);

                // If we had a saved state then restore those values.
                if (useSavedState) {
                    String unsolvedCell = unsolvedPuzzle.get(flattenedIndex);

                    if (unsolvedCell != null) {
                        sudokuCellView.setText(unsolvedCell);
                    }
                    if (flattenedIndex == selectedCellIndex) {
                        toggleSelectedCell(sudokuCellView);
                    }
                } else {
                    unsolvedPuzzle.add(flattenedIndex, "");
                }

                // Add the ClickListener and onClick() method for this cell.
                sudokuCellView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleSelectedCell(view);
                    }
                });
            }
        }

        // Set up the keypad buttons
        mKeyPad1 = (Button) rootView.findViewById(R.id.keypad_1);
        mKeyPad1.setOnClickListener(this);
        mKeyPad2 = (Button) rootView.findViewById(R.id.keypad_2);
        mKeyPad2.setOnClickListener(this);
        mKeyPad3 = (Button) rootView.findViewById(R.id.keypad_3);
        mKeyPad3.setOnClickListener(this);
        mKeyPad4 = (Button) rootView.findViewById(R.id.keypad_4);
        mKeyPad4.setOnClickListener(this);
        mKeyPad5 = (Button) rootView.findViewById(R.id.keypad_5);
        mKeyPad5.setOnClickListener(this);
        mKeyPad6 = (Button) rootView.findViewById(R.id.keypad_6);
        mKeyPad6.setOnClickListener(this);
        mKeyPad7 = (Button) rootView.findViewById(R.id.keypad_7);
        mKeyPad7.setOnClickListener(this);
        mKeyPad8 = (Button) rootView.findViewById(R.id.keypad_8);
        mKeyPad8.setOnClickListener(this);
        mKeyPad9 = (Button) rootView.findViewById(R.id.keypad_9);
        mKeyPad9.setOnClickListener(this);
        mKeyPadClear = (Button) rootView.findViewById(R.id.clear_cell_button);
        mKeyPadClear.setOnClickListener(this);
        mKeyPadClearAll = (Button) rootView.findViewById(R.id.clear_all_button);
        mKeyPadClearAll.setOnClickListener(this);
        mKeyPadClearAll.setOnLongClickListener(new View.OnLongClickListener() {
            // A long click of the clear button will clear the entire grid.
            @Override
            public boolean onLongClick(View v) {
                clearAllCellContents();
                return true;
            }
        });
        mKeyPadSolve = (Button) rootView.findViewById(R.id.solve_button);
        mKeyPadSolve.setOnClickListener(this);

        // Return the sudokuBoard to the solved state if that's how it was before.
        if (displaySolved) {
            solve();
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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

    // Store the contents of the sudokuBoard and the index of selectedCell to preserve the
    // app state through the fragment lifecycle.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int selectedCellIndex = -1;

        for (int i = 0; i < sudokuCellViews.size(); i++) {
            if (selectedCell == sudokuCellViews.get(i)) {
                selectedCellIndex = i;
            }
        }
        outState.putStringArrayList("unsolvedPuzzle", unsolvedPuzzle);
        outState.putInt("selectedCellIndex", selectedCellIndex);
        outState.putBoolean("displaySolved", displaySolved);
    }

    // An onClick function for the keypad
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.keypad_1:
                setSelectedCellContents("1");
                break;
            case R.id.keypad_2:
                setSelectedCellContents("2");
                break;
            case R.id.keypad_3:
                setSelectedCellContents("3");
                break;
            case R.id.keypad_4:
                setSelectedCellContents("4");
                break;
            case R.id.keypad_5:
                setSelectedCellContents("5");
                break;
            case R.id.keypad_6:
                setSelectedCellContents("6");
                break;
            case R.id.keypad_7:
                setSelectedCellContents("7");
                break;
            case R.id.keypad_8:
                setSelectedCellContents("8");
                break;
            case R.id.keypad_9:
                setSelectedCellContents("9");
                break;
            case R.id.clear_cell_button:
                setSelectedCellContents("");
                break;
            case R.id.clear_all_button:
                createClearAllToast();
                break;
            case R.id.solve_button:
                toggleSolve();
                break;
        }
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
            selectedCell.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selectedBlue));
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

    private void setSelectedCellContents(String content) {
        if (selectedCell != null) {
            ((SudokuCellView) selectedCell).setText(content);
            unsolvedPuzzle.set(getIndexofCell((SudokuCellView) selectedCell), content);
        }
    }

    // Clear the contents of all cells in the sudokuBoard and the supporting contents in the
    // unsolvedPuzzle ArrayList.
    private void clearAllCellContents() {
        unsolve();
        for (int i = 0; i < sudokuCellViews.size(); i++) {
            SudokuCellView cell = sudokuCellViews.get(i);
            if (cell != null) {
                cell.setText("");
                unsolvedPuzzle.set(i, "");
            }
        }
    }

    private int getIndexofCell(SudokuCellView cell) {
        return sudokuCellViews.indexOf(cell);
    }

    // Solve runs the sudokuBotDlx code to get a solution, and then displays that solution on the
    // sudoku game board.
    private void solve() {

        ArrayList<String> solvedPuzzle = Utility.solvePuzzle(unsolvedPuzzle);
        if (!solvedPuzzle.isEmpty()) {
            for (int i = 0; i < sudokuCellViews.size(); i++) {
                String solvedCell = solvedPuzzle.get(i);
                String unsolvedCell = unsolvedPuzzle.get(i);
                SudokuCellView cell = sudokuCellViews.get(i);

                cell.setText(solvedCell);

                if (!solvedCell.equals(unsolvedCell)) {
                    cell.setTextColor(ContextCompat.getColor(getActivity(), R.color.solvedGreen));
                } else {
                    cell.setTypeface(null, Typeface.BOLD);
                }
            }
        } else {
            ArrayList<Boolean> contradictions = Utility.findPuzzleContradictions(unsolvedPuzzle);
            for (int i = 0; i < sudokuCellViews.size(); i++) {
                String unsolvedCell = unsolvedPuzzle.get(i);
                SudokuCellView cell = sudokuCellViews.get(i);

                cell.setText(unsolvedCell);
                if (contradictions.get(i)) {
                    cell.setTextColor(ContextCompat.getColor(getActivity(), R.color.impossibleRed));
                    cell.setTypeface(null, Typeface.BOLD);
                }
            }

            Toast toast = Toast.makeText(getActivity(), "This sudoku is impossible to solve.",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        displaySolved = true;
        mKeyPadSolve.setText(R.string.keypad_unsolve);
        toggleKeypadEnabled();
    }

    // Unsolve sets the gameboard back to the state it was in
    private void unsolve() {
        for (int i = 0; i < sudokuCellViews.size(); i++) {
            SudokuCellView cell = sudokuCellViews.get(i);
            cell.setText(unsolvedPuzzle.get(i));
            cell.setTypeface(null, Typeface.NORMAL);
            cell.setTextColor(Color.DKGRAY);
        }

        displaySolved = false;
        mKeyPadSolve.setText(R.string.keypad_solve);
        toggleKeypadEnabled();
    }

    // ToggleSolve is a wrapper method that makes it easy for the solve button to double as the
    // unsolve button given the displayed state of the game board.
    private void toggleSolve() {
        if (displaySolved) {
            unsolve();
        } else if (!displaySolved) {
            solve();
        }
    }

    // ToggleKeypadEnabled is a function that allows the numeric inputs to be enabled and disable
    // so that accidental input cannot occur when the puzzle is in the solved state
    private void toggleKeypadEnabled() {

        // The keypad should be enabled iff the solution is not being displayed.
        boolean keyPadEnabled = !displaySolved;

        mKeyPad1.setEnabled(keyPadEnabled);
        mKeyPad2.setEnabled(keyPadEnabled);
        mKeyPad3.setEnabled(keyPadEnabled);
        mKeyPad4.setEnabled(keyPadEnabled);
        mKeyPad5.setEnabled(keyPadEnabled);
        mKeyPad6.setEnabled(keyPadEnabled);
        mKeyPad7.setEnabled(keyPadEnabled);
        mKeyPad8.setEnabled(keyPadEnabled);
        mKeyPad9.setEnabled(keyPadEnabled);
        mKeyPadClear.setEnabled(keyPadEnabled);
    }

    //
    private void createClearAllToast() {
        Toast toast = Toast.makeText(getActivity(), "Please press the button longer to confirm",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
