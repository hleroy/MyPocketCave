package com.myadridev.mypocketcave.tasks.bottles;

import android.content.Context;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.managers.BottleManager;

public class PlaceBottleTask extends AsyncTask<Integer, Void, Void> {

    private Context context;

    public PlaceBottleTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int bottleId = params[0];

        BottleManager.placeBottle(context, bottleId);

        return null;
    }
}
