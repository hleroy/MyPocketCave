package com.myadridev.mypocketcave.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleNumberViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.PlaceBottleViewHolder;
import com.myadridev.mypocketcave.dialogs.PlaceBottleAlertDialog;
import com.myadridev.mypocketcave.listeners.OnBottleBindListener;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.listeners.OnBottlePlacedClickListener;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;

import java.util.List;

public class BottlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private final OnBottleClickListener listener;
    private final boolean hasTitle;
    public int MaxBottleToPlace;
    private List<BottleModelV2> bottles;
    private OnBottleBindListener onBottleBindListener;
    private OnBottleClickListener onBottleClickListener;
    private OnBottlePlacedClickListener onBottlePlacedClickListener;
    private int bottleIdInHighlight;
    private View.OnClickListener onResetHighlightlistener;

    public BottlesAdapter(Activity activity, List<BottleModelV2> bottles, boolean hasTitle, int maxBottleToPlace) {
        this(activity, bottles, hasTitle, maxBottleToPlace, -1);
    }

    public BottlesAdapter(Activity activity, List<BottleModelV2> bottles, boolean hasTitle, int maxBottleToPlace, int bottleIdInHighlight) {
        this.activity = activity;
        this.bottles = bottles;
        this.hasTitle = hasTitle;
        this.MaxBottleToPlace = maxBottleToPlace;
        this.bottleIdInHighlight = bottleIdInHighlight;
        layoutInflater = LayoutInflater.from(this.activity);
        listener = (int bottleId) -> {
            if (onBottleClickListener != null) {
                onBottleClickListener.onItemClick(bottleId);
            }
        };
    }

    public void setBottleIdInHighlight(int bottleIdInHighlight) {
        this.bottleIdInHighlight = bottleIdInHighlight;
        notifyDataSetChanged();
    }

    public void setBottles(List<BottleModelV2> bottles) {
        this.bottles = bottles;
        notifyDataSetChanged();
    }

    public void setOnBottleClickListener(OnBottleClickListener onBottleClickListener) {
        this.onBottleClickListener = onBottleClickListener;
    }

    public void setOnBottlePlacedClickListener(OnBottlePlacedClickListener onBottlePlacedClickListener) {
        this.onBottlePlacedClickListener = onBottlePlacedClickListener;
    }

    public void setOnBottleBindListener(OnBottleBindListener onBottleBindListener) {
        this.onBottleBindListener = onBottleBindListener;
    }

    public void setOnResetHighlightlistener(View.OnClickListener onResetHighlightlistener) {
        this.onResetHighlightlistener = onResetHighlightlistener;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasTitle && position == 0) {
            // Title
            return 0;
        } else if (MaxBottleToPlace > 0 && position == getItemCount() - 1) {
            // Place bottle
            return 2;
        } else {
            // Bottle
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return bottles.size() + (hasTitle ? 1 : 0) + (MaxBottleToPlace > 0 ? 1 : 0);
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
                // Place bottle
                view = layoutInflater.inflate(R.layout.item_place_bottle, parent, false);
                return PlaceBottleViewHolder.newInstance(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (hasTitle && position == 0) {
            // Title
            BottleNumberViewHolder holder = (BottleNumberViewHolder) viewHolder;
            int numberBottles = bottles.size();
            holder.setLabelViewText(activity.getResources().getQuantityString(R.plurals.bottle_number_label, numberBottles, numberBottles));
        } else if (MaxBottleToPlace > 0 && position == getItemCount() - 1) {
            // Place bottle
            PlaceBottleViewHolder holder = (PlaceBottleViewHolder) viewHolder;
            holder.setOnItemClickListener((view) -> onPlaceButtonClickListener());
            if (bottleIdInHighlight != -1) {
                setHighlightProperties(holder, false);
            } else {
                holder.resetHighlight();
            }
        } else {
            // Bottle
            BottleViewHolder holder = (BottleViewHolder) viewHolder;
            BottleModelV2 bottle = bottles.get(position - (hasTitle ? 1 : 0));
            if (bottle != null) {
                onBottleBindListener.onBottleBind(holder, bottle);
                holder.setOnItemClickListener(listener, bottle.Id);

                if (bottleIdInHighlight != -1) {
                    setHighlightProperties(holder, bottle.Id == bottleIdInHighlight);
                } else {
                    holder.resetHighlight();
                }
            }
        }
    }

    private void setHighlightProperties(BottleViewHolder holder, boolean isHighlight) {
        holder.setHighlight(isHighlight);
        if (!isHighlight) {
            holder.setResetHighlightClickListener((View v) -> {
                if (onResetHighlightlistener != null) {
                    onResetHighlightlistener.onClick(v);
                }
            });
        }
    }

    private void setHighlightProperties(PlaceBottleViewHolder holder, boolean isHighlight) {
        holder.setHighlight(isHighlight);
        if (!isHighlight) {
            holder.setResetHighlightClickListener((View v) -> {
                if (onResetHighlightlistener != null) {
                    onResetHighlightlistener.onClick(v);
                }
            });
        }
    }

    private void onPlaceButtonClickListener() {
        PlaceBottleAlertDialog alertDialog = new PlaceBottleAlertDialog(activity, null, null, onBottlePlacedClickListener, MaxBottleToPlace);
        alertDialog.show();
    }
}
