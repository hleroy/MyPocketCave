package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.listeners.OnValueChangedListener;
import com.myadridev.mypocketcave.models.v1.CaveArrangementModel;
import com.myadridev.mypocketcave.models.v1.CoordinatesModel;
import com.myadridev.mypocketcave.models.v1.PatternModelWithBottles;
import com.myadridev.mypocketcave.tasks.bottles.DrinkBottleTask;
import com.myadridev.mypocketcave.tasks.bottles.PlaceBottleTask;
import com.myadridev.mypocketcave.tasks.bottles.UpdateNumberPlacedTask;

import java.util.List;
import java.util.Map;

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

        patternAdapter.setOnBottlePlacedClickListener((int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates) -> {
            // here quantity is 1 in all cases : we ignore it
            Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = caveArrangement.placeBottle(patternCoordinates, coordinates, bottleId);
            PlaceBottleTask placeBottleTask = new PlaceBottleTask(detailActivity);
            placeBottleTask.execute(bottleId);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged(coordinatesUpdated);
            }
        });
        patternAdapter.setOnBottleDrunkClickListener((int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates) -> {
            // here quantity is 1 in all cases : we ignore it
            Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = caveArrangement.unplaceBottle(patternCoordinates, coordinates, bottleId);
            DrinkBottleTask drinkBottleTask = new DrinkBottleTask(detailActivity);
            drinkBottleTask.execute(bottleId);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged(coordinatesUpdated);
            }
        });
        patternAdapter.setOnBottleUnplacedClickListener((int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates) -> {
            // here quantity is 1 in all cases : we ignore it
            Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = caveArrangement.unplaceBottle(patternCoordinates, coordinates, bottleId);
            UpdateNumberPlacedTask updateNumberPlacedTask = new UpdateNumberPlacedTask(detailActivity);
            updateNumberPlacedTask.execute(bottleId, -1);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged(coordinatesUpdated);
            }
        });

        patternAdapter.setOnSetHighlightlistener(this::setBottleIdInHighlight);
        patternAdapter.setOnResetHighlightlistener((View v) -> resetBottleIdInHighlight());
        return patternAdapter;
    }

    @Override
    protected void onPostExecute(PatternAdapter patternAdapter) {
        holder.getPatternView().getRecycledViewPool().setMaxRecycledViews(0, 0);
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
