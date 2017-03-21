package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;

import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

public class CreateCaveEditArrangementTask extends AsyncTask<Void, Void, PatternAdapter> {

    private final CaveArrangementAdapter caveArrangementAdapter;
    private final CaveArrangementViewHolder holder;
    private final AbstractCaveEditActivity editActivity;
    private final PatternModelWithBottles patternWithBottles;
    private final int numberRowsGridLayout;
    private final int numberColumnsGridLayout;
    private final int itemWidth;
    private final CoordinatesModel coordinates;
    private final OnPatternClickListener listener;

    public CreateCaveEditArrangementTask(CaveArrangementAdapter caveArrangementAdapter, CaveArrangementViewHolder holder, AbstractCaveEditActivity editActivity,
                                         PatternModelWithBottles patternWithBottles, int numberRowsGridLayout,
                                         int numberColumnsGridLayout, int itemWidth, CoordinatesModel coordinates,
                                         OnPatternClickListener listener) {
        this.caveArrangementAdapter = caveArrangementAdapter;
        this.holder = holder;
        this.editActivity = editActivity;
        this.patternWithBottles = patternWithBottles;
        this.numberRowsGridLayout = numberRowsGridLayout;
        this.numberColumnsGridLayout = numberColumnsGridLayout;
        this.itemWidth = itemWidth;
        this.coordinates = coordinates;
        this.listener = listener;
    }

    @Override
    protected PatternAdapter doInBackground(Void... params) {
        return new PatternAdapter(editActivity, patternWithBottles.PlaceMapWithBottles,
                new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout),
                false, itemWidth, null);
    }

    @Override
    protected void onPostExecute(PatternAdapter patternAdapter) {
        holder.setPatternViewLayoutManager(new GridLayoutManager(editActivity, numberColumnsGridLayout));
        holder.setOnItemClickListener(listener, coordinates);
        holder.setClickableSpaceDimensions(itemWidth, itemWidth);
        holder.setPatternViewAdapter(patternAdapter);
        caveArrangementAdapter.onPatternLoaded();
    }
}
