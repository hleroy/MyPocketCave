package com.myadridev.mypocketcave.tasks.bottles;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractBottleEditActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;

public class SetBottleValuesTask extends AsyncTask<Void, Void, Void> {

    private AbstractBottleEditActivity bottleEditActivity;
    private CoordinatorLayout coordinatorLayout;

    public SetBottleValuesTask(AbstractBottleEditActivity bottleEditActivity, CoordinatorLayout coordinatorLayout) {
        this.bottleEditActivity = bottleEditActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        SnackbarHelper.displayInfoSnackbar(bottleEditActivity, coordinatorLayout, R.string.ongoig_bottle_save, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        bottleEditActivity.setValues();
        return null;
    }

    @Override
    protected void onPostExecute(Void value) {
        bottleEditActivity.saveBottle();
    }
}
