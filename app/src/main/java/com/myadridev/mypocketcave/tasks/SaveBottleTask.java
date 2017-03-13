package com.myadridev.mypocketcave.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SyncActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.SyncManager;
import com.myadridev.mypocketcave.models.BottleModel;

public class SaveBottleTask extends AsyncTask<BottleModel, Void, Integer> {

    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private boolean isAddBottle;

    public SaveBottleTask(Context context, CoordinatorLayout coordinatorLayout, boolean isAddBottle) {
        this.context = context;
        this.coordinatorLayout = coordinatorLayout;
        this.isAddBottle = isAddBottle;
    }

    @Override
    protected void onPreExecute() {
        SnackbarHelper.displayInfoSnackbar(context, coordinatorLayout, R.string.ongoig_bottle_save, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Integer doInBackground(BottleModel... params) {
        BottleModel bottle = params[0];

        if (isAddBottle) {
            bottle.Id = BottleManager.addBottle(context, bottle);
        } else {
            BottleManager.editBottle(context, bottle);
        }

        return bottle.Id;
    }

    @Override
    protected void onPostExecute(Integer bottleId) {
        NavigationManager.navigateToBottleDetail(context, bottleId);
    }
}
