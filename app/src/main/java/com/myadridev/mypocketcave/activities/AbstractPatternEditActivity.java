package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.helpers.ControlsHelper;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;
import com.myadridev.mypocketcave.tasks.pattern.SavePatternTask;

public class AbstractPatternEditActivity extends AppCompatActivity {

    public static final int overviewScreenWidthMarginLeft = 8;
    public static final int overviewScreenWidthMarginRight = 8;
    private final View.OnTouchListener hideKeyboardOnClick;

    private CoordinatorLayout coordinatorLayout;
    protected Spinner typeSpinner;
    protected ImageView typeImage;
    protected TextView typeText;
    protected EditText numberBottlesByColumnEditText;
    protected EditText numberBottlesByRowEditText;
    private TextView verticallyExpendableTextView;
    protected CheckBox verticallyExpendableCheckbox;
    private TextView horizontallyExpendableTextView;
    protected CheckBox horizontallyExpendableCheckbox;
    private TextView invertPatternTextView;
    protected CheckBox invertPatternCheckbox;
    private PercentRelativeLayout containerLayout;

    private RecyclerView patternOverviewRecyclerView;
    protected PatternAdapter patternAdapter;
    protected PatternModel pattern;
    private CompoundButton.OnCheckedChangeListener checkboxCheckedChangeListener = (CompoundButton compoundButton, boolean b) -> {
        hideKeyboard();
        updateValuesAndAdapter();
    };
    private AdapterView.OnItemSelectedListener patternTypeChangedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateValuesAndAdapter();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
    private TextWatcher dispositionChangedListener = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            updateValuesAndAdapter();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public AbstractPatternEditActivity() {
        hideKeyboardOnClick = (View v, MotionEvent event) -> {
            hideKeyboard();
            return false;
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pattern_edit);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            setToolbarTitle(supportActionBar);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initLayout();
    }

    protected void setToolbarTitle(ActionBar supportActionBar) {
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
                if (checkValues()) {
                    SavePatternTask savePatternTask = new SavePatternTask(this, coordinatorLayout);
                    savePatternTask.execute(pattern);
                }
                return true;
            default:
                onBackPressed();
                return true;
        }
    }

    public void onSaveSucceed(int patternId) {
        setResultAndFinish(RESULT_OK, patternId);
    }

    private void initLayout() {
        setLayout();
        setLayoutValues();
    }

    protected void setLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.pattern_edit_coordinator_layout);
        coordinatorLayout.setOnTouchListener(hideKeyboardOnClick);

        containerLayout = (PercentRelativeLayout) findViewById(R.id.pattern_edit_relative_layout);
        containerLayout.setOnTouchListener(hideKeyboardOnClick);

        typeSpinner = (Spinner) findViewById(R.id.pattern_edit_type_spinner);
        typeSpinner.setOnItemSelectedListener(patternTypeChangedListener);
        typeSpinner.setOnTouchListener(hideKeyboardOnClick);

        typeImage = (ImageView) findViewById(R.id.pattern_type_icon);
        typeText = (TextView) findViewById(R.id.pattern_type_label);

        numberBottlesByColumnEditText = (EditText) findViewById(R.id.pattern_edit_number_bottles_by_column);
        numberBottlesByRowEditText = (EditText) findViewById(R.id.pattern_edit_number_bottles_by_row);

        verticallyExpendableTextView = (TextView) findViewById(R.id.pattern_edit_vertically_expendable);
        verticallyExpendableTextView.setOnTouchListener(hideKeyboardOnClick);
        verticallyExpendableCheckbox = (CheckBox) findViewById(R.id.pattern_edit_vertically_expendable_checkbox);

        horizontallyExpendableTextView = (TextView) findViewById(R.id.pattern_edit_horizontally_expendable);
        horizontallyExpendableTextView.setOnTouchListener(hideKeyboardOnClick);
        horizontallyExpendableCheckbox = (CheckBox) findViewById(R.id.pattern_edit_horizontally_expendable_checkbox);

        invertPatternTextView = (TextView) findViewById(R.id.pattern_edit_inverted);
        invertPatternTextView.setOnTouchListener(hideKeyboardOnClick);
        invertPatternCheckbox = (CheckBox) findViewById(R.id.pattern_edit_inverted_checkbox);

        patternOverviewRecyclerView = (RecyclerView) findViewById(R.id.pattern_edit_overview_recyclerview);
        patternOverviewRecyclerView.setOnTouchListener(hideKeyboardOnClick);
    }

    private void setLayoutValues() {
        setVisibilityIndependantValues();
        updateFieldsVisibility();
        enableCheckboxes();
        setVisibilityDependantValues();
        pattern.computePlacesMap();
        updateAdapter();

        numberBottlesByColumnEditText.addTextChangedListener(dispositionChangedListener);
        numberBottlesByRowEditText.addTextChangedListener(dispositionChangedListener);
        verticallyExpendableCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);
        horizontallyExpendableCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);
        invertPatternCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);
    }

    protected void setVisibilityDependantValues() {
    }

    protected void enableCheckboxes() {
    }

    private void updateValuesAndAdapter() {
        updateVisibilityIndependantValuesInner();
        updateVisibilityIndependantValues();
        updateFieldsVisibility();
        updateVisibilityDependantValues();
        updateAdapter();
    }

    private void updateFieldsVisibility() {
        switch (pattern.Type) {
            case LINEAR:
                verticallyExpendableTextView.setVisibility(View.INVISIBLE);
                verticallyExpendableCheckbox.setVisibility(View.INVISIBLE);
                horizontallyExpendableTextView.setVisibility(View.INVISIBLE);
                horizontallyExpendableCheckbox.setVisibility(View.INVISIBLE);
                invertPatternTextView.setVisibility(View.INVISIBLE);
                invertPatternCheckbox.setVisibility(View.INVISIBLE);
                break;
            case STAGGERED_ROWS:
                verticallyExpendableTextView.setVisibility(View.VISIBLE);
                verticallyExpendableCheckbox.setVisibility(View.VISIBLE);
                horizontallyExpendableTextView.setVisibility(View.VISIBLE);
                horizontallyExpendableCheckbox.setVisibility(View.VISIBLE);
                invertPatternTextView.setVisibility(View.VISIBLE);
                invertPatternCheckbox.setVisibility(View.VISIBLE);
                break;
        }
        if (pattern.NumberBottlesByRow == 0 || pattern.NumberBottlesByColumn == 0) {
            patternOverviewRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            patternOverviewRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void updateAdapter() {
        if (pattern.NumberBottlesByRow == 0 || pattern.NumberBottlesByColumn == 0) {
            return;
        }
        patternOverviewRecyclerView.setLayoutManager(new GridLayoutManager(this, pattern.getNumberColumnsGridLayout()));
        createAdapter();
        patternOverviewRecyclerView.setAdapter(patternAdapter);
    }

    private void createAdapter() {
        patternAdapter = new PatternAdapter(this, pattern.getPlaceMapForDisplay(), new CoordinatesModel(pattern.getNumberRowsGridLayout(), pattern.getNumberColumnsGridLayout()),
                false, ScreenHelper.getScreenWidth(this) - overviewScreenWidthMarginLeft - overviewScreenWidthMarginRight, null);
    }

    private void updateVisibilityDependantValues() {
        pattern.IsHorizontallyExpendable = horizontallyExpendableCheckbox.getVisibility() == View.VISIBLE && horizontallyExpendableCheckbox.isChecked();
        pattern.IsVerticallyExpendable = verticallyExpendableCheckbox.getVisibility() == View.VISIBLE && verticallyExpendableCheckbox.isChecked();
        pattern.IsInverted = invertPatternCheckbox.getVisibility() == View.VISIBLE && invertPatternCheckbox.isChecked();
        pattern.computePlacesMap();
    }

    private boolean checkValues() {
        boolean isErrors = false;

        if (pattern.NumberBottlesByColumn == 0 || pattern.NumberBottlesByRow == 0) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_pattern_incorrect_rows_cols, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        }
        return !isErrors;
    }

    protected void updateVisibilityIndependantValuesInner() {
    }

    protected void updateVisibilityIndependantValues() {
        pattern.NumberBottlesByColumn = ControlsHelper.getIntValue(numberBottlesByColumnEditText);
        pattern.NumberBottlesByRow = ControlsHelper.getIntValue(numberBottlesByRowEditText);
    }

    protected void setVisibilityIndependantValues() {
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitDialogBuilder = new AlertDialog.Builder(this);
        exitDialogBuilder.setCancelable(true);
        exitDialogBuilder.setMessage(R.string.detail_exit_confirmation);
        exitDialogBuilder.setNegativeButton(R.string.global_stay, (DialogInterface dialog, int which) -> dialog.dismiss());
        exitDialogBuilder.setPositiveButton(R.string.global_exit, (DialogInterface dialog, int which) -> {
            dialog.dismiss();
            setResultAndFinish(RESULT_CANCELED, -1);
        });
        exitDialogBuilder.show();
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

    private void setResultAndFinish(int resultCode, int patternId) {
        Intent intent = new Intent();
        intent.putExtra("patternId", patternId);
        setResult(resultCode, intent);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // redraw the grid
        updateAdapter();
    }

    @Override
    protected void onResume() {
        NavigationManager.restartIfNeeded(this);
        super.onResume();
    }
}
