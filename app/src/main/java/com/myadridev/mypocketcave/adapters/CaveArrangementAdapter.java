package com.myadridev.mypocketcave.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.adapters.viewHolders.AddPatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.NoPatternViewHolder;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class CaveArrangementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final AbstractCaveEditActivity activity;
    private final Map<CoordinatesModel, Integer> patternMap;
    private final LayoutInflater layoutInflater;

    private final OnPatternClickListener listener;
    private final int nbRows;
    private final int nbCols;
    private int itemWidth;
    private int numberOfColumnsForDisplay;

    public CaveArrangementAdapter(AbstractCaveEditActivity _activity, Map<CoordinatesModel, Integer> _patternMap, int nbRows, int nbCols) {
        activity = _activity;
        patternMap = _patternMap;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        numberOfColumnsForDisplay = nbCols;
        layoutInflater = LayoutInflater.from(activity);
        listener = new OnPatternClickListener() {
            @Override
            public void onPatternClick(CoordinatesModel coordinates) {
                activity.ClickedPatternCoordinates = coordinates;
                NavigationManager.navigateToPatternSelection(activity);
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.Instance.getRowFromPosition(position, getItemCount()), CoordinatesManager.Instance.getColFromPosition(position));

        if (patternMap.containsKey(coordinates)) {
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
        return (patternMap.size() == 0 && coordinates.Col == 1 && coordinates.Row == 0)
                // over existing
                || patternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))
                // right to existing and no line under or existing under
                || (patternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col - 1))
                && (coordinates.Row == 0 || patternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))))
                // left to existing and no line under or existing under
                || (patternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col + 1))
                && (coordinates.Row == 0 || patternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))));
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
            itemWidth = ScreenHelper.getScreenWidth(activity) / numberOfColumnsForDisplay;
        }
        view.setMinimumWidth(itemWidth);
        view.setMinimumHeight(itemWidth);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.Instance.getRowFromPosition(position, getItemCount()), CoordinatesManager.Instance.getColFromPosition(position));

        if (patternMap.containsKey(coordinates)) {
            CaveArrangementViewHolder holder = (CaveArrangementViewHolder) viewHolder;
            PatternModel pattern = PatternManager.Instance.getPattern(patternMap.get(coordinates));
            if (pattern != null) {
                int numberRowsGridLayout = pattern.getNumberRowsGridLayout();
                int numberColumnsGridLayout = pattern.getNumberColumnsGridLayout();
                if (numberRowsGridLayout > 0) {
                    holder.setPatternViewLayoutManager(new GridLayoutManager(activity, numberColumnsGridLayout));
                    PatternAdapter patternAdapter = new PatternAdapter(activity, pattern.PlaceMap, new CoordinatesModel(numberRowsGridLayout, numberColumnsGridLayout),
                            false, itemWidth, itemWidth);
                    holder.setPatternViewAdapter(patternAdapter);
                    holder.setOnItemClickListener(listener, coordinates);
                    holder.setClickableSpaceDimensions(itemWidth, itemWidth);
                }
            }
        } else if (isAddPattern(coordinates)) {
            AddPatternViewHolder holder = (AddPatternViewHolder) viewHolder;
            Picasso.with(activity).load(R.drawable.add).resize(itemWidth / 2, itemWidth / 2).into(holder.getImageView());
            holder.setOnItemClickListener(listener, coordinates);
        }
    }

    private CoordinatesModel getCoordinateByPosition(int rowPosition, int colPosition) {
        return new CoordinatesModel(rowPosition / nbCols, colPosition % nbCols);
    }
}