package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;

import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelWithBottlesV2;

public class CreateCaveEditArrangementTask extends AsyncTask<Void, Void, PatternAdapter> {

    private final CaveArrangementAdapter caveArrangementAdapter;
    private final CaveArrangementViewHolder holder;
    private final AbstractCaveEditActivity editActivity;
    private final PatternModelWithBottlesV2 patternWithBottles;
    private final int numberRowsGridLayout;
    private final int numberColumnsGridLayout;
    private final int itemWidth;
    private final CoordinatesModelV2 coordinates;
    private final OnPatternClickListener listener;

    public CreateCaveEditArrangementTask(CaveArrangementAdapter caveArrangementAdapter, CaveArrangementViewHolder holder, AbstractCaveEditActivity editActivity,
                                         PatternModelWithBottlesV2 patternWithBottles, int numberRowsGridLayout,
                                         int numberColumnsGridLayout, int itemWidth, CoordinatesModelV2 coordinates,
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
                new CoordinatesModelV2(numberRowsGridLayout, numberColumnsGridLayout),
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
