package com.myadridev.mypocketcave.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.adapters.viewHolders.AddPatternViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.CaveArrangementViewHolder;
import com.myadridev.mypocketcave.adapters.viewHolders.NoPatternViewHolder;
import com.myadridev.mypocketcave.dialogs.EditPatternAlertDialog;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.listeners.OnValueChangedListener;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;
import com.myadridev.mypocketcave.tasks.caves.CaveRemovePatternTask;
import com.myadridev.mypocketcave.tasks.caves.CreateCaveDetailArrangementTask;
import com.myadridev.mypocketcave.tasks.caves.CreateCaveEditArrangementTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class CaveArrangementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final AbstractCaveEditActivity editActivity;
    private final CaveDetailActivity detailActivity;
    private final CaveArrangementModel caveArrangement;
    private final LayoutInflater layoutInflater;
    private final boolean isIndividualPlacesClickable;

    private final OnPatternClickListener listener;
    private final int nbRows;
    private final int nbCols;
    private final int totalWidth;
    private OnValueChangedListener onValueChangedListener;
    private int itemWidth;

    public int BottleIdInHighlight;

    public CaveArrangementAdapter(AbstractCaveEditActivity activity, CaveArrangementModel caveArrangement, int nbRows, int nbCols, int totalWidth) {
        editActivity = activity;
        detailActivity = null;
        this.caveArrangement = caveArrangement;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.totalWidth = totalWidth;
        layoutInflater = LayoutInflater.from(editActivity);
        isIndividualPlacesClickable = false;
        listener = (CoordinatesModel coordinates) -> {
            PatternModelWithBottles oldPattern = this.caveArrangement.PatternMap.containsKey(coordinates) ? this.caveArrangement.PatternMap.get(coordinates) : null;
            editActivity.OldClickedPatternId = oldPattern != null ? oldPattern.Id : -1;
            editActivity.ClickedPatternCoordinates = coordinates;
            if (editActivity.OldClickedPatternId == -1) {
                NavigationManager.navigateToPatternSelection(editActivity);
            } else {
                EditPatternAlertDialog alertDialog = new EditPatternAlertDialog(editActivity, editActivity.OldClickedPatternId, oldPattern.Type == PatternTypeEnum.LINEAR, () -> {
                    CaveRemovePatternTask caveRemovePatternTask = new CaveRemovePatternTask(editActivity);
                    caveRemovePatternTask.execute(coordinates);
                });
                alertDialog.show();
            }
        };
        this.BottleIdInHighlight = -1;
        nbPatternsLeftToLoad = getItemCount();
    }

    public CaveArrangementAdapter(CaveDetailActivity activity, CaveArrangementModel caveArrangement, int nbRows, int nbCols, int totalWidth, int bottleIdInHighlight) {
        detailActivity = activity;
        editActivity = null;
        this.caveArrangement = caveArrangement;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.totalWidth = totalWidth;
        layoutInflater = LayoutInflater.from(detailActivity);
        isIndividualPlacesClickable = true;
        listener = null;
        this.BottleIdInHighlight = bottleIdInHighlight;
        nbPatternsLeftToLoad = getItemCount();
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    @Override
    public int getItemViewType(int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.getRowFromPosition(position, getItemCount()), CoordinatesManager.getColFromPosition(position));

        if (caveArrangement.PatternMap.containsKey(coordinates)) {
            // Existing pattern
            return 0;
        } else if (!isIndividualPlacesClickable && isAddPattern(coordinates)) {
            // Add new pattern
            return 1;
        } else {
            return -1;
        }
    }

    private boolean isAddPattern(CoordinatesModel coordinates) {
        return (caveArrangement.PatternMap.size() == 0 && coordinates.Col == 1 && coordinates.Row == 0)
                // over existing
                || caveArrangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))
                // right to existing and no line under or existing under
                || (caveArrangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col - 1))
                && (coordinates.Row == 0 || caveArrangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))))
                // left to existing and no line under or existing under
                || (caveArrangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row, coordinates.Col + 1))
                && (coordinates.Row == 0 || caveArrangement.PatternMap.containsKey(new CoordinatesModel(coordinates.Row - 1, coordinates.Col))));
    }

    @Override
    public int getItemCount() {
        return nbRows * nbCols;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                // Existing pattern
                view = layoutInflater.inflate(R.layout.item_pattern, parent, false);
                setItemDimensions(view);
                return CaveArrangementViewHolder.newInstance(view);
            case 1:
                // Add new pattern
                view = layoutInflater.inflate(R.layout.item_add_pattern, parent, false);
                setItemDimensions(view);
                return AddPatternViewHolder.newInstance(view);
            default:
                view = layoutInflater.inflate(R.layout.item_no_pattern, parent, false);
                setItemDimensions(view);
                return NoPatternViewHolder.newInstance(view);
        }
    }

    private void setItemDimensions(View view) {
        itemWidth = totalWidth / nbCols;
        view.setMinimumWidth(itemWidth);
        view.setMinimumHeight(itemWidth);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CoordinatesModel coordinates = getCoordinateByPosition(CoordinatesManager.getRowFromPosition(position, getItemCount()), CoordinatesManager.getColFromPosition(position));

        if (caveArrangement.PatternMap.containsKey(coordinates)) {
            PatternModelWithBottles patternWithBottles = caveArrangement.PatternMap.get(coordinates);
            if (patternWithBottles == null) {
                onPatternLoaded();
                return;
            }

            CaveArrangementViewHolder holder = (CaveArrangementViewHolder) viewHolder;
            int numberRowsGridLayout = patternWithBottles.getNumberRowsGridLayout();
            if (numberRowsGridLayout <= 0) {
                onPatternLoaded();
                return;
            }

            int numberColumnsGridLayout = patternWithBottles.getNumberColumnsGridLayout();
            if (isIndividualPlacesClickable) {
                CreateCaveDetailArrangementTask createCaveDetailArrangementTask =
                        new CreateCaveDetailArrangementTask(this, holder, detailActivity, patternWithBottles, numberRowsGridLayout, numberColumnsGridLayout,
                                itemWidth, coordinates, BottleIdInHighlight, caveArrangement, onValueChangedListener);
                createCaveDetailArrangementTask.execute();
            } else {
                CreateCaveEditArrangementTask createCaveEditArrangementTask =
                        new CreateCaveEditArrangementTask(this, holder, editActivity, patternWithBottles, numberRowsGridLayout, numberColumnsGridLayout,
                                itemWidth, coordinates, listener);
                createCaveEditArrangementTask.execute();
            }
        } else if (!isIndividualPlacesClickable && isAddPattern(coordinates)) {
            AddPatternViewHolder holder = (AddPatternViewHolder) viewHolder;
            Picasso.with(editActivity).load(R.drawable.add).resize(itemWidth / 2, itemWidth / 2).into(holder.getImageView());
            holder.setOnItemClickListener(listener, coordinates);
            onPatternLoaded();
        } else {
            onPatternLoaded();
        }
    }

    private int nbPatternsLeftToLoad;

    public void onPatternLoaded() {
        nbPatternsLeftToLoad--;
        if (nbPatternsLeftToLoad == 0) {
            if (editActivity != null) {
                editActivity.onCaveArrangementLoaded();
            }
            if (detailActivity != null) {
                detailActivity.onCaveArrangementLoaded();
            }
        }
    }

    private CoordinatesModel getCoordinateByPosition(int rowPosition, int colPosition) {
        return new CoordinatesModel(rowPosition / nbCols, colPosition % nbCols);
    }

    public void updatePositions(RecyclerView recyclerView, Map<CoordinatesModel, List<CoordinatesModel>> coordinatesToUpdate) {
        for (CoordinatesModel patternCoordinates : coordinatesToUpdate.keySet()) {
            int position = CoordinatesManager.getPositionFromCoordinates(patternCoordinates.Row, patternCoordinates.Col, nbRows, nbCols);
            RecyclerView.ViewHolder viewHolder = getViewHolder(recyclerView, position);
            CaveArrangementViewHolder holder = (CaveArrangementViewHolder) viewHolder;
            PatternAdapter adapter = holder.getPatternAdapter();
            adapter.updatePositions(coordinatesToUpdate.get(patternCoordinates));
        }
    }

    private RecyclerView.ViewHolder getViewHolder(RecyclerView recyclerView, int position) {
        return recyclerView.findViewHolderForAdapterPosition(position);
    }
}