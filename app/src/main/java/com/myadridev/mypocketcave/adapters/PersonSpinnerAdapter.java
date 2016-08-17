package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.PersonViewHolder;
import com.myadridev.mypocketcave.managers.BottleManager;

public class PersonSpinnerAdapter implements SpinnerAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final boolean containsNone;
    private String[] allDifferentPersonsWithNone;

    public PersonSpinnerAdapter(Context _context, boolean _containsNone) {
        context = _context;
        layoutInflater = LayoutInflater.from(context);
        containsNone = _containsNone;

        String[] allDifferentPersons = BottleManager.getAllDistinctPersons();
        allDifferentPersonsWithNone = new String[allDifferentPersons.length + 1];
        allDifferentPersonsWithNone[0] = context.getString(R.string.suggest_bottle_person_none);
        System.arraycopy(allDifferentPersons, 0, allDifferentPersonsWithNone, 1, allDifferentPersons.length);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        PersonViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_person, parent, false);
            viewHolder = PersonViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PersonViewHolder) convertView.getTag();
        }

        String person = allDifferentPersonsWithNone[position];
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
        return containsNone ? allDifferentPersonsWithNone.length : allDifferentPersonsWithNone.length - 1;
    }

    @Override
    public Object getItem(int position) {
        return allDifferentPersonsWithNone[position];
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
        PersonViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_person, parent, false);
            viewHolder = PersonViewHolder.newInstance(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PersonViewHolder) convertView.getTag();
        }

        String person = allDifferentPersonsWithNone[position];
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
