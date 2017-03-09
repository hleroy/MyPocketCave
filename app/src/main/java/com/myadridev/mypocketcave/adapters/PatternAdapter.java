package com.myadridev.mypocketcave.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.PatternPlaceViewHolder;
import com.myadridev.mypocketcave.dialogs.PlaceBottleAlertDialog;
import com.myadridev.mypocketcave.dialogs.SeeBottleAlertDialog;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleDrunkClickListener;
import com.myadridev.mypocketcave.listeners.OnBottlePlacedClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleUnplacedClickListener;
import com.myadridev.mypocketcave.listeners.OnPlaceClickListener;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.models.CavePlaceModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatternAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity activity;
    private final Map<CoordinatesModel, CavePlaceModel> patternPlace;
    private final LayoutInflater layoutInflater;

    private final OnPlaceClickListener listener;
    private final boolean isClickable;
    private final int totalWidth;
    private final int numberRows;
    private final int numberCols;
    private final int bottleIdInHighlight;
    private List<View.OnClickListener> onResetHighlightlisteners;
    private List<OnBottleClickListener> onSetHighlightlisteners;
    private List<OnBottlePlacedClickListener> onBottlePlacedClickListeners;
    private List<OnBottleDrunkClickListener> onBottleDrunkClickListeners;
    private List<OnBottleUnplacedClickListener> onBottleUnplacedClickListeners;
    private CoordinatesModel patternCoordinates;

    public PatternAdapter(Activity activity, Map<CoordinatesModel, CavePlaceModel> patternPlace, CoordinatesModel maxRawCol,
                          boolean isClickable, int totalWidth, CoordinatesModel patternCoordinates) {
        this(activity, patternPlace, maxRawCol, isClickable, totalWidth, patternCoordinates, -1);
    }

    public PatternAdapter(Activity activity, Map<CoordinatesModel, CavePlaceModel> patternPlace, CoordinatesModel maxRawCol,
                          boolean isClickable, int totalWidth, CoordinatesModel patternCoordinates, int bottleIdInHighlight) {
        this.activity = activity;
        this.patternPlace = patternPlace;
        this.numberRows = maxRawCol.Row;
        this.numberCols = maxRawCol.Col;
        this.totalWidth = totalWidth;
        this.patternCoordinates = patternCoordinates;
        this.bottleIdInHighlight = bottleIdInHighlight;
        this.isClickable = isClickable;
        layoutInflater = LayoutInflater.from(this.activity);
        listener = (CoordinatesModel patternCoordinates1, CoordinatesModel coordinates) -> {
            CavePlaceModel cavePlace = this.patternPlace.get(coordinates);
            if (cavePlace.BottleId != -1) {
                SeeBottleAlertDialog alertDialog = new SeeBottleAlertDialog(this.activity, cavePlace.BottleId, patternCoordinates1, coordinates,
                        onBottleDrunkClickListeners, onBottleUnplacedClickListeners, bottleIdInHighlight, onSetHighlightlisteners, 0);
                alertDialog.show();
            } else {
                PlaceBottleAlertDialog alertDialog = new PlaceBottleAlertDialog(this.activity, patternCoordinates1, coordinates, onBottlePlacedClickListeners, 1);
                alertDialog.show();
            }
        };
    }

    public void addOnBottleUnplacedClickListener(OnBottleUnplacedClickListener onBottleUnplacedClickListener) {
        if (onBottleUnplacedClickListeners == null) {
            onBottleUnplacedClickListeners = new ArrayList<>();
        }
        onBottleUnplacedClickListeners.add(onBottleUnplacedClickListener);
    }

    public void addOnBottleDrunkClickListener(OnBottleDrunkClickListener onBottleDrunkClickListener) {
        if (onBottleDrunkClickListeners == null) {
            onBottleDrunkClickListeners = new ArrayList<>();
        }
        onBottleDrunkClickListeners.add(onBottleDrunkClickListener);
    }

    public void addOnBottlePlacedClickListener(OnBottlePlacedClickListener onBottlePlacedClickListener) {
        if (onBottlePlacedClickListeners == null) {
            onBottlePlacedClickListeners = new ArrayList<>();
        }
        onBottlePlacedClickListeners.add(onBottlePlacedClickListener);
    }

    public void addonSetHighlightlistener(OnBottleClickListener onSetHighlightlistener) {
        if (onSetHighlightlisteners == null) {
            onSetHighlightlisteners = new ArrayList<>();
        }
        onSetHighlightlisteners.add(onSetHighlightlistener);
    }

    public void addOnResetHighlightlisteners(View.OnClickListener onResetHighlightlistener) {
        if (onResetHighlightlisteners == null) {
            onResetHighlightlisteners = new ArrayList<>();
        }
        onResetHighlightlisteners.add(onResetHighlightlistener);
    }

    @Override
    public int getItemViewType(int position) {
        // PlaceType
        return 0;
    }

    @Override
    public int getItemCount() {
        return numberRows * numberCols;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                // PlaceType
                view = layoutInflater.inflate(R.layout.item_pattern_place, parent, false);
                return PatternPlaceViewHolder.newInstance(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PatternPlaceViewHolder holder = (PatternPlaceViewHolder) viewHolder;
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.getRowFromPosition(position, getItemCount()), CoordinatesManager.getColFromPosition(position));
        CavePlaceModel cavePlace = patternPlace.get(coordinates);
        if (cavePlace != null) {
            CavePlaceTypeEnum cavePlaceType = cavePlace.PlaceType;
            int itemWidth = getItemWidth();
            int itemHeight = getItemHeight();

            holder.itemView.setMinimumWidth(itemWidth);
            holder.itemView.setMinimumHeight(itemHeight);

            int caveTypeDrawableId = cavePlaceType.DrawableResourceId;
            if (caveTypeDrawableId != -1) {
                Picasso.with(activity).load(caveTypeDrawableId)
                        .resize(itemWidth, itemHeight).into(holder.getPlaceTypeView());
            } else {
                holder.setPlaceTypeViewImageDrawable(null);
            }
            if (isClickable && cavePlace.IsClickable) {
                holder.setOnItemClickListener(listener, patternCoordinates, coordinates);
            } else {
                holder.setClickable(false);
            }

            if (bottleIdInHighlight != -1) {
                setHighlightProperties(holder, cavePlace.BottleId == bottleIdInHighlight);
            } else {
                holder.resetHighlight();
            }
        } else {
            holder.setPlaceTypeViewImageDrawable(null);
            if (bottleIdInHighlight != -1) {
                setHighlightProperties(holder, false);
            } else {
                holder.resetHighlight();
            }
        }
    }

    private void setHighlightProperties(PatternPlaceViewHolder holder, boolean isHighlight) {
        holder.setHighlight(isHighlight);
        if (!isHighlight) {
            holder.setResetHighlightClickListener((View v) -> {
                if (onResetHighlightlisteners != null) {
                    for (View.OnClickListener onResetHighlightlistener : onResetHighlightlisteners) {
                        onResetHighlightlistener.onClick(v);
                    }
                }
            });
        }
    }

    private int getItemWidth() {
        return totalWidth / numberCols;
    }

    private int getItemHeight() {
        return totalWidth / numberRows;
    }

    private CoordinatesModel getCoordinateByPosition(int rowPosition, int colPosition) {
        return new CoordinatesModel(rowPosition / numberCols, colPosition % numberCols);
    }
}
