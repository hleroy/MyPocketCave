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
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleDrunkClickListener;
import com.myadridev.mypocketcave.listeners.OnBottlePlacedClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleUnplacedClickListener;
import com.myadridev.mypocketcave.listeners.OnPlaceClickListener;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.models.CavePlaceModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class PatternAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity activity;
    private final Map<CoordinatesModel, CavePlaceModel> patternPlace;
    private final LayoutInflater layoutInflater;

    private final OnPlaceClickListener listener;
    private final boolean isClickable;
    private final int nbRows;
    private final int nbCols;
    private final int bottleIdInHighlight;
    private View.OnClickListener onResetHighlightlistener;
    private OnBottleClickListener onSetHighlightlistener;
    private OnBottlePlacedClickListener onBottlePlacedClickListener;
    private OnBottleDrunkClickListener onBottleDrunkClickListener;
    private OnBottleUnplacedClickListener onBottleUnplacedClickListener;
    private CoordinatesModel patternCoordinates;

    private int itemCount;
    private int itemWidth;
    private int itemHeight;

    public PatternAdapter(Activity activity, Map<CoordinatesModel, CavePlaceModel> patternPlace, CoordinatesModel maxRawCol,
                          boolean isClickable, int totalWidth, CoordinatesModel patternCoordinates) {
        this(activity, patternPlace, maxRawCol, isClickable, totalWidth, patternCoordinates, -1);
    }

    public PatternAdapter(Activity activity, Map<CoordinatesModel, CavePlaceModel> patternPlace, CoordinatesModel maxRawCol,
                          boolean isClickable, int totalWidth, CoordinatesModel patternCoordinates, int bottleIdInHighlight) {
        this.activity = activity;
        this.patternPlace = patternPlace;
        this.nbRows = maxRawCol.Row;
        this.nbCols = maxRawCol.Col;
        this.patternCoordinates = patternCoordinates;
        this.bottleIdInHighlight = bottleIdInHighlight;
        this.isClickable = isClickable;
        layoutInflater = LayoutInflater.from(this.activity);
        listener = (CoordinatesModel patternCoordinates1, CoordinatesModel coordinates) -> {
            CavePlaceModel cavePlace = this.patternPlace.get(coordinates);
            if (cavePlace.BottleId != -1) {
                SeeBottleAlertDialog alertDialog = new SeeBottleAlertDialog(this.activity, cavePlace.BottleId, patternCoordinates1, coordinates,
                        onBottleDrunkClickListener, onBottleUnplacedClickListener, bottleIdInHighlight, onSetHighlightlistener, 0);
                alertDialog.show();
            } else {
                PlaceBottleAlertDialog alertDialog = new PlaceBottleAlertDialog(this.activity, patternCoordinates1, coordinates, onBottlePlacedClickListener, 1);
                alertDialog.show();
            }
        };
        itemCount = nbRows * nbCols;
        itemWidth = totalWidth / nbCols;
        itemHeight = totalWidth / nbRows;
    }

    public void setOnBottleUnplacedClickListener(OnBottleUnplacedClickListener onBottleUnplacedClickListener) {
        this.onBottleUnplacedClickListener = onBottleUnplacedClickListener;
    }

    public void setOnBottleDrunkClickListener(OnBottleDrunkClickListener onBottleDrunkClickListener) {
        this.onBottleDrunkClickListener = onBottleDrunkClickListener;
    }

    public void setOnBottlePlacedClickListener(OnBottlePlacedClickListener onBottlePlacedClickListener) {
        this.onBottlePlacedClickListener = onBottlePlacedClickListener;
    }

    public void setOnSetHighlightlistener(OnBottleClickListener onSetHighlightlistener) {
        this.onSetHighlightlistener = onSetHighlightlistener;
    }

    public void setOnResetHighlightlistener(View.OnClickListener onResetHighlightlistener) {
        this.onResetHighlightlistener = onResetHighlightlistener;
    }

    @Override
    public int getItemViewType(int position) {
        // PlaceType
        return 0;
    }

    @Override
    public int getItemCount() {
        return itemCount;
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
            holder.itemView.setMinimumWidth(itemWidth);
            holder.itemView.setMinimumHeight(itemHeight);

            int caveTypeDrawableId = cavePlace.PlaceType.DrawableResourceId;
            if (caveTypeDrawableId != -1) {
                Picasso.with(activity).load(caveTypeDrawableId).resize(itemWidth, itemHeight).into(holder.getPlaceTypeView());
            }
            if (isClickable && cavePlace.IsClickable) {
                holder.setOnItemClickListener(listener, patternCoordinates, coordinates);
            } else {
                holder.setClickable(false);
            }

            if (bottleIdInHighlight != -1) {
                setHighlightProperties(holder, cavePlace.BottleId == bottleIdInHighlight);
            }
        } else {
            if (bottleIdInHighlight != -1) {
                setHighlightProperties(holder, false);
            }
        }
    }

    private void setHighlightProperties(PatternPlaceViewHolder holder, boolean isHighlight) {
        holder.setHighlight(isHighlight);
        if (!isHighlight) {
            holder.setResetHighlightClickListener((View v) -> {
                if (onResetHighlightlistener != null) {
                    onResetHighlightlistener.onClick(v);
                }
            });
        }
    }

    private CoordinatesModel getCoordinateByPosition(int rowPosition, int colPosition) {
        return new CoordinatesModel(rowPosition / nbCols, colPosition % nbCols);
    }

    public void updatePositions(List<CoordinatesModel> coordinatesModelList) {
        for (CoordinatesModel coordinatesModel : coordinatesModelList) {
            notifyItemChanged(CoordinatesManager.getPositionFromCoordinates(coordinatesModel.Row, coordinatesModel.Col, nbRows, nbCols));
        }
    }
}
