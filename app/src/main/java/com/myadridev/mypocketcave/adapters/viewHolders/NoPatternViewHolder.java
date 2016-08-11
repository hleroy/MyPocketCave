package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class NoPatternViewHolder extends RecyclerView.ViewHolder {

    private NoPatternViewHolder(View itemView) {
        super(itemView);
    }

    public static NoPatternViewHolder newInstance(View parent) {
        return new NoPatternViewHolder(parent);
    }
}
