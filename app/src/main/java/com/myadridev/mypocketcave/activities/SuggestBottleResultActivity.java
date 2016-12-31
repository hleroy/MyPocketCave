package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.SuggestBottlesResultAdapter;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.SuggestBottleResultModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestBottleResultActivity extends AppCompatActivity {

    private TextView noBottlesLabelView;
    private RecyclerView bottlesRecyclerView;
    private List<SuggestBottleResultModel> allBottles;
    private CoordinatorLayout coordinatorLayout;
    private SuggestBottlesResultAdapter bottlesResultAdapter;
    private TextView bottlesCountLabelView;
    private TextView bottlesCountDetailLabelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_bottle_result);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.suggest_bottle_result_coordinator_layout);

        noBottlesLabelView = (TextView) findViewById(R.id.suggest_bottle_result_no_bottles_label);

        bottlesRecyclerView = (RecyclerView) findViewById(R.id.suggest_bottle_result_bottles_recyclerview);
        bottlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottlesCountLabelView = (TextView) findViewById(R.id.suggest_bottle_result_bottles_count);
        bottlesCountLabelView.setOnClickListener(v -> {
            bottlesCountDetailLabelView.setVisibility(View.VISIBLE);
            bottlesCountLabelView.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bottlesRecyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.suggest_bottle_result_bottles_count_detail);
        });

        bottlesCountDetailLabelView = (TextView) findViewById(R.id.suggest_bottle_result_bottles_count_detail);
        bottlesCountDetailLabelView.setOnClickListener(v -> {
            bottlesCountDetailLabelView.setVisibility(View.GONE);
            bottlesCountLabelView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bottlesRecyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.suggest_bottle_result_bottles_count);
        });

        Bundle bundle = getIntent().getExtras();
        String serializedSearchCriteria = bundle.getString("searchCriteria");

        ObjectMapper jsonMapper = new ObjectMapper();
        SuggestBottleCriteria searchCriteria;
        try {
            searchCriteria = jsonMapper.readValue(serializedSearchCriteria, SuggestBottleCriteria.class);
        } catch (IOException e) {
            e.printStackTrace();

            final Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.error_technical), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.error_ok), v -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorError));
            snackbar.show();
            return;
        }

        allBottles = BottleManager.getSuggestBottles(searchCriteria);

        bottlesResultAdapter = new SuggestBottlesResultAdapter(this, allBottles);
        bottlesResultAdapter.addOnSeeMoreClickListener(() -> setVisibility(bottlesResultAdapter));
        bottlesRecyclerView.setAdapter(bottlesResultAdapter);

        setVisibility(bottlesResultAdapter);
    }

    private void setVisibility(SuggestBottlesResultAdapter bottlesResultAdapter) {
        if (bottlesResultAdapter.isRecyclerViewDisplayed()) {
            bottlesRecyclerView.setVisibility(View.VISIBLE);
            if (bottlesResultAdapter.getNumberDisplayedBottles() == 0) {
                noBottlesLabelView.setVisibility(View.VISIBLE);
            } else {
                noBottlesLabelView.setVisibility(View.GONE);
            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bottlesRecyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.suggest_bottle_result_bottles_count);

            bottlesCountLabelView.setVisibility(View.VISIBLE);
            int bottlesCount = getBottlesCount(allBottles, bottlesResultAdapter.getIsAllBottlesVisible());
            bottlesCountLabelView.setText(getResources().getQuantityString(R.plurals.bottles_count, bottlesCount, bottlesCount));

            bottlesCountDetailLabelView.setVisibility(View.GONE);
            bottlesCountDetailLabelView.setText(getString(R.string.bottles_count_detail,
                    getBottlesCount(allBottles, bottlesResultAdapter.getIsAllBottlesVisible(), WineColorEnum.RED),
                    getBottlesCount(allBottles, bottlesResultAdapter.getIsAllBottlesVisible(), WineColorEnum.WHITE),
                    getBottlesCount(allBottles, bottlesResultAdapter.getIsAllBottlesVisible(), WineColorEnum.ROSE),
                    getBottlesCount(allBottles, bottlesResultAdapter.getIsAllBottlesVisible(), WineColorEnum.CHAMPAGNE)));
        } else {
            bottlesRecyclerView.setVisibility(View.GONE);
            noBottlesLabelView.setVisibility(View.VISIBLE);
            bottlesCountLabelView.setVisibility(View.GONE);
            bottlesCountDetailLabelView.setVisibility(View.GONE);
        }
    }

    private int getBottlesCount(List<SuggestBottleResultModel> allBottles, boolean isAllBottlesVisible) {
        List<BottleModel> bottles = getBottleModels(allBottles, isAllBottlesVisible);

        return BottleManager.getBottlesCount(bottles);
    }

    @NonNull
    private List<BottleModel> getBottleModels(List<SuggestBottleResultModel> allBottles, boolean isAllBottlesVisible) {
        int scoreMin = isAllBottlesVisible ? 0 : SuggestBottleCriteria.NumberOfCriteria;
        List<BottleModel> bottles = new ArrayList<>();
        for (SuggestBottleResultModel suggestBottle : allBottles) {
            if (suggestBottle.Score >= scoreMin) {
                bottles.add(suggestBottle.Bottle);
            }
        }
        return bottles;
    }

    private int getBottlesCount(List<SuggestBottleResultModel> allBottles, boolean isAllBottlesVisible, WineColorEnum wineColor) {
        List<BottleModel> bottles = getBottleModels(allBottles, isAllBottlesVisible);
        return BottleManager.getBottlesCount(bottles, wineColor);
    }
}
