package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.IBottleStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICaveStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICavesStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.IPatternsStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.BottlesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.CaveSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.CavesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.PatternsSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.SharedPreferencesManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    public static final int RunnableDelayBetweenSteps = 200;
    private ImageView splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        splashImageView = (ImageView) findViewById(R.id.splash_image);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Handler initializationHandler = new Handler();
        Runnable runnable = new Runnable() {
            int step = 0;

            public void run() {
                switch (step) {
                    case 0:
                        splashImageView.setImageDrawable(ContextCompat.getDrawable(SplashScreenActivity.this, R.mipmap.splash_1));
                        // init DependencyManager
                        DependencyManager.init();
                        // SharedPreferences
                        if (!SharedPreferencesManager.IsInitialized()) {
                            SharedPreferencesManager.Init(SplashScreenActivity.this);
                        }
                        DependencyManager.registerSingleton(ISharedPreferencesManager.class, SharedPreferencesManager.Instance);

                        // Bottles
                        if (!BottlesSharedPreferencesManager.IsInitialized()) {
                            BottlesSharedPreferencesManager.Init(SplashScreenActivity.this);
                        }
                        DependencyManager.registerSingleton(IBottleStorageManager.class, BottlesSharedPreferencesManager.Instance);
                        step++;
                        initializationHandler.postDelayed(this, RunnableDelayBetweenSteps);
                        break;
                    case 1:
                        splashImageView.setImageDrawable(ContextCompat.getDrawable(SplashScreenActivity.this, R.mipmap.splash_2));
                        // Caves
                        if (!CavesSharedPreferencesManager.IsInitialized()) {
                            CavesSharedPreferencesManager.Init(SplashScreenActivity.this);
                        }
                        DependencyManager.registerSingleton(ICavesStorageManager.class, CavesSharedPreferencesManager.Instance);
                        step++;
                        initializationHandler.postDelayed(this, RunnableDelayBetweenSteps);
                        break;
                    case 2:
                        splashImageView.setImageDrawable(ContextCompat.getDrawable(SplashScreenActivity.this, R.mipmap.splash_3));
                        // Patterns
                        if (!PatternsSharedPreferencesManager.IsInitialized()) {
                            PatternsSharedPreferencesManager.Init(SplashScreenActivity.this);
                        }
                        DependencyManager.registerSingleton(IPatternsStorageManager.class, PatternsSharedPreferencesManager.Instance);
                        step++;
                        initializationHandler.postDelayed(this, RunnableDelayBetweenSteps);
                        break;
                    case 3:
                        splashImageView.setImageDrawable(ContextCompat.getDrawable(SplashScreenActivity.this, R.mipmap.splash_4));
                        // Cave
                        if (!CaveSharedPreferencesManager.IsInitialized()) {
                            CaveSharedPreferencesManager.Init();
                        }
                        DependencyManager.registerSingleton(ICaveStorageManager.class, CaveSharedPreferencesManager.Instance);
                        step++;
                        initializationHandler.postDelayed(this, RunnableDelayBetweenSteps);
                        break;
                    case 4:
                        initializationHandler.removeCallbacks(this);
                        NavigationManager.navigateToMain(SplashScreenActivity.this);
                        finish();
                        break;
                }
            }
        };
        initializationHandler.post(runnable);
    }
}
