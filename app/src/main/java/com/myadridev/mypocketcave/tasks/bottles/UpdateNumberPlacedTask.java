package com.myadridev.mypocketcave.tasks.bottles;

import android.content.Context;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.managers.BottleManager;

public class UpdateNumberPlacedTask extends AsyncTask<Integer, Void, Void> {

    private Context context;

    public UpdateNumberPlacedTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int bottleId = params[0];
        int increment = params[1];

        BottleManager.updateNumberPlaced(context, bottleId, increment);

        return null;
    }
}
