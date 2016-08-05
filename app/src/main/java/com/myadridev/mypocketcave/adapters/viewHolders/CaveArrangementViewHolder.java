package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class CaveArrangementViewHolder extends RecyclerView.ViewHolder {
    private final RecyclerView patternView;

    private CaveArrangementViewHolder(View itemView, RecyclerView patternView) {
        super(itemView);
        this.patternView = patternView;
    }

    public static CaveArrangementViewHolder newInstance(View parent) {
        RecyclerView patternView = (RecyclerView) parent.findViewById(R.id.pattern_recycler);
        return new CaveArrangementViewHolder(parent, patternView);
    }

    public void setPatternViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        patternView.setLayoutManager(layoutManager);
    }

    public void setPatternViewAdapter(PatternAdapter patternAdapter) {
        patternView.setAdapter(patternAdapter);
    }

    public void setOnItemClickListener(final OnPatternClickListener listener, final CoordinatesModel coordinates) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPatternClick(coordinates);
            }
        });
    }
}
