package com.myadridev.mypocketcave.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.helpers.CompatibilityHelper;
import com.myadridev.mypocketcave.helpers.FloatingActionButtonHelper;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class CaveDetailActivity extends AppCompatActivity {

    private CaveModel cave;
    private ImageView caveTypeIconView;
    private TextView caveTypeView;
    private TextView capacityUsedView;
    private Toolbar toolbar;
    private TextView bulkBottlesNumberView;
    private TextView boxesNumberView;
    private TextView boxesBottlesNumberView;
    private RecyclerView arrangementRecyclerView;

    private FloatingActionButton fabMenu;
    private FloatingActionButton fabCloseMenu;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;
    private int caveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cave_detail);
        cave = null;

        toolbar = (Toolbar) findViewById(R.id.toolbar_cave);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        caveId = bundle.getInt("caveId");

        setupFloatingActionButtons();
        setupFloatingActionButtonsVisibility();
    }

    private void setupFloatingActionButtons() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu_cave);
        fabMenu.setOnClickListener((View view) -> openFloatingActionButtonsMenu());

        fabCloseMenu = (FloatingActionButton) findViewById(R.id.fab_close_menu_cave);
        fabCloseMenu.setOnClickListener((View view) -> closeFloatingActionButtonsMenu());

        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_cave);
        fabEdit.setOnClickListener((View view) -> {
            NavigationManager.navigateToCaveEdit(CaveDetailActivity.this, cave.Id);
            finish();
        });

        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete_cave);
        fabDelete.setOnClickListener((View view) -> {
            AlertDialog.Builder deleteCaveDialogBuilder = new AlertDialog.Builder(CaveDetailActivity.this);
            deleteCaveDialogBuilder.setCancelable(true);
            deleteCaveDialogBuilder.setMessage(R.string.cave_delete_confirmation);
            deleteCaveDialogBuilder.setNegativeButton(R.string.global_no, (DialogInterface dialog, int which) -> dialog.dismiss());
            deleteCaveDialogBuilder.setPositiveButton(R.string.global_yes, (DialogInterface dialog, int which) -> {
                CaveManager.removeCave(this, cave);
                dialog.dismiss();
                finish();
            });
            deleteCaveDialogBuilder.show();
        });
    }

    private void closeFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabCloseMenu, 0);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabEdit, 1);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabDelete, 2);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabMenu, 0);

        fabCloseMenu.postDelayed(() -> FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterHide(fabCloseMenu, 0), 150);
    }

    private void openFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.showFloatingActionButton(this, fabCloseMenu, 0);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabEdit, 1);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabDelete, 2);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabMenu, 0);
        FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterShow(fabCloseMenu, 0);
    }

    private void setupFloatingActionButtonsVisibility() {
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.setClickable(true);
        fabCloseMenu.setVisibility(View.INVISIBLE);
        fabCloseMenu.setClickable(false);
        fabEdit.setVisibility(View.INVISIBLE);
        fabEdit.setClickable(false);
        fabDelete.setVisibility(View.INVISIBLE);
        fabDelete.setClickable(false);
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
        bulkBottlesNumberView = (TextView) findViewById(R.id.cave_detail_bulk_bottles_number);
        boxesNumberView = (TextView) findViewById(R.id.cave_detail_boxes_number);
        boxesBottlesNumberView = (TextView) findViewById(R.id.cave_detail_boxes_bottles_number);
        arrangementRecyclerView = (RecyclerView) findViewById(R.id.cave_detail_arrangement_pattern);
        CompatibilityHelper.setNestedScrollEnable(arrangementRecyclerView, false);
    }

    private void setLayoutValues() {
        int caveTypeDrawableId = cave.CaveType.DrawableResourceId;
        if (caveTypeDrawableId != -1) {
            caveTypeIconView.setImageDrawable(ContextCompat.getDrawable(this, caveTypeDrawableId));
        }
        caveTypeView.setText(cave.CaveType.StringResourceId);
        capacityUsedView.setText(getResources().getQuantityString(R.plurals.cave_used_capacity, cave.CaveArrangement.TotalCapacity,
                cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
        switch (cave.CaveType) {
            case BULK:
                bulkBottlesNumberView.setVisibility(View.VISIBLE);
                bulkBottlesNumberView.setText(getResources().getQuantityString(R.plurals.cave_bulk_bottles_number_detail, cave.CaveArrangement.NumberBottlesBulk, cave.CaveArrangement.NumberBottlesBulk));
                boxesNumberView.setVisibility(View.GONE);
                boxesBottlesNumberView.setVisibility(View.GONE);
                arrangementRecyclerView.setVisibility(View.GONE);
                break;
            case BOX:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.VISIBLE);
                boxesNumberView.setText(getResources().getQuantityString(R.plurals.cave_boxes_number_detail, cave.CaveArrangement.NumberBoxes, cave.CaveArrangement.NumberBoxes));
                boxesBottlesNumberView.setVisibility(View.VISIBLE);
                boxesBottlesNumberView.setText(getResources().getQuantityString(R.plurals.cave_boxes_bottles_number_detail,
                        cave.CaveArrangement.NumberBottlesPerBox, cave.CaveArrangement.NumberBottlesPerBox));
                arrangementRecyclerView.setVisibility(View.GONE);
                break;
            case RACK:
            case FRIDGE:
                bulkBottlesNumberView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.GONE);
                boxesBottlesNumberView.setVisibility(View.GONE);

                CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(cave.CaveArrangement.PatternMap.keySet());
                if (maxRowCol.Col >= 0) {
                    int nbCols = maxRowCol.Col + 1;
                    int nbRows = maxRowCol.Row + 1;
                    arrangementRecyclerView.setLayoutManager(new GridLayoutManager(this, nbCols));
                    int marginLeftRight = (int) getResources().getDimension(R.dimen.horizontal_big_margin_between_elements);
                    int totalWidth = ScreenHelper.getScreenWidth(this) - (2 * marginLeftRight);

                    CaveArrangementAdapter caveArrangementAdapter = new CaveArrangementAdapter(this, cave.CaveArrangement, nbRows, nbCols, totalWidth);
                    caveArrangementAdapter.addOnValueChangedListener(() -> {
                        CaveManager.editCave(this, cave);
                        capacityUsedView.setText(getResources().getQuantityString(R.plurals.cave_used_capacity, cave.CaveArrangement.TotalCapacity,
                                cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
                    });
                    arrangementRecyclerView.setAdapter(caveArrangementAdapter);
                    arrangementRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    arrangementRecyclerView.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCave(cave == null ? caveId : cave.Id);
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

    private void refreshCave(int caveId) {
        cave = CaveManager.getCave(this, caveId);
    }
}
