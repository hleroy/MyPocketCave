package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

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
        bottle = new BottleModel();
    }

    @Override
    protected void saveBottle() {
        bottle.Id = BottleManager.addBottle(this, bottle);
        NavigationManager.navigateToBottleDetail(this, bottle.Id);
    }

    @Override
    protected boolean setValues() {
        return super.setValues();
    }
}
