package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleNumberViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.listeners.OnBottleBindListener;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.models.BottleModel;

import java.util.ArrayList;
import java.util.List;

public class BottlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<BottleModel> bottles;
    private final LayoutInflater layoutInflater;
    private final OnBottleClickListener listener;
    private final OnBottleBindListener onBottleBindListener;
    private final boolean hasTitle;
    private List<OnBottleClickListener> onBottleClickListeners;

    public BottlesAdapter(Context context, List<BottleModel> bottles, boolean hasTitle, OnBottleBindListener onBottleBindListener) {
        this.context = context;
        this.bottles = bottles;
        this.hasTitle = hasTitle;
        this.onBottleBindListener = onBottleBindListener;
        layoutInflater = LayoutInflater.from(this.context);
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
        if (hasTitle && position == 0) {
            // Title
            return 0;
        } else {
            // Bottle
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return bottles.size() + (hasTitle ? 1 : 0);
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
        if (hasTitle && position == 0) {
            BottleNumberViewHolder holder = (BottleNumberViewHolder) viewHolder;
            int numberBottles = bottles.size();
            holder.setLabelViewText(context.getResources().getQuantityString(R.plurals.bottle_number_label, numberBottles, numberBottles));
        } else {
            BottleViewHolder holder = (BottleViewHolder) viewHolder;
            BottleModel bottle = bottles.get(position - (hasTitle ? 1 : 0));
            if (bottle != null) {
                onBottleBindListener.onBottleBind(holder, bottle);
                holder.setOnItemClickListener(listener, bottle.Id);
            }
        }
    }
}
