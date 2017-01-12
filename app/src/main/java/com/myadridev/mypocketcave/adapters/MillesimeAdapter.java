package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.MillesimeViewHolder;

import java.util.Calendar;

public class MillesimeAdapter implements SpinnerAdapter {

    public final int currentYear;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public MillesimeAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
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

        String millesime = position == 0 ? context.getString(R.string.no_millesime) : String.valueOf(currentYear + 1 - position);
        viewHolder.setLabelViewText(millesime);

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
        return currentYear - 1900 + 2;
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? 0 : currentYear + 1 - position;
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
        MillesimeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_millesime, parent, false);
            viewHolder = MillesimeViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MillesimeViewHolder) convertView.getTag();
        }

        String millesime = position == 0 ? context.getString(R.string.no_millesime) : String.valueOf(currentYear + 1 - position);
        viewHolder.setLabelViewText(millesime);

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
