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
    private final int totalHeight;
    private final int numberRows;
    private final int numberCols;
    private List<OnBottlePlacedClickListener> onBottlePlacedClickListeners;
    private List<OnBottleDrunkClickListener> onBottleDrunkClickListeners;
    private List<OnBottleUnplacedClickListener> onBottleUnplacedClickListeners;
    private CoordinatesModel patternCoordinates;

    public PatternAdapter(Activity _activity, Map<CoordinatesModel, CavePlaceModel> _patternPlace, CoordinatesModel maxRawCol,
                          boolean _isClickable, int _totalWidth, int _totalHeight, CoordinatesModel patternCoordinates) {
        this.activity = _activity;
        patternPlace = _patternPlace;
        numberRows = maxRawCol.Row;
        numberCols = maxRawCol.Col;
        this.patternCoordinates = patternCoordinates;
        isClickable = _isClickable;
        layoutInflater = LayoutInflater.from(activity);
        listener = (CoordinatesModel patternCoordinates1, CoordinatesModel coordinates) -> {
            CavePlaceModel cavePlace = patternPlace.get(coordinates);
            if (cavePlace.BottleId != -1) {
                SeeBottleAlertDialog alertDialog = new SeeBottleAlertDialog(activity, cavePlace.BottleId, patternCoordinates1, coordinates,
                        onBottleDrunkClickListeners, onBottleUnplacedClickListeners);
                alertDialog.show();
            } else {
                PlaceBottleAlertDialog alertDialog = new PlaceBottleAlertDialog(activity, patternCoordinates1, coordinates, onBottlePlacedClickListeners);
                alertDialog.show();
            }
        };
        totalWidth = _totalWidth;
        totalHeight = _totalHeight;
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
        } else {
            holder.setPlaceTypeViewImageDrawable(null);
        }
    }

    private int getItemWidth() {
        return totalWidth / numberCols;
    }

    private int getItemHeight() {
        return totalHeight / numberRows;
    }

    private CoordinatesModel getCoordinateByPosition(int rowPosition, int colPosition) {
        return new CoordinatesModel(rowPosition / numberCols, colPosition % numberCols);
    }
}
