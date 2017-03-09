package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.CaveTypeSpinnerAdapter;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.enums.ActivityRequestEnum;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

public abstract class AbstractCaveEditActivity extends AppCompatActivity {

    public static final int overviewScreenWidthMarginLeft = 8;
    public static final int overviewScreenWidthMarginRight = 8;

    private final View.OnTouchListener hideKeyboardOnClick;
    private final View.OnTouchListener arrangementTooltipOnClick;
    public int OldClickedPatternId;
    public CoordinatesModel ClickedPatternCoordinates;
    protected CaveModel cave;
    protected EditText nameView;
    protected Spinner caveTypeView;
    protected CoordinatorLayout coordinatorLayout;

    protected EditText bulkBottlesNumberView;
    protected TextInputLayout bulkBottlesNumberInputLayout;

    protected EditText boxesNumberView;
    protected TextInputLayout boxesNumberInputLayout;

    protected EditText boxesPatternNumberBottlesByColumnView;
    protected TextInputLayout boxesPatternNumberBottlesByColumnInputLayout;

    protected EditText boxesPatternNumberBottlesByRowView;
    protected TextInputLayout boxesPatternNumberBottlesByRowInputLayout;

    protected PatternModel boxesPattern;
    protected CaveModel oldCave;
    private TextView arrangementView;
    private ImageView arrangementTooltipView;
    private TextView arrangementWarningView;
    private TextView boxesOverviewView;
    private RecyclerView boxesOverviewRecyclerView;
    private RecyclerView caveArrangementRecyclerView;
    private CaveArrangementAdapter caveArrangementAdapter;
    private PatternAdapter patternAdapter;
    private int lastCaveTypeSelected;

    private TextWatcher dispositionChangedListener = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            updateBoxesPatternValuesAndAdapter();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    protected AbstractCaveEditActivity() {
        hideKeyboardOnClick = (View v, MotionEvent event) -> {
            hideKeyboard();
            return false;
        };
        arrangementTooltipOnClick = (View v, MotionEvent event) -> {
            hideKeyboard();
            SnackbarHelper.displayInfoSnackbar(this, coordinatorLayout, R.string.message_cave_edit_arrangement_info, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            return false;
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initCave();
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
                    saveCave();
                    finish();
                }
                return true;
            default:
                onBackPressed();
                return true;
        }
    }

    private void initLayout() {
        setLayout();
        setLayoutValues();
    }

    protected void setLayout() {
        setCoordinatorLayout();
        coordinatorLayout.setOnTouchListener(hideKeyboardOnClick);
        nameView = (EditText) findViewById(R.id.cave_edit_name);
        caveTypeView = (Spinner) findViewById(R.id.cave_edit_type);
        caveTypeView.setOnTouchListener(hideKeyboardOnClick);
        arrangementView = (TextView) findViewById(R.id.cave_edit_arrangement);
        arrangementView.setOnTouchListener(hideKeyboardOnClick);
        arrangementTooltipView = (ImageView) findViewById(R.id.cave_edit_arrangement_tooltip);
        arrangementTooltipView.setOnTouchListener(arrangementTooltipOnClick);
        arrangementWarningView = (TextView) findViewById(R.id.cave_arrangement_warning);
        arrangementWarningView.setOnTouchListener(hideKeyboardOnClick);

        bulkBottlesNumberView = (EditText) findViewById(R.id.cave_edit_bulk_bottles_number);
        bulkBottlesNumberInputLayout = (TextInputLayout) findViewById(R.id.input_cave_edit_bulk_bottles_number);

        boxesNumberView = (EditText) findViewById(R.id.cave_edit_boxes_number);
        boxesNumberInputLayout = (TextInputLayout) findViewById(R.id.input_cave_edit_boxes_number);

        boxesPatternNumberBottlesByColumnView = (EditText) findViewById(R.id.cave_edit_pattern_number_bottles_by_column);
        boxesPatternNumberBottlesByColumnView.addTextChangedListener(dispositionChangedListener);
        boxesPatternNumberBottlesByColumnInputLayout = (TextInputLayout) findViewById(R.id.input_cave_edit_pattern_number_bottles_by_column);

        boxesPatternNumberBottlesByRowView = (EditText) findViewById(R.id.cave_edit_pattern_number_bottles_by_row);
        boxesPatternNumberBottlesByRowView.addTextChangedListener(dispositionChangedListener);
        boxesPatternNumberBottlesByRowInputLayout = (TextInputLayout) findViewById(R.id.input_cave_edit_pattern_number_bottles_by_row);

        boxesOverviewView = (TextView) findViewById(R.id.cave_edit_pattern_overview);
        boxesOverviewRecyclerView = (RecyclerView) findViewById(R.id.cave_edit_pattern_overview_recyclerview);

        caveArrangementRecyclerView = (RecyclerView) findViewById(R.id.cave_edit_arrangement_patterns);
        caveArrangementRecyclerView.setOnTouchListener(hideKeyboardOnClick);
    }

    protected abstract void setCoordinatorLayout();

    private void setLayoutValues() {
        CaveTypeSpinnerAdapter caveTypeAdapter = new CaveTypeSpinnerAdapter(this, false);
        caveTypeView.setAdapter(caveTypeAdapter);

        if (cave.Id > 0) {
            nameView.setText(cave.Name);
            int caveTypePosition = getCaveTypePosition(cave.CaveType.Id);
            caveTypeView.setSelection(caveTypePosition);
            lastCaveTypeSelected = caveTypePosition;
        }
        setArrangementByCaveType();
        caveTypeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != lastCaveTypeSelected) {
                    setArrangementByCaveType();
                    lastCaveTypeSelected = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
    }

    private void setArrangementByCaveType() {
        CaveTypeEnum caveType = (CaveTypeEnum) caveTypeView.getSelectedItem();
        switch (caveType) {
            case BULK:
                bulkBottlesNumberInputLayout.setVisibility(View.VISIBLE);
                if (cave.Id > 0) {
                    bulkBottlesNumberView.setText(String.valueOf(cave.CaveArrangement.NumberBottlesBulk));
                }
                arrangementTooltipView.setVisibility(View.GONE);
                boxesNumberInputLayout.setVisibility(View.GONE);
                boxesPatternNumberBottlesByColumnInputLayout.setVisibility(View.GONE);
                boxesPatternNumberBottlesByRowInputLayout.setVisibility(View.GONE);
                boxesOverviewView.setVisibility(View.GONE);
                boxesOverviewRecyclerView.setVisibility(View.GONE);
                caveArrangementRecyclerView.setVisibility(View.GONE);
                break;
            case BOX:
                arrangementTooltipView.setVisibility(View.VISIBLE);
                bulkBottlesNumberInputLayout.setVisibility(View.GONE);
                boxesNumberInputLayout.setVisibility(View.VISIBLE);
                boxesPatternNumberBottlesByColumnInputLayout.setVisibility(View.VISIBLE);
                boxesPatternNumberBottlesByRowInputLayout.setVisibility(View.VISIBLE);
                boxesOverviewView.setVisibility(View.VISIBLE);
                boxesOverviewRecyclerView.setVisibility(View.VISIBLE);
                caveArrangementRecyclerView.setVisibility(View.GONE);
                updateBoxesPatternValuesAndAdapter();
                if (cave.Id > 0) {
                    boxesNumberView.setText(String.valueOf(cave.CaveArrangement.NumberBoxes));
                    boxesPatternNumberBottlesByColumnView.setText(String.valueOf(cave.CaveArrangement.BoxesNumberBottlesByColumn));
                    boxesPatternNumberBottlesByRowView.setText(String.valueOf(cave.CaveArrangement.BoxesNumberBottlesByRow));
                }
                break;
            case RACK:
            case FRIDGE:
                arrangementTooltipView.setVisibility(View.VISIBLE);
                bulkBottlesNumberInputLayout.setVisibility(View.GONE);
                boxesNumberInputLayout.setVisibility(View.GONE);
                boxesPatternNumberBottlesByColumnInputLayout.setVisibility(View.GONE);
                boxesPatternNumberBottlesByRowInputLayout.setVisibility(View.GONE);
                boxesOverviewView.setVisibility(View.GONE);
                boxesOverviewRecyclerView.setVisibility(View.GONE);
                caveArrangementRecyclerView.setVisibility(View.VISIBLE);

                createCaveArrangementAdapter();
                break;
        }
    }

    private void updateBoxesPatternValuesAndAdapter() {
        setBoxesPatternValues();
        updateBoxesPatternAdapter();
    }

    private void setBoxesPatternValues() {
        boxesPattern = new PatternModel();
        boxesPattern.Type = PatternTypeEnum.LINEAR;
        String NumberBottlesByColumnString = boxesPatternNumberBottlesByColumnView.getText().toString();
        boxesPattern.NumberBottlesByColumn = NumberBottlesByColumnString.isEmpty() ? 0 : Integer.valueOf(NumberBottlesByColumnString);
        String NumberBottlesByRowString = boxesPatternNumberBottlesByRowView.getText().toString();
        boxesPattern.NumberBottlesByRow = NumberBottlesByRowString.isEmpty() ? 0 : Integer.valueOf(NumberBottlesByRowString);
        boxesPattern.computePlacesMap();
    }

    private void updateBoxesPatternAdapter() {
        if (boxesPattern.NumberBottlesByRow == 0 || boxesPattern.NumberBottlesByColumn == 0) {
            return;
        }
        createBoxesPatternAdapter();
        boxesOverviewRecyclerView.setLayoutManager(new GridLayoutManager(this, boxesPattern.getNumberColumnsGridLayout()));
        boxesOverviewRecyclerView.setAdapter(patternAdapter);
    }

    private void createBoxesPatternAdapter() {
        patternAdapter = new PatternAdapter(this, boxesPattern.getPlaceMapForDisplay(), new CoordinatesModel(boxesPattern.getNumberRowsGridLayout(), boxesPattern.getNumberColumnsGridLayout()),
                false, ScreenHelper.getScreenWidth(this) - overviewScreenWidthMarginLeft - overviewScreenWidthMarginRight, null);
    }

    private void createCaveArrangementAdapter() {
        cave.CaveArrangement.movePatternMapToRight();
        if (oldCave != null) {
            oldCave.CaveArrangement.movePatternMapToRight();
        }
        drawCaveArrangement();
    }

    private void drawCaveArrangement() {
        CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(cave.CaveArrangement.PatternMap.keySet());
        int nbCols = Math.max(maxRowCol.Col + 2, 3);
        int nbRows = maxRowCol.Row + 2;
        caveArrangementRecyclerView.setLayoutManager(new GridLayoutManager(this, nbCols));
        caveArrangementAdapter = new CaveArrangementAdapter(this, cave.CaveArrangement, nbRows, nbCols, ScreenHelper.getScreenWidth(this));
        caveArrangementRecyclerView.setAdapter(caveArrangementAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestEnum.PATTERN_SELECTION.Id) {
            if (resultCode == RESULT_OK && ClickedPatternCoordinates != null) {
                int patternId = data.getIntExtra("patternId", -1);
                if (patternId != -1) {
                    if (OldClickedPatternId != patternId) {
                        PatternModelWithBottles oldPattern = null;
                        if (oldCave != null && oldCave.CaveArrangement.PatternMap.containsKey(ClickedPatternCoordinates)) {
                            oldPattern = oldCave.CaveArrangement.PatternMap.get(ClickedPatternCoordinates);
                        }
                        if (oldPattern != null && oldPattern.Id == patternId) {
                            cave.CaveArrangement.PatternMap.put(ClickedPatternCoordinates, oldPattern);
                        } else {
                            cave.CaveArrangement.PatternMap.put(ClickedPatternCoordinates, new PatternModelWithBottles(PatternManager.getPattern(patternId)));
                        }
                        createCaveArrangementAdapter();
                    }
                }
            }
        }
    }

    private int getCaveTypePosition(int id) {
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
                resetPatternPositionIfNeeded();
                dialog.dismiss();
                AbstractCaveEditActivity.this.cancelCave();
                AbstractCaveEditActivity.this.finish();
            });
            exitDialogBuilder.show();
        } else {
            resetPatternPositionIfNeeded();
            cancelCave();
            finish();
        }
    }

    private void resetPatternPositionIfNeeded() {
        if (oldCave != null) {
            switch (oldCave.CaveType) {
                case FRIDGE:
                case RACK:
                    oldCave.CaveArrangement.movePatternMapToLeft();
                    break;
            }
        }
    }

    protected boolean hasDifferences() {
        return true;
    }

    protected void initCave() {
    }

    protected void saveCave() {
    }

    protected void cancelCave() {
    }

    protected boolean setValues() {
        boolean isValid = checkValues();

        if (!isValid) {
            return false;
        }

        cave.Name = nameView.getText().toString();
        cave.CaveType = (CaveTypeEnum) caveTypeView.getSelectedItem();
        if (oldCave != null && cave.CaveType != oldCave.CaveType) {
            cave.CaveArrangement.TotalUsed = 0;
        }

        switch (cave.CaveType) {
            case BULK:
                String NumberBottlesBulk = bulkBottlesNumberView.getText().toString();
                cave.CaveArrangement.NumberBottlesBulk = NumberBottlesBulk.isEmpty() ? 0 : Integer.valueOf(NumberBottlesBulk);
                cave.CaveArrangement.NumberBoxes = 0;
                cave.CaveArrangement.BoxesNumberBottlesByColumn = 0;
                cave.CaveArrangement.BoxesNumberBottlesByRow = 0;
                cave.CaveArrangement.PatternMap.clear();
                cave.CaveArrangement.computeTotalCapacityWithBulk();
                if (oldCave != null) {
                    cave.CaveArrangement.resetBottlesPlacedIfNeeded(this, cave.CaveType, oldCave);
                }
                break;
            case BOX:
                cave.CaveArrangement.NumberBottlesBulk = 0;
                String NumberBoxes = boxesNumberView.getText().toString();
                cave.CaveArrangement.NumberBoxes = NumberBoxes.isEmpty() ? 0 : Integer.valueOf(NumberBoxes);
                String boxesNumberBottlesByColumn = boxesPatternNumberBottlesByColumnView.getText().toString();
                cave.CaveArrangement.BoxesNumberBottlesByColumn = boxesNumberBottlesByColumn.isEmpty() ? 0 : Integer.valueOf(boxesNumberBottlesByColumn);
                String boxesNumberBottlesByRow = boxesPatternNumberBottlesByRowView.getText().toString();
                cave.CaveArrangement.BoxesNumberBottlesByRow = boxesNumberBottlesByRow.isEmpty() ? 0 : Integer.valueOf(boxesNumberBottlesByRow);
                if (oldCave != null) {
                    cave.CaveArrangement.resetBottlesPlacedIfNeeded(this, cave.CaveType, oldCave);
                }
                int patternId = PatternManager.addPattern(this, boxesPattern);
                cave.CaveArrangement.setPatternMapWithBoxes(this, patternId, oldCave);
                cave.CaveArrangement.setClickablePlaces();
                cave.CaveArrangement.computeTotalCapacityWithBoxes();
                cave.CaveArrangement.resetFloatNumberPlacedBottlesByIdMap();
                break;
            case FRIDGE:
            case RACK:
                cave.CaveArrangement.NumberBottlesBulk = 0;
                cave.CaveArrangement.NumberBoxes = 0;
                cave.CaveArrangement.BoxesNumberBottlesByColumn = 0;
                cave.CaveArrangement.BoxesNumberBottlesByRow = 0;
                cave.CaveArrangement.movePatternMapToLeft();
                cave.CaveArrangement.resetFloatNumberPlacedBottlesByIdMap();
                if (oldCave != null) {
                    oldCave.CaveArrangement.movePatternMapToLeft();
                }
                cave.CaveArrangement.setClickablePlaces();
                cave.CaveArrangement.computeTotalCapacityWithPattern();
                if (oldCave != null) {
                    cave.CaveArrangement.recomputeBottlesPlaced(this, oldCave);
                }
                break;
            default:
                cave.CaveArrangement.TotalCapacity = 0;
                break;
        }

        cave.trimAll();
        return true;
    }

    private boolean checkValues() {
        boolean isErrors = false;

        String name = nameView.getText().toString();

        if (name.isEmpty()) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_cave_no_name, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            isErrors = true;
        } else {
            CaveTypeEnum caveType = (CaveTypeEnum) caveTypeView.getSelectedItem();
            String numberBottlesBulkString = bulkBottlesNumberView.getText().toString();
            int numberBottlesBulk = numberBottlesBulkString.isEmpty() ? 0 : Integer.valueOf(numberBottlesBulkString);

            if (cave.CaveType == caveType && caveType == CaveTypeEnum.BULK && numberBottlesBulk < cave.CaveArrangement.TotalUsed) {
                SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_cave_bulk_not_enough, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
                isErrors = true;
            } else if (caveType == CaveTypeEnum.BOX && boxesPattern != null && (boxesPattern.NumberBottlesByColumn == 0 || boxesPattern.NumberBottlesByRow == 0)) {
                SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_pattern_incorrect_rows_cols, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
                isErrors = true;
            }

            final int existingCaveId = CaveManager.getExistingCaveId(cave.Id, name, caveType);
            if (existingCaveId > 0) {
                SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_cave_already_exists, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
                isErrors = true;
            }
        }
        return !isErrors;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // redraw the grid
        CaveTypeEnum caveType = (CaveTypeEnum) caveTypeView.getSelectedItem();
        switch (caveType) {
            case BOX:
                updateBoxesPatternAdapter();
                break;
            case RACK:
            case FRIDGE:
                drawCaveArrangement();
                break;
        }
    }
}
