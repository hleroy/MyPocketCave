package com.myadridev.mypocketcave.dialogs;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.SeeCavesAdapter;
import com.myadridev.mypocketcave.layoutManagers.GridAutofitLayoutManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveLightModel;

import java.util.List;

public class SeeCavesAlertDialog extends AlertDialog {

    public SeeCavesAlertDialog(Activity activity, int bottleId) {
        super(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_see_caves, null);
        setView(dialogView);

        TextView noCavesView = (TextView) dialogView.findViewById(R.id.alert_see_caves_no_caves_label);
        RecyclerView cavesRecyclerView = (RecyclerView) dialogView.findViewById(R.id.alert_see_caves_caves_recyclerview);

        List<CaveLightModel> caves = CaveManager.getCavesWithBottle(bottleId);
        if (caves.isEmpty()) {
            noCavesView.setVisibility(View.VISIBLE);
            cavesRecyclerView.setVisibility(View.GONE);
        } else {
            noCavesView.setVisibility(View.GONE);
            cavesRecyclerView.setVisibility(View.VISIBLE);

            cavesRecyclerView.setLayoutManager(new GridAutofitLayoutManager(activity, (int) activity.getResources().getDimension(R.dimen.cave_image_size_large)));
            SeeCavesAdapter cavesAdapter = new SeeCavesAdapter(activity, caves);
            cavesAdapter.addOnCaveClickListener((int caveId) -> NavigationManager.navigateToCaveDetail(activity, caveId, bottleId));
            cavesRecyclerView.setAdapter(cavesAdapter);
        }
    }
}
