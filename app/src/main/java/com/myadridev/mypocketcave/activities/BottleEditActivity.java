package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.tasks.SaveBottleTask;

public class BottleEditActivity extends AbstractBottleEditActivity {

    private int bottleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bottle_edit);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        bottleId = bundle.getInt("bottleId");
    }

    @Override
    protected void setCoordinatorLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottle_edit_coordinator_layout);
    }

    @Override
    protected boolean hasDifferences() {
        if (!bottle.Name.equals(nameView.getText().toString())) {
            return true;
        }
        if (!bottle.Domain.equals(domainView.getText().toString())) {
            return true;
        }
        if (bottle.WineColor != wineColorView.getSelectedItem()) {
            return true;
        }
        if (bottle.Millesime != (int) millesimeView.getSelectedItem()) {
            return true;
        }
        if (!bottle.PersonToShareWith.equals(personView.getText().toString())) {
            return true;
        }
        if (!bottle.Comments.equals(commentsView.getText().toString())) {
            return true;
        }
        for (FoodToEatWithEnum food : FoodToEatWithEnum.values()) {
            boolean oldContains = bottle.FoodToEatWithList.contains(food);
            boolean newContains = foodToEatWithList[food.Id];
            if ((newContains && oldContains) || (!newContains && !oldContains)) {
                return true;
            }
        }
        String stockString = stockView.getText().toString();
        if (bottle.Stock != (stockString.isEmpty() ? 0 : Integer.valueOf(stockString))) {
            return true;
        }
        return false;
    }

    @Override
    protected void initBottle() {
        refreshBottle();
    }

    @Override
    protected void saveBottle() {
        SaveBottleTask saveBottleTask = new SaveBottleTask(this, coordinatorLayout, false);
        saveBottleTask.execute(bottle);
    }

    @Override
    protected void cancelBottle() {
        NavigationManager.navigateToBottleDetail(this, bottle.Id);
    }

    private void refreshBottle() {
        bottle = new BottleModel(BottleManager.getBottle(bottleId));
    }
}
