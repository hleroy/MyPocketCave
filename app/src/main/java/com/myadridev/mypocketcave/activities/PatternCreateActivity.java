package com.myadridev.mypocketcave.activities;

import android.support.v7.app.ActionBar;
import android.view.View;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternTypeSpinnerAdapter;
import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

public class PatternCreateActivity extends AbstractPatternEditActivity {

    @Override
    protected void setToolbarTitle(ActionBar supportActionBar) {
        supportActionBar.setTitle(R.string.title_create_pattern);
    }

    @Override
    protected void setVisibilityDependantValues() {
        pattern.IsHorizontallyExpendable = horizontallyExpendableCheckbox.getVisibility() == View.VISIBLE && horizontallyExpendableCheckbox.isChecked();
        pattern.IsVerticallyExpendable = verticallyExpendableCheckbox.getVisibility() == View.VISIBLE && verticallyExpendableCheckbox.isChecked();
        pattern.IsInverted = invertPatternCheckbox.getVisibility() == View.VISIBLE && invertPatternCheckbox.isChecked();
    }

    @Override
    protected void updateVisibilityIndependantValuesInner() {
        pattern.Type = (PatternTypeEnumV2) typeSpinner.getSelectedItem();
    }

    @Override
    protected void setVisibilityIndependantValues() {
        pattern = new PatternModelV2();
        typeSpinner.setVisibility(View.VISIBLE);
        typeImage.setVisibility(View.GONE);
        typeText.setVisibility(View.GONE);
        PatternTypeSpinnerAdapter patternTypeAdapter = new PatternTypeSpinnerAdapter(this);
        typeSpinner.setAdapter(patternTypeAdapter);
        verticallyExpendableCheckbox.setChecked(true);
        horizontallyExpendableCheckbox.setChecked(true);
        invertPatternCheckbox.setChecked(false);
        updateVisibilityIndependantValuesInner();
    }

    @Override
    protected void enableCheckboxes() {
        verticallyExpendableCheckbox.setEnabled(true);
        horizontallyExpendableCheckbox.setEnabled(true);
        invertPatternCheckbox.setEnabled(true);
    }
}
