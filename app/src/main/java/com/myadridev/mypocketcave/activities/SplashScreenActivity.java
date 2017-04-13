package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.migration.MigrationManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISyncStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ISharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.SyncSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.SharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.SharedPreferencesManagerV2;
import com.myadridev.mypocketcave.tasks.startup.MigrationTask;
import com.myadridev.mypocketcave.tasks.startup.StartupTask;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splashImageView;
    private TextView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        splashImageView = (ImageView) findViewById(R.id.splash_image);
        progressView = (TextView) findViewById(R.id.splash_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // init DependencyManager
        DependencyManager.init();

        // SharedPreferences
        SharedPreferencesManagerV2.Init(this);
        DependencyManager.registerSingleton(ISharedPreferencesManagerV2.class, SharedPreferencesManagerV2.Instance);

        // Sync
        SyncSharedPreferencesManager.Init();
        DependencyManager.registerSingleton(ISyncStorageManager.class, SyncSharedPreferencesManager.Instance);

        if (MigrationManager.needsMigration(this)) {
            // SharedPreferences
            SharedPreferencesManager.Init(this);
            DependencyManager.registerSingleton(ISharedPreferencesManager.class, SharedPreferencesManager.Instance);

            MigrationTask migrationTask = new MigrationTask(this, splashImageView, progressView);
            migrationTask.execute();
        } else {
            StartupTask startupTask = new StartupTask(this, splashImageView, progressView);
            startupTask.execute();
        }
    }
}
