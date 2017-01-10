package com.myadridev.mypocketcave.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternSelectionAdapter;
import com.myadridev.mypocketcave.enums.ActivityRequestEnum;
import com.myadridev.mypocketcave.managers.PatternManager;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.List;

public class PatternSelectionActivity extends AppCompatActivity {

    private RecyclerView patternSelectionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_selection);
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
        patternSelectionRecyclerView.setLayoutManager(new GridLayoutManager(this, PatternManager.numberOfColumnsForDisplay));
        PatternSelectionAdapter patternSelectionAdapter = new PatternSelectionAdapter(this, recentPatternList);
        patternSelectionRecyclerView.setAdapter(patternSelectionAdapter);
        patternSelectionAdapter.addOnSelectionPatternClickListener((int patternId) -> {
            PatternManager.setLastUsedPattern(this, patternId);
            setResultAndFinish(RESULT_OK, patternId);
        });
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
}
