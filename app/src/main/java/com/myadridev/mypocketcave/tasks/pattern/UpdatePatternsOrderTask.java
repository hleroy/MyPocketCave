package com.myadridev.mypocketcave.tasks.pattern;

import android.content.Context;
import android.os.AsyncTask;

import com.myadridev.mypocketcave.managers.PatternManager;

public class UpdatePatternsOrderTask extends AsyncTask<Integer, Void, Void> {

    private Context context;

    public UpdatePatternsOrderTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int patternId = params[0];

        PatternManager.setLastUsedPattern(context, patternId);

        return null;
    }
}
