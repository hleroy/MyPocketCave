package com.myadridev.mypocketcave.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.BottlesAdapter;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.helpers.CompatibilityHelper;
import com.myadridev.mypocketcave.helpers.FloatingActionButtonHelper;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class CaveDetailActivity extends AppCompatActivity {

    public int BottleIdInHighlight;
    private CaveModel cave;
    private ImageView caveTypeIconView;
    private TextView caveTypeView;
    private TextView capacityUsedView;
    private Toolbar toolbar;
    private TextView boxesNumberView;
    private TextView boxesBottlesNumberView;
    private RecyclerView arrangementRecyclerView;
    private CaveArrangementAdapter caveArrangementAdapter;
    private BottlesAdapter bottlesAdapter;
    private boolean isMenuOpened;
    private FloatingActionButton fabMenu;
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

        BottleIdInHighlight = bundle.getInt("bottleIdInHighlight", -1);
    }

    private void setupFloatingActionButtons() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu_cave);
        fabMenu.setOnClickListener((View view) -> {
            if (isMenuOpened) {
                closeFloatingActionButtonsMenu();
            } else {
                openFloatingActionButtonsMenu();
            }
        });

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
            deleteCaveDialogBuilder.setOnDismissListener((DialogInterface dialog) -> closeFloatingActionButtonsMenu());
            deleteCaveDialogBuilder.show();
        });
    }

    private void closeFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabEdit, 1);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabDelete, 2);

        FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterHide(fabMenu, 0);
        fabMenu.setSize(FloatingActionButton.SIZE_NORMAL);
        fabMenu.setImageResource(R.drawable.menu);
        isMenuOpened = !isMenuOpened;
    }

    private void openFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.showFloatingActionButton(this, fabEdit, 1);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabDelete, 2);

        FloatingActionButtonHelper.hideFloatingActionButton(this, fabMenu, 0);
        fabMenu.setSize(FloatingActionButton.SIZE_MINI);
        fabMenu.setImageResource(R.drawable.close);
        fabMenu.postDelayed(() -> FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterShow(fabMenu, 0), 20);
        isMenuOpened = !isMenuOpened;
    }

    private void setupFloatingActionButtonsVisibility() {
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.setClickable(true);
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
                boxesNumberView.setVisibility(View.GONE);
                boxesBottlesNumberView.setVisibility(View.GONE);
                arrangementRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                bottlesAdapter = new BottlesAdapter(this, cave.getBottles(), true, cave.CaveArrangement.TotalCapacity - cave.CaveArrangement.TotalUsed);
                bottlesAdapter.setOnBottleBindListener(this::setHolderPropertiesFromBottle);
                bottlesAdapter.addOnBottleClickListener((int bottleId) -> NavigationManager.navigateToBottleDetail(this, bottleId)); // TODO
                bottlesAdapter.addOnBottlePlacedClickListener((int bottleId, int quantity) -> {
                    cave.CaveArrangement.placeBottle(bottleId, quantity);
                    BottleManager.placeBottle(this, bottleId, quantity);
                    bottlesAdapter.MaxBottleToPlace -= quantity;
                    CaveManager.editCave(this, cave);
                    capacityUsedView.setText(getResources().getQuantityString(R.plurals.cave_used_capacity, cave.CaveArrangement.TotalCapacity,
                            cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
                    bottlesAdapter.setBottles(cave.getBottles());
                });
                arrangementRecyclerView.setAdapter(bottlesAdapter);
                arrangementRecyclerView.setVisibility(View.VISIBLE);
                break;
            case BOX:
                boxesNumberView.setVisibility(View.VISIBLE);
                boxesNumberView.setText(getResources().getQuantityString(R.plurals.cave_boxes_number_detail, cave.CaveArrangement.NumberBoxes, cave.CaveArrangement.NumberBoxes));
                boxesBottlesNumberView.setVisibility(View.VISIBLE);
                boxesBottlesNumberView.setText(getResources().getQuantityString(R.plurals.cave_boxes_bottles_number_detail,
                        cave.CaveArrangement.NumberBottlesPerBox, cave.CaveArrangement.NumberBottlesPerBox));
                arrangementRecyclerView.setVisibility(View.GONE);
                break;
            case RACK:
            case FRIDGE:
                boxesNumberView.setVisibility(View.GONE);
                boxesBottlesNumberView.setVisibility(View.GONE);

                CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(cave.CaveArrangement.PatternMap.keySet());
                if (maxRowCol.Col >= 0) {
                    int nbCols = maxRowCol.Col + 1;
                    int nbRows = maxRowCol.Row + 1;
                    arrangementRecyclerView.setLayoutManager(new GridLayoutManager(this, nbCols));
                    int marginLeftRight = (int) getResources().getDimension(R.dimen.horizontal_big_margin_between_elements);
                    int totalWidth = ScreenHelper.getScreenWidth(this) - (2 * marginLeftRight);

                    caveArrangementAdapter = new CaveArrangementAdapter(this, cave.CaveArrangement, nbRows, nbCols, totalWidth, BottleIdInHighlight);
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

    private void setHolderPropertiesFromBottle(BottleViewHolder holder, BottleModel bottle) {
        holder.setLabelViewText(bottle.Domain + " - " + bottle.Name);
        holder.setMillesimeViewText(bottle.Millesime == 0 ? "-" : String.valueOf(bottle.Millesime));
        holder.setStockLabelViewText(getString(R.string.bottles_here,
                cave.CaveArrangement.IntNumberPlacedBottlesByIdMap.containsKey(bottle.Id) ? cave.CaveArrangement.IntNumberPlacedBottlesByIdMap.get(bottle.Id) : 0));
        int wineColorDrawableId = bottle.WineColor.DrawableResourceId;
        holder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(this, wineColorDrawableId) : null);
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
