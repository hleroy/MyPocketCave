package com.myadridev.mypocketcave.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.CaveSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.DomainSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.MillesimeSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.PersonSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.WineColorSpinnerAdapter;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.MillesimeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveLightModel;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;

public class SuggestBottleSearchActivity extends AppCompatActivity {

    private final boolean[] foodToEatWithList = new boolean[FoodToEatWithEnum.values().length];
    private Spinner wineColorSpinner;
    private CheckBox wineColorCheckBox;
    private Spinner domainSpinner;
    private CheckBox domainCheckBox;
    private Spinner millesimeSpinner;
    private CheckBox millesimeCheckBox;
    private TextView foodTextView;
    private CheckBox foodCheckBox;
    private Spinner personSpinner;
    private CheckBox personCheckBox;
    private Spinner caveSpinner;
    private CheckBox caveCheckBox;
    private Button searchButton;
    private CoordinatorLayout coordinatorLayout;

    private boolean isFoodListOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_bottle_search);
        isFoodListOpen = false;
        for (int i = 0; i < FoodToEatWithEnum.values().length; i++) {
            foodToEatWithList[i] = false;
        }

        setLayout();
        setLayoutValues();
    }

    private void setLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.suggest_bottle_search_coordinator_layout);

        wineColorSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_wine_color);
        wineColorCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_wine_color_must_have);

        domainSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_domain);
        domainCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_domain_must_have);

        millesimeSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_millesime);
        millesimeCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_millesime_must_have);

        foodTextView = (TextView) findViewById(R.id.suggest_bottle_search_food);
        foodTextView.setOnClickListener(onFoodViewClick());
        foodCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_food_must_have);

        personSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_person);
        personCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_person_must_have);

        caveSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_cave);
        caveCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_cave_must_have);

        searchButton = (Button) findViewById(R.id.suggest_bottle_search_button);
        searchButton.setOnClickListener(onSearchButtonClick());
    }

    private View.OnClickListener onFoodViewClick() {
        return (View v) -> {
            if (!isFoodListOpen) {
                isFoodListOpen = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(SuggestBottleSearchActivity.this);
                builder.setMultiChoiceItems(FoodToEatWithEnum.getAllFoodLabels(SuggestBottleSearchActivity.this), foodToEatWithList,
                        (DialogInterface dialog, int which, boolean isChecked) -> foodTextView.setText(FoodToEatHelper.computeFoodViewText(SuggestBottleSearchActivity.this, foodToEatWithList)));
                builder.setOnDismissListener((DialogInterface dialog) -> {
                    isFoodListOpen = false;
                    foodTextView.setText(FoodToEatHelper.computeFoodViewText(SuggestBottleSearchActivity.this, foodToEatWithList));
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

    private View.OnClickListener onSearchButtonClick() {
        return (View view) -> {
            SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
            searchCriteria.WineColor = (WineColorEnum) wineColorSpinner.getSelectedItem();
            searchCriteria.IsWineColorRequired = wineColorCheckBox.isChecked();
            searchCriteria.Domain = domainSpinner.getSelectedItemPosition() != 0 ? (String) domainSpinner.getSelectedItem() : "";
            searchCriteria.IsDomainRequired = domainCheckBox.isChecked();
            searchCriteria.Millesime = (MillesimeEnum) millesimeSpinner.getSelectedItem();
            searchCriteria.IsMillesimeRequired = millesimeCheckBox.isChecked();
            for (int i = 0; i < foodToEatWithList.length; i++) {
                if (foodToEatWithList[i]) {
                    searchCriteria.FoodToEatWithList.add(FoodToEatWithEnum.getById(i));
                }
            }
            searchCriteria.IsFoodRequired = foodCheckBox.isChecked();
            searchCriteria.PersonToShareWith = personSpinner.getSelectedItemPosition() != 0 ? (String) personSpinner.getSelectedItem() : "";
            searchCriteria.IsPersonRequired = personCheckBox.isChecked();
            searchCriteria.Cave = (CaveLightModel) caveSpinner.getSelectedItem();
            searchCriteria.IsCaveRequired = caveCheckBox.isChecked();

            if (checkCriteria(searchCriteria)) {
                if (!NavigationManager.navigateToSuggestBottleResult(SuggestBottleSearchActivity.this, searchCriteria)) {
                    final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_technical), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
                    snackbar.setActionTextColor(ContextCompat.getColor(SuggestBottleSearchActivity.this, R.color.colorError));
                    snackbar.show();
                }
            }
        };
    }

    private boolean checkCriteria(SuggestBottleCriteria searchCriteria) {
        boolean isErrors = false;
        if (searchCriteria.IsWineColorRequired && searchCriteria.WineColor == WineColorEnum.ANY) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_suggest_wine_color), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        } else if (searchCriteria.IsDomainRequired && searchCriteria.Domain.isEmpty()) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_suggest_domain), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        } else if (searchCriteria.IsMillesimeRequired && searchCriteria.Millesime == MillesimeEnum.ANY) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_suggest_millesime), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        } else if (searchCriteria.IsFoodRequired && searchCriteria.FoodToEatWithList.isEmpty()) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_suggest_food), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        } else if (searchCriteria.IsPersonRequired && searchCriteria.PersonToShareWith.isEmpty()) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_suggest_person), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        } else if (searchCriteria.IsCaveRequired && searchCriteria.Cave == null) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_suggest_cave), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        }
        return !isErrors;
    }

    private void setLayoutValues() {
        WineColorSpinnerAdapter wineColorAdapter = new WineColorSpinnerAdapter(this, true);
        wineColorSpinner.setAdapter(wineColorAdapter);

        DomainSpinnerAdapter domainSpinnerAdapter = new DomainSpinnerAdapter(this, true);
        domainSpinner.setAdapter(domainSpinnerAdapter);

        MillesimeSpinnerAdapter millesimeSpinnerAdapter = new MillesimeSpinnerAdapter(this, true);
        millesimeSpinner.setAdapter(millesimeSpinnerAdapter);

        PersonSpinnerAdapter personSpinnerAdapter = new PersonSpinnerAdapter(this, true);
        personSpinner.setAdapter(personSpinnerAdapter);

        CaveSpinnerAdapter caveSpinnerAdapter = new CaveSpinnerAdapter(this);
        caveSpinner.setAdapter(caveSpinnerAdapter);
    }
}
