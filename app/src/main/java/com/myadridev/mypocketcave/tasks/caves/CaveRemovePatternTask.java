package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;

import com.myadridev.mypocketcave.models.CoordinatesModel;

public class CaveRemovePatternTask extends AsyncTask<CoordinatesModel, Void, Void> {

    public CaveRemovePatternTask() {
    }

    @Override
    protected Void doInBackground(CoordinatesModel... params) {
        CoordinatesModel coordinates = params[0];
        return null;
    }

    @Override
    protected void onPostExecute(Void value) {
    }
}
