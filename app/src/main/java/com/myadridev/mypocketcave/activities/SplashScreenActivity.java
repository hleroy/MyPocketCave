package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.tasks.StartupTask;

public class SplashScreenActivity extends AppCompatActivity {

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

        StartupTask startupTask = new StartupTask(this, splashImageView);
        startupTask.execute();
    }
}
