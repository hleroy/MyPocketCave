package com.myadridev.mypocketcave.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.CavesAdapter;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.layoutManagers.GridAutofitLayoutManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.models.CaveLightModel;

import java.util.Collections;
import java.util.List;

public class CavesFragment extends Fragment implements IVisibleFragment {

    private List<CaveLightModel> allCaves;
    private TextView noCavesLabelView;
    private View rootView;
    private RecyclerView cavesRecyclerView;
    private CavesAdapter cavesAdapter;
    private TextView cavesCountLabelView;
    private TextView cavesCountDetailLabelView;

    private boolean isVisible;
    private int firstVisibleItemPosition;
    private boolean isDisplayedAtFirstLaunch;

    public CavesFragment() {
        isVisible = false;
    }

    public void setIsDisplayedAtFirstLaunch(boolean isDisplayed) {
        isDisplayedAtFirstLaunch = isDisplayed;
    }

    public void setIsVisible(boolean _isVisible) {
        boolean oldIsVisible = isVisible;
        isVisible = _isVisible;
        if (!oldIsVisible && isVisible) {
            refreshCaves();
            createAdapter();
            GridAutofitLayoutManager layoutManager = (GridAutofitLayoutManager) cavesRecyclerView.getLayoutManager();
            layoutManager.scrollToPosition(firstVisibleItemPosition);
        }
        if (oldIsVisible && !isVisible) {
            GridAutofitLayoutManager layoutManager = (GridAutofitLayoutManager) cavesRecyclerView.getLayoutManager();
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        }
    }

    private void refreshCaves() {
        allCaves = CaveManager.getCaves();
        Collections.sort(allCaves);
        if (allCaves.isEmpty()) {
            noCavesLabelView.setVisibility(View.VISIBLE);
            cavesRecyclerView.setVisibility(View.GONE);
            cavesCountLabelView.setVisibility(View.GONE);
            cavesCountDetailLabelView.setVisibility(View.GONE);
        } else {
            noCavesLabelView.setVisibility(View.GONE);
            cavesRecyclerView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cavesRecyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.caves_count);

            cavesCountLabelView.setVisibility(View.VISIBLE);
            int cavesCount = CaveManager.getCavesCount();
            cavesCountLabelView.setText(getResources().getQuantityString(R.plurals.caves_count, cavesCount, cavesCount));

            cavesCountDetailLabelView.setVisibility(View.GONE);
            cavesCountDetailLabelView.setText(getString(R.string.caves_count_detail,
                    CaveManager.getCavesCount(CaveTypeEnum.BULK),
                    CaveManager.getCavesCount(CaveTypeEnum.BOX),
                    CaveManager.getCavesCount(CaveTypeEnum.FRIDGE),
                    CaveManager.getCavesCount(CaveTypeEnum.RACK)));
        }
    }

    private void createAdapter() {
        cavesAdapter = new CavesAdapter(getContext(), allCaves);
        cavesRecyclerView.setAdapter(cavesAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_caves, container, false);
        noCavesLabelView = (TextView) rootView.findViewById(R.id.no_caves_label);

        cavesRecyclerView = (RecyclerView) rootView.findViewById(R.id.caves_recyclerview);
        cavesRecyclerView.setLayoutManager(new GridAutofitLayoutManager(getActivity(), (int) getResources().getDimension(R.dimen.cave_image_size_large)));

        cavesCountLabelView = (TextView) rootView.findViewById(R.id.caves_count);
        cavesCountLabelView.setOnClickListener((View v) -> {
            cavesCountDetailLabelView.setVisibility(View.VISIBLE);
            cavesCountLabelView.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cavesRecyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.caves_count_detail);
        });

        cavesCountDetailLabelView = (TextView) rootView.findViewById(R.id.caves_count_detail);
        cavesCountDetailLabelView.setOnClickListener((View v) -> {
            cavesCountDetailLabelView.setVisibility(View.GONE);
            cavesCountLabelView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cavesRecyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.caves_count);
        });

        setIsVisible(isDisplayedAtFirstLaunch);
        return rootView;
    }
}
