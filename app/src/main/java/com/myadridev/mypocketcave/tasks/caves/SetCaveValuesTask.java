package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;

public class SetCaveValuesTask extends AsyncTask<Void, Void, Void> {

    private AbstractCaveEditActivity caveEditActivity;
    private CoordinatorLayout coordinatorLayout;

    public SetCaveValuesTask(AbstractCaveEditActivity caveEditActivity, CoordinatorLayout coordinatorLayout) {
        this.caveEditActivity = caveEditActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        SnackbarHelper.displayInfoSnackbar(caveEditActivity, coordinatorLayout, R.string.ongoig_cave_save, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        caveEditActivity.setValues();
        return null;
    }

    @Override
    protected void onPostExecute(Void value) {
        caveEditActivity.saveCave();
    }
}
