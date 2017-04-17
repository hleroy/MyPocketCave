package com.myadridev.mypocketcave.tasks.bottles;

import android.content.Context;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.managers.BottleManager;

public class DrinkBottleTask extends AsyncTask<Integer, Void, Void> {

    private final Context context;

    public DrinkBottleTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int bottleId = params[0];

        BottleManager.drinkBottle(context, bottleId);

        return null;
    }
}
