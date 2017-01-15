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
    protected boolean hasDifferences() {
        if (!oldCave.Name.equals(nameView.getText().toString())) {
            return true;
        }
        if (oldCave.CaveType != caveTypeView.getSelectedItem()) {
            return true;
        }

        switch (oldCave.CaveType) {
            case BULK:
                String NumberBottlesBulk = bulkBottlesNumberView.getText().toString();
                if (oldCave.CaveArrangement.NumberBottlesBulk != (NumberBottlesBulk.isEmpty() ? 0 : Integer.valueOf(NumberBottlesBulk))) {
                    return true;
                }
                break;
            case BOX:
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
            case FRIDGE:
            case RACK:
                if (oldCave.CaveArrangement.hasDifferentPattern(cave.CaveArrangement)) {
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    protected void initCave() {
        refreshCave();
    }

    @Override
    protected void saveCave() {
        CaveManager.editCave(this, cave);
        NavigationManager.navigateToCaveDetail(this, cave.Id);
    }

    @Override
    protected void cancelCave() {
        NavigationManager.navigateToCaveDetail(this, cave.Id);
    }

    private void refreshCave() {
        oldCave = CaveManager.getCave(this, caveId);
        cave = new CaveModel(oldCave);
    }
}
