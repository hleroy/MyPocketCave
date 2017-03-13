package com.myadridev.mypocketcave.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternSelectionAdapter;
import com.myadridev.mypocketcave.enums.ActivityRequestEnum;
import com.myadridev.mypocketcave.layoutManagers.GridAutofitLayoutManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.PatternModel;
import com.myadridev.mypocketcave.tasks.UpdatePatternsOrderTask;

import java.util.List;

public class PatternSelectionActivity extends AppCompatActivity {

    private RecyclerView patternSelectionRecyclerView;
    private PatternSelectionAdapter patternSelectionAdapter;
    private GridAutofitLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pattern_selection);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        initLayout();
    }

    private void initLayout() {
        setLayout();
        setLayoutValues();
    }

    protected void setLayout() {
        patternSelectionRecyclerView = (RecyclerView) findViewById(R.id.pattern_selection_recyclerview);
    }

    private void setLayoutValues() {
        List<PatternModel> recentPatternList = PatternManager.getPatterns();
        layoutManager = new GridAutofitLayoutManager(this, (int) getResources().getDimension(R.dimen.pattern_image_size_large));
        patternSelectionRecyclerView.setLayoutManager(layoutManager);
        patternSelectionAdapter = new PatternSelectionAdapter(this, recentPatternList, layoutManager);
        patternSelectionRecyclerView.setAdapter(patternSelectionAdapter);
        patternSelectionAdapter.addOnSelectionPatternClickListener((int patternId) -> {
            setResultAndFinish(RESULT_OK, patternId);
            UpdatePatternsOrderTask updatePatternsOrderTask = new UpdatePatternsOrderTask(this);
            updatePatternsOrderTask.execute(patternId);
        });
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
    public void onBackPressed() {
        setResultAndFinish(RESULT_CANCELED, -1);
    }

    private void setResultAndFinish(int resultCode, int patternId) {
        Intent intent = new Intent();
        intent.putExtra("patternId", patternId);
        setResult(resultCode, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestEnum.CREATE_PATTERN.Id) {
            if (resultCode == RESULT_OK) {
                int patternId = data.getIntExtra("patternId", -1);
                if (patternId != -1) {
                    setResultAndFinish(RESULT_OK, patternId);
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // redraw the grid
        layoutManager.notifyColumnWidthChanged();
        if (patternSelectionAdapter != null) patternSelectionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        NavigationManager.restartIfNeeded(this);
        super.onResume();
    }
}
