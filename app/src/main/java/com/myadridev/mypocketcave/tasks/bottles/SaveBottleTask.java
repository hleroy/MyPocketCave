package com.myadridev.mypocketcave.tasks.bottles;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractBottleEditActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

public class SaveBottleTask extends AsyncTask<BottleModel, Void, Integer> {

    private AbstractBottleEditActivity bottleEditActivity;
    private CoordinatorLayout coordinatorLayout;
    private boolean isAddBottle;

    public SaveBottleTask(AbstractBottleEditActivity bottleEditActivity, CoordinatorLayout coordinatorLayout, boolean isAddBottle) {
        this.bottleEditActivity = bottleEditActivity;
        this.coordinatorLayout = coordinatorLayout;
        this.isAddBottle = isAddBottle;
    }

    @Override
    protected void onPreExecute() {
        bottleEditActivity.IsSaving = true;
        SnackbarHelper.displayInfoSnackbar(bottleEditActivity, coordinatorLayout, R.string.ongoig_bottle_save, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Integer doInBackground(BottleModel... params) {
        BottleModel bottle = params[0];

        if (isAddBottle) {
            bottle.Id = BottleManager.addBottle(bottleEditActivity, bottle);
        } else {
            BottleManager.editBottle(bottleEditActivity, bottle);
        }

        return bottle.Id;
    }

    @Override
    protected void onPostExecute(Integer bottleId) {
        bottleEditActivity.IsSaving = false;
        NavigationManager.navigateToBottleDetail(bottleEditActivity, bottleId);
        bottleEditActivity.finish();
    }
}
