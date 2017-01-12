package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveTypeViewHolder;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;

public class CaveTypeSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final boolean containsNone;

    public CaveTypeSpinnerAdapter(Context context, boolean containsNone) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        this.containsNone = containsNone;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CaveTypeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_cave_type, parent, false);
            viewHolder = CaveTypeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CaveTypeViewHolder) convertView.getTag();
        }

        CaveTypeEnum caveType = CaveTypeEnum.getById(getCaveTypeId(position));
        if (caveType != null) {
            int caveTypeDrawableId = caveType.DrawableResourceId;
            viewHolder.setTypeViewImageDrawable(caveTypeDrawableId != -1 ? ContextCompat.getDrawable(context, caveTypeDrawableId) : null);
            viewHolder.setLabelViewText(context.getString(caveType.StringResourceId));
        }

        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return containsNone ? CaveTypeEnum.values().length : CaveTypeEnum.values().length - 1;
    }

    @Override
    public Object getItem(int position) {
        return CaveTypeEnum.getById(getCaveTypeId(position));
    }

    @Override
    public long getItemId(int position) {
        return getCaveTypeId(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CaveTypeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_cave_type, parent, false);
            viewHolder = CaveTypeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CaveTypeViewHolder) convertView.getTag();
        }

        CaveTypeEnum caveType = CaveTypeEnum.getById(getCaveTypeId(position));
        if (caveType != null) {
            int caveTypeDrawableId = caveType.DrawableResourceId;
            if (caveTypeDrawableId != -1)
                viewHolder.setTypeViewImageDrawable(ContextCompat.getDrawable(context, caveTypeDrawableId));
            else
                viewHolder.hideTypeView();
            viewHolder.setLabelViewText(context.getString(caveType.StringResourceId));
        }

        return convertView;
    }

    private int getCaveTypeId(int position) {
        return containsNone ? position : position + 1;
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
