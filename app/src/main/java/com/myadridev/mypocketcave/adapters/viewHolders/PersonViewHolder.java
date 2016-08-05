package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class PersonViewHolder extends RecyclerView.ViewHolder {
    private final TextView labelView;

    private PersonViewHolder(View itemView, TextView labelView) {
        super(itemView);
        this.labelView = labelView;
    }

    public static PersonViewHolder newInstance(View parent) {
        TextView labelView = (TextView) parent.findViewById(R.id.person_label);
        return new PersonViewHolder(parent, labelView);
    }

    public void setLabelViewText(CharSequence labelViewText) {
        labelView.setText(labelViewText);
    }
}
