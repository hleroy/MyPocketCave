package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;

public class PlaceBottleViewHolder extends RecyclerView.ViewHolder {
    private final ImageView greyOverView;

    private PlaceBottleViewHolder(View itemView, ImageView greyOverView) {
        super(itemView);
        this.greyOverView = greyOverView;
    }

    public static PlaceBottleViewHolder newInstance(View parent) {
        ImageView greyOverView = (ImageView) parent.findViewById(R.id.place_bottle_grey_over);
        return new PlaceBottleViewHolder(parent, greyOverView);
    }

    public void setOnItemClickListener(final View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    public void setResetHighlightClickListener(final View.OnClickListener listener) {
        greyOverView.setOnClickListener(listener);
    }

    public void setHighlight(boolean isHighlight) {
        if (isHighlight) {
            greyOverView.setVisibility(View.INVISIBLE);
        } else {
            greyOverView.setVisibility(View.VISIBLE);
        }
    }

    public void resetHighlight() {
        greyOverView.setVisibility(View.INVISIBLE);
    }
}
