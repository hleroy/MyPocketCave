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
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.Map;

public class CaveArrangementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final AbstractCaveEditActivity activity;
    private final Map<CoordinatesModel, PatternModel> patternMap;
    private final LayoutInflater layoutInflater;

    private final OnPatternClickListener listener;
    private final int maxRow;
    private final int maxCol;
    private int itemWidth;
    private int numberOfColumnsForDisplay;

    public CaveArrangementAdapter(AbstractCaveEditActivity _activity, Map<CoordinatesModel, PatternModel> _patternMap, CoordinatesModel maxRawCol) {
        activity = _activity;
        patternMap = _patternMap;
        maxRow = maxRawCol.Row;
        maxCol = maxRawCol.Col;
        numberOfColumnsForDisplay = maxRow + 1;
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
        CoordinatesModel coordinates = getCoordinateByPosition(position);

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
        return (patternMap.size() == 0 && coordinates.Col + coordinates.Row == 0)
                || patternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))
                || patternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col - 1))
                || patternMap.containsKey(new CoordinatesModel(coordinates.Row + 1, coordinates.Col))
                || patternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col + 1));
    }

    @Override
    public int getItemCount() {
        return (maxRow + 1) * (maxCol + 1);
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
        CoordinatesModel coordinates = getCoordinateByPosition(position);

        if (patternMap.containsKey(coordinates)) {
            CaveArrangementViewHolder holder = (CaveArrangementViewHolder) viewHolder;
            PatternModel pattern = patternMap.get(coordinates);
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
            holder.setOnItemClickListener(listener, coordinates);
        }
    }

    private CoordinatesModel getCoordinateByPosition(int position) {
        return new CoordinatesModel(position / (maxCol + 1), position % (maxCol + 1));
    }
}


