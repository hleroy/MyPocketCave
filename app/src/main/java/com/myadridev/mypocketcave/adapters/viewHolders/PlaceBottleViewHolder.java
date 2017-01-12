package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PlaceBottleViewHolder extends RecyclerView.ViewHolder {

    private PlaceBottleViewHolder(View itemView) {
        super(itemView);
    }

    public static PlaceBottleViewHolder newInstance(View parent) {
        return new PlaceBottleViewHolder(parent);
    }

    public void setOnItemClickListener(final View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }
}
