package com.everettsprojects.sudokubotsolver;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;


public class SudokuLayout extends GridLayout implements View.OnClickListener{

    public SudokuLayout(Context context) {
        super(context);
        init();
    }

    public SudokuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SudokuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // handle focus and click states
    public void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnClickListener(this);
    }

    @Override
    protected void onLayout(boolean c, int l, int t, int r, int b) {
        super.onLayout(c, l, t, r, b);

        final int MARGIN = 2;

        int pWidth = this.getWidth();
        int pHeight = this.getHeight();
        int columnCount = this.getColumnCount();
        int rowCount = this.getRowCount();
        int w = (pWidth - (16 * MARGIN))/ columnCount;
        int h = (pHeight - (16 * MARGIN))/ rowCount;

        int leftoverWidth = pWidth - (columnCount * w + 16 * MARGIN);
        int leftoverHeight = pHeight - (getRowCount() * h + 16 * MARGIN);

        for (int yIndex = 0; yIndex < rowCount; yIndex++) {
            for (int xIndex = 0; xIndex < columnCount; xIndex++) {

                int flattenedIndex = yIndex * columnCount + xIndex;

                // The currently indexed cell of the sudokuBoard
                View cell = this.getChildAt(flattenedIndex);

                int childL = leftoverWidth + MARGIN + (xIndex * (MARGIN + w) + 3 * (xIndex / 3));
                int childR = leftoverWidth + ((xIndex + 1) * (MARGIN + w) + 3 * (xIndex / 3));

                int childT = leftoverHeight + MARGIN + (yIndex * (MARGIN + h) + 3 * (yIndex / 3));
                int childB = leftoverHeight + ((yIndex + 1) * (MARGIN + h) + 3 * (yIndex / 3));
                cell.layout(childL, childT, childR, childB);
            }
        }
    }

    // handle the click events
    @Override
    public void onClick(View view) {
        // clear and set the focus on this viewgroup
        this.clearFocus();
        this.requestFocus();
        // now, the focus listener in Activity will handle
        // the focus change state when this layout is clicked
    }
}
