package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveModel;

public class SaveCaveTask extends AsyncTask<CaveModel, Void, Integer> {

    private AbstractCaveEditActivity caveEditActivity;
    private CoordinatorLayout coordinatorLayout;
    private boolean isAddCave;

    public SaveCaveTask(AbstractCaveEditActivity caveEditActivity, CoordinatorLayout coordinatorLayout, boolean isAddCave) {
        this.caveEditActivity = caveEditActivity;
        this.coordinatorLayout = coordinatorLayout;
        this.isAddCave = isAddCave;
    }

    @Override
    protected void onPreExecute() {
        caveEditActivity.IsSaving = true;
        SnackbarHelper.displayInfoSnackbar(caveEditActivity, coordinatorLayout, R.string.ongoig_cave_save, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Integer doInBackground(CaveModel... params) {
        CaveModel cave = params[0];

        if (isAddCave) {
            cave.Id = CaveManager.addCave(caveEditActivity, cave);
        } else {
            CaveManager.editCave(caveEditActivity, cave);
        }

        return cave.Id;
    }

    @Override
    protected void onPostExecute(Integer caveId) {
        caveEditActivity.IsSaving = false;
        NavigationManager.navigateToCaveDetail(caveEditActivity, caveId);
        caveEditActivity.finish();
    }
}
