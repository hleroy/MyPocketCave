package com.myadridev.mypocketcave.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.PatternAdapter;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.managers.CaveArrangementManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesModelManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

import java.util.Map;

public class CaveDetailActivity extends AppCompatActivity {

    private CaveModel cave;
    private ImageView caveTypeIconView;
    private TextView caveTypeView;
    private TextView capacityUsedView;
    private Toolbar toolbar;
    private TextView arrangementView;
    private TextView bulkBottlesNumberView;
    private TextView boxesNumberView;
    private TextView boxesBottlesNumberView;
    private PercentRelativeLayout patternContainerView;
    private RecyclerView patternRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cave_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar_cave);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        refreshCave(bundle.getInt("caveId"));

        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_cave);
        assert fabEdit != null;
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationManager.navigateToCaveEdit(CaveDetailActivity.this, cave.Id);
                finish();
            }
        });

        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete_cave);
        assert fabDelete != null;
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteCaveDialogBuilder = new AlertDialog.Builder(CaveDetailActivity.this);
                deleteCaveDialogBuilder.setCancelable(true);
                deleteCaveDialogBuilder.setMessage(R.string.cave_delete_confirmation);
                deleteCaveDialogBuilder.setNegativeButton(R.string.global_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                deleteCaveDialogBuilder.setPositiveButton(R.string.global_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CaveManager.Instance.removeCave(cave.Id);
                        dialog.dismiss();
                        finish();
                    }
                });
                deleteCaveDialogBuilder.show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setLayout();
    }

    private void setLayout() {
        caveTypeIconView = (ImageView) findViewById(R.id.cave_detail_type_icon);
        caveTypeView = (TextView) findViewById(R.id.cave_detail_type);
        capacityUsedView = (TextView) findViewById(R.id.cave_detail_capacity_used_total);
        arrangementView = (TextView) findViewById(R.id.cave_detail_arrangement);
        bulkBottlesNumberView = (TextView) findViewById(R.id.cave_detail_bulk_bottles_number);
        boxesNumberView = (TextView) findViewById(R.id.cave_detail_boxes_number);
        boxesBottlesNumberView = (TextView) findViewById(R.id.cave_detail_boxes_bottles_number);
        patternContainerView = (PercentRelativeLayout) findViewById(R.id.cave_detail_arrangement_pattern_container);
        patternRecyclerView = (RecyclerView) findViewById(R.id.cave_detail_arrangement_pattern);
    }

    private void setLayoutValues() {
        int caveTypeDrawableId = cave.CaveType.drawableResourceId;
        if (caveTypeDrawableId != -1) {
            caveTypeIconView.setImageDrawable(ContextCompat.getDrawable(this, caveTypeDrawableId));
        }
        caveTypeView.setText(cave.CaveType.stringResourceId);
        capacityUsedView.setText(getString(R.string.cave_used_capacity, cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
        switch (cave.CaveType) {
            case BULK:
                bulkBottlesNumberView.setVisibility(View.VISIBLE);
                bulkBottlesNumberView.setText(getString(R.string.cave_bulk_bottles_number_detail, cave.CaveArrangement.NumberBottlesBulk));
                boxesNumberView.setVisibility(View.GONE);
                boxesBottlesNumberView.setVisibility(View.GONE);
                patternContainerView.setVisibility(View.GONE);
                break;
            case BOX:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.VISIBLE);
                boxesNumberView.setText(getString(R.string.cave_boxes_number_detail, cave.CaveArrangement.NumberBoxes));
                boxesBottlesNumberView.setVisibility(View.VISIBLE);
                boxesBottlesNumberView.setText(getString(R.string.cave_boxes_bottles_number_detail, cave.CaveArrangement.NumberBottlesPerBox));
                patternContainerView.setVisibility(View.GONE);
                break;
            case RACK:
            case FRIDGE:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.GONE);
                boxesBottlesNumberView.setVisibility(View.GONE);
                patternContainerView.setVisibility(View.VISIBLE);

                Map<CoordinatesModel, CavePlaceTypeEnum> caveArrangementPlaceMap = CaveArrangementManager.Instance.getPlaceMap(cave.CaveArrangement);
                CoordinatesModel maxRawCol = CoordinatesModelManager.Instance.getMaxRawCol(caveArrangementPlaceMap.keySet());
                if (maxRawCol.Col > 0) {
                    patternRecyclerView.setLayoutManager(new GridLayoutManager(this, maxRawCol.Col));
                    PatternAdapter patternAdapter = new PatternAdapter(this, caveArrangementPlaceMap, maxRawCol, true);
                    patternRecyclerView.setAdapter(patternAdapter);
                } else {
                    patternRecyclerView.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCave();
        refreshActionBar();
        setLayoutValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                return true;
        }
    }

    private void refreshActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(cave.Name);
        }
    }

    private void refreshCave() {
        refreshCave(cave.Id);
    }

    private void refreshCave(int caveId) {
        cave = new CaveModel(CaveManager.Instance.getCave(caveId));
    }
}
