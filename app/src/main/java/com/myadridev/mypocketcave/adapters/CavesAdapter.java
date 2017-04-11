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
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;

import java.util.List;

public class CavesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<CaveLightModelV2> caves;
    private final LayoutInflater layoutInflater;

    private final OnCaveClickListener listener;
    private final OnCaveBindListener onCaveBindListener;
    private OnCaveClickListener onCaveClickListener;

    public CavesAdapter(Context context, List<CaveLightModelV2> caves, OnCaveBindListener onCaveBindListener) {
        this.caves = caves;
        this.onCaveBindListener = onCaveBindListener;
        layoutInflater = LayoutInflater.from(context);
        listener = (int caveId) -> {
            if (onCaveClickListener != null) {
                onCaveClickListener.onItemClick(caveId);
            }
        };
    }

    public void setOnCaveClickListener(OnCaveClickListener onCaveClickListener) {
        this.onCaveClickListener = onCaveClickListener;
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
        CaveLightModelV2 cave = caves.get(position);
        if (cave != null) {
            onCaveBindListener.onCaveBind(holder, cave);
            holder.setOnItemClickListener(listener, cave.Id);
        }
    }
}
