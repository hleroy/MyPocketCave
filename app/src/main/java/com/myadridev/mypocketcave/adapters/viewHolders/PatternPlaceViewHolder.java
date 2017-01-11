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
    private final ImageView greyOverView;
    private final ImageView greyUnderView;

    private PatternPlaceViewHolder(View itemView, ImageView placeTypeView, ImageView greyOverView, ImageView greyUnderView) {
        super(itemView);
        this.placeTypeView = placeTypeView;
        this.greyOverView = greyOverView;
        this.greyUnderView = greyUnderView;
    }

    public static PatternPlaceViewHolder newInstance(View parent) {
        ImageView placeTypeView = (ImageView) parent.findViewById(R.id.pattern_place_image);
        ImageView greyOverView = (ImageView) parent.findViewById(R.id.pattern_place_grey_over);
        ImageView greyUnderView = (ImageView) parent.findViewById(R.id.pattern_place_grey_under);
        return new PatternPlaceViewHolder(parent, placeTypeView, greyOverView, greyUnderView);
    }

    public void setPlaceTypeViewImageDrawable(Drawable placeTypeViewDrawable) {
        placeTypeView.setImageDrawable(placeTypeViewDrawable);
    }

    public ImageView getPlaceTypeView() {
        return placeTypeView;
    }

    public void setOnItemClickListener(final OnPlaceClickListener listener, final CoordinatesModel patternCoordinates, final CoordinatesModel coordinates) {
        placeTypeView.setOnClickListener((View v) -> listener.onPlaceClick(patternCoordinates, coordinates));
    }

    public void setResetHighlightClickListener(final View.OnClickListener listener) {
        greyOverView.setOnClickListener(listener);
    }

    public void setClickable(boolean isClickable) {
        placeTypeView.setClickable(isClickable);
    }

    public void setHighlight(boolean isHighlight) {
        if (isHighlight) {
            greyOverView.setVisibility(View.INVISIBLE);
            greyUnderView.setVisibility(View.VISIBLE);
        } else {
            greyOverView.setVisibility(View.VISIBLE);
            greyUnderView.setVisibility(View.INVISIBLE);
        }
    }

    public void resetHighlight() {
        greyOverView.setVisibility(View.INVISIBLE);
        greyUnderView.setVisibility(View.INVISIBLE);
    }
}
