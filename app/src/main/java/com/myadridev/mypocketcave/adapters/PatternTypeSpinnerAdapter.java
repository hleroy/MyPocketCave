package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.PatternTypeViewHolder;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;

public class PatternTypeSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;

    public PatternTypeSpinnerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        PatternTypeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pattern_type, parent, false);
            viewHolder = PatternTypeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PatternTypeViewHolder) convertView.getTag();
        }

        PatternTypeEnum patternType = PatternTypeEnum.getById(position);
        if (patternType != null) {
            int drawableResourceId = patternType.DrawableResourceId;
            if (drawableResourceId != -1) {
                viewHolder.setIconViewImageDrawable(ContextCompat.getDrawable(context, drawableResourceId));
            }
            viewHolder.setLabelViewText(context.getString(patternType.StringResourceId));
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
        return PatternTypeEnum.values().length;
    }

    @Override
    public Object getItem(int position) {
        return PatternTypeEnum.getById(position);
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
        PatternTypeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pattern_type, parent, false);
            viewHolder = PatternTypeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PatternTypeViewHolder) convertView.getTag();
        }

        PatternTypeEnum patternType = PatternTypeEnum.getById(position);
        if (patternType != null) {
            int drawableResourceId = patternType.DrawableResourceId;
            if (drawableResourceId != -1) {
                viewHolder.setIconViewImageDrawable(ContextCompat.getDrawable(context, drawableResourceId));
            }
            viewHolder.setLabelViewText(context.getString(patternType.StringResourceId));
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
