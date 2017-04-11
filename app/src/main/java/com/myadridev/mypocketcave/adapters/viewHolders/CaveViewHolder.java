package com.myadridev.mypocketcave.adapters.viewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnCaveClickListener;

public class CaveViewHolder extends RecyclerView.ViewHolder {
    private final ImageView typeView;
    private final TextView labelView;
    private final TextView usedCapacityLabelView;

    private CaveViewHolder(View itemView, ImageView typeView, TextView labelView, TextView usedCapacityLabelView) {
        super(itemView);
        this.typeView = typeView;
        this.labelView = labelView;
        this.usedCapacityLabelView = usedCapacityLabelView;
    }

    public static CaveViewHolder newInstance(View parent) {
        ImageView typeView = (ImageView) parent.findViewById(R.id.cave_type);
        TextView labelView = (TextView) parent.findViewById(R.id.cave_label);
        TextView usedCapacityLabelView = (TextView) parent.findViewById(R.id.cave_used_capacity);
        return new CaveViewHolder(parent, typeView, labelView, usedCapacityLabelView);
    }

    public void setTypeViewImageDrawable(Drawable colorViewDrawable) {
        typeView.setImageDrawable(colorViewDrawable);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }

    public void setUsedLabelViewText(CharSequence usedCapacityLabelViewText) {
        usedCapacityLabelView.setText(usedCapacityLabelViewText);
    }

    public void setOnItemClickListener(final OnCaveClickListener listener, final int caveId) {
        itemView.setOnClickListener((View v) -> listener.onItemClick(caveId));
    }
}
