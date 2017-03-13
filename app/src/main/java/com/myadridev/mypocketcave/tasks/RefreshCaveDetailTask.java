package com.myadridev.mypocketcave.tasks;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.models.CaveModel;

public class RefreshCaveDetailTask extends AsyncTask<Integer, Void, CaveModel> {

    private CaveDetailActivity caveDetailActivity;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    public RefreshCaveDetailTask(CaveDetailActivity caveDetailActivity, CoordinatorLayout coordinatorLayout) {
        this.caveDetailActivity = caveDetailActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        snackbar = SnackbarHelper.displayInfoSnackbar(caveDetailActivity, coordinatorLayout, R.string.ongoig_cave_refresh, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected CaveModel doInBackground(Integer... params) {
        int caveId = params[0];

        return CaveManager.getCave(caveDetailActivity, caveId);
    }

    @Override
    protected void onPostExecute(CaveModel cave) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        caveDetailActivity.onRefreshCaveSucceed(cave);
    }
}
