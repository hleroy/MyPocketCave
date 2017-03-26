package com.myadridev.mypocketcave.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleDrunkClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleUnplacedClickListener;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class SeeBottleAlertDialog extends AlertDialog {

    private final int maxBottlesToUnplace;

    public SeeBottleAlertDialog(Activity activity, int bottleId, final CoordinatesModel patternCoordinates, final CoordinatesModel coordinates,
                                final OnBottleDrunkClickListener onBottleDrunkClickListener, final OnBottleUnplacedClickListener onBottleUnplacedClickListener,
                                int bottleIdInHighlight, final OnBottleClickListener onBottleHighlightClickListener, int maxBottlesToUnplace) {
        super(activity);
        this.maxBottlesToUnplace = maxBottlesToUnplace;

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_see_bottle_in_cave, null);
        setView(dialogView);

        BottleViewHolder bottleViewHolder = BottleViewHolder.newInstance(dialogView);
        TextView seeDetailView = (TextView) dialogView.findViewById(R.id.alert_see_bottle_see_detail);
        TextView drinkView = (TextView) dialogView.findViewById(R.id.alert_drink_bottle);
        TextView unplaceView = (TextView) dialogView.findViewById(R.id.alert_see_bottle_unplace_bottle);
        TextView highlightView = (TextView) dialogView.findViewById(R.id.alert_see_bottle_highlight_bottle);

        BottleModel bottle = BottleManager.getBottle(bottleId);
        if (bottle == null) {
            // should not occur
            dismiss();
        } else {
            bottleViewHolder.setLabelViewText(bottle.Domain + " - " + bottle.Name);
            bottleViewHolder.setMillesimeViewText(bottle.Millesime == 0 ? "-" : String.valueOf(bottle.Millesime));
            bottleViewHolder.setStockLabelViewText(activity.getString(R.string.bottles_stock, bottle.Stock));
            bottleViewHolder.setRating(bottle.Rating);
            bottleViewHolder.setPriceRating(bottle.PriceRating);
            bottleViewHolder.resetHighlight();
            int wineColorDrawableId = bottle.WineColor.DrawableResourceId;
            bottleViewHolder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(activity, wineColorDrawableId) : null);

            seeDetailView.setOnClickListener((View v) -> NavigationManager.navigateToBottleDetail(activity, bottleId));

            drinkView.setOnClickListener((View v) -> {
                if (patternCoordinates == null) {
                    AlertDialog.Builder numberRemindersDialogBuilder = new AlertDialog.Builder(activity);

                    View numberPickerView = inflater.inflate(R.layout.number_picker_dialog, null);
                    final NumberPicker numberPicker = (NumberPicker) numberPickerView.findViewById(R.id.number_picker);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(Math.min(bottle.NumberPlaced, this.maxBottlesToUnplace));
                    numberPicker.setValue(1);
                    numberPicker.setWrapSelectorWheel(false);

                    numberRemindersDialogBuilder.setTitle(activity.getString(R.string.place_bottle_quantity));
                    numberRemindersDialogBuilder.setView(numberPickerView);
                    numberRemindersDialogBuilder.setPositiveButton(activity.getString(R.string.global_ok),
                            (DialogInterface dialog, int index) -> {
                                if (onBottleDrunkClickListener != null) {
                                    onBottleDrunkClickListener.onBottleDrunk(bottleId, numberPicker.getValue(), patternCoordinates, coordinates);
                                }
                                dialog.dismiss();
                                dismiss();
                            });
                    numberRemindersDialogBuilder.setNegativeButton(activity.getString(R.string.global_cancel), (DialogInterface dialog, int index) -> {
                        dialog.dismiss();
                        dismiss();
                    });
                    numberRemindersDialogBuilder.setOnDismissListener((DialogInterface dialog) -> dismiss());

                    final AlertDialog numberRemindersDialog = numberRemindersDialogBuilder.create();
                    numberRemindersDialog.show();
                } else {
                    final AlertDialog drinkAlertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle(R.string.title_drink)
                            .setMessage(R.string.message_drink)
                            .setPositiveButton(R.string.drink_ok, (DialogInterface dialog, int which) -> {
                                if (onBottleDrunkClickListener != null) {
                                    onBottleDrunkClickListener.onBottleDrunk(bottleId, 1, patternCoordinates, coordinates);
                                }
                                dialog.dismiss();
                                dismiss();
                            })
                            .setNegativeButton(R.string.drink_no, (DialogInterface dialog, int which) -> {
                                dialog.dismiss();
                                dismiss();
                            })
                            .create();
                    drinkAlertDialogBuilder.show();
                }
            });

            unplaceView.setOnClickListener((View v) -> {
                if (patternCoordinates == null) {
                    AlertDialog.Builder numberRemindersDialogBuilder = new AlertDialog.Builder(activity);

                    View numberPickerView = inflater.inflate(R.layout.number_picker_dialog, null);
                    final NumberPicker numberPicker = (NumberPicker) numberPickerView.findViewById(R.id.number_picker);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(Math.min(bottle.NumberPlaced, this.maxBottlesToUnplace));
                    numberPicker.setValue(1);
                    numberPicker.setWrapSelectorWheel(false);

                    numberRemindersDialogBuilder.setTitle(activity.getString(R.string.place_bottle_quantity));
                    numberRemindersDialogBuilder.setView(numberPickerView);
                    numberRemindersDialogBuilder.setPositiveButton(activity.getString(R.string.global_ok),
                            (DialogInterface dialog, int index) -> {
                                if (onBottleUnplacedClickListener != null) {
                                    onBottleUnplacedClickListener.onBottleUnplaced(bottleId, numberPicker.getValue(), patternCoordinates, coordinates);
                                }
                                dialog.dismiss();
                                dismiss();
                            });
                    numberRemindersDialogBuilder.setNegativeButton(activity.getString(R.string.global_cancel), (DialogInterface dialog, int index) -> {
                        dialog.dismiss();
                        dismiss();
                    });
                    numberRemindersDialogBuilder.setOnDismissListener((DialogInterface dialog) -> dismiss());

                    final AlertDialog numberRemindersDialog = numberRemindersDialogBuilder.create();
                    numberRemindersDialog.show();
                } else {
                    final AlertDialog drinkAlertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle(R.string.title_unplace)
                            .setMessage(R.string.message_unplace)
                            .setPositiveButton(R.string.unplace_ok, (DialogInterface dialog, int which) -> {
                                if (onBottleDrunkClickListener != null) {
                                    onBottleUnplacedClickListener.onBottleUnplaced(bottleId, 1, patternCoordinates, coordinates);
                                }
                                dialog.dismiss();
                                dismiss();
                            })
                            .setNegativeButton(R.string.unplace_no, (DialogInterface dialog, int which) -> {
                                dialog.dismiss();
                                dismiss();
                            })
                            .create();
                    drinkAlertDialogBuilder.show();
                }
            });

            if (bottleIdInHighlight == bottleId) {
                highlightView.setVisibility(View.GONE);
            } else {
                highlightView.setVisibility(View.VISIBLE);
                highlightView.setOnClickListener((View v) -> {
                    if (onBottleHighlightClickListener != null) {
                        onBottleHighlightClickListener.onItemClick(bottleId);
                    }
                    dismiss();
                });
            }
        }
    }
}
