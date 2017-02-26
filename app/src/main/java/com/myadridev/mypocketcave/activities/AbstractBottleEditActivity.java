package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.MillesimeAdapter;
import com.myadridev.mypocketcave.adapters.WineColorSpinnerAdapter;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;

public abstract class AbstractBottleEditActivity extends AppCompatActivity {

    protected final boolean[] foodToEatWithList = new boolean[FoodToEatWithEnum.values().length];
    private final View.OnTouchListener hideKeyboardOnClick;
    protected BottleModel bottle;
    protected EditText nameView;
    protected EditText stockView;
    protected Spinner wineColorView;
    protected EditText personView;
    protected EditText commentsView;
    protected TextView foodView;
    protected Spinner millesimeView;
    protected EditText domainView;
    protected CoordinatorLayout coordinatorLayout;
    private boolean isFoodListOpen;

    protected AbstractBottleEditActivity() {
        hideKeyboardOnClick = (View v, MotionEvent event) -> {
            hideKeyboard();
            return false;
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFoodListOpen = false;
        for (int i = 0; i < FoodToEatWithEnum.values().length; i++) {
            foodToEatWithList[i] = false;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initBottle();
        initLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                hideKeyboard();
                if (setValues()) {
                    saveBottle();
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initLayout() {
        setLayout();
        setLayoutValues();
    }

    protected void setLayout() {
        setCoordinatorLayout();
        coordinatorLayout.setOnTouchListener(hideKeyboardOnClick);
        nameView = (EditText) findViewById(R.id.bottle_edit_name);
        domainView = (EditText) findViewById(R.id.bottle_edit_domain);
        stockView = (EditText) findViewById(R.id.bottle_edit_stock);
        wineColorView = (Spinner) findViewById(R.id.bottle_edit_wine_color);
        wineColorView.setOnTouchListener(hideKeyboardOnClick);
        personView = (EditText) findViewById(R.id.bottle_edit_person);
        commentsView = (EditText) findViewById(R.id.bottle_edit_comments);
        foodView = (TextView) findViewById(R.id.bottle_edit_food);
        foodView.setOnTouchListener(hideKeyboardOnClick);
        foodView.setOnClickListener(onFoodViewClick());
        millesimeView = (Spinner) findViewById(R.id.bottle_edit_millesime);
        millesimeView.setOnTouchListener(hideKeyboardOnClick);
    }

    protected abstract void setCoordinatorLayout();

    private View.OnClickListener onFoodViewClick() {
        return (View v) -> {
            if (!isFoodListOpen) {
                isFoodListOpen = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(AbstractBottleEditActivity.this);
                builder.setMultiChoiceItems(FoodToEatWithEnum.getAllFoodLabels(AbstractBottleEditActivity.this), foodToEatWithList,
                        (DialogInterface dialog, int which, boolean isChecked) -> foodView.setText(FoodToEatHelper.computeFoodViewText(AbstractBottleEditActivity.this, foodToEatWithList)));
                builder.setOnDismissListener((DialogInterface dialog) -> {
                    isFoodListOpen = false;
                    foodView.setText(FoodToEatHelper.computeFoodViewText(AbstractBottleEditActivity.this, foodToEatWithList));
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                int width = displaymetrics.widthPixels;
                int height = displaymetrics.heightPixels;

                dialog.getWindow().setLayout(width * 95 / 100, height * 2 / 3);
            }
        };
    }

    private void setLayoutValues() {
        WineColorSpinnerAdapter wineColorAdapter = new WineColorSpinnerAdapter(this, false);
        wineColorView.setAdapter(wineColorAdapter);

        MillesimeAdapter millesimeAdapter = new MillesimeAdapter(this);
        millesimeView.setAdapter(millesimeAdapter);

        if (bottle.Id > 0) {
            wineColorView.setSelection(getWineColorPosition(bottle.WineColor.Id));
            millesimeView.setSelection(bottle.Millesime == 0 ? 0 : millesimeAdapter.currentYear + 1 - bottle.Millesime);
            nameView.setText(bottle.Name);
            domainView.setText(bottle.Domain);
            if (bottle.Stock > 0)
                stockView.setText(String.valueOf(bottle.Stock));
            personView.setText(bottle.PersonToShareWith);
            commentsView.setText(bottle.Comments);
            for (FoodToEatWithEnum food : bottle.FoodToEatWithList) {
                foodToEatWithList[food.Id] = true;
            }
            foodView.setText(FoodToEatHelper.computeFoodViewText(this, foodToEatWithList));
        }
    }

    private int getWineColorPosition(int id) {
        return id - 1;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        if (hasDifferences()) {
            AlertDialog.Builder exitDialogBuilder = new AlertDialog.Builder(this);
            exitDialogBuilder.setCancelable(true);
            exitDialogBuilder.setMessage(R.string.detail_exit_confirmation);
            exitDialogBuilder.setNegativeButton(R.string.global_stay, (DialogInterface dialog, int which) -> dialog.dismiss());
            exitDialogBuilder.setPositiveButton(R.string.global_exit, (DialogInterface dialog, int which) -> {
                dialog.dismiss();
                AbstractBottleEditActivity.this.cancelBottle();
                AbstractBottleEditActivity.this.finish();
            });
            exitDialogBuilder.show();
        } else {
            cancelBottle();
            finish();
        }
    }

    protected boolean hasDifferences() {
        return true;
    }

    protected void initBottle() {
    }

    protected void saveBottle() {
    }

    protected void cancelBottle() {
    }

    protected void removeBottle() {
    }

    protected boolean setValues() {
        boolean isValid = checkValues();

        if (!isValid) {
            return false;
        }

        bottle.Name = nameView.getText().toString();
        bottle.Domain = domainView.getText().toString();
        bottle.WineColor = (WineColorEnum) wineColorView.getSelectedItem();
        bottle.Millesime = (int) millesimeView.getSelectedItem();
        bottle.PersonToShareWith = personView.getText().toString();
        bottle.Comments = commentsView.getText().toString();

        bottle.FoodToEatWithList.clear();
        for (FoodToEatWithEnum food : FoodToEatWithEnum.values()) {
            if (foodToEatWithList[food.Id]) {
                bottle.FoodToEatWithList.add(food);
            }
        }
        String stockString = stockView.getText().toString();
        bottle.Stock = stockString.isEmpty() ? 0 : Integer.valueOf(stockString);
        return true;
    }

    private boolean checkValues() {
        boolean isErrors = false;

        String name = nameView.getText().toString();
        String stockString = stockView.getText().toString();
        int stock = stockString.isEmpty() ? 0 : Integer.valueOf(stockString);

        if (name.isEmpty()) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_bottle_no_name, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else if (stock < bottle.NumberPlaced) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_bottle_not_enough, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else {
            String domain = domainView.getText().toString();
            WineColorEnum wineColor = (WineColorEnum) wineColorView.getSelectedItem();
            int millesime = (int) millesimeView.getSelectedItem();

            final int existingBottleId = BottleManager.getExistingBottleId(bottle.Id, name, domain, wineColor, millesime);
            if (existingBottleId > 0) {
                SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_bottle_already_exists, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
                isErrors = true;
            }
        }
        return !isErrors;
    }
}
