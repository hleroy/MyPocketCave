package com.myadridev.mypocketcave.adapters.viewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class CaveTypeViewHolder extends RecyclerView.ViewHolder {
    private final ImageView typeView;
    private final TextView labelView;

    private CaveTypeViewHolder(View itemView, ImageView typeView, TextView labelView) {
        super(itemView);
        this.typeView = typeView;
        this.labelView = labelView;
    }

    public static CaveTypeViewHolder newInstance(View parent) {
        ImageView typeView = (ImageView) parent.findViewById(R.id.cave_type_image);
        TextView labelView = (TextView) parent.findViewById(R.id.cave_type_label);
        return new CaveTypeViewHolder(parent, typeView, labelView);
    }

    public void setTypeViewImageDrawable(Drawable typeViewDrawable) {
        typeView.setImageDrawable(typeViewDrawable);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }

    public void hideTypeView() {
        typeView.setVisibility(View.GONE);
    }
}
