package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveSpinnerViewHolder;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.models.CaveLightModel;

import java.util.List;

public class CaveSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<CaveLightModel> allDifferentCavesWithNone;

    public CaveSpinnerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);

        allDifferentCavesWithNone = CaveManager.getLightCaves();
        allDifferentCavesWithNone.add(0, null);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CaveSpinnerViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_cave_spinner, parent, false);
            viewHolder = CaveSpinnerViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CaveSpinnerViewHolder) convertView.getTag();
        }

        CaveLightModel cave = allDifferentCavesWithNone.get(position);
        if (cave != null) {
            int caveTypeDrawableId = cave.CaveType.DrawableResourceId;
            viewHolder.setCaveTypeViewImageDrawable(ContextCompat.getDrawable(context, caveTypeDrawableId));
            viewHolder.setLabelViewText(cave.Name);
        } else {
            viewHolder.setCaveTypeViewImageDrawable(null);
            viewHolder.setLabelViewText(context.getString(R.string.suggest_bottle_cave_none));
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
        return allDifferentCavesWithNone.size();
    }

    @Override
    public Object getItem(int position) {
        return allDifferentCavesWithNone.get(position);
    }

    @Override
    public long getItemId(int position) {
        CaveLightModel cave = allDifferentCavesWithNone.get(position);
        return cave != null ? cave.Id : -1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CaveSpinnerViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_cave_spinner, parent, false);
            viewHolder = CaveSpinnerViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CaveSpinnerViewHolder) convertView.getTag();
        }

        CaveLightModel cave = allDifferentCavesWithNone.get(position);
        if (cave != null) {
            int caveTypeDrawableId = cave.CaveType.DrawableResourceId;
            viewHolder.setCaveTypeViewImageDrawable(caveTypeDrawableId != -1 ? ContextCompat.getDrawable(context, caveTypeDrawableId) : null);
            viewHolder.setLabelViewText(cave.Name);
        } else {
            viewHolder.hideCaveTypeView();
            viewHolder.setLabelViewText(context.getString(R.string.suggest_bottle_cave_none));
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
