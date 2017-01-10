package com.myadridev.mypocketcave.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnBottlePlacedClickListener;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

import java.util.List;

public class PlaceBottleAlertDialog extends AlertDialog {

    public PlaceBottleAlertDialog(Activity activity, final CoordinatesModel patternCoordinates, final CoordinatesModel coordinates,
                                  final List<OnBottlePlacedClickListener> onBottlePlacedClickListeners) {
        super(activity);

        setTitle(R.string.title_place_bottle);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_bottle_to_cave, null);
        setView(dialogView);

        TextView noBottlesView = (TextView) dialogView.findViewById(R.id.alert_add_bottle_no_bottles_label);
        RecyclerView bottlesRecyclerView = (RecyclerView) dialogView.findViewById(R.id.alert_add_bottle_bottles_recyclerview);

        List<BottleModel> nonPlacedBottles = BottleManager.getNonPlacedBottles();
        final int[] bottleIdToPlace = {-1};
        if (nonPlacedBottles.isEmpty()) {
            noBottlesView.setVisibility(View.VISIBLE);
            bottlesRecyclerView.setVisibility(View.GONE);
        } else {
            noBottlesView.setVisibility(View.GONE);
            bottlesRecyclerView.setVisibility(View.VISIBLE);

            bottlesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            PlaceBottlesAdapter bottlesAdapter = new PlaceBottlesAdapter(activity, nonPlacedBottles);
            bottlesAdapter.addOnBottleClickListener((int bottleId) -> {
                bottleIdToPlace[0] = bottleId;
                dismiss();
            });
            bottlesRecyclerView.setAdapter(bottlesAdapter);
        }
        setOnDismissListener((DialogInterface dialog) -> {
            int bottleId = bottleIdToPlace[0];
            if (bottleId != -1) {
                if (onBottlePlacedClickListeners != null) {
                    for (OnBottlePlacedClickListener onBottlePlacedClickListener : onBottlePlacedClickListeners) {
                        onBottlePlacedClickListener.onBottlePlaced(patternCoordinates, coordinates, bottleId);
                    }
                }
            }
        });
    }
}