package com.myadridev.mypocketcave.tasks;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.activities.SplashScreenActivity;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.IBottleStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICaveStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICavesStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.IPatternsStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISyncStorageManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.BottlesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.CaveSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.CavesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.PatternsSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.SharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.SyncSharedPreferencesManager;

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

        // init DependencyManager
        DependencyManager.init();
        // SharedPreferences
        if (!SharedPreferencesManager.IsInitialized()) {
            SharedPreferencesManager.Init(splashScreenActivity);
        }
        DependencyManager.registerSingleton(ISharedPreferencesManager.class, SharedPreferencesManager.Instance);

        // Bottles
        if (!BottlesSharedPreferencesManager.IsInitialized()) {
            BottlesSharedPreferencesManager.Init(splashScreenActivity);
        }
        DependencyManager.registerSingleton(IBottleStorageManager.class, BottlesSharedPreferencesManager.Instance);

        publishProgress(step++);

        // Caves
        if (!CavesSharedPreferencesManager.IsInitialized()) {
            CavesSharedPreferencesManager.Init(splashScreenActivity);
        }
        DependencyManager.registerSingleton(ICavesStorageManager.class, CavesSharedPreferencesManager.Instance);

        publishProgress(step++);

        // Patterns
        if (!PatternsSharedPreferencesManager.IsInitialized()) {
            PatternsSharedPreferencesManager.Init(splashScreenActivity);
        }
        DependencyManager.registerSingleton(IPatternsStorageManager.class, PatternsSharedPreferencesManager.Instance);

        publishProgress(step++);

        // Cave
        if (!CaveSharedPreferencesManager.IsInitialized()) {
            CaveSharedPreferencesManager.Init(splashScreenActivity);
        }
        DependencyManager.registerSingleton(ICaveStorageManager.class, CaveSharedPreferencesManager.Instance);

        // Sync
        if (!SyncSharedPreferencesManager.IsInitialized()) {
            SyncSharedPreferencesManager.Init();
        }
        DependencyManager.registerSingleton(ISyncStorageManager.class, SyncSharedPreferencesManager.Instance);

        publishProgress(step++);

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
