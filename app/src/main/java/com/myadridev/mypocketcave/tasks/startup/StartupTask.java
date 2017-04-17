package com.myadridev.mypocketcave.tasks.startup;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SplashScreenActivity;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IBottleStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICaveStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICavesStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IPatternsStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.BottlesSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.CaveSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.CavesSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.PatternsSharedPreferencesManagerV2;

public class StartupTask extends AsyncTask<Void, Integer, Void> {

    private final SplashScreenActivity splashScreenActivity;
    private final ImageView splashImageView;
    private final TextView progressView;

    public StartupTask(SplashScreenActivity splashScreenActivity, ImageView splashImageView, TextView progressView) {
        this.splashScreenActivity = splashScreenActivity;
        this.splashImageView = splashImageView;
        this.progressView = progressView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int step = 0;
        publishProgress(step);

        // Bottles
        BottlesSharedPreferencesManagerV2.init(splashScreenActivity);
        DependencyManager.registerSingleton(IBottleStorageManagerV2.class, BottlesSharedPreferencesManagerV2.Instance);

        publishProgress(++step);

        // Patterns
        PatternsSharedPreferencesManagerV2.init(splashScreenActivity);
        DependencyManager.registerSingleton(IPatternsStorageManagerV2.class, PatternsSharedPreferencesManagerV2.Instance);

        publishProgress(++step);

        // Caves
        CavesSharedPreferencesManagerV2.init(splashScreenActivity);
        DependencyManager.registerSingleton(ICavesStorageManagerV2.class, CavesSharedPreferencesManagerV2.Instance);

        publishProgress(++step);

        // Cave
        CaveSharedPreferencesManagerV2.init(splashScreenActivity);
        DependencyManager.registerSingleton(ICaveStorageManagerV2.class, CaveSharedPreferencesManagerV2.Instance);

        publishProgress(++step);

        BottleManager.recomputeNumberPlaced(splashScreenActivity);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int step = values[0];
        switch (step) {
            case 0:
                progressView.setText(R.string.startup_bottles);
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_1));
                break;
            case 1:
                progressView.setText(R.string.startup_patterns);
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_2));
                break;
            case 2:
                progressView.setText(R.string.startup_caves);
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_3));
                break;
            case 3:
                progressView.setText(R.string.startup_caves);
                splashImageView.setImageDrawable(ContextCompat.getDrawable(splashScreenActivity, R.mipmap.splash_4));
                break;
            case 4:
                progressView.setText(R.string.startup_finalize);
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
