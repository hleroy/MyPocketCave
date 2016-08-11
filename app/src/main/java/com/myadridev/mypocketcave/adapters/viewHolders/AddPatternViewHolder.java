package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class AddPatternViewHolder extends RecyclerView.ViewHolder {

    private AddPatternViewHolder(View itemView) {
        super(itemView);
    }

    public static AddPatternViewHolder newInstance(View parent) {
        return new AddPatternViewHolder(parent);
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
