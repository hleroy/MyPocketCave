package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.AddPatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.managers.CoordinatesModelManager;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.Map;

public class CaveArrangementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final Map<CoordinatesModel, PatternModel> patternMap;
    private final LayoutInflater layoutInflater;

    private final OnPatternClickListener listener;
    private final int maxRaw;
    private final int maxCol;

    public CaveArrangementAdapter(Context _context, Map<CoordinatesModel, PatternModel> _patternMap, CoordinatesModel maxRawCol) {
        context = _context;
        patternMap = _patternMap;
        maxRaw = maxRawCol.Raw;
        maxCol = maxRawCol.Col;
        layoutInflater = LayoutInflater.from(context);
        listener = new OnPatternClickListener() {
            @Override
            public void onPatternClick(CoordinatesModel coordinates) {
                // TODO : modifier / ajouter un pattern
                Toast.makeText(context, "click on raw : " + coordinates.Raw + ", col : " + coordinates.Col, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(position);

        if (patternMap.containsKey(coordinates)) {
            // Existing pattern
            return 0;
        } else {
            // Add new pattern
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return maxRaw * maxCol;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                // Existing pattern
                view = layoutInflater.inflate(R.layout.item_pattern, parent, false);
                return CaveArrangementViewHolder.newInstance(view);
            case 1:
                // Add new pattern
                view = layoutInflater.inflate(R.layout.item_add_pattern, parent, false);
                return AddPatternViewHolder.newInstance(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(position);

        if (patternMap.containsKey(coordinates)) {
            CaveArrangementViewHolder holder = (CaveArrangementViewHolder) viewHolder;
            PatternModel pattern = patternMap.get(coordinates);
            if (pattern != null) {
                CoordinatesModel maxRawCol = CoordinatesModelManager.Instance.getMaxRawCol(pattern.PlaceMap.keySet());
                if (maxRawCol.Col > 0) {
                    holder.setPatternViewLayoutManager(new GridLayoutManager(context, maxRawCol.Col));
                    PatternAdapter patternAdapter = new PatternAdapter(context, pattern.PlaceMap, maxRawCol, false);
                    holder.setPatternViewAdapter(patternAdapter);
                    holder.setOnItemClickListener(listener, coordinates);
                }
            }
        } else {
            AddPatternViewHolder holder = (AddPatternViewHolder) viewHolder;
            holder.setOnItemClickListener(listener, coordinates);
        }
    }

    private CoordinatesModel getCoordinateByPosition(int position) {
        return new CoordinatesModel(position / maxCol, position % maxCol);
    }
}


