package com.myadridev.mypocketcave.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.adapters.viewHolders.AddPatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.NoPatternViewHolder;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.listeners.OnValueChangedListener;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CaveArrangementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final AbstractCaveEditActivity editActivity;
    private final CaveDetailActivity detailActivity;
    private final CaveArrangementModel caveArangement;
    private final LayoutInflater layoutInflater;
    private final boolean isIndividualPlacesClickable;

    private final OnPatternClickListener listener;
    private final int nbRows;
    private final int nbCols;
    private final int totalWidth;
    private final List<OnValueChangedListener> onValueChangedListeners;
    private int itemWidth;

    public CaveArrangementAdapter(AbstractCaveEditActivity activity, CaveArrangementModel _caveArrangement, int nbRows, int nbCols, int totalWidth) {
        editActivity = activity;
        detailActivity = null;
        caveArangement = _caveArrangement;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.totalWidth = totalWidth;
        layoutInflater = LayoutInflater.from(editActivity);
        isIndividualPlacesClickable = false;
        listener = (CoordinatesModel coordinates) -> {
            editActivity.OldClickedPatternId = caveArangement.PatternMap.containsKey(coordinates) ? caveArangement.PatternMap.get(coordinates).Id : -1;
            editActivity.ClickedPatternCoordinates = coordinates;
            NavigationManager.navigateToPatternSelection(editActivity);
        };
        onValueChangedListeners = new ArrayList<>();
    }

    public CaveArrangementAdapter(CaveDetailActivity activity, CaveArrangementModel _caveArangement, int nbRows, int nbCols, int totalWidth) {
        detailActivity = activity;
        editActivity = null;
        caveArangement = _caveArangement;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.totalWidth = totalWidth;
        layoutInflater = LayoutInflater.from(detailActivity);
        isIndividualPlacesClickable = true;
        listener = null;
        onValueChangedListeners = new ArrayList<>();
    }

    public void addOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        onValueChangedListeners.add(onValueChangedListener);
    }

    @Override
    public int getItemViewType(int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.getRowFromPosition(position, getItemCount()), CoordinatesManager.getColFromPosition(position));

        if (caveArangement.PatternMap.containsKey(coordinates)) {
            // Existing pattern
            return 0;
        } else if (!isIndividualPlacesClickable && isAddPattern(coordinates)) {
            // Add new pattern
            return 1;
        } else {
            return -1;
        }
    }

    private boolean isAddPattern(CoordinatesModel coordinates) {
        return (caveArangement.PatternMap.size() == 0 && coordinates.Col == 1 && coordinates.Row == 0)
                // over existing
                || caveArangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))
                // right to existing and no line under or existing under
                || (caveArangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col - 1))
                && (coordinates.Row == 0 || caveArangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))))
                // left to existing and no line under or existing under
                || (caveArangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col + 1))
                && (coordinates.Row == 0 || caveArangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))));
    }

    @Override
    public int getItemCount() {
        return nbRows * nbCols;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                // Existing pattern
                view = layoutInflater.inflate(R.layout.item_pattern, parent, false);
                setItemDimensions(view);
                return CaveArrangementViewHolder.newInstance(view);
            case 1:
                // Add new pattern
                view = layoutInflater.inflate(R.layout.item_add_pattern, parent, false);
                setItemDimensions(view);
                return AddPatternViewHolder.newInstance(view);
            default:
                view = layoutInflater.inflate(R.layout.item_no_pattern, parent, false);
                setItemDimensions(view);
                return NoPatternViewHolder.newInstance(view);
        }
    }

    private void setItemDimensions(View view) {
        if (itemWidth == 0) {
            itemWidth = totalWidth / nbCols;
        }
        view.setMinimumWidth(itemWidth);
        view.setMinimumHeight(itemWidth);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.getRowFromPosition(position, getItemCount()), CoordinatesManager.getColFromPosition(position));

        if (caveArangement.PatternMap.containsKey(coordinates)) {
            PatternModelWithBottles patternWithBottles = caveArangement.PatternMap.get(coordinates);
            if (patternWithBottles == null) return;

            CaveArrangementViewHolder holder = (CaveArrangementViewHolder) viewHolder;
            int numberRowsGridLayout = patternWithBottles.getNumberRowsGridLayout();
            if (numberRowsGridLayout <= 0) return;

            int numberColumnsGridLayout = patternWithBottles.getNumberColumnsGridLayout();
            PatternAdapter patternAdapter;
            if (isIndividualPlacesClickable) {
                holder.setPatternViewLayoutManager(new GridLayoutManager(detailActivity, numberColumnsGridLayout));
                patternAdapter = new PatternAdapter(detailActivity, patternWithBottles.PlaceMapWithBottles,
                        new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout),
                        true, itemWidth, itemWidth, coordinates);
                patternAdapter.addOnBottlePlacedClickListener((CoordinatesModel patternCoordinates, CoordinatesModel coordinates1, int bottleId) -> {
                    caveArangement.placeBottle(patternCoordinates, coordinates1, bottleId);
                    BottleManager.placeBottle(bottleId);
                    for (OnValueChangedListener onValueChangedListener : onValueChangedListeners) {
                        onValueChangedListener.onValueChanged();
                    }
                    notifyDataSetChanged();
                });
                patternAdapter.addOnBottleDrunkClickListener((CoordinatesModel patternCoordinates, CoordinatesModel coordinates1, int bottleId) -> {
                    caveArangement.unplaceBottle(patternCoordinates, coordinates1, bottleId);
                    BottleManager.drinkBottle(bottleId);
                    for (OnValueChangedListener onValueChangedListener : onValueChangedListeners) {
                        onValueChangedListener.onValueChanged();
                    }
                    notifyDataSetChanged();
                });
                patternAdapter.addOnBottleUnplacedClickListener((CoordinatesModel patternCoordinates, CoordinatesModel coordinates1, int bottleId) -> {
                    caveArangement.unplaceBottle(patternCoordinates, coordinates1, bottleId);
                    BottleManager.updateNumberPlaced(bottleId, -1);
                    for (OnValueChangedListener onValueChangedListener : onValueChangedListeners) {
                        onValueChangedListener.onValueChanged();
                    }
                    notifyDataSetChanged();
                });
                holder.hideClickableSpace();
            } else {
                holder.setPatternViewLayoutManager(new GridLayoutManager(editActivity, numberColumnsGridLayout));
                patternAdapter = new PatternAdapter(editActivity, patternWithBottles.PlaceMapWithBottles,
                        new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout),
                        false, itemWidth, itemWidth, null);
                holder.setOnItemClickListener(listener, coordinates);
                holder.setClickableSpaceDimensions(itemWidth, itemWidth);
            }
            holder.setPatternViewAdapter(patternAdapter);
        } else if (!isIndividualPlacesClickable && isAddPattern(coordinates)) {
            AddPatternViewHolder holder = (AddPatternViewHolder) viewHolder;
            Picasso.with(editActivity).load(R.drawable.add).resize(itemWidth / 2, itemWidth / 2).into(holder.getImageView());
            holder.setOnItemClickListener(listener, coordinates);
        }
    }

    private CoordinatesModel getCoordinateByPosition(int rowPosition, int colPosition) {
        return new CoordinatesModel(rowPosition / nbCols, colPosition % nbCols);
    }
}