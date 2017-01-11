package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveViewHolder;
import com.myadridev.mypocketcave.listeners.OnCaveClickListener;
import com.myadridev.mypocketcave.models.CaveLightModel;

import java.util.ArrayList;
import java.util.List;

public class SeeCavesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<CaveLightModel> allCaves;
    private final LayoutInflater layoutInflater;

    private final OnCaveClickListener listener;
    private List<OnCaveClickListener> onCaveClickListeners;

    public SeeCavesAdapter(Context _context, List<CaveLightModel> _allCaves) {
        context = _context;
        allCaves = _allCaves;
        layoutInflater = LayoutInflater.from(context);
        listener = (int caveId) -> {
            if (onCaveClickListeners != null) {
                for (OnCaveClickListener onCaveClickListener : onCaveClickListeners) {
                    onCaveClickListener.onItemClick(caveId);
                }
            }
        };
    }

    public void addOnCaveClickListener(OnCaveClickListener onCaveClickListener) {
        if (onCaveClickListeners == null) {
            onCaveClickListeners = new ArrayList<>();
        }
        onCaveClickListeners.add(onCaveClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        // Cave
        return 0;
    }

    @Override
    public int getItemCount() {
        return allCaves.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                // Cave
                view = layoutInflater.inflate(R.layout.item_cave, parent, false);
                return CaveViewHolder.newInstance(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CaveViewHolder holder = (CaveViewHolder) viewHolder;
        CaveLightModel cave = allCaves.get(position);
        if (cave != null) {
            holder.setLabelViewText(cave.Name);
            int caveTypeDrawableId = cave.CaveType.DrawableResourceId;
            holder.setTypeViewImageDrawable(caveTypeDrawableId != -1 ? ContextCompat.getDrawable(context, caveTypeDrawableId) : null);
            holder.setUsedLabelViewText(context.getResources().getQuantityString(R.plurals.cave_number_bottles, cave.TotalUsed, cave.TotalUsed));
            holder.setOnItemClickListener(listener, cave.Id);
        }
    }
}
