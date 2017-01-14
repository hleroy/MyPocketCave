package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
    private EditText bulkBottlesNumberView;

    private EditText boxesNumberView;
    private EditText boxesPatternNumberBottlesByColumnView;
    private EditText boxesPatternNumberBottlesByRowView;
    private TextView boxesOverviewView;
    private RecyclerView boxesOverviewRecyclerView;
    private PatternModel pattern;
    private int screenHeight;
    private int screenWidth;

    private RecyclerView caveArrangementRecyclerView;
    private CaveArrangementAdapter caveArrangementAdapter;

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
        pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.LINEAR;
        String NumberBottlesByColumnString = boxesPatternNumberBottlesByColumnView.getText().toString();
        pattern.NumberBottlesByColumn = NumberBottlesByColumnString.isEmpty() ? 0 : Integer.valueOf(NumberBottlesByColumnString);
        String NumberBottlesByRowString = boxesPatternNumberBottlesByRowView.getText().toString();
        pattern.NumberBottlesByRow = NumberBottlesByRowString.isEmpty() ? 0 : Integer.valueOf(NumberBottlesByRowString);
        pattern.computePlacesMap();
    }

    private void updateAdapter() {
        if (pattern.NumberBottlesByRow == 0 || pattern.NumberBottlesByColumn == 0) {
            return;
        }
        PatternAdapter patternAdapter = createBoxesPatternAdapter();
        boxesOverviewRecyclerView.setLayoutManager(new GridLayoutManager(this, pattern.getNumberColumnsGridLayout()));
        boxesOverviewRecyclerView.setAdapter(patternAdapter);
    }

    private PatternAdapter createBoxesPatternAdapter() {
        if (screenHeight == 0 || screenWidth == 0) {
            setScreenDimensions();
        }
        return new PatternAdapter(this, pattern.getPlaceMapForDisplay(), new CoordinatesModel(pattern.getNumberRowsGridLayout(), pattern.getNumberColumnsGridLayout()),
                false, screenWidth - overviewScreenWidthMarginLeft - overviewScreenWidthMarginRight,
                (screenHeight * overviewScreenHeightPercent / 100), null);
    }

    private void setScreenDimensions() {
        screenHeight = ScreenHelper.getScreenHeight(this);
        screenWidth = ScreenHelper.getScreenWidth(this);
    }

    private void createAdapter() {
        cave.CaveArrangement.movePatternMapToRight();
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
                        cave.CaveArrangement.PatternMap.put(ClickedPatternCoordinates, new PatternModelWithBottles(PatternManager.getPattern(patternId)));
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
        if (setValues()) {
            saveCave();
            finish();
        }
    }

    protected void initCave() {
    }

    protected void saveCave() {
    }

    protected void cancelCave() {
    }

    protected void removeCave() {
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
                int patternId = PatternManager.addPattern(this, pattern);
                cave.CaveArrangement.setPatternMapWithBoxes(patternId);
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
            AlertDialog.Builder noNameDialogBuilder = new AlertDialog.Builder(this);
            noNameDialogBuilder.setCancelable(true);
            noNameDialogBuilder.setMessage(R.string.error_cave_no_name);
            noNameDialogBuilder.setNegativeButton(R.string.global_stay_and_fix, (DialogInterface dialog, int which) -> dialog.dismiss());
            noNameDialogBuilder.setPositiveButton(R.string.global_exit, (DialogInterface dialog, int which) -> {
                dialog.dismiss();
                finish();
                cancelCave();
            });
            noNameDialogBuilder.show();
            isErrors = true;
        } else if (pattern != null && (pattern.NumberBottlesByColumn == 0 || pattern.NumberBottlesByRow == 0)) {
            AlertDialog.Builder IncorrectRowsColsDialogBuilder = new AlertDialog.Builder(this);
            IncorrectRowsColsDialogBuilder.setCancelable(true);
            IncorrectRowsColsDialogBuilder.setMessage(R.string.error_pattern_incorrect_rows_cols);
            IncorrectRowsColsDialogBuilder.setNegativeButton(R.string.global_stay_and_fix, (DialogInterface dialog, int which) -> dialog.dismiss());
            IncorrectRowsColsDialogBuilder.setPositiveButton(R.string.global_exit, (DialogInterface dialog, int which) -> {
                dialog.dismiss();
                finish();
                cancelCave();
            });
            IncorrectRowsColsDialogBuilder.show();
            isErrors = true;
        } else {
            CaveTypeEnum caveType = (CaveTypeEnum) caveTypeView.getSelectedItem();

            final int existingCaveId = CaveManager.getExistingCaveId(cave.Id, name, caveType);
            if (existingCaveId > 0) {
                AlertDialog.Builder existingCaveDialogBuilder = new AlertDialog.Builder(this);
                existingCaveDialogBuilder.setCancelable(true);
                existingCaveDialogBuilder.setMessage(R.string.error_cave_already_exists);
                existingCaveDialogBuilder.setNeutralButton(R.string.global_stay_and_fix, (DialogInterface dialog, int which) -> dialog.dismiss());
                existingCaveDialogBuilder.setNegativeButton(R.string.global_exit, (DialogInterface dialog, int which) -> {
                    dialog.dismiss();
                    finish();
                    cancelCave();
                });
                existingCaveDialogBuilder.setPositiveButton(R.string.global_merge, (DialogInterface dialog, int which) -> {
                    dialog.dismiss();
                    removeCave();
                    redirectToExistingCave(existingCaveId);
                });
                existingCaveDialogBuilder.show();
                isErrors = true;
            }
        }
        return !isErrors;
    }

    protected abstract void redirectToExistingCave(int existingCaveId);
}
