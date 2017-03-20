package com.myadridev.mypocketcave.tasks.caves;

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

    public RefreshCaveDetailTask(CaveDetailActivity caveDetailActivity) {
        this.caveDetailActivity = caveDetailActivity;
    }

    @Override
    protected CaveModel doInBackground(Integer... params) {
        int caveId = params[0];

        return CaveManager.getCave(caveDetailActivity, caveId);
    }

    @Override
    protected void onPostExecute(CaveModel cave) {
        caveDetailActivity.onRefreshCaveSucceed(cave);
    }
}
