package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.models.BottleModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceBottlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<BottleModel> allBottles;
    private final LayoutInflater layoutInflater;

    private final OnBottleClickListener listener;
    private List<OnBottleClickListener> onBottleClickListeners;

    public PlaceBottlesAdapter(Context _context, List<BottleModel> _allBottles) {
        context = _context;
        allBottles = _allBottles;
        layoutInflater = LayoutInflater.from(context);
        listener = (int bottleId) -> {
            if (onBottleClickListeners != null) {
                for (OnBottleClickListener onBottleClickListener : onBottleClickListeners) {
                    onBottleClickListener.onItemClick(bottleId);
                }
            }
        };
    }

    public void addOnBottleClickListener(OnBottleClickListener onBottleClickListener) {
        if (onBottleClickListeners == null) {
            onBottleClickListeners = new ArrayList<>();
        }
        onBottleClickListeners.add(onBottleClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        // Bottle
        return 1;
    }

    @Override
    public int getItemCount() {
        return allBottles.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                // Bottle
                view = layoutInflater.inflate(R.layout.item_bottle, parent, false);
                return BottleViewHolder.newInstance(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        BottleViewHolder holder = (BottleViewHolder) viewHolder;
        BottleModel bottle = allBottles.get(position);
        if (bottle != null) {
            holder.setLabelViewText(bottle.Domain + " - " + bottle.Name);
            holder.setMillesimeViewText(bottle.Millesime == 0 ? "-" : String.valueOf(bottle.Millesime));
            holder.setStockLabelViewText(context.getString(R.string.bottles_to_place, bottle.Stock - bottle.NumberPlaced));
            int wineColorDrawableId = bottle.WineColor.DrawableResourceId;
            holder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(context, wineColorDrawableId) : null);
            holder.setOnItemClickListener(listener, bottle.Id);
        }
    }
}
