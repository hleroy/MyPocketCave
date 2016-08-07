package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

public class BottleCreateActivity extends AbstractBottleEditActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_create);
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
        bottle.Id = BottleManager.Instance.addBottle(bottle);
        NavigationManager.navigateToBottleDetail(this, bottle.Id);
    }

    @Override
    protected boolean setValues() {
        return super.setValues();
    }

    @Override
    protected void redirectToExistingBottle(int existingBottleId) {
        NavigationManager.navigateToBottleDetail(this, existingBottleId);
        finish();
    }
}
