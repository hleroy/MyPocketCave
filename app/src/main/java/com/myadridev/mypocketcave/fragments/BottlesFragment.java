package com.myadridev.mypocketcave.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.BottlesAdapter;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BottlesFragment extends Fragment implements IVisibleFragment {

    private List<BottleModel> allBottles;
    private TextView noBottlesLabelView;
    private View rootView;
    private RecyclerView bottlesRecyclerView;
    private BottlesAdapter bottlesAdapter;
    private TextView bottlesCountLabelView;
    private TextView bottlesCountDetailLabelView;

    private boolean isVisible;
    private int firstVisibleItemPosition;
    private boolean isDisplayedAtFirstLaunch;

    public BottlesFragment() {
        allBottles = new ArrayList<>();
        isVisible = false;
    }

    public void setIsDisplayedAtFirstLaunch(boolean isDisplayed) {
        isDisplayedAtFirstLaunch = isDisplayed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bottles, container, false);
        noBottlesLabelView = (TextView) rootView.findViewById(R.id.no_bottles_label);

        bottlesRecyclerView = (RecyclerView) rootView.findViewById(R.id.bottles_recyclerview);
        bottlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        bottlesCountLabelView = (TextView) rootView.findViewById(R.id.bottles_count);
        bottlesCountLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottlesCountDetailLabelView.setVisibility(View.VISIBLE);
                bottlesCountLabelView.setVisibility(View.GONE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bottlesRecyclerView.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE, R.id.bottles_count_detail);
            }
        });

        bottlesCountDetailLabelView = (TextView) rootView.findViewById(R.id.bottles_count_detail);
        bottlesCountDetailLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottlesCountDetailLabelView.setVisibility(View.GONE);
                bottlesCountLabelView.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bottlesRecyclerView.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE, R.id.bottles_count);
            }
        });

        setIsVisible(isDisplayedAtFirstLaunch);
        return rootView;
    }

    public void setIsVisible(boolean _isVisible) {
        boolean oldIsVisible = isVisible;
        isVisible = _isVisible;
        if (!oldIsVisible && isVisible) {
            refreshBottles();
            createAdapter();
            LinearLayoutManager layoutManager = (LinearLayoutManager) bottlesRecyclerView.getLayoutManager();
            layoutManager.scrollToPosition(firstVisibleItemPosition);
        }
        if (oldIsVisible && !isVisible) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) bottlesRecyclerView.getLayoutManager();
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        }
    }

    private void refreshBottles() {
        allBottles = BottleManager.getBottles();
        Collections.sort(allBottles);
        if (allBottles.isEmpty()) {
            noBottlesLabelView.setVisibility(View.VISIBLE);
            bottlesRecyclerView.setVisibility(View.GONE);
            bottlesCountLabelView.setVisibility(View.GONE);
            bottlesCountDetailLabelView.setVisibility(View.GONE);
        } else {
            noBottlesLabelView.setVisibility(View.GONE);
            bottlesRecyclerView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bottlesRecyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.bottles_count);

            bottlesCountLabelView.setVisibility(View.VISIBLE);
            bottlesCountLabelView.setText(getString(R.string.bottles_count, BottleManager.getBottlesCount()));

            bottlesCountDetailLabelView.setVisibility(View.GONE);
            bottlesCountDetailLabelView.setText(getString(R.string.bottles_count_detail,
                    BottleManager.getBottlesCount(WineColorEnum.RED),
                    BottleManager.getBottlesCount(WineColorEnum.WHITE),
                    BottleManager.getBottlesCount(WineColorEnum.ROSE),
                    BottleManager.getBottlesCount(WineColorEnum.CHAMPAGNE)));
        }
    }

    private void createAdapter() {
        bottlesAdapter = new BottlesAdapter(getContext(), allBottles);
        bottlesRecyclerView.setAdapter(bottlesAdapter);
    }
}
