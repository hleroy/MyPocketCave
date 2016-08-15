package com.myadridev.mypocketcave.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.PatternPlaceViewHolder;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.listeners.OnBottlePlacedClickListener;
import com.myadridev.mypocketcave.listeners.OnPlaceClickListener;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;
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
    private List<OnBottlePlacedClickListener> onBottlePlacedClickListeners;

    private final boolean isClickable;
    private final int totalWidth;
    private final int totalHeight;
    private final int numberRows;
    private final int numberCols;
    private CoordinatesModel patternCoordinates;

    private static class PlaceBottleAlertDialog extends AlertDialog {

        protected PlaceBottleAlertDialog(Activity activity, final List<OnBottlePlacedClickListener> onBottlePlacedClickListeners, final CoordinatesModel patternCoordinates, final CoordinatesModel coordinates) {
            super(activity);

            setTitle(R.string.title_place_bottle);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_add_bottle_to_cave, null);
            setView(dialogView);

            TextView noBottlesView = (TextView) dialogView.findViewById(R.id.alert_add_bottle_no_bottles_label);
            RecyclerView bottlesRecyclerView = (RecyclerView) dialogView.findViewById(R.id.alert_add_bottle_bottles_recyclerview);

            List<BottleModel> nonPlacedBottles = BottleManager.Instance.getNonPlacedBottles();
            final int[] bottleIdToPlace = {-1};
            if (nonPlacedBottles.isEmpty()) {
                noBottlesView.setVisibility(View.VISIBLE);
                bottlesRecyclerView.setVisibility(View.GONE);
            } else {
                noBottlesView.setVisibility(View.GONE);
                bottlesRecyclerView.setVisibility(View.VISIBLE);

                bottlesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                PlaceBottlesAdapter bottlesAdapter = new PlaceBottlesAdapter(activity, nonPlacedBottles);
                bottlesAdapter.addOnBottleClickListener(new OnBottleClickListener() {
                    @Override
                    public void onItemClick(int bottleId) {
                        bottleIdToPlace[0] = bottleId;
                        dismiss();
                    }
                });
                bottlesRecyclerView.setAdapter(bottlesAdapter);
            }
            setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    int bottleId = bottleIdToPlace[0];
                    if (bottleId != -1) {
                        if (onBottlePlacedClickListeners != null) {
                            for (OnBottlePlacedClickListener onBottlePlacedClickListener : onBottlePlacedClickListeners) {
                                onBottlePlacedClickListener.onBottlePlaced(patternCoordinates, coordinates, bottleId);
                            }
                        }
                    }
                }
            });
        }
    }


    public PatternAdapter(Activity _activity, Map<CoordinatesModel, CavePlaceModel> _patternPlace, CoordinatesModel maxRawCol,
                          boolean _isClickable, int _totalWidth, int _totalHeight, CoordinatesModel patternCoordinates) {
        this.activity = _activity;
        patternPlace = _patternPlace;
        numberRows = maxRawCol.Row;
        numberCols = maxRawCol.Col;
        this.patternCoordinates = patternCoordinates;
        isClickable = _isClickable;
        layoutInflater = LayoutInflater.from(activity);
        listener = new OnPlaceClickListener() {
            @Override
            public void onPlaceClick(final CoordinatesModel patternCoordinates, final CoordinatesModel coordinates) {
                CavePlaceModel cavePlace = patternPlace.get(coordinates);
                if (cavePlace.BottleId != -1) {
                    // TODO
                    NavigationManager.navigateToBottleDetail(activity, cavePlace.BottleId);
                } else {
                    PlaceBottleAlertDialog alertDialog = new PlaceBottleAlertDialog(activity, onBottlePlacedClickListeners, patternCoordinates, coordinates);
                    alertDialog.show();
                }
            }
        };
        totalWidth = _totalWidth;
        totalHeight = _totalHeight;
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
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.Instance.getRowFromPosition(position, getItemCount()), CoordinatesManager.Instance.getColFromPosition(position));
        CavePlaceModel cavePlace = patternPlace.get(coordinates);
        if (cavePlace != null) {
            CavePlaceTypeEnum cavePlaceType = cavePlace.PlaceType;
            int itemWidth = getItemWidth();
            int itemHeight = getItemHeight();

            holder.itemView.setMinimumWidth(itemWidth);
            holder.itemView.setMinimumHeight(itemHeight);

            // TODO : quand il y a une bouteille, drawable différent
            int caveTypeDrawableId = cavePlaceType.drawableResourceId;
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


