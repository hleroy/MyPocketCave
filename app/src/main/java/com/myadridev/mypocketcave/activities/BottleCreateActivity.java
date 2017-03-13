package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.tasks.RefreshBottleEditTask;
import com.myadridev.mypocketcave.tasks.SaveBottleTask;

public class BottleCreateActivity extends AbstractBottleEditActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bottle_create);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void setCoordinatorLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottle_create_coordinator_layout);
    }

    @Override
    protected void initBottle() {
        RefreshBottleEditTask refreshBottleEditTask = new RefreshBottleEditTask(this, coordinatorLayout);
        refreshBottleEditTask.execute(0);
    }

    @Override
    protected void saveBottle() {
        SaveBottleTask saveBottleTask = new SaveBottleTask(this, coordinatorLayout, true);
        saveBottleTask.execute(bottle);
    }
}
