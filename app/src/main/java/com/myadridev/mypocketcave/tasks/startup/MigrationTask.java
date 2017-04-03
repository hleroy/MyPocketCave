package com.myadridev.mypocketcave.tasks.startup;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SplashScreenActivity;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.migration.MigrationManager;

public class MigrationTask extends AsyncTask<Void, Integer, Void> {

    private SplashScreenActivity splashScreenActivity;
    private ImageView splashImageView;

    public MigrationTask(SplashScreenActivity splashScreenActivity, ImageView splashImageView) {
        this.splashScreenActivity = splashScreenActivity;
        this.splashImageView = splashImageView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int step = 0;
        publishProgress(step);

        // Bottles
        MigrationManager.migrateBottles(splashScreenActivity);
        publishProgress(++step);

        // Caves
        MigrationManager.migrateCaves(splashScreenActivity);
        publishProgress(++step);

        // Patterns
        MigrationManager.migratePatterns(splashScreenActivity);
        publishProgress(++step);

        // Cave
        MigrationManager.migrateCave(splashScreenActivity);
        publishProgress(++step);

        MigrationManager.finalizeMigration(splashScreenActivity);

        BottleManager.recomputeNumberPlaced(splashScreenActivity);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int step = values[0];
        switch (step) {
            case 0:
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_1));
                break;
            case 1:
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_2));
                break;
            case 2:
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_3));
                break;
            case 3:
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_4));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPostExecute(Void value) {
        NavigationManager.navigateToMain(splashScreenActivity);
        splashScreenActivity.finish();
    }
}
