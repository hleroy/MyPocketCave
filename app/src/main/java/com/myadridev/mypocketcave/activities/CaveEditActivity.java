package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveModel;

public class CaveEditActivity extends AbstractCaveEditActivity {

    private int caveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cave_edit);

        Bundle bundle = getIntent().getExtras();
        caveId = bundle.getInt("caveId");
    }

    @Override
    protected void setCoordinatorLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cave_edit_coordinator_layout);
    }

    @Override
    protected void initCave() {
        refreshCave();
    }

    @Override
    protected void saveCave() {
        CaveManager.editCave(cave);
        NavigationManager.navigateToCaveDetail(this, cave.Id);
    }

    @Override
    protected void cancelCave() {
        NavigationManager.navigateToCaveDetail(this, cave.Id);
    }

    @Override
    protected void removeCave() {
        CaveManager.removeCave(cave);
    }

    @Override
    protected void redirectToExistingCave(int existingCaveId) {
        CaveManager.removeCave(cave);
        NavigationManager.navigateToCaveDetail(this, existingCaveId);
        finish();
    }

    private void refreshCave() {
        cave = new CaveModel(CaveManager.getCave(caveId));
    }
}
