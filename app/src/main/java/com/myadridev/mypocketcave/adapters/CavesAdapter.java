package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveViewHolder;
import com.myadridev.mypocketcave.listeners.OnCaveBindListener;
import com.myadridev.mypocketcave.listeners.OnCaveClickListener;
import com.myadridev.mypocketcave.models.CaveLightModel;

import java.util.ArrayList;
import java.util.List;

public class CavesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<CaveLightModel> caves;
    private final LayoutInflater layoutInflater;

    private final OnCaveClickListener listener;
    private final OnCaveBindListener onCaveBindListener;
    private List<OnCaveClickListener> onCaveClickListeners;

    public CavesAdapter(Context context, List<CaveLightModel> caves, OnCaveBindListener onCaveBindListener) {
        this.context = context;
        this.caves = caves;
        this.onCaveBindListener = onCaveBindListener;
        layoutInflater = LayoutInflater.from(this.context);
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
        return caves.size();
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
        CaveLightModel cave = caves.get(position);
        if (cave != null) {
            onCaveBindListener.onCaveBind(holder, cave);
            holder.setOnItemClickListener(listener, cave.Id);
        }
    }
}
