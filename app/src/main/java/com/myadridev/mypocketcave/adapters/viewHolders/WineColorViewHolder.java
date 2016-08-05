package com.myadridev.mypocketcave.adapters.viewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class WineColorViewHolder extends RecyclerView.ViewHolder {
    private final ImageView colorView;
    private final TextView labelView;

    private WineColorViewHolder(View itemView, ImageView colorView, TextView labelView) {
        super(itemView);
        this.colorView = colorView;
        this.labelView = labelView;
    }

    public static WineColorViewHolder newInstance(View parent) {
        ImageView colorView = (ImageView) parent.findViewById(R.id.wine_color_image);
        TextView labelView = (TextView) parent.findViewById(R.id.wine_color_label);
        return new WineColorViewHolder(parent, colorView, labelView);
    }

    public void setColorViewImageDrawable(Drawable colorViewDrawable) {
        colorView.setImageDrawable(colorViewDrawable);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }

    public void hideColorView() {
        colorView.setVisibility(View.GONE);
    }
}
