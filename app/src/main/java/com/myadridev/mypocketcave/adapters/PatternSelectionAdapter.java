package com.myadridev.mypocketcave.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.PatternSelectionActivity;
import com.myadridev.mypocketcave.adapters.viewHolders.CreatePatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.NoPatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.SelectionPatternViewHolder;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.listeners.OnSelectionPatternClickListener;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.ArrayList;
import java.util.List;

public class PatternSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final PatternSelectionActivity activity;
    private final List<PatternModel> patternList;
    private final LayoutInflater layoutInflater;

    private final OnSelectionPatternClickListener listener;
    private List<OnSelectionPatternClickListener> listeners;
    private int itemWidth;
    private int numberOfColumnsForDisplay = PatternManager.numberOfColumnsForDisplay;

    public PatternSelectionAdapter(PatternSelectionActivity _activity, List<PatternModel> _patternList) {
        activity = _activity;
        patternList = _patternList;
        layoutInflater = LayoutInflater.from(activity);
        listener = new OnSelectionPatternClickListener() {
            @Override
            public void onItemClick(int patternId) {
                if (patternId == -1) {
                    NavigationManager.navigateToCreatePattern(activity);
                } else {
                    if (listeners != null) {
                        for (OnSelectionPatternClickListener listener : listeners) {
                            listener.onItemClick(patternId);
                        }
                    }
                }
            }
        };
    }

    public void addOnSelectionPatternClickListener(OnSelectionPatternClickListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < patternList.size()) {
            // Existing pattern
            return 0;
        } else if (position == patternList.size()) {
            // Add new pattern
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return PatternManager.maxNumberOfPatterns + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                // Existing pattern
                view = layoutInflater.inflate(R.layout.item_pattern, parent, false);
                setItemDimensions(view);
                return SelectionPatternViewHolder.newInstance(view);
            case 1:
                // Create new pattern
                view = layoutInflater.inflate(R.layout.item_add_pattern, parent, false);
                setItemDimensions(view);
                return CreatePatternViewHolder.newInstance(view);
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
        if (position < patternList.size()) {
            SelectionPatternViewHolder holder = (SelectionPatternViewHolder) viewHolder;
            PatternModel pattern = patternList.get(position);
            if (pattern != null) {
                int numberRowsGridLayout = pattern.getNumberRowsGridLayout();
                if (numberRowsGridLayout > 0) {
                    holder.setPatternViewLayoutManager(new GridLayoutManager(activity, numberRowsGridLayout));
                    PatternAdapter patternAdapter = new PatternAdapter(activity, pattern.PlaceMap, new CoordinatesModel(numberRowsGridLayout, pattern.getNumberColumnsGridLayout()),
                            false, itemWidth, itemWidth);
                    holder.setPatternViewAdapter(patternAdapter);
                    holder.setOnItemClickListener(listener, pattern.Id);
                    holder.setClickableSpaceDimensions(itemWidth, itemWidth);
                }
            }
        } else if (position == patternList.size()) {
            CreatePatternViewHolder holder = (CreatePatternViewHolder) viewHolder;
            holder.setOnItemClickListener(listener, -1);
        }
    }
}


