package com.myadridev.mypocketcave.tasks.startup;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SplashScreenActivity;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.IBottleStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ICaveStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ICavesStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.IPatternsStorageManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.BottlesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.CaveSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.CavesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.PatternsSharedPreferencesManager;

public class StartupTask extends AsyncTask<Void, Integer, Void> {

    private SplashScreenActivity splashScreenActivity;
    private ImageView splashImageView;

    public StartupTask(SplashScreenActivity splashScreenActivity, ImageView splashImageView) {
        this.splashScreenActivity = splashScreenActivity;
        this.splashImageView = splashImageView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int step = 0;
        publishProgress(step);

        // Bottles
        BottlesSharedPreferencesManager.init(splashScreenActivity);
        DependencyManager.registerSingleton(IBottleStorageManager.class, BottlesSharedPreferencesManager.Instance);

        publishProgress(++step);

        // Caves
        CavesSharedPreferencesManager.init(splashScreenActivity);
        DependencyManager.registerSingleton(ICavesStorageManager.class, CavesSharedPreferencesManager.Instance);

        publishProgress(++step);

        // Patterns
        PatternsSharedPreferencesManager.init(splashScreenActivity);
        DependencyManager.registerSingleton(IPatternsStorageManager.class, PatternsSharedPreferencesManager.Instance);

        publishProgress(++step);

        // Cave
        CaveSharedPreferencesManager.init(splashScreenActivity);
        DependencyManager.registerSingleton(ICaveStorageManager.class, CaveSharedPreferencesManager.Instance);

        publishProgress(++step);

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
