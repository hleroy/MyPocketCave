package com.myadridev.mypocketcave.tasks;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.BottleDetailActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;

public class RefreshBottleDetailTask extends AsyncTask<Integer, Void, BottleModel> {

    private BottleDetailActivity bottleDetailActivity;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    public RefreshBottleDetailTask(BottleDetailActivity bottleDetailActivity, CoordinatorLayout coordinatorLayout) {
        this.bottleDetailActivity = bottleDetailActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        snackbar = SnackbarHelper.displayInfoSnackbar(bottleDetailActivity, coordinatorLayout, R.string.ongoig_bottle_refresh, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected BottleModel doInBackground(Integer... params) {
        int bottleId = params[0];

        return BottleManager.getBottle(bottleId);
    }

    @Override
    protected void onPostExecute(BottleModel bottle) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        bottleDetailActivity.onRefreshBottleSucceed(bottle);
    }
}
