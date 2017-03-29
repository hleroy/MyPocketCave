package com.myadridev.mypocketcave.tasks.pattern;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;

import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.SelectionPatternViewHolder;
import com.myadridev.mypocketcave.listeners.OnSelectionPatternClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

public class CreatePatternAdapterTask extends AsyncTask<Void, Void, PatternAdapter> {

    private final Activity activity;
    private final SelectionPatternViewHolder holder;
    private final PatternModel pattern;
    private final int itemWidth;
    private final OnSelectionPatternClickListener onItemClickListener;
    private int numberColumnsGridLayout;

    public CreatePatternAdapterTask(Activity activity, SelectionPatternViewHolder holder, PatternModel pattern, int itemWidth, OnSelectionPatternClickListener onItemClickListener) {
        this.activity = activity;
        this.holder = holder;
        this.pattern = pattern;
        this.itemWidth = itemWidth;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected PatternAdapter doInBackground(Void... params) {
        int numberRowsGridLayout = pattern.getNumberRowsGridLayout();
        numberColumnsGridLayout = pattern.getNumberColumnsGridLayout();
        if (numberColumnsGridLayout > 0) {
            return new PatternAdapter(activity, pattern.getPlaceMapForDisplay(),
                    new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout), false, itemWidth, null);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(PatternAdapter adapter) {
        if (adapter == null) return;
        holder.setPatternViewLayoutManager(new GridLayoutManager(activity, numberColumnsGridLayout));
        holder.setPatternViewAdapter(adapter);
        holder.setOnItemClickListener(onItemClickListener, pattern.Id);
        holder.setClickableSpaceDimensions(itemWidth, itemWidth);
    }
}
