package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

public class RefreshCaveEditTask extends AsyncTask<Integer, Void, Void> {

    private final AbstractCaveEditActivity caveEditActivity;
    private final CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    public RefreshCaveEditTask(AbstractCaveEditActivity caveEditActivity, CoordinatorLayout coordinatorLayout) {
        this.caveEditActivity = caveEditActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        snackbar = SnackbarHelper.displayInfoSnackbar(caveEditActivity, coordinatorLayout, R.string.ongoig_cave_refresh, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int caveId = params[0];

        if (caveId == 0) {
            caveEditActivity.cave = new CaveModelV2();
            caveEditActivity.oldCave = null;
        } else {
            caveEditActivity.oldCave = new CaveModelV2(CaveManager.getCave(caveId));
            caveEditActivity.cave = new CaveModelV2(caveEditActivity.oldCave);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void value) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        caveEditActivity.setLayoutValues();
    }
}
