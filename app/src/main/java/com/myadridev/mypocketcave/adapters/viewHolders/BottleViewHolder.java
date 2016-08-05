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

    private BottleViewHolder(View itemView, ImageView colorView, TextView labelView, TextView millesimeView, TextView stockLabelView) {
        super(itemView);
        this.colorView = colorView;
        this.labelView = labelView;
        this.millesimeView = millesimeView;
        this.stockLabelView = stockLabelView;
    }

    public static BottleViewHolder newInstance(View parent) {
        ImageView colorView = (ImageView) parent.findViewById(R.id.bottle_color);
        TextView labelView = (TextView) parent.findViewById(R.id.bottle_label);
        TextView millesimeView = (TextView) parent.findViewById(R.id.bottle_millesime);
        TextView stockLabelView = (TextView) parent.findViewById(R.id.bottle_stock_label);
        return new BottleViewHolder(parent, colorView, labelView, millesimeView, stockLabelView);
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(bottleId);
            }
        });
    }
}
