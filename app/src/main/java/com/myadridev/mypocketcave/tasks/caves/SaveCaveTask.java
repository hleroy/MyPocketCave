package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;

import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.v1.CaveModel;

public class SaveCaveTask extends AsyncTask<CaveModel, Void, Integer> {

    private AbstractCaveEditActivity caveEditActivity;
    private boolean isAddCave;

    public SaveCaveTask(AbstractCaveEditActivity caveEditActivity, boolean isAddCave) {
        this.caveEditActivity = caveEditActivity;
        this.isAddCave = isAddCave;
    }

    @Override
    protected void onPreExecute() {
        caveEditActivity.IsSaving = true;
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
