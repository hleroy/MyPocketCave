package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class BottleNumberViewHolder extends RecyclerView.ViewHolder {
    private final TextView labelView;

    private BottleNumberViewHolder(View itemView, TextView labelView) {
        super(itemView);
        this.labelView = labelView;
    }

    public static BottleNumberViewHolder newInstance(View parent) {
        TextView labelView = (TextView) parent.findViewById(R.id.bottle_number_label);
        return new BottleNumberViewHolder(parent, labelView);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }
}
