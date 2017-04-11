package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.myadridev.mypocketcave.listeners.OnSelectionPatternClickListener;

public class CreatePatternViewHolder extends RecyclerView.ViewHolder {

    private CreatePatternViewHolder(View itemView) {
        super(itemView);
    }

    public static CreatePatternViewHolder newInstance(View parent) {
        return new CreatePatternViewHolder(parent);
    }

    public void setOnItemClickListener(final OnSelectionPatternClickListener listener, final int position) {
        itemView.setOnClickListener((View v) -> listener.onItemClick(position));
    }
}
