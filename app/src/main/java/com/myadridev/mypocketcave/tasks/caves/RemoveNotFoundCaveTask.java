package com.myadridev.mypocketcave.tasks.caves;

import android.content.DialogInterface;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.managers.CaveManager;

public class RemoveNotFoundCaveTask extends AsyncTask<Integer, Void, Void> {

    private final CaveDetailActivity caveDetailActivity;
    private final DialogInterface dialog;

    public RemoveNotFoundCaveTask(CaveDetailActivity caveDetailActivity, DialogInterface dialog) {
        this.caveDetailActivity = caveDetailActivity;
        this.dialog = dialog;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int caveId = params[0];

        CaveManager.removeNotFoundCave(caveDetailActivity, caveId);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        caveDetailActivity.onRemovePostExecute();
    }
}
