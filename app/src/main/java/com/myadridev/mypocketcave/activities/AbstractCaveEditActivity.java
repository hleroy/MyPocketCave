package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.managers.CaveArrangementManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesModelManager;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public abstract class AbstractCaveEditActivity extends AppCompatActivity {

    protected CaveModel cave;
    protected EditText nameView;
    protected Spinner caveTypeView;
    protected CoordinatorLayout coordinatorLayout;
    private final View.OnTouchListener hideKeyboardOnClick;
    private TextView arrangementView;
    private EditText bulkBottlesNumberView;
    private EditText boxesNumberView;
    private EditText boxesBottlesNumberView;
    private PercentRelativeLayout caveArrangementContainerView;
    private RecyclerView caveArrangementRecyclerView;

    protected AbstractCaveEditActivity() {
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
        boxesBottlesNumberView = (EditText) findViewById(R.id.cave_edit_boxes_bottles_number);
        caveArrangementContainerView = (PercentRelativeLayout) findViewById(R.id.cave_edit_arrangement_patterns_container);
        caveArrangementContainerView.setOnTouchListener(hideKeyboardOnClick);
        caveArrangementRecyclerView = (RecyclerView) findViewById(R.id.cave_edit_arrangement_patterns);
        caveArrangementRecyclerView.setOnTouchListener(hideKeyboardOnClick);
    }

    protected abstract void setCoordinatorLayout();

    private void setLayoutValues() {
        CaveTypeSpinnerAdapter caveTypeAdapter = new CaveTypeSpinnerAdapter(this, false);
        caveTypeView.setAdapter(caveTypeAdapter);

        if (cave.Id > 0) {
            nameView.setText(cave.Name);
            caveTypeView.setSelection(getCaveTypePosition(cave.CaveType.id));
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
                boxesBottlesNumberView.setVisibility(View.GONE);
                caveArrangementContainerView.setVisibility(View.GONE);
                break;
            case BOX:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.VISIBLE);
                boxesBottlesNumberView.setVisibility(View.VISIBLE);
                if (cave.Id > 0) {
                    boxesNumberView.setText(String.valueOf(cave.CaveArrangement.NumberBoxes));
                    boxesBottlesNumberView.setText(String.valueOf(cave.CaveArrangement.NumberBottlesPerBox));
                }
                caveArrangementContainerView.setVisibility(View.GONE);
                break;
            case RACK:
            case FRIDGE:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.GONE);
                boxesBottlesNumberView.setVisibility(View.GONE);
                caveArrangementContainerView.setVisibility(View.VISIBLE);

                CoordinatesModel maxRawCol = CoordinatesModelManager.Instance.getMaxRawCol(cave.CaveArrangement.PatternMap.keySet());
                caveArrangementRecyclerView.setLayoutManager(new GridLayoutManager(this, Math.max(maxRawCol.Col + 1, 2)));
                CaveArrangementAdapter caveArrangementAdapter = new CaveArrangementAdapter(this, cave.CaveArrangement.PatternMap, new CoordinatesModel(maxRawCol.Raw + 1, maxRawCol.Col + 1));
                caveArrangementRecyclerView.setAdapter(caveArrangementAdapter);
                break;
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

    protected void initCave() {}
    protected void saveCave() {}
    protected void cancelCave() {}
    protected void removeCave() {}

    protected boolean setValues() {
        boolean isValid = checkValues();

        if (!isValid) {
            return false;
        }

        cave.Name = nameView.getText().toString();
        cave.CaveType = (CaveTypeEnum) caveTypeView.getSelectedItem();
        String NumberBottlesBulk = bulkBottlesNumberView.getText().toString();
        cave.CaveArrangement.NumberBottlesBulk = NumberBottlesBulk.isEmpty() ? 0 : Integer.valueOf(NumberBottlesBulk);
        String NumberBoxes = boxesNumberView.getText().toString();
        cave.CaveArrangement.NumberBoxes = NumberBoxes.isEmpty() ? 0 : Integer.valueOf(NumberBoxes);
        String NumberBottlesPerBox = boxesBottlesNumberView.getText().toString();
        cave.CaveArrangement.NumberBottlesPerBox = NumberBottlesPerBox.isEmpty() ? 0 : Integer.valueOf(NumberBottlesPerBox);

        switch (cave.CaveType) {
            case BULK:
                cave.CaveArrangement.TotalCapacity = CaveArrangementManager.Instance.computeTotalCapacityWithBulk(cave.CaveArrangement);
                break;
            case BOX:
                cave.CaveArrangement.TotalCapacity = CaveArrangementManager.Instance.computeTotalCapacityWithBoxes(cave.CaveArrangement);
                break;
            case FRIDGE:
            case RACK:
                cave.CaveArrangement.TotalCapacity = CaveArrangementManager.Instance.computeTotalCapacityWithPattern(cave.CaveArrangement);
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
            AlertDialog.Builder noNameBottleDialogBuilder = new AlertDialog.Builder(this);
            noNameBottleDialogBuilder.setCancelable(true);
            noNameBottleDialogBuilder.setMessage(R.string.error_cave_no_name);
            noNameBottleDialogBuilder.setNegativeButton(R.string.global_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            noNameBottleDialogBuilder.setPositiveButton(R.string.global_exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    cancelCave();
                }
            });
            noNameBottleDialogBuilder.show();
            isErrors = true;
        } else {
            CaveTypeEnum caveType = (CaveTypeEnum) caveTypeView.getSelectedItem();

            final int existingCaveId = CaveManager.Instance.getExistingCaveId(cave.Id, name, caveType);
            if (existingCaveId > 0) {
                AlertDialog.Builder existingBottleDialogBuilder = new AlertDialog.Builder(this);
                existingBottleDialogBuilder.setCancelable(true);
                existingBottleDialogBuilder.setMessage(R.string.error_cave_already_exists);
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
                        cancelCave();
                    }
                });
                existingBottleDialogBuilder.setPositiveButton(R.string.global_merge, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeCave();
                        redirectToExistingCave(existingCaveId);
                    }
                });
                existingBottleDialogBuilder.show();
                isErrors = true;
            }
        }
        return !isErrors;
    }

    protected abstract void redirectToExistingCave(int existingCaveId);
}
