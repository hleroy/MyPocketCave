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
import com.myadridev.mypocketcave.adapters.viewHolders.SuggestBottleResultSeeMoreButtonViewHolder;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.listeners.OnSeeMoreClickListener;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.v1.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.v1.SuggestBottleResultModel;

import java.util.List;

public class SuggestBottlesResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<SuggestBottleResultModel> bottles;
    private final LayoutInflater layoutInflater;
    private final OnBottleClickListener onBottleClickListener;
    private OnSeeMoreClickListener onSeeMoreClickListener;
    private boolean isAllBottlesVisible;
    private int numberBottlesAllCriteria;

    public SuggestBottlesResultAdapter(Context context, List<SuggestBottleResultModel> bottles) {
        this.context = context;
        this.bottles = bottles;
        isAllBottlesVisible = false;
        computeVisibleBottlesCount();
        layoutInflater = LayoutInflater.from(this.context);
        onBottleClickListener = (int bottleId) -> NavigationManager.navigateToBottleDetail(this.context, bottleId);
    }

    public int getNumberDisplayedBottles() {
        return isAllBottlesVisible ? bottles.size() : numberBottlesAllCriteria;
    }

    public boolean getIsAllBottlesVisible() {
        return isAllBottlesVisible;
    }

    public boolean isRecyclerViewDisplayed() {
        return !isAllBottlesVisible || bottles.size() > 0;
    }

    private void computeVisibleBottlesCount() {
        int count = 0;
        for (SuggestBottleResultModel bottle : bottles) {
            if (bottle.Score == SuggestBottleCriteria.NumberOfCriteria)
                count++;
        }
        numberBottlesAllCriteria = count;
    }

    private void seeAllBottles() {
        isAllBottlesVisible = true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (isAllBottlesVisible) {
            int allBottleSize = bottles.size();
            return allBottleSize == 0 ? 0 : allBottleSize + 2;
        } else {
            return numberBottlesAllCriteria == 0 ? 1 : numberBottlesAllCriteria + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!isAllBottlesVisible && ((numberBottlesAllCriteria == 0 && position == 0) || (position == numberBottlesAllCriteria + 1))) {
            // See more button
            return 2;
        } else if (position == 0 || isAllBottlesVisible && position == numberBottlesAllCriteria + 1) {
            // Title
            return 0;
        } else {
            // Bottle
            return 1;
        }
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
            case 2:
                // See more button
                view = layoutInflater.inflate(R.layout.item_bottle_result_see_more, parent, false);
                return SuggestBottleResultSeeMoreButtonViewHolder.newInstance(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (!isAllBottlesVisible && ((numberBottlesAllCriteria == 0 && position == 0) || (position == numberBottlesAllCriteria + 1))) {
            SuggestBottleResultSeeMoreButtonViewHolder holder = (SuggestBottleResultSeeMoreButtonViewHolder) viewHolder;
            holder.button.setOnClickListener((View view) -> {
                seeAllBottles();
                if (onSeeMoreClickListener != null) {
                    onSeeMoreClickListener.onClick();
                }
            });

        } else if (position == 0) {
            BottleNumberViewHolder holder = (BottleNumberViewHolder) viewHolder;
            holder.setLabelViewText(context.getResources().getQuantityString(R.plurals.bottle_result_all_criteria_number_label, numberBottlesAllCriteria, numberBottlesAllCriteria));
        } else if (isAllBottlesVisible && position == numberBottlesAllCriteria + 1) {
            BottleNumberViewHolder holder = (BottleNumberViewHolder) viewHolder;
            int bottleResults = bottles.size() - numberBottlesAllCriteria;
            holder.setLabelViewText(context.getResources().getQuantityString(R.plurals.bottle_result_not_all_criteria_number_label, bottleResults, bottleResults));
        } else if (isAllBottlesVisible && position > numberBottlesAllCriteria + 1) {
            BindBottleViewHolder((BottleViewHolder) viewHolder, bottles.get(position - 2));
        } else if (position <= numberBottlesAllCriteria) {
            BindBottleViewHolder((BottleViewHolder) viewHolder, bottles.get(position - 1));
        }
    }

    private void BindBottleViewHolder(BottleViewHolder viewHolder, SuggestBottleResultModel bottle) {
        if (bottle != null) {
            viewHolder.setLabelViewText(bottle.Bottle.Domain + " - " + bottle.Bottle.Name);
            viewHolder.setMillesimeViewText(bottle.Bottle.Millesime == 0 ? "-" : String.valueOf(bottle.Bottle.Millesime));
            viewHolder.setStockLabelViewText(context.getString(R.string.bottles_stock, bottle.Bottle.Stock));
            int wineColorDrawableId = bottle.Bottle.WineColor.DrawableResourceId;
            viewHolder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(context, wineColorDrawableId) : null);
            viewHolder.setRating(bottle.Bottle.Rating);
            viewHolder.setPriceRating(bottle.Bottle.PriceRating);
            viewHolder.setOnItemClickListener(onBottleClickListener, bottle.Bottle.Id);
            viewHolder.resetHighlight();
        }
    }

    public void setOnSeeMoreClickListener(OnSeeMoreClickListener onSeeMoreClickListener) {
        this.onSeeMoreClickListener = onSeeMoreClickListener;
    }
}