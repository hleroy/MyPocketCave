package com.myadridev.mypocketcave.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.BottlesAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.listeners.OnBottlePlacedClickListener;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

import java.util.List;

public class PlaceBottleAlertDialog extends AlertDialog {
    private final Activity activity;

    public PlaceBottleAlertDialog(Activity activity, final CoordinatesModel patternCoordinates, final CoordinatesModel coordinates,
                                  final List<OnBottlePlacedClickListener> onBottlePlacedClickListeners, int maxBottleToPlace) {
        super(activity);

        this.activity = activity;
        setTitle(R.string.title_place_bottle);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_bottle_to_cave, null);
        setView(dialogView);

        TextView noBottlesView = (TextView) dialogView.findViewById(R.id.alert_add_bottle_no_bottles_label);
        RecyclerView bottlesRecyclerView = (RecyclerView) dialogView.findViewById(R.id.alert_add_bottle_bottles_recyclerview);

        List<BottleModel> nonPlacedBottles = BottleManager.getNonPlacedBottles();
        final int[] bottleIdToPlace = {-1};
        final int[] quantityToPlace = {1};
        if (nonPlacedBottles.isEmpty()) {
            noBottlesView.setVisibility(View.VISIBLE);
            bottlesRecyclerView.setVisibility(View.GONE);
        } else {
            noBottlesView.setVisibility(View.GONE);
            bottlesRecyclerView.setVisibility(View.VISIBLE);

            bottlesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            BottlesAdapter bottlesAdapter = new BottlesAdapter(activity, nonPlacedBottles, false, 0);
            bottlesAdapter.setOnBottleBindListener(this::setHolderPropertiesFromBottle);
            bottlesAdapter.addOnBottleClickListener((int bottleId) -> {
                if (patternCoordinates == null) {
                    AlertDialog.Builder numberRemindersDialogBuilder = new AlertDialog.Builder(activity);

                    View numberPickerView = inflater.inflate(R.layout.number_picker_dialog, null);
                    final NumberPicker numberPicker = (NumberPicker) numberPickerView.findViewById(R.id.number_picker);
                    numberPicker.setMinValue(1);
                    BottleModel bottle = BottleManager.getBottle(bottleId);
                    numberPicker.setMaxValue(Math.min(maxBottleToPlace, bottle.Stock - bottle.NumberPlaced));
                    numberPicker.setValue(1);
                    numberPicker.setWrapSelectorWheel(false);

                    numberRemindersDialogBuilder.setTitle(activity.getString(R.string.place_bottle_quantity));
                    numberRemindersDialogBuilder.setView(numberPickerView);
                    numberRemindersDialogBuilder.setPositiveButton(activity.getString(R.string.error_ok),
                            (DialogInterface dialog, int index) -> {
                                bottleIdToPlace[0] = bottleId;
                                quantityToPlace[0] = numberPicker.getValue();
                                dialog.dismiss();
                            });
                    numberRemindersDialogBuilder.setNegativeButton(activity.getString(R.string.global_cancel), (DialogInterface dialog, int index) -> dialog.dismiss());
                    numberRemindersDialogBuilder.setOnDismissListener((DialogInterface dialog) -> dismiss());

                    final AlertDialog numberRemindersDialog = numberRemindersDialogBuilder.create();
                    numberRemindersDialog.show();
                } else {
                    bottleIdToPlace[0] = bottleId;
                    quantityToPlace[0] = 1;
                    dismiss();
                }
            });
            bottlesRecyclerView.setAdapter(bottlesAdapter);
        }
        setOnDismissListener(getOnDismissListener(onBottlePlacedClickListeners, bottleIdToPlace, quantityToPlace, patternCoordinates, coordinates));
    }

    @NonNull
    private OnDismissListener getOnDismissListener(List<OnBottlePlacedClickListener> onBottlePlacedClickListeners, int[] bottleIdToPlace, int[] quantityToPlace,
                                                   CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        return (DialogInterface dialog) -> {
            if (bottleIdToPlace[0] != -1) {
                if (onBottlePlacedClickListeners != null) {
                    for (OnBottlePlacedClickListener onBottlePlacedClickListener : onBottlePlacedClickListeners) {
                        onBottlePlacedClickListener.onBottlePlaced(bottleIdToPlace[0], quantityToPlace[0], patternCoordinates, coordinates);
                    }
                }
            }
        };
    }

    private void setHolderPropertiesFromBottle(BottleViewHolder holder, BottleModel bottle) {
        holder.setLabelViewText(bottle.Domain + " - " + bottle.Name);
        holder.setMillesimeViewText(bottle.Millesime == 0 ? "-" : String.valueOf(bottle.Millesime));
        holder.setStockLabelViewText(activity.getString(R.string.bottles_to_place, bottle.Stock - bottle.NumberPlaced));
        int wineColorDrawableId = bottle.WineColor.DrawableResourceId;
        holder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(activity, wineColorDrawableId) : null);
    }
}
