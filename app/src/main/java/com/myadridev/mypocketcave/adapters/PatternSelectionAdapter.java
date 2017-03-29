package com.myadridev.mypocketcave.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.PatternSelectionActivity;
import com.myadridev.mypocketcave.adapters.viewHolders.CreatePatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.NoPatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.SelectionPatternViewHolder;
import com.myadridev.mypocketcave.layoutManagers.GridAutofitLayoutManager;
import com.myadridev.mypocketcave.listeners.OnSelectionPatternClickListener;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.PatternModel;
import com.myadridev.mypocketcave.tasks.pattern.CreatePatternAdapterTask;

import java.util.List;

public class PatternSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final PatternSelectionActivity activity;
    private final List<PatternModel> patternList;
    private GridAutofitLayoutManager layoutManager;
    private final LayoutInflater layoutInflater;

    private final OnSelectionPatternClickListener onItemClickListener;
    private OnSelectionPatternClickListener onSelectionPatternClickListener;
    private int itemWidth;

    public PatternSelectionAdapter(PatternSelectionActivity activity, List<PatternModel> patternList, GridAutofitLayoutManager layoutManager) {
        this.activity = activity;
        this.patternList = patternList;
        this.layoutManager = layoutManager;
        layoutInflater = LayoutInflater.from(this.activity);
        onItemClickListener = (int patternId) -> {
            if (patternId == -1) {
                NavigationManager.navigateToCreatePattern(this.activity);
            } else {
                if (onSelectionPatternClickListener != null) {
                    onSelectionPatternClickListener.onItemClick(patternId);
                }
            }
        };
    }

    public void setOnSelectionPatternClickListener(OnSelectionPatternClickListener onSelectionPatternClickListener) {
        this.onSelectionPatternClickListener = onSelectionPatternClickListener;
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
        return patternList.size() + 1;
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
        itemWidth = layoutManager.ColumnWidth;
        view.setMinimumWidth(itemWidth);
        view.setMinimumHeight(itemWidth);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position < patternList.size()) {
            SelectionPatternViewHolder holder = (SelectionPatternViewHolder) viewHolder;
            PatternModel pattern = patternList.get(position);
            if (pattern != null) {
                CreatePatternAdapterTask createPatternAdapterTask = new CreatePatternAdapterTask(activity, holder, pattern, itemWidth, onItemClickListener);
                createPatternAdapterTask.execute();
            }
        } else if (position == patternList.size()) {
            CreatePatternViewHolder holder = (CreatePatternViewHolder) viewHolder;
            holder.setOnItemClickListener(onItemClickListener, -1);
        }
    }
}
