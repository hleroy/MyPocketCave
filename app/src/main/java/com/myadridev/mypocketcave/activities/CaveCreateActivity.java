package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveModel;

public class CaveCreateActivity extends AbstractCaveEditActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cave_create);
    }

    @Override
    protected void setCoordinatorLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cave_create_coordinator_layout);
    }

    @Override
    protected void initCave() {
        cave = new CaveModel();
    }

    @Override
    protected void saveCave() {
        cave.Id = CaveManager.Instance.addCave(cave);
        NavigationManager.navigateToCaveDetail(this, cave.Id);
    }

    @Override
    protected boolean setValues() {
        return super.setValues();
    }

    @Override
    protected void redirectToExistingCave(int existingCaveId) {
        NavigationManager.navigateToCaveDetail(this, existingCaveId);
        finish();
    }
}
