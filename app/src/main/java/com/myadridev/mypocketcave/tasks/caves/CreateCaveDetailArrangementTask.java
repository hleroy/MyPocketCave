package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.listeners.OnValueChangedListener;
import com.myadridev.mypocketcave.managers.CaveArrangementModelManager;
import com.myadridev.mypocketcave.models.v2.CaveArrangementModelV2;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelWithBottlesV2;
import com.myadridev.mypocketcave.tasks.bottles.DrinkBottleTask;
import com.myadridev.mypocketcave.tasks.bottles.PlaceBottleTask;
import com.myadridev.mypocketcave.tasks.bottles.UpdateNumberPlacedTask;

import java.util.List;
import java.util.Map;

public class CreateCaveDetailArrangementTask extends AsyncTask<Void, Void, PatternAdapter> {

    private final CaveArrangementViewHolder holder;
    private final CaveArrangementAdapter caveArrangementAdapter;
    private final CaveDetailActivity detailActivity;
    private final PatternModelWithBottlesV2 patternWithBottles;
    private final int numberRowsGridLayout;
    private final int numberColumnsGridLayout;
    private final int itemWidth;
    private final CoordinatesModelV2 coordinates;
    private final int bottleIdInHighlight;
    private final CaveArrangementModelV2 caveArrangement;
    private final OnValueChangedListener onValueChangedListener;

    public CreateCaveDetailArrangementTask(CaveArrangementAdapter caveArrangementAdapter, CaveArrangementViewHolder holder,
                                           CaveDetailActivity detailActivity, PatternModelWithBottlesV2 patternWithBottles,
                                           int numberRowsGridLayout, int numberColumnsGridLayout, int itemWidth,
                                           CoordinatesModelV2 coordinates, int bottleIdInHighlight, CaveArrangementModelV2 caveArrangement,
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
                new CoordinatesModelV2(numberRowsGridLayout, numberColumnsGridLayout),
                true, itemWidth, coordinates, bottleIdInHighlight);

        patternAdapter.setOnBottlePlacedClickListener((int bottleId, int quantity, CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 coordinates) -> {
            // here quantity is 1 in all cases : we ignore it
            Map<CoordinatesModelV2, List<CoordinatesModelV2>> coordinatesUpdated = CaveArrangementModelManager.placeBottle(caveArrangement, patternCoordinates, coordinates, bottleId);
            PlaceBottleTask placeBottleTask = new PlaceBottleTask(detailActivity);
            placeBottleTask.execute(bottleId);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged(coordinatesUpdated);
            }
        });
        patternAdapter.setOnBottleDrunkClickListener((int bottleId, int quantity, CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 coordinates) -> {
            // here quantity is 1 in all cases : we ignore it
            Map<CoordinatesModelV2, List<CoordinatesModelV2>> coordinatesUpdated = CaveArrangementModelManager.unplaceBottle(caveArrangement, patternCoordinates, coordinates, bottleId);
            DrinkBottleTask drinkBottleTask = new DrinkBottleTask(detailActivity);
            drinkBottleTask.execute(bottleId);
            if (onValueChangedListener != null) {
                onValueChangedListener.onValueChanged(coordinatesUpdated);
            }
        });
        patternAdapter.setOnBottleUnplacedClickListener((int bottleId, int quantity, CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 coordinates) -> {
            // here quantity is 1 in all cases : we ignore it
            Map<CoordinatesModelV2, List<CoordinatesModelV2>> coordinatesUpdated = CaveArrangementModelManager.unplaceBottle(caveArrangement, patternCoordinates, coordinates, bottleId);
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
