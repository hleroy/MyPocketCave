package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class CaveArrangementViewHolder extends RecyclerView.ViewHolder {
    private final RecyclerView patternView;
    private final Button clickableSpace;
    private PatternAdapter patternAdapter;

    private CaveArrangementViewHolder(View itemView, RecyclerView patternView, Button clickableSpace) {
        super(itemView);
        this.patternView = patternView;
        this.clickableSpace = clickableSpace;
    }

    public static CaveArrangementViewHolder newInstance(View parent) {
        RecyclerView patternView = (RecyclerView) parent.findViewById(R.id.pattern_recycler);
        ViewCompat.setNestedScrollingEnabled(patternView, false);

        Button clickableSpace = (Button) parent.findViewById(R.id.pattern_clickable_space);
        return new CaveArrangementViewHolder(parent, patternView, clickableSpace);
    }

    public void setPatternViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        patternView.setLayoutManager(layoutManager);
    }

    public RecyclerView getPatternView() {
        return patternView;
    }

    public PatternAdapter getPatternAdapter() {
        return patternAdapter;
    }

    public void setPatternViewAdapter(PatternAdapter patternAdapter) {
        this.patternAdapter = patternAdapter;
        patternView.setAdapter(patternAdapter);
    }

    public void setClickableSpaceDimensions(int width, int height) {
        clickableSpace.setMinimumWidth(width);
        clickableSpace.setMinimumHeight(height);
    }

    public void setOnItemClickListener(final OnPatternClickListener listener, final CoordinatesModel coordinates) {
        clickableSpace.setOnClickListener((View v) -> listener.onPatternClick(coordinates));
    }

    public void hideClickableSpace() {
        clickableSpace.setVisibility(View.GONE);
    }
}
