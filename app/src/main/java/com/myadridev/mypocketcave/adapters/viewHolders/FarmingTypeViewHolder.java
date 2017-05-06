package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class FarmingTypeViewHolder extends RecyclerView.ViewHolder {
    private final TextView labelView;

    private FarmingTypeViewHolder(View itemView, TextView labelView) {
        super(itemView);
        this.labelView = labelView;
    }

    public static FarmingTypeViewHolder newInstance(View parent) {
        TextView labelView = (TextView) parent.findViewById(R.id.farming_type_label);
        return new FarmingTypeViewHolder(parent, labelView);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }
}
