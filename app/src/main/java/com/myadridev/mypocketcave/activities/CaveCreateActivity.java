package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveModel;

public class CaveCreateActivity extends AbstractCaveEditActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cave_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cave_create);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void setCoordinatorLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cave_create_coordinator_layout);
    }

    @Override
    protected void initCave() {
        cave = new CaveModel();
        oldCave = null;
    }

    @Override
    protected void saveCave() {
        cave.Id = CaveManager.addCave(this, cave);
        NavigationManager.navigateToCaveDetail(this, cave.Id);
    }
}
