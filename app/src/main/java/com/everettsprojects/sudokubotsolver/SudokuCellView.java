package com.everettsprojects.sudokubotsolver;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;


public class SudokuCellView extends android.support.v7.widget.AppCompatTextView {

    // Default constructor override
    public SudokuCellView(Context context) {
        this(context, null);
    }

    // Default constructor when inflating from XML file
    public SudokuCellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // Default constructor override
    public SudokuCellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Have onLayout properly set the width and height of this View so that Gravity.Center
    // will properly position the text. Set the text size to a fraction of the height so that
    // the sudoku game board will scale up well on larger screens.
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float textSize =  (float) ((bottom - top) / 1.5);

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        int widthSpec = MeasureSpec.makeMeasureSpec(right - left, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(bottom - top, MeasureSpec.EXACTLY);
        this.measure(widthSpec, heightSpec);
    }
}
