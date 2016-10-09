package com.myadridev.mypocketcave.layoutManagers;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GridAutofitLayoutManager extends GridLayoutManager {

    private int _columnWidth;
    private boolean _columnWidthChanged = true;

    public GridAutofitLayoutManager(Context context, int columnWidth) {
        // Initially set spanCount to 1, will be changed automatically later.
        super(context, 1);
        setColumnWidth(columnWidth);
    }

    private void setColumnWidth(int newColumnWidth) {
        if (newColumnWidth > 0 && newColumnWidth != _columnWidth) {
            _columnWidth = newColumnWidth;
            _columnWidthChanged = true;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (_columnWidthChanged && _columnWidth > 0) {
            int totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            int spanCount = Math.max(1, totalSpace / _columnWidth);
            setSpanCount(spanCount);
            _columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}
