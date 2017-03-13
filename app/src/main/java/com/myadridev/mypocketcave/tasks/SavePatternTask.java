package com.myadridev.mypocketcave.tasks;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.PatternCreateActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.PatternModel;

public class SavePatternTask extends AsyncTask<PatternModel, Void, Integer> {

    private PatternCreateActivity patternCreateActivity;
    private CoordinatorLayout coordinatorLayout;

    public SavePatternTask(PatternCreateActivity patternCreateActivity, CoordinatorLayout coordinatorLayout) {
        this.patternCreateActivity = patternCreateActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        SnackbarHelper.displayInfoSnackbar(patternCreateActivity, coordinatorLayout, R.string.ongoig_pattern_save, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Integer doInBackground(PatternModel... params) {
        PatternModel pattern = params[0];

        PatternManager.addPattern(patternCreateActivity, pattern);

        return pattern.Id;
    }

    @Override
    protected void onPostExecute(Integer patternId) {
        patternCreateActivity.onSaveSucceed(patternId);
    }
}
