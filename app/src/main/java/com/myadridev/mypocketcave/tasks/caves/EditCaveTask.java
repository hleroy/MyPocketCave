package com.myadridev.mypocketcave.tasks.caves;

import android.content.Context;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

public class EditCaveTask extends AsyncTask<CaveModelV2, Void, Void> {

    private final Context context;

    public EditCaveTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(CaveModelV2... params) {
        CaveModelV2 cave = params[0];

        CaveManager.editCave(context, cave);

        return null;
    }
}
