package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;

import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

public class SaveCaveTask extends AsyncTask<CaveModelV2, Void, Integer> {

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
    protected Integer doInBackground(CaveModelV2... params) {
        CaveModelV2 cave = params[0];

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
