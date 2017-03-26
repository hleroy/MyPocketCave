package com.myadridev.mypocketcave.tasks.pattern;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.AbstractPatternEditActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.PatternModel;

public class SavePatternTask extends AsyncTask<PatternModel, Void, Integer> {

    private AbstractPatternEditActivity patternEditActivity;
    private CoordinatorLayout coordinatorLayout;

    public SavePatternTask(AbstractPatternEditActivity patternEditActivity, CoordinatorLayout coordinatorLayout) {
        this.patternEditActivity = patternEditActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        SnackbarHelper.displayInfoSnackbar(patternEditActivity, coordinatorLayout, R.string.ongoig_pattern_save, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected Integer doInBackground(PatternModel... params) {
        PatternModel pattern = params[0];

        pattern.Id = PatternManager.addPattern(patternEditActivity, pattern);
        PatternManager.setLastUsedPattern(patternEditActivity, pattern.Id);

        return pattern.Id;
    }

    @Override
    protected void onPostExecute(Integer patternId) {
        patternEditActivity.onSaveSucceed(patternId);
    }
}
