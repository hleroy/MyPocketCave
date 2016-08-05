package com.myadridev.mypocketcave.adapters.viewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnPlaceClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class PatternPlaceViewHolder extends RecyclerView.ViewHolder {
    private final ImageView placeTypeView;

    private PatternPlaceViewHolder(View itemView, ImageView placeTypeView) {
        super(itemView);
        this.placeTypeView = placeTypeView;
    }

    public static PatternPlaceViewHolder newInstance(View parent) {
        ImageView placeTypeView = (ImageView) parent.findViewById(R.id.pattern_place_image);
        return new PatternPlaceViewHolder(parent, placeTypeView);
    }

    public void setPlaceTypeViewImageDrawable(Drawable placeTypeViewDrawable) {
        placeTypeView.setImageDrawable(placeTypeViewDrawable);
    }

    public void setOnItemClickListener(final OnPlaceClickListener listener, final CoordinatesModel coordinates) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPlaceClick(coordinates);
            }
        });
    }
}
