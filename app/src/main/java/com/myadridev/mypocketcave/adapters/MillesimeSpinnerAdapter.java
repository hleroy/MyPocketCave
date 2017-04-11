package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.MillesimeViewHolder;
import com.myadridev.mypocketcave.enums.v2.MillesimeEnumV2;

public class MillesimeSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final boolean containsNone;

    public MillesimeSpinnerAdapter(Context context, boolean containsNone) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        this.containsNone = containsNone;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        MillesimeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_millesime, parent, false);
            viewHolder = MillesimeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MillesimeViewHolder) convertView.getTag();
        }

        MillesimeEnumV2 millesime = MillesimeEnumV2.getById(getMillesimeEnumId(position));
        if (millesime != null) {
            viewHolder.setLabelViewText(context.getString(millesime.StringResourceId));
        }

        return convertView;
    }

    private int getMillesimeEnumId(int position) {
        return containsNone ? position : position + 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return containsNone ? MillesimeEnumV2.values().length : MillesimeEnumV2.values().length - 1;
    }

    @Override
    public Object getItem(int position) {
        return MillesimeEnumV2.getById(getMillesimeEnumId(position));
    }

    @Override
    public long getItemId(int position) {
        return getMillesimeEnumId(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MillesimeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_millesime, parent, false);
            viewHolder = MillesimeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MillesimeViewHolder) convertView.getTag();
        }

        MillesimeEnumV2 millesime = MillesimeEnumV2.getById(getMillesimeEnumId(position));
        if (millesime != null) {
            viewHolder.setLabelViewText(context.getString(millesime.StringResourceId));
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
