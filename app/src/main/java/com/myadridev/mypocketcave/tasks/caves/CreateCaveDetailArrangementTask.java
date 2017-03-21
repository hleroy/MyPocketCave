package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.listeners.OnValueChangedListener;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

public class CreateCaveDetailArrangementTask extends AsyncTask<Void, Void, PatternAdapter> {

    private final CaveArrangementViewHolder holder;
    private final CaveArrangementAdapter caveArrangementAdapter;
    private final CaveDetailActivity detailActivity;
    private final PatternModelWithBottles patternWithBottles;
    private final int numberRowsGridLayout;
    private final int numberColumnsGridLayout;
    private final int itemWidth;
    private final CoordinatesModel coordinates;
    private final int bottleIdInHighlight;
    private final CaveArrangementModel caveArrangement;
    private final OnValueChangedListener onValueChangedListener;

    public CreateCaveDetailArrangementTask(CaveArrangementAdapter caveArrangementAdapter, CaveArrangementViewHolder holder,
                                           CaveDetailActivity detailActivity, PatternModelWithBottles patternWithBottles,
                                           int numberRowsGridLayout, int numberColumnsGridLayout, int itemWidth,
                                           CoordinatesModel coordinates, int bottleIdInHighlight, CaveArrangementModel caveArrangement,
                                           OnValueChangedListener onValueChangedListener) {
        this.caveArrangementAdapter = caveArrangementAdapter;
        this.holder = holder;
        this.detailActivity = detailActivity;
        this.patternWithBottles = patternWithBottles;
        this.numberRowsGridLayout = numberRowsGridLayout;
        this.numberColumnsGridLayout = numberColumnsGridLayout;
        this.itemWidth = itemWidth;
        this.coordinates = coordinates;
        this.bottleIdInHighlight = bottleIdInHighlight;
        this.caveArrangement = caveArrangement;
        this.onValueChangedListener = onValueChangedListener;
    }

    @Override
    protected PatternAdapter doInBackground(Void... params) {
        PatternAdapter patternAdapter = new PatternAdapter(detailActivity, patternWithBottles.PlaceMapWithBottles,
                new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout),
                true, itemWidth, coordinates, bottleIdInHighlight);

        patternAdapter.setOnBottlePlacedClickListener((int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates1) -> {
            // here quantity is 1 in all cases : we ignore it
            caveArrangement.placeBottle(patternCoordinates, coordinates1, bottleId);
            BottleManager.placeBottle(detailActivity, bottleId);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged();
            }
        });
        patternAdapter.setOnBottleDrunkClickListener((int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates1) -> {
            // here quantity is 1 in all cases : we ignore it
            caveArrangement.unplaceBottle(patternCoordinates, coordinates1, bottleId);
            BottleManager.drinkBottle(detailActivity, bottleId);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged();
            }
        });
        patternAdapter.setOnBottleUnplacedClickListener((int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates1) -> {
            // here quantity is 1 in all cases : we ignore it
            caveArrangement.unplaceBottle(patternCoordinates, coordinates1, bottleId);
            BottleManager.updateNumberPlaced(detailActivity, bottleId, -1);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged();
            }
        });

        patternAdapter.setOnSetHighlightlistener(this::setBottleIdInHighlight);
        patternAdapter.setOnResetHighlightlistener((View v) -> resetBottleIdInHighlight());
        return patternAdapter;
    }

    @Override
    protected void onPostExecute(PatternAdapter patternAdapter) {
        holder.setPatternViewLayoutManager(new GridLayoutManager(detailActivity, numberColumnsGridLayout));
        holder.hideClickableSpace();
        holder.setPatternViewAdapter(patternAdapter);
        caveArrangementAdapter.onPatternLoaded();
    }

    private void resetBottleIdInHighlight() {
        setBottleIdInHighlight(-1);
    }

    private void setBottleIdInHighlight(int bottleIdInHighlight) {
        caveArrangementAdapter.BottleIdInHighlight = bottleIdInHighlight;
        if (detailActivity != null) {
            detailActivity.BottleIdInHighlight = bottleIdInHighlight;
        }
        caveArrangementAdapter.notifyDataSetChanged();
    }
}
