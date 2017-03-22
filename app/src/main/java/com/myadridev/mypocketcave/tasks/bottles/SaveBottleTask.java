package com.myadridev.mypocketcave.tasks.bottles;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;

import com.myadridev.mypocketcave.activities.AbstractBottleEditActivity;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

public class SaveBottleTask extends AsyncTask<BottleModel, Void, Integer> {

    private AbstractBottleEditActivity bottleEditActivity;
    private CoordinatorLayout coordinatorLayout;
    private boolean isAddBottle;

    public SaveBottleTask(AbstractBottleEditActivity bottleEditActivity, boolean isAddBottle) {
        this.bottleEditActivity = bottleEditActivity;
        this.isAddBottle = isAddBottle;
    }

    @Override
    protected void onPreExecute() {
        bottleEditActivity.IsSaving = true;
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
