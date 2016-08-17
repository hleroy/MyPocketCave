package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

public class BottleEditActivity extends AbstractBottleEditActivity {

    private int bottleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_edit);

        Bundle bundle = getIntent().getExtras();
        bottleId = bundle.getInt("bottleId");
    }

    @Override
    protected void setCoordinatorLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottle_edit_coordinator_layout);
    }

    @Override
    protected void initBottle() {
        refreshBottle();
    }

    @Override
    protected void saveBottle() {
        BottleManager.editBottle(bottle);
        NavigationManager.navigateToBottleDetail(this, bottle.Id);
    }

    @Override
    protected void cancelBottle() {
        NavigationManager.navigateToBottleDetail(this, bottle.Id);
    }

    @Override
    protected void removeBottle() {
        BottleManager.removeBottle(bottle.Id);
    }

    @Override
    protected boolean setValues() {
        return super.setValues();
    }

    @Override
    protected void redirectToExistingBottle(int existingBottleId) {
        BottleManager.removeBottle(bottle.Id);
        NavigationManager.navigateToBottleDetail(this, existingBottleId);
        finish();
    }

    private void refreshBottle() {
        bottle = new BottleModel(BottleManager.getBottle(bottleId));
    }
}
