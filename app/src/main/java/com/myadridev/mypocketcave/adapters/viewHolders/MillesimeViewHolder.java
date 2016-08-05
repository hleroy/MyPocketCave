package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class MillesimeViewHolder extends RecyclerView.ViewHolder {
    private final TextView labelView;

    private MillesimeViewHolder(View itemView, TextView labelView) {
        super(itemView);
        this.labelView = labelView;
    }

    public static MillesimeViewHolder newInstance(View parent) {
        TextView labelView = (TextView) parent.findViewById(R.id.millesime_label);
        return new MillesimeViewHolder(parent, labelView);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }
}
