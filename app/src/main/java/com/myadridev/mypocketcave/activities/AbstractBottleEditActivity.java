package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;

public abstract class AbstractBottleEditActivity extends AppCompatActivity {

    private final boolean[] foodToEatWithList = new boolean[FoodToEatWithEnum.number];
    protected BottleModel bottle;
    protected EditText nameView;
    protected EditText stockView;
    protected Spinner wineColorView;
    protected EditText personView;
    protected EditText commentsView;
    protected TextView foodView;
    protected Spinner millesimeView;
    private boolean isFoodListOpen;
    private EditText domainView;
    protected CoordinatorLayout coordinatorLayout;
    private final View.OnTouchListener hideKeyboardOnClick;

    protected AbstractBottleEditActivity() {
        hideKeyboardOnClick = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFoodListOpen = false;
        for (int i = 0; i < FoodToEatWithEnum.number; i++) {
            foodToEatWithList[i] = false;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initBottle();
        initLayout();
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
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFoodListOpen) {
                    isFoodListOpen = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AbstractBottleEditActivity.this);
                    builder.setMultiChoiceItems(FoodToEatWithEnum.getAllFoodLabels(AbstractBottleEditActivity.this), foodToEatWithList, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            foodView.setText(FoodToEatHelper.computeFoodViewText(AbstractBottleEditActivity.this, foodToEatWithList));
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            isFoodListOpen = false;
                            foodView.setText(FoodToEatHelper.computeFoodViewText(AbstractBottleEditActivity.this, foodToEatWithList));
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                    int width = displaymetrics.widthPixels;
                    int height = displaymetrics.heightPixels;

                    dialog.getWindow().setLayout(width * 95 / 100, height * 2 / 3);
                }
            }
        };
    }

    private void setLayoutValues() {
        WineColorSpinnerAdapter wineColorAdapter = new WineColorSpinnerAdapter(this, false);
        wineColorView.setAdapter(wineColorAdapter);

        MillesimeAdapter millesimeAdapter = new MillesimeAdapter(this);
        millesimeView.setAdapter(millesimeAdapter);

        if (bottle.Id > 0) {
            wineColorView.setSelection(getWineColorPosition(bottle.WineColor.id));
            millesimeView.setSelection(bottle.Millesime == 0 ? 0 : millesimeAdapter.currentYear + 1 - bottle.Millesime);
            nameView.setText(bottle.Name);
            domainView.setText(bottle.Domain);
            if (bottle.Stock > 0)
                stockView.setText(String.valueOf(bottle.Stock));
            personView.setText(bottle.PersonToShareWith);
            commentsView.setText(bottle.Comments);
            for (FoodToEatWithEnum food : bottle.FoodToEatWithList) {
                foodToEatWithList[food.id] = true;
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
        if (setValues()) {
            saveBottle();
            finish();
        }
    }

    protected void initBottle() {}
    protected void saveBottle() {}
    protected void cancelBottle() {}
    protected void removeBottle() {}

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
            if (foodToEatWithList[food.id]) {
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

        if (name.isEmpty()) {
            AlertDialog.Builder noNameDialogBuilder = new AlertDialog.Builder(this);
            noNameDialogBuilder.setCancelable(true);
            noNameDialogBuilder.setMessage(R.string.error_bottle_no_name);
            noNameDialogBuilder.setNegativeButton(R.string.global_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            noNameDialogBuilder.setPositiveButton(R.string.global_exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    cancelBottle();
                }
            });
            noNameDialogBuilder.show();
            isErrors = true;
        } else {
            String domain = domainView.getText().toString();
            WineColorEnum wineColor = (WineColorEnum) wineColorView.getSelectedItem();
            int millesime = (int) millesimeView.getSelectedItem();

            final int existingBottleId = BottleManager.Instance.getExistingBottleId(bottle.Id, name, domain, wineColor, millesime);
            if (existingBottleId > 0) {
                AlertDialog.Builder existingBottleDialogBuilder = new AlertDialog.Builder(this);
                existingBottleDialogBuilder.setCancelable(true);
                existingBottleDialogBuilder.setMessage(R.string.error_bottle_already_exists);
                existingBottleDialogBuilder.setNeutralButton(R.string.global_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                existingBottleDialogBuilder.setNegativeButton(R.string.global_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        cancelBottle();
                    }
                });
                existingBottleDialogBuilder.setPositiveButton(R.string.global_merge, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeBottle();
                        redirectToExistingBottle(existingBottleId);
                    }
                });
                existingBottleDialogBuilder.show();
                isErrors = true;
            }
        }
        return !isErrors;
    }

    protected abstract void redirectToExistingBottle(int existingBottleId);
}
