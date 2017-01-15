package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

public abstract class AbstractCaveEditActivity extends AppCompatActivity {

    public static final int overviewScreenHeightPercent = 50;
    public static final int overviewScreenWidthMarginLeft = 8;
    public static final int overviewScreenWidthMarginRight = 8;

    private final View.OnTouchListener hideKeyboardOnClick;
    public int OldClickedPatternId;
    public CoordinatesModel ClickedPatternCoordinates;
    protected CaveModel cave;
    protected EditText nameView;
    protected Spinner caveTypeView;
    protected CoordinatorLayout coordinatorLayout;
    private TextView arrangementView;
    protected EditText bulkBottlesNumberView;

    protected EditText boxesNumberView;
    protected EditText boxesPatternNumberBottlesByColumnView;
    protected EditText boxesPatternNumberBottlesByRowView;
    private TextView boxesOverviewView;
    private RecyclerView boxesOverviewRecyclerView;
    protected PatternModel boxesPattern;
    private int screenHeight;
    private int screenWidth;

    private RecyclerView caveArrangementRecyclerView;
    private CaveArrangementAdapter caveArrangementAdapter;

    protected CaveModel oldCave;

    protected AbstractCaveEditActivity() {
        hideKeyboardOnClick = (View v, MotionEvent event) -> {
            hideKeyboard();
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
        nameView = (EditText) findViewById(R.id.cave_edit_name);
        caveTypeView = (Spinner) findViewById(R.id.cave_edit_type);
        caveTypeView.setOnTouchListener(hideKeyboardOnClick);
        arrangementView = (TextView) findViewById(R.id.cave_edit_arrangement);
        arrangementView.setOnTouchListener(hideKeyboardOnClick);
        bulkBottlesNumberView = (EditText) findViewById(R.id.cave_edit_bulk_bottles_number);

        boxesNumberView = (EditText) findViewById(R.id.cave_edit_boxes_number);
        boxesPatternNumberBottlesByColumnView = (EditText) findViewById(R.id.cave_edit_pattern_number_bottles_by_column);
        boxesPatternNumberBottlesByColumnView.addTextChangedListener(dispositionChangedListener);
        boxesPatternNumberBottlesByRowView = (EditText) findViewById(R.id.cave_edit_pattern_number_bottles_by_row);
        boxesPatternNumberBottlesByRowView.addTextChangedListener(dispositionChangedListener);
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
            caveTypeView.setSelection(getCaveTypePosition(cave.CaveType.Id));
        }
        setArrangementByCaveType();
        caveTypeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setArrangementByCaveType();
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
                bulkBottlesNumberView.setVisibility(View.VISIBLE);
                if (cave.Id > 0) {
                    bulkBottlesNumberView.setText(String.valueOf(cave.CaveArrangement.NumberBottlesBulk));
                }
                boxesNumberView.setVisibility(View.GONE);
                boxesPatternNumberBottlesByColumnView.setVisibility(View.GONE);
                boxesPatternNumberBottlesByRowView.setVisibility(View.GONE);
                boxesOverviewView.setVisibility(View.GONE);
                boxesOverviewRecyclerView.setVisibility(View.GONE);
                caveArrangementRecyclerView.setVisibility(View.GONE);
                break;
            case BOX:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.VISIBLE);
                boxesPatternNumberBottlesByColumnView.setVisibility(View.VISIBLE);
                boxesPatternNumberBottlesByRowView.setVisibility(View.VISIBLE);
                boxesOverviewView.setVisibility(View.VISIBLE);
                boxesOverviewRecyclerView.setVisibility(View.VISIBLE);
                caveArrangementRecyclerView.setVisibility(View.GONE);
                updateValuesAndAdapter();
                if (cave.Id > 0) {
                    boxesNumberView.setText(String.valueOf(cave.CaveArrangement.NumberBoxes));
                    boxesPatternNumberBottlesByColumnView.setText(String.valueOf(cave.CaveArrangement.BoxesNumberBottlesByColumn));
                    boxesPatternNumberBottlesByRowView.setText(String.valueOf(cave.CaveArrangement.BoxesNumberBottlesByRow));
                }
                break;
            case RACK:
            case FRIDGE:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.GONE);
                boxesPatternNumberBottlesByColumnView.setVisibility(View.GONE);
                boxesPatternNumberBottlesByRowView.setVisibility(View.GONE);
                boxesOverviewView.setVisibility(View.GONE);
                boxesOverviewRecyclerView.setVisibility(View.GONE);
                caveArrangementRecyclerView.setVisibility(View.VISIBLE);

                createAdapter();
                caveArrangementRecyclerView.setAdapter(caveArrangementAdapter);
                break;
        }
    }

    private TextWatcher dispositionChangedListener = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            updateValuesAndAdapter();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void updateValuesAndAdapter() {
        setBoxesPatternValues();
        updateAdapter();
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

    private void updateAdapter() {
        if (boxesPattern.NumberBottlesByRow == 0 || boxesPattern.NumberBottlesByColumn == 0) {
            return;
        }
        PatternAdapter patternAdapter = createBoxesPatternAdapter();
        boxesOverviewRecyclerView.setLayoutManager(new GridLayoutManager(this, boxesPattern.getNumberColumnsGridLayout()));
        boxesOverviewRecyclerView.setAdapter(patternAdapter);
    }

    private PatternAdapter createBoxesPatternAdapter() {
        if (screenHeight == 0 || screenWidth == 0) {
            setScreenDimensions();
        }
        return new PatternAdapter(this, boxesPattern.getPlaceMapForDisplay(), new CoordinatesModel(boxesPattern.getNumberRowsGridLayout(), boxesPattern.getNumberColumnsGridLayout()),
                false, screenWidth - overviewScreenWidthMarginLeft - overviewScreenWidthMarginRight,
                (screenHeight * overviewScreenHeightPercent / 100), null);
    }

    private void setScreenDimensions() {
        screenHeight = ScreenHelper.getScreenHeight(this);
        screenWidth = ScreenHelper.getScreenWidth(this);
    }

    private void createAdapter() {
        cave.CaveArrangement.movePatternMapToRight();
        if (oldCave != null) {
            oldCave.CaveArrangement.movePatternMapToRight();
        }
        CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(cave.CaveArrangement.PatternMap.keySet());
        int nbCols = Math.max(maxRowCol.Col + 2, 3);
        int nbRows = maxRowCol.Row + 2;
        caveArrangementRecyclerView.setLayoutManager(new GridLayoutManager(this, nbCols));
        caveArrangementAdapter = new CaveArrangementAdapter(this, cave.CaveArrangement, nbRows, nbCols, ScreenHelper.getScreenWidth(this));
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
                        createAdapter();
                        caveArrangementRecyclerView.setAdapter(caveArrangementAdapter);
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

        switch (cave.CaveType) {
            case BULK:
                String NumberBottlesBulk = bulkBottlesNumberView.getText().toString();
                cave.CaveArrangement.NumberBottlesBulk = NumberBottlesBulk.isEmpty() ? 0 : Integer.valueOf(NumberBottlesBulk);
                cave.CaveArrangement.NumberBoxes = 0;
                cave.CaveArrangement.BoxesNumberBottlesByColumn = 0;
                cave.CaveArrangement.BoxesNumberBottlesByRow = 0;
                cave.CaveArrangement.PatternMap.clear();
                cave.CaveArrangement.computeTotalCapacityWithBulk();
                break;
            case BOX:
                cave.CaveArrangement.NumberBottlesBulk = 0;
                String NumberBoxes = boxesNumberView.getText().toString();
                cave.CaveArrangement.NumberBoxes = NumberBoxes.isEmpty() ? 0 : Integer.valueOf(NumberBoxes);
                String boxesNumberBottlesByColumn = boxesPatternNumberBottlesByColumnView.getText().toString();
                cave.CaveArrangement.BoxesNumberBottlesByColumn = boxesNumberBottlesByColumn.isEmpty() ? 0 : Integer.valueOf(boxesNumberBottlesByColumn);
                String boxesNumberBottlesByRow = boxesPatternNumberBottlesByRowView.getText().toString();
                cave.CaveArrangement.BoxesNumberBottlesByRow = boxesNumberBottlesByRow.isEmpty() ? 0 : Integer.valueOf(boxesNumberBottlesByRow);
                int patternId = PatternManager.addPattern(this, boxesPattern);
                cave.CaveArrangement.setPatternMapWithBoxes(patternId, oldCave);
                cave.CaveArrangement.setClickablePlaces();
                cave.CaveArrangement.computeTotalCapacityWithBoxes();
                break;
            case FRIDGE:
            case RACK:
                cave.CaveArrangement.NumberBottlesBulk = 0;
                cave.CaveArrangement.NumberBoxes = 0;
                cave.CaveArrangement.BoxesNumberBottlesByColumn = 0;
                cave.CaveArrangement.BoxesNumberBottlesByRow = 0;
                cave.CaveArrangement.movePatternMapToLeft();
                if (oldCave != null) {
                    oldCave.CaveArrangement.movePatternMapToLeft();
                }
                cave.CaveArrangement.setClickablePlaces();
                cave.CaveArrangement.computeTotalCapacityWithPattern();
                break;
            default:
                cave.CaveArrangement.TotalCapacity = 0;
                break;
        }
        return true;
    }

    private boolean checkValues() {
        boolean isErrors = false;

        String name = nameView.getText().toString();

        if (name.isEmpty()) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.error_cave_no_name, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        } else if (boxesPattern != null && (boxesPattern.NumberBottlesByColumn == 0 || boxesPattern.NumberBottlesByRow == 0)) {
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.error_pattern_incorrect_rows_cols, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            isErrors = true;
        } else {
            CaveTypeEnum caveType = (CaveTypeEnum) caveTypeView.getSelectedItem();
            String numberBottlesBulkString = bulkBottlesNumberView.getText().toString();
            int numberBottlesBulk = numberBottlesBulkString.isEmpty() ? 0 : Integer.valueOf(numberBottlesBulkString);

            if (caveType == CaveTypeEnum.BULK && numberBottlesBulk < cave.CaveArrangement.TotalUsed) {
                final Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.error_cave_bulk_not_enough, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
                snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
                snackbar.show();
                isErrors = true;
            }

            final int existingCaveId = CaveManager.getExistingCaveId(cave.Id, name, caveType);
            if (existingCaveId > 0) {
                final Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.error_cave_already_exists, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(getString(R.string.error_ok), (View v) -> snackbar.dismiss());
                snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
                snackbar.show();
                isErrors = true;
            }
        }
        return !isErrors;
    }
}
