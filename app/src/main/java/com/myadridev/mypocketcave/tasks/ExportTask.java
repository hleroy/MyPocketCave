package com.myadridev.mypocketcave.tasks;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SyncActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.SyncManager;

public class ExportTask extends AsyncTask<String, Void, String> {

    private SyncActivity syncActivity;
    private CoordinatorLayout coordinatorLayout;

    public ExportTask(SyncActivity syncActivity, CoordinatorLayout coordinatorLayout) {
        this.syncActivity = syncActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        syncActivity.IsExportOngoing = true;
        syncActivity.SetSyncButtonEnabled(false);
        SnackbarHelper.displayInfoSnackbar(syncActivity, coordinatorLayout, R.string.ongoig_export, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected String doInBackground(String... params) {
        String exportLocation = params[0];
        String extension = params[1];

        return SyncManager.exportData(syncActivity, exportLocation, extension);
    }

    @Override
    protected void onPostExecute(String exportedFileName) {
        if (exportedFileName != null && !exportedFileName.isEmpty()) {
            SnackbarHelper.displaySuccessSnackbar(syncActivity, coordinatorLayout, syncActivity.getString(R.string.success_export, exportedFileName), R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        } else {
            SnackbarHelper.displayErrorSnackbar(syncActivity, coordinatorLayout, R.string.error_export, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        }
        syncActivity.SetSyncButtonEnabled(true);
        syncActivity.IsExportOngoing = false;
    }
}
