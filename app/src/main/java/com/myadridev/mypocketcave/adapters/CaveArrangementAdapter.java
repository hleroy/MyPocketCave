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
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class CaveArrangementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final AbstractCaveEditActivity editActivity;
    private final CaveDetailActivity detailActivity;
    private final Map<CoordinatesModel, PatternModelWithBottles> patternWithBottlesMap;
    private final LayoutInflater layoutInflater;
    private final boolean isIndividualPlacesClickable;

    private final OnPatternClickListener listener;
    private final int nbRows;
    private final int nbCols;
    private final int totalWidth;
    private int itemWidth;

    public CaveArrangementAdapter(AbstractCaveEditActivity activity, Map<CoordinatesModel, PatternModelWithBottles> _patternWithBottlesMap, int nbRows, int nbCols, int totalWidth) {
        editActivity = activity;
        detailActivity = null;
        patternWithBottlesMap = _patternWithBottlesMap;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.totalWidth = totalWidth;
        layoutInflater = LayoutInflater.from(editActivity);
        isIndividualPlacesClickable = false;
        listener = new OnPatternClickListener() {
            @Override
            public void onPatternClick(CoordinatesModel coordinates) {
                editActivity.OldClickedPatternId = patternWithBottlesMap.containsKey(coordinates) ? patternWithBottlesMap.get(coordinates).Id : -1;
                editActivity.ClickedPatternCoordinates = coordinates;
                NavigationManager.navigateToPatternSelection(editActivity);
            }
        };
    }

    public CaveArrangementAdapter(CaveDetailActivity activity, Map<CoordinatesModel, PatternModelWithBottles> _patternWithBottlesMap, int nbRows, int nbCols, int totalWidth) {
        detailActivity = activity;
        editActivity = null;
        patternWithBottlesMap = _patternWithBottlesMap;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.totalWidth = totalWidth;
        layoutInflater = LayoutInflater.from(detailActivity);
        isIndividualPlacesClickable = true;
        listener = new OnPatternClickListener() {
            @Override
            public void onPatternClick(CoordinatesModel coordinates) {
                // TODO
//                editActivity.OldClickedPatternId = patternWithBottlesMap.containsKey(coordinates) ? patternWithBottlesMap.get(coordinates).Id : -1;
//                editActivity.ClickedPatternCoordinates = coordinates;
//                NavigationManager.navigateToPatternSelection(editActivity);
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.Instance.getRowFromPosition(position, getItemCount()), CoordinatesManager.Instance.getColFromPosition(position));

        if (patternWithBottlesMap.containsKey(coordinates)) {
            // Existing pattern
            return 0;
        } else if (isAddPattern(coordinates)) {
            // Add new pattern
            return 1;
        } else {
            return -1;
        }
    }

    private boolean isAddPattern(CoordinatesModel coordinates) {
        return (patternWithBottlesMap.size() == 0 && coordinates.Col == 1 && coordinates.Row == 0)
                // over existing
                || patternWithBottlesMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))
                // right to existing and no line under or existing under
                || (patternWithBottlesMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col - 1))
                && (coordinates.Row == 0 || patternWithBottlesMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))))
                // left to existing and no line under or existing under
                || (patternWithBottlesMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col + 1))
                && (coordinates.Row == 0 || patternWithBottlesMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))));
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
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.Instance.getRowFromPosition(position, getItemCount()), CoordinatesManager.Instance.getColFromPosition(position));

        if (patternWithBottlesMap.containsKey(coordinates)) {
            CaveArrangementViewHolder holder = (CaveArrangementViewHolder) viewHolder;
            PatternModelWithBottles patternWithBottles = patternWithBottlesMap.get(coordinates);
            if (patternWithBottles != null) {
                int numberRowsGridLayout = patternWithBottles.getNumberRowsGridLayout();
                int numberColumnsGridLayout = patternWithBottles.getNumberColumnsGridLayout();
                if (numberRowsGridLayout > 0) {
                    PatternAdapter patternAdapter;
                    if (isIndividualPlacesClickable) {
                        // TODO : add listener
                        holder.setPatternViewLayoutManager(new GridLayoutManager(detailActivity, numberColumnsGridLayout));
                        patternAdapter = new PatternAdapter(detailActivity, patternWithBottles.PlaceMapWithBottles,
                                new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout),
                                true, itemWidth, itemWidth);
                        holder.hideClickableSpace();
                    }else {
                        holder.setPatternViewLayoutManager(new GridLayoutManager(editActivity, numberColumnsGridLayout));
                        patternAdapter = new PatternAdapter(editActivity, patternWithBottles.PlaceMapWithBottles,
                                new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout),
                                false, itemWidth, itemWidth);
                        holder.setOnItemClickListener(listener, coordinates);
                        holder.setClickableSpaceDimensions(itemWidth, itemWidth);
                    }
                    holder.setPatternViewAdapter(patternAdapter);
                }
            }
        } else if (isAddPattern(coordinates)) {
            AddPatternViewHolder holder = (AddPatternViewHolder) viewHolder;
            Picasso.with(editActivity).load(R.drawable.add).resize(itemWidth / 2, itemWidth / 2).into(holder.getImageView());
            holder.setOnItemClickListener(listener, coordinates);
        }
    }

    private CoordinatesModel getCoordinateByPosition(int rowPosition, int colPosition) {
        return new CoordinatesModel(rowPosition / nbCols, colPosition % nbCols);
    }
}