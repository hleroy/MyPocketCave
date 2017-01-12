package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.DomainViewHolder;
import com.myadridev.mypocketcave.managers.BottleManager;

import java.util.List;

public class DomainSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final boolean containsNone;
    private List<String> allDifferentDomainsWithNone;

    public DomainSpinnerAdapter(Context context, boolean containsNone) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        this.containsNone = containsNone;

        allDifferentDomainsWithNone = BottleManager.getAllDistinctDomains();
        allDifferentDomainsWithNone.add(0, this.context.getString(R.string.suggest_bottle_domain_none));
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        DomainViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_domain, parent, false);
            viewHolder = DomainViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DomainViewHolder) convertView.getTag();
        }

        String person = allDifferentDomainsWithNone.get(position);
        viewHolder.setLabelViewText(person);

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
        int size = allDifferentDomainsWithNone.size();
        return containsNone ? size : size - 1;
    }

    @Override
    public Object getItem(int position) {
        return allDifferentDomainsWithNone.get(position);
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
        DomainViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_domain, parent, false);
            viewHolder = DomainViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DomainViewHolder) convertView.getTag();
        }

        String person = allDifferentDomainsWithNone.get(position);
        viewHolder.setLabelViewText(person);

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
