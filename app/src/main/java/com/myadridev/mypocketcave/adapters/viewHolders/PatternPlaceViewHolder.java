package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnPlaceClickListener;
import com.myadridev.mypocketcave.models.v1.CoordinatesModel;

public class PatternPlaceViewHolder extends RecyclerView.ViewHolder {
    private final ImageView placeTypeView;
    private final ImageView greyOverView;
    private final ImageView greyUnderView;
    private final RelativeLayout container;

    private PatternPlaceViewHolder(View itemView, ImageView placeTypeView, ImageView greyOverView, ImageView greyUnderView, RelativeLayout container) {
        super(itemView);
        this.placeTypeView = placeTypeView;
        this.greyOverView = greyOverView;
        this.greyUnderView = greyUnderView;
        this.container = container;
    }

    public static PatternPlaceViewHolder newInstance(View parent) {
        ImageView placeTypeView = (ImageView) parent.findViewById(R.id.pattern_place_image);
        ImageView greyOverView = (ImageView) parent.findViewById(R.id.pattern_place_grey_over);
        ImageView greyUnderView = (ImageView) parent.findViewById(R.id.pattern_place_grey_under);
        RelativeLayout container = (RelativeLayout) parent.findViewById(R.id.pattern_place_container);
        return new PatternPlaceViewHolder(parent, placeTypeView, greyOverView, greyUnderView, container);
    }

    public ImageView getPlaceTypeView() {
        return placeTypeView;
    }

    public RelativeLayout getContainer() {
        return container;
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
        greyOverView.setVisibility(isHighlight ? View.INVISIBLE : View.VISIBLE);
        greyUnderView.setVisibility(isHighlight ? View.VISIBLE : View.INVISIBLE);
    }
}
