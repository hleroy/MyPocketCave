package com.myadridev.mypocketcave.adapters.viewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class CaveSpinnerViewHolder extends RecyclerView.ViewHolder {
    private final ImageView caveTypeView;
    private final TextView labelView;

    private CaveSpinnerViewHolder(View itemView, ImageView caveTypeView, TextView labelView) {
        super(itemView);
        this.caveTypeView = caveTypeView;
        this.labelView = labelView;
    }

    public static CaveSpinnerViewHolder newInstance(View parent) {
        ImageView caveTypeView = (ImageView) parent.findViewById(R.id.cave_spinner_cave_type);
        TextView labelView = (TextView) parent.findViewById(R.id.cave_spinner_label);
        return new CaveSpinnerViewHolder(parent, caveTypeView, labelView);
    }

    public void setCaveTypeViewImageDrawable(Drawable caveTypeViewDrawable) {
        caveTypeView.setImageDrawable(caveTypeViewDrawable);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }

    public void hideCaveTypeView() {
        caveTypeView.setVisibility(View.GONE);
    }
}
