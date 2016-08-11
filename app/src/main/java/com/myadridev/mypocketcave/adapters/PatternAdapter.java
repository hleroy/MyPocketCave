package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.PatternPlaceViewHolder;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.listeners.OnPlaceClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class PatternAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final Map<CoordinatesModel, CavePlaceTypeEnum> patternPlaceType;
    private final LayoutInflater layoutInflater;

    private final OnPlaceClickListener listener;
    private final int numberRows;
    private final int numberCols;
    private final boolean isClickable;
    private final int totalWidth;
    private final int totalHeight;

    public PatternAdapter(Context _context, Map<CoordinatesModel, CavePlaceTypeEnum> _patternPlaceType, CoordinatesModel maxRawCol, boolean _isClickable, int _totalWidth, int _totalHeight) {
        context = _context;
        patternPlaceType = _patternPlaceType;
        numberRows = maxRawCol.Row;
        numberCols = maxRawCol.Col;
        isClickable = _isClickable;
        layoutInflater = LayoutInflater.from(context);
        listener = new OnPlaceClickListener() {
            @Override
            public void onPlaceClick(CoordinatesModel coordinates) {
                // TODO : voir la bouteille / popup d'ajout de bouteille
                Toast.makeText(context, "click on raw : " + coordinates.Row + ", col : " + coordinates.Col, Toast.LENGTH_LONG).show();
            }
        };
        totalWidth = _totalWidth;
        totalHeight = _totalHeight;
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
        CoordinatesModel coordinates = getCoordinateByPosition(position);
        CavePlaceTypeEnum cavePlace = patternPlaceType.get(coordinates);
        if (cavePlace != null) {
            // TODO : quand il y a une bouteille, drawable différent
            int caveTypeDrawableId = cavePlace.drawableResourceId;
            if (caveTypeDrawableId != -1) {
                Picasso.with(context).load(caveTypeDrawableId)
                        .resize(getItemWidth(), getItemHeight()).into(holder.getPlaceTypeView());
            } else {
                holder.setPlaceTypeViewImageDrawable(null);
            }
            if (isClickable) {
                if (cavePlace == CavePlaceTypeEnum.PLACE) {
                    holder.setOnItemClickListener(listener, coordinates);
                }
            }
            else {
                holder.setClickable(false);
            }
        }
    }

    private int getItemWidth() {
        return totalWidth / numberCols;
    }

    private int getItemHeight() {
        return totalHeight / numberRows;
    }

    private CoordinatesModel getCoordinateByPosition(int position) {
        return new CoordinatesModel(position / numberCols, position % numberCols);
    }
}


