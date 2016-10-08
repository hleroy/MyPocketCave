package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.WineColorViewHolder;
import com.myadridev.mypocketcave.enums.WineColorEnum;

public class WineColorSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final boolean containsNone;

    public WineColorSpinnerAdapter(Context _context, boolean _containsNone) {
        context = _context;
        layoutInflater = LayoutInflater.from(context);
        containsNone = _containsNone;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        WineColorViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_wine_color, parent, false);
            viewHolder = WineColorViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (WineColorViewHolder) convertView.getTag();
        }

        WineColorEnum wineColor = WineColorEnum.getById(getWineColorId(position));
        if (wineColor != null) {
            int wineColorDrawableId = wineColor.DrawableResourceId;
            viewHolder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(context, wineColorDrawableId) : null);
            viewHolder.setLabelViewText(context.getString(wineColor.StringResourceId));
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
        return containsNone ? WineColorEnum.values().length : WineColorEnum.values().length - 1;
    }

    @Override
    public Object getItem(int position) {
        return WineColorEnum.getById(getWineColorId(position));
    }

    @Override
    public long getItemId(int position) {
        return getWineColorId(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WineColorViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_wine_color, parent, false);
            viewHolder = WineColorViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (WineColorViewHolder) convertView.getTag();
        }

        WineColorEnum wineColor = WineColorEnum.getById(getWineColorId(position));
        if (wineColor != null) {
            int wineColorDrawableId = wineColor.DrawableResourceId;
            if (wineColorDrawableId != -1)
                viewHolder.setColorViewImageDrawable(ContextCompat.getDrawable(context, wineColorDrawableId));
            else
                viewHolder.hideColorView();
            viewHolder.setLabelViewText(context.getString(wineColor.StringResourceId));
        }

        return convertView;
    }

    private int getWineColorId(int position) {
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
