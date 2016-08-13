package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.CaveArrangementManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.managers.StorageManager;

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
                        if (!StorageManager.IsInitialized()) {
                            StorageManager.Init(SplashScreenActivity.this);
                        }
                        if (!CaveManager.IsInitialized()) {
                            CaveManager.Init();
                        }
                        step++;
                        initializationHandler.postDelayed(this, RunnableDelayBetweenSteps);
                        break;
                    case 1:
                        splashImageView.setImageDrawable(ContextCompat.getDrawable(SplashScreenActivity.this, R.mipmap.splash_2));
                        if (!BottleManager.IsInitialized()) {
                            BottleManager.Init();
                        }
                        step++;
                        initializationHandler.postDelayed(this, RunnableDelayBetweenSteps);
                        break;
                    case 2:
                        splashImageView.setImageDrawable(ContextCompat.getDrawable(SplashScreenActivity.this, R.mipmap.splash_3));
                        if (!CaveArrangementManager.IsInitialized()) {
                            CaveArrangementManager.Init();
                        }

//                        String storeFile = getString(R.string.filename_patterns);
//                        String storeSet = getString(R.string.store_patterns);
//
//                        SharedPreferences storedData = getSharedPreferences(storeFile, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = storedData.edit();
//                        editor.putStringSet(storeSet, new HashSet<String>());
//                        editor.apply();
                        if (!PatternManager.IsInitialized()) {
                            PatternManager.Init();
                        }
                        step++;
                        initializationHandler.postDelayed(this, RunnableDelayBetweenSteps);
                        break;
                    case 3:
                        splashImageView.setImageDrawable(ContextCompat.getDrawable(SplashScreenActivity.this, R.mipmap.splash_4));
                        if (!CoordinatesManager.IsInitialized()) {
                            CoordinatesManager.Init();
                        }
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
