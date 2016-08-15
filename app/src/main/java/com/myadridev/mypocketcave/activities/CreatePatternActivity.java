package com.myadridev.mypocketcave.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.adapters.PatternTypeSpinnerAdapter;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

public class CreatePatternActivity extends AppCompatActivity {

    public static final int overviewScreenHeightPercent = 50;
    public static final int overviewScreenWidthMarginLeft = 8;
    public static final int overviewScreenWidthMarginRight = 8;

    private Spinner typeSpinner;
    private EditText numberBottlesByColumnEditText;
    private EditText numberBottlesByRowEditText;
    private TextView verticallyExpendableTextView;
    private CheckBox verticallyExpendableCheckbox;
    private TextView horizontallyExpendableTextView;
    private CheckBox horizontallyExpendableCheckbox;
    private TextView invertPatternTextView;
    private CheckBox invertPatternCheckbox;
    private final View.OnTouchListener hideKeyboardOnClick;
    private PercentRelativeLayout containerLayout;

    private RecyclerView patternOverviewRecyclerView;
    private PatternModel pattern;
    private int screenHeight;
    private int screenWidth;

    protected CreatePatternActivity() {
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
        setContentView(R.layout.create_pattern);
        initLayout();
    }

    private void initLayout() {
        setLayout();
        setLayoutValues();
    }

    protected void setLayout() {
        containerLayout = (PercentRelativeLayout) findViewById(R.id.create_pattern_relative_layout);
        containerLayout.setOnTouchListener(hideKeyboardOnClick);

        typeSpinner = (Spinner) findViewById(R.id.create_pattern_type);
        typeSpinner.setOnItemSelectedListener(patternTypeChangedListener);
        typeSpinner.setOnTouchListener(hideKeyboardOnClick);

        numberBottlesByColumnEditText = (EditText) findViewById(R.id.create_pattern_number_bottles_by_column);
        numberBottlesByColumnEditText.addTextChangedListener(dispositionChangedListener);
        numberBottlesByRowEditText = (EditText) findViewById(R.id.create_pattern_number_bottles_by_row);
        numberBottlesByRowEditText.addTextChangedListener(dispositionChangedListener);

        verticallyExpendableTextView = (TextView) findViewById(R.id.create_pattern_vertically_expendable);
        verticallyExpendableTextView.setOnTouchListener(hideKeyboardOnClick);
        verticallyExpendableCheckbox = (CheckBox) findViewById(R.id.create_pattern_vertically_expendable_checkbox);
        verticallyExpendableCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);

        horizontallyExpendableTextView = (TextView) findViewById(R.id.create_pattern_horizontally_expendable);
        horizontallyExpendableTextView.setOnTouchListener(hideKeyboardOnClick);
        horizontallyExpendableCheckbox = (CheckBox) findViewById(R.id.create_pattern_horizontally_expendable_checkbox);
        horizontallyExpendableCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);

        invertPatternTextView = (TextView) findViewById(R.id.create_pattern_inverted);
        invertPatternTextView.setOnTouchListener(hideKeyboardOnClick);
        invertPatternCheckbox = (CheckBox) findViewById(R.id.create_pattern_inverted_checkbox);
        invertPatternCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);

        patternOverviewRecyclerView = (RecyclerView) findViewById(R.id.create_pattern_overview_recyclerview);
        patternOverviewRecyclerView.setOnTouchListener(hideKeyboardOnClick);
    }

    private CompoundButton.OnCheckedChangeListener checkboxCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            hideKeyboard();
            updateValuesAndAdapter();
        }
    };

    private void updateValuesAndAdapter() {
        setVisibilityIndependantValues();
        updateFieldsVisibility();
        setVisibilityDependantValues();
        updateAdapter();
    }

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

    private void setLayoutValues() {
        PatternTypeSpinnerAdapter patternTypeAdapter = new PatternTypeSpinnerAdapter(this);
        typeSpinner.setAdapter(patternTypeAdapter);
        verticallyExpendableCheckbox.setChecked(true);
        horizontallyExpendableCheckbox.setChecked(true);
        invertPatternCheckbox.setChecked(false);
        updateValuesAndAdapter();
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
        PatternAdapter patternAdapter = createAdapter();
        patternOverviewRecyclerView.setLayoutManager(new GridLayoutManager(this, pattern.getNumberColumnsGridLayout()));
        patternOverviewRecyclerView.setAdapter(patternAdapter);
    }

    private PatternAdapter createAdapter() {
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

    private void setVisibilityDependantValues() {
        pattern.IsHorizontallyExpendable = horizontallyExpendableCheckbox.getVisibility() == View.VISIBLE && horizontallyExpendableCheckbox.isChecked();
        pattern.IsVerticallyExpendable = verticallyExpendableCheckbox.getVisibility() == View.VISIBLE && verticallyExpendableCheckbox.isChecked();
        pattern.IsInverted = invertPatternCheckbox.getVisibility() == View.VISIBLE && invertPatternCheckbox.isChecked();
        pattern.computePlacesMap();
    }

    private boolean checkValues() {
        boolean isErrors = false;

        setVisibilityIndependantValues();
        updateFieldsVisibility();
        setVisibilityDependantValues();

        if (pattern.NumberBottlesByColumn == 0 || pattern.NumberBottlesByRow == 0) {
            AlertDialog.Builder IncorrectRowsColsDialogBuilder = new AlertDialog.Builder(this);
            IncorrectRowsColsDialogBuilder.setCancelable(true);
            IncorrectRowsColsDialogBuilder.setMessage(R.string.error_pattern_incorrect_rows_cols);
            IncorrectRowsColsDialogBuilder.setNegativeButton(R.string.global_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            IncorrectRowsColsDialogBuilder.setPositiveButton(R.string.global_exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    setResultAndFinish(RESULT_CANCELED, -1);
                }
            });
            IncorrectRowsColsDialogBuilder.show();
            isErrors = true;
        }
        return !isErrors;
    }

    private void setVisibilityIndependantValues() {
        pattern = new PatternModel();
        pattern.Type = (PatternTypeEnum) typeSpinner.getSelectedItem();
        pattern.NumberBottlesByColumn = getIntValue(numberBottlesByColumnEditText);
        pattern.NumberBottlesByRow = getIntValue(numberBottlesByRowEditText);
    }

    private int getIntValue(EditText textField) {
        String valueString = textField.getText().toString();
        return valueString.isEmpty() ? 0 : Integer.valueOf(valueString);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        if (checkValues()) {
            setResultAndFinish(RESULT_OK, PatternManager.Instance.addPattern(pattern));
        }
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
}
