package com.myadridev.mypocketcave.tasks.caves;

import android.content.Context;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.models.CaveModel;

public class EditCaveTask extends AsyncTask<CaveModel, Void, Void> {

    private Context context;

    public EditCaveTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(CaveModel... params) {
        CaveModel cave = params[0];

        CaveManager.editCave(context, cave);

        return null;
    }
}
