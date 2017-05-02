package com.myadridev.mypocketcave.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.CaveSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.DomainSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.FarmingTypeSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.MillesimeSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.PersonSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.WineColorSpinnerAdapter;
import com.myadridev.mypocketcave.enums.v2.FarmingTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.MillesimeEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.SuggestBottleCriteriaV2;
import com.myadridev.mypocketcave.views.SeekbarRange;

public class SuggestBottleSearchActivity extends AppCompatActivity {

    private final boolean[] foodToEatWithList = new boolean[FoodToEatWithEnumV2.values().length];
    private Spinner wineColorSpinner;
    private CheckBox wineColorCheckBox;
    private Spinner domainSpinner;
    private CheckBox domainCheckBox;
    private Spinner millesimeSpinner;
    private CheckBox millesimeCheckBox;
    private SeekbarRange seekbarRating;
    private CheckBox ratingCheckBox;
    private SeekbarRange seekbarPriceRating;
    private CheckBox priceRatingCheckBox;
    private TextView foodTextView;
    private CheckBox foodCheckBox;
    private Spinner personSpinner;
    private CheckBox personCheckBox;
    private Spinner caveSpinner;
    private CheckBox caveCheckBox;
    private Spinner farmingTypeSpinner;
    private CheckBox farmingTypeCheckBox;
    private Button searchButton;
    private CoordinatorLayout coordinatorLayout;

    private boolean isFoodListOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_bottle_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_suggest_bottle_search);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        isFoodListOpen = false;
        for (int i = 0; i < FoodToEatWithEnumV2.values().length; i++) {
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

        seekbarRating = (SeekbarRange) findViewById(R.id.suggest_bottle_search_rating_seekbar);
        ratingCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_rating_seekbar_must_have);

        seekbarPriceRating = (SeekbarRange) findViewById(R.id.suggest_bottle_search_price_rating_seekbar);
        priceRatingCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_price_rating_seekbar_must_have);

        foodTextView = (TextView) findViewById(R.id.suggest_bottle_search_food);
        foodTextView.setOnClickListener(onFoodViewClick());
        foodCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_food_must_have);

        personSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_person);
        personCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_person_must_have);

        caveSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_cave);
        caveCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_cave_must_have);

        farmingTypeSpinner = (Spinner) findViewById(R.id.suggest_bottle_search_farming_type);
        farmingTypeCheckBox = (CheckBox) findViewById(R.id.suggest_bottle_search_farming_type_must_have);

        searchButton = (Button) findViewById(R.id.suggest_bottle_search_button);
        searchButton.setOnClickListener(onSearchButtonClick());
    }

    private View.OnClickListener onFoodViewClick() {
        return (View v) -> {
            if (!isFoodListOpen) {
                isFoodListOpen = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(SuggestBottleSearchActivity.this);
                builder.setMultiChoiceItems(FoodToEatWithEnumV2.getAllFoodLabels(SuggestBottleSearchActivity.this), foodToEatWithList,
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
            SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
            searchCriteria.WineColor = (WineColorEnumV2) wineColorSpinner.getSelectedItem();
            searchCriteria.IsWineColorRequired = wineColorCheckBox.isChecked();
            searchCriteria.Domain = domainSpinner.getSelectedItemPosition() != 0 ? (String) domainSpinner.getSelectedItem() : "";
            searchCriteria.IsDomainRequired = domainCheckBox.isChecked();
            searchCriteria.Millesime = (MillesimeEnumV2) millesimeSpinner.getSelectedItem();
            searchCriteria.IsMillesimeRequired = millesimeCheckBox.isChecked();
            searchCriteria.RatingMinValue = seekbarRating.getSelectedMinValue();
            searchCriteria.RatingMaxValue = seekbarRating.getSelectedMaxValue();
            searchCriteria.IsRatingRequired = ratingCheckBox.isChecked();
            searchCriteria.PriceRatingMinValue = seekbarPriceRating.getSelectedMinValue();
            searchCriteria.PriceRatingMaxValue = seekbarPriceRating.getSelectedMaxValue();
            searchCriteria.IsPriceRatingRequired = priceRatingCheckBox.isChecked();
            for (int i = 0; i < foodToEatWithList.length; i++) {
                if (foodToEatWithList[i]) {
                    searchCriteria.FoodToEatWithList.add(FoodToEatWithEnumV2.getById(i));
                }
            }
            searchCriteria.IsFoodRequired = foodCheckBox.isChecked();
            searchCriteria.PersonToShareWith = personSpinner.getSelectedItemPosition() != 0 ? (String) personSpinner.getSelectedItem() : "";
            searchCriteria.IsPersonRequired = personCheckBox.isChecked();
            searchCriteria.Cave = (CaveLightModelV2) caveSpinner.getSelectedItem();
            searchCriteria.IsCaveRequired = caveCheckBox.isChecked();
            searchCriteria.FarmingType = (FarmingTypeEnumV2) farmingTypeSpinner.getSelectedItem();
            searchCriteria.IsFarmingTypeRequired = farmingTypeCheckBox.isChecked();

            if (checkCriteria(searchCriteria)) {
                if (!NavigationManager.navigateToSuggestBottleResult(SuggestBottleSearchActivity.this, searchCriteria)) {
                    SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_technical, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
                }
            }
        };
    }

    private boolean checkCriteria(SuggestBottleCriteriaV2 searchCriteria) {
        boolean isErrors = false;
        if (searchCriteria.IsWineColorRequired && searchCriteria.WineColor == WineColorEnumV2.a) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_suggest_wine_color, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else if (searchCriteria.IsDomainRequired && searchCriteria.Domain.isEmpty()) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_suggest_domain, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else if (searchCriteria.IsMillesimeRequired && searchCriteria.Millesime == MillesimeEnumV2.a) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_suggest_millesime, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else if (searchCriteria.IsFoodRequired && searchCriteria.FoodToEatWithList.isEmpty()) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_suggest_food, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else if (searchCriteria.IsPersonRequired && searchCriteria.PersonToShareWith.isEmpty()) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_suggest_person, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else if (searchCriteria.IsCaveRequired && searchCriteria.Cave == null) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_suggest_cave, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
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

        FarmingTypeSpinnerAdapter farmingTypeSpinnerAdapter = new FarmingTypeSpinnerAdapter(this);
        farmingTypeSpinner.setAdapter(farmingTypeSpinnerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                return true;
        }
    }

    @Override
    protected void onResume() {
        if (NavigationManager.restartIfNeeded(this)) {
            finish();
        } else {
            super.onResume();
        }
    }
}
