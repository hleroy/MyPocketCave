package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

public class PatternEditActivity extends AbstractPatternEditActivity {

    private int patternId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        patternId = bundle.getInt("patternId");
    }

    @Override
    protected void setToolbarTitle(ActionBar supportActionBar) {
        supportActionBar.setTitle(R.string.title_edit_pattern);
    }

    @Override
    protected void setVisibilityIndependantValues() {
        pattern = new PatternModelV2(PatternManager.getPattern(patternId));
        typeSpinner.setVisibility(View.GONE);
        typeImage.setVisibility(View.VISIBLE);
        typeText.setVisibility(View.VISIBLE);
        int patternTypeDrawableId = pattern.Type.DrawableResourceId;
        if (patternTypeDrawableId != -1) {
            typeImage.setImageDrawable(ContextCompat.getDrawable(this, patternTypeDrawableId));
        }
        typeText.setText(pattern.Type.StringResourceId);
        numberBottlesByColumnEditText.setText(String.valueOf(pattern.NumberBottlesByColumn));
        numberBottlesByRowEditText.setText(String.valueOf(pattern.NumberBottlesByRow));
        verticallyExpendableCheckbox.setChecked(pattern.IsVerticallyExpendable);
        horizontallyExpendableCheckbox.setChecked(pattern.IsHorizontallyExpendable);
        invertPatternCheckbox.setChecked(pattern.IsInverted);
    }

    @Override
    protected void setVisibilityDependantValues() {
        horizontallyExpendableCheckbox.setChecked(pattern.IsHorizontallyExpendable);
        verticallyExpendableCheckbox.setChecked(pattern.IsVerticallyExpendable);
        invertPatternCheckbox.setChecked(pattern.IsInverted);
    }

    @Override
    protected void enableCheckboxes() {
        verticallyExpendableCheckbox.setEnabled(false);
        horizontallyExpendableCheckbox.setEnabled(false);
        invertPatternCheckbox.setEnabled(false);
    }
}
