package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.FarmingTypeViewHolder;
import com.myadridev.mypocketcave.enums.v2.FarmingTypeEnumV2;

public class FarmingTypeSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;

    public FarmingTypeSpinnerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        FarmingTypeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_farming_type, parent, false);
            viewHolder = FarmingTypeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FarmingTypeViewHolder) convertView.getTag();
        }

        FarmingTypeEnumV2 farmingType = FarmingTypeEnumV2.getById(position);
        if (farmingType != null) {
            viewHolder.setLabelViewText(context.getString(farmingType.StringResourceId));
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
        return FarmingTypeEnumV2.values().length;
    }

    @Override
    public Object getItem(int position) {
        return FarmingTypeEnumV2.getById(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FarmingTypeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_farming_type, parent, false);
            viewHolder = FarmingTypeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FarmingTypeViewHolder) convertView.getTag();
        }

        FarmingTypeEnumV2 farmingType = FarmingTypeEnumV2.getById(position);
        if (farmingType != null) {
            CharSequence str = context.getString(farmingType.StringResourceId);
            viewHolder.setLabelViewText(str);
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
