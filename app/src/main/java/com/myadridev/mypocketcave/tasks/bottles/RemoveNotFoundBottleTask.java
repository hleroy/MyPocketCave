package com.myadridev.mypocketcave.tasks.bottles;

import android.content.DialogInterface;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.activities.BottleDetailActivity;
import com.myadridev.mypocketcave.managers.BottleManager;

public class RemoveNotFoundBottleTask extends AsyncTask<Integer, Void, Void> {

    private final BottleDetailActivity bottleDetailActivity;
    private final DialogInterface dialog;

    public RemoveNotFoundBottleTask(BottleDetailActivity bottleDetailActivity, DialogInterface dialog) {
        this.bottleDetailActivity = bottleDetailActivity;
        this.dialog = dialog;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int caveId = params[0];

        BottleManager.removeNotFoundBottle(bottleDetailActivity, caveId);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        bottleDetailActivity.onRemovePostExecute();
    }
}
