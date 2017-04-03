package com.myadridev.mypocketcave.tasks.suggest;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SuggestBottleResultActivity;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.v1.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.v1.SuggestBottleResultModel;

import java.util.List;

public class GetSuggestBottlesTask extends AsyncTask<SuggestBottleCriteria, Void, List<SuggestBottleResultModel>> {

    private SuggestBottleResultActivity suggestBottleResultActivity;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    public GetSuggestBottlesTask(SuggestBottleResultActivity suggestBottleResultActivity, CoordinatorLayout coordinatorLayout) {
        this.suggestBottleResultActivity = suggestBottleResultActivity;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        snackbar = SnackbarHelper.displayInfoSnackbar(suggestBottleResultActivity, coordinatorLayout, R.string.ongoig_bottles_suggest, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected List<SuggestBottleResultModel> doInBackground(SuggestBottleCriteria... params) {
        SuggestBottleCriteria searchCriteria = params[0];

        return BottleManager.getSuggestBottles(searchCriteria);
    }

    @Override
    protected void onPostExecute(List<SuggestBottleResultModel> allBottles) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        suggestBottleResultActivity.onGetBottlesSucceed(allBottles);
    }
}
