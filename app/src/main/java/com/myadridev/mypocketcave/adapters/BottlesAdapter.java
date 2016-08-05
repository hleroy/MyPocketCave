package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleNumberViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

import java.util.List;

public class BottlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<BottleModel> allBottles;
    private final LayoutInflater layoutInflater;

    private final OnBottleClickListener listener;

    public BottlesAdapter(Context _context, List<BottleModel> _allBottles) {
        context = _context;
        allBottles = _allBottles;
        layoutInflater = LayoutInflater.from(context);
        listener = new OnBottleClickListener() {
            @Override
            public void onItemClick(int bottleId) {
                NavigationManager.navigateToBottleDetail(context, bottleId);
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            // Title
            return 0;
        } else {
            // Bottle
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return allBottles.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                // Title
                view = layoutInflater.inflate(R.layout.item_bottle_number, parent, false);
                return BottleNumberViewHolder.newInstance(view);
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
        if (position == 0) {
            BottleNumberViewHolder holder = (BottleNumberViewHolder) viewHolder;
            holder.setLabelViewText(context.getString(R.string.bottle_number_label, allBottles.size()));
        } else {
            BottleViewHolder holder = (BottleViewHolder) viewHolder;
            BottleModel bottle = allBottles.get(position - 1);
            if (bottle != null) {
                holder.setLabelViewText(bottle.Domain + " - " + bottle.Name);
                holder.setMillesimeViewText(bottle.Millesime == 0 ? "-" : String.valueOf(bottle.Millesime));
                holder.setStockLabelViewText(context.getString(R.string.bottles_stock, bottle.Stock));
                int wineColorDrawableId = bottle.WineColor.drawableResourceId;
                holder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(context, wineColorDrawableId) : null);
                holder.setOnItemClickListener(listener, bottle.Id);
            }
        }
    }
}


