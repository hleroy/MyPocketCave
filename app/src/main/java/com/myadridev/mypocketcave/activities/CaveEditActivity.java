package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.CaveArrangementModelManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.tasks.caves.RefreshCaveEditTask;
import com.myadridev.mypocketcave.tasks.caves.SaveCaveTask;

public class CaveEditActivity extends AbstractCaveEditActivity {

    private int caveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cave_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cave_edit);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        caveId = bundle.getInt("caveId");
    }

    @Override
    protected void setCoordinatorLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cave_edit_coordinator_layout);
    }

    @Override
    protected boolean hasDifferences() {
        if (!oldCave.Name.equals(nameView.getText().toString())) {
            return true;
        }
        if (oldCave.CaveType != caveTypeView.getSelectedItem()) {
            return true;
        }

        switch (oldCave.CaveType) {
            case bu:
                String NumberBottlesBulk = bulkBottlesNumberView.getText().toString();
                if (oldCave.CaveArrangement.NumberBottlesBulk != (NumberBottlesBulk.isEmpty() ? 0 : Integer.valueOf(NumberBottlesBulk))) {
                    return true;
                }
                break;
            case bo:
                String NumberBoxes = boxesNumberView.getText().toString();
                if (oldCave.CaveArrangement.NumberBoxes != (NumberBoxes.isEmpty() ? 0 : Integer.valueOf(NumberBoxes))) {
                    return true;
                }
                String boxesNumberBottlesByColumn = boxesPatternNumberBottlesByColumnView.getText().toString();
                if (oldCave.CaveArrangement.BoxesNumberBottlesByColumn != (boxesNumberBottlesByColumn.isEmpty() ? 0 : Integer.valueOf(boxesNumberBottlesByColumn))) {
                    return true;
                }
                String boxesNumberBottlesByRow = boxesPatternNumberBottlesByRowView.getText().toString();
                if (oldCave.CaveArrangement.BoxesNumberBottlesByRow != (boxesNumberBottlesByRow.isEmpty() ? 0 : Integer.valueOf(boxesNumberBottlesByRow))) {
                    return true;
                }
                break;
            case f:
            case r:
                if (CaveArrangementModelManager.hasDifferentPattern(oldCave.CaveArrangement, cave.CaveArrangement)) {
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    protected void initCave() {
        RefreshCaveEditTask refreshCaveEditTask = new RefreshCaveEditTask(this, coordinatorLayout);
        refreshCaveEditTask.execute(caveId);
    }

    @Override
    public void saveCave() {
        SaveCaveTask saveCaveTask = new SaveCaveTask(this, false);
        saveCaveTask.execute(cave);
    }

    @Override
    protected void cancelCave() {
        NavigationManager.navigateToCaveDetail(this, cave.Id);
    }
}
