package com.myadridev.mypocketcave.tasks.bottles;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractBottleEditActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;

public class RefreshBottleEditTask extends AsyncTask<Integer, Void, Void> {

    private AbstractBottleEditActivity bottleEditActivity;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    public RefreshBottleEditTask(AbstractBottleEditActivity bottleEditActivity, CoordinatorLayout coordinatorLayout) {
        this.bottleEditActivity = bottleEditActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        snackbar = SnackbarHelper.displayInfoSnackbar(bottleEditActivity, coordinatorLayout, R.string.ongoig_bottle_refresh, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int bottleId = params[0];

        if (bottleId == 0) {
            bottleEditActivity.bottle = new BottleModel();
        } else {
            bottleEditActivity.bottle = new BottleModel(BottleManager.getBottle(bottleId));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void value) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        bottleEditActivity.setLayoutValues();
    }
}
