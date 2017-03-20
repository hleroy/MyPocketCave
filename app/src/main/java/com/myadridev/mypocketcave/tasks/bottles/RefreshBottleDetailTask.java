package com.myadridev.mypocketcave.tasks.bottles;

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

    public RefreshBottleDetailTask(BottleDetailActivity bottleDetailActivity) {
        this.bottleDetailActivity = bottleDetailActivity;
    }

    @Override
    protected BottleModel doInBackground(Integer... params) {
        int bottleId = params[0];

        return BottleManager.getBottle(bottleId);
    }

    @Override
    protected void onPostExecute(BottleModel bottle) {
        bottleDetailActivity.onRefreshBottleSucceed(bottle);
    }
}
