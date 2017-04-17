package com.myadridev.mypocketcave.tasks.sync;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SyncActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.SyncManager;

public class ImportTask extends AsyncTask<String, Void, Integer> {

    private final SyncActivity syncActivity;
    private final CoordinatorLayout coordinatorLayout;

    public ImportTask(SyncActivity syncActivity, CoordinatorLayout coordinatorLayout) {
        this.syncActivity = syncActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        syncActivity.IsImportOngoing = true;
        syncActivity.SetSyncButtonEnabled(false);
        SnackbarHelper.displayInfoSnackbar(syncActivity, coordinatorLayout, R.string.ongoig_import, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Integer doInBackground(String... params) {
        String importLocation = params[0];

        return SyncManager.importData(syncActivity, importLocation);
    }

    @Override
    protected void onPostExecute(Integer errorResourceId) {
        if (errorResourceId > -1) {
            SnackbarHelper.displayErrorSnackbar(syncActivity, coordinatorLayout, errorResourceId, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        } else {
            SnackbarHelper.displaySuccessSnackbar(syncActivity, coordinatorLayout, R.string.success_import, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        }
        syncActivity.SetSyncButtonEnabled(true);
        syncActivity.IsImportOngoing = false;
    }
}
