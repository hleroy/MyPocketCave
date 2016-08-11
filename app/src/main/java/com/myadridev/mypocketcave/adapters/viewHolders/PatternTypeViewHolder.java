package com.myadridev.mypocketcave.adapters.viewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class PatternTypeViewHolder extends RecyclerView.ViewHolder {
    private final ImageView iconView;
    private final TextView labelView;

    private PatternTypeViewHolder(View itemView, ImageView iconView, TextView labelView) {
        super(itemView);
        this.iconView = iconView;
        this.labelView = labelView;
    }

    public static PatternTypeViewHolder newInstance(View parent) {
        ImageView iconView = (ImageView) parent.findViewById(R.id.pattern_type_icon);
        TextView labelView = (TextView) parent.findViewById(R.id.pattern_type_label);
        return new PatternTypeViewHolder(parent, iconView, labelView);
    }

    public void setIconViewImageDrawable(Drawable iconViewDrawable) {
        iconView.setImageDrawable(iconViewDrawable);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }
}
