package com.myadridev.mypocketcave.adapters.viewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;

public class BottleViewHolder extends RecyclerView.ViewHolder {
    private final ImageView colorView;
    private final TextView labelView;
    private final TextView millesimeView;
    private final TextView stockLabelView;
    private final ImageView greyOverView;

    private BottleViewHolder(View itemView, ImageView colorView, TextView labelView, TextView millesimeView, TextView stockLabelView, ImageView greyOverView) {
        super(itemView);
        this.colorView = colorView;
        this.labelView = labelView;
        this.millesimeView = millesimeView;
        this.stockLabelView = stockLabelView;
        this.greyOverView = greyOverView;
    }

    public static BottleViewHolder newInstance(View parent) {
        ImageView colorView = (ImageView) parent.findViewById(R.id.bottle_color);
        TextView labelView = (TextView) parent.findViewById(R.id.bottle_label);
        TextView millesimeView = (TextView) parent.findViewById(R.id.bottle_millesime);
        TextView stockLabelView = (TextView) parent.findViewById(R.id.bottle_stock_label);
        ImageView greyOverView = (ImageView) parent.findViewById(R.id.bottle_grey_over);
        return new BottleViewHolder(parent, colorView, labelView, millesimeView, stockLabelView, greyOverView);
    }

    public void setColorViewImageDrawable(Drawable colorViewDrawable) {
        colorView.setImageDrawable(colorViewDrawable);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }

    public void setMillesimeViewText(CharSequence millesimeViewText) {
        millesimeView.setText(millesimeViewText);
    }

    public void setStockLabelViewText(CharSequence stockLabelViewText) {
        stockLabelView.setText(stockLabelViewText);
    }

    public void setOnItemClickListener(final OnBottleClickListener listener, final int bottleId) {
        itemView.setOnClickListener((View v) -> listener.onItemClick(bottleId));
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
