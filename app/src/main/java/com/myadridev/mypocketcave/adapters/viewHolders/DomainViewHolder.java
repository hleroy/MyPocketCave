package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class DomainViewHolder extends RecyclerView.ViewHolder {
    private final TextView labelView;

    private DomainViewHolder(View itemView, TextView labelView) {
        super(itemView);
        this.labelView = labelView;
    }

    public static DomainViewHolder newInstance(View parent) {
        TextView labelView = (TextView) parent.findViewById(R.id.domain_label);
        return new DomainViewHolder(parent, labelView);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }
}
