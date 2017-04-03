package com.myadridev.mypocketcave.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.BottlesAdapter;
import com.myadridev.mypocketcave.adapters.CaveArrangementAdapter;
import com.myadridev.mypocketcave.adapters.SimpleDividerItemDecoration;
import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.dialogs.SeeBottleAlertDialog;
import com.myadridev.mypocketcave.helpers.FloatingActionButtonHelper;
import com.myadridev.mypocketcave.helpers.RotationHelper;
import com.myadridev.mypocketcave.helpers.ScreenHelper;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.listeners.OnBottleClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleDrunkClickListener;
import com.myadridev.mypocketcave.listeners.OnBottleUnplacedClickListener;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.v1.BottleModel;
import com.myadridev.mypocketcave.models.v1.CaveModel;
import com.myadridev.mypocketcave.models.v1.CoordinatesModel;
import com.myadridev.mypocketcave.tasks.caves.EditCaveTask;

import java.util.List;
import java.util.Map;

public class CaveDetailActivity extends AppCompatActivity {

    private final View.OnTouchListener arrangementTooltipOnClick;
    public int BottleIdInHighlight;
    private CaveModel cave;
    private ImageView caveTypeIconView;
    private TextView caveTypeView;
    private TextView capacityUsedView;
    private TextView boxesNumberView;
    private CoordinatorLayout coordinatorLayout;
    private ImageView arrangementTooltipView;
    private RecyclerView arrangementRecyclerView;
    private ProgressBar arrangementRecyclerViewProgress;
    private CaveArrangementAdapter caveArrangementAdapter;
    private BottlesAdapter bottlesAdapter;
    private boolean isMenuOpened;
    private FloatingActionButton fabMenu;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;
    private int caveId;
    private OnBottleClickListener onSetHighlightlistener;
    private OnBottleDrunkClickListener onBottleDrunkClickListener;
    private OnBottleUnplacedClickListener onBottleUnplacedClickListener;
    private SimpleDividerItemDecoration dividerItemDecoration;

    public CaveDetailActivity() {
        arrangementTooltipOnClick = (View v, MotionEvent event) -> {
            SnackbarHelper.displayInfoSnackbar(this, coordinatorLayout, R.string.message_cave_detail_arrangement_info, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            return false;
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cave_detail);
        cave = null;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cave_detail);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        caveId = bundle.getInt("caveId");

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
        setupFloatingActionButtons();
    }

    private void setLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cave_detail_coordinator_layout);
        arrangementTooltipView = (ImageView) findViewById(R.id.cave_detail_arrangement_tooltip);
        arrangementTooltipView.setOnTouchListener(arrangementTooltipOnClick);
        caveTypeIconView = (ImageView) findViewById(R.id.cave_detail_type_icon);
        caveTypeView = (TextView) findViewById(R.id.cave_detail_type);
        capacityUsedView = (TextView) findViewById(R.id.cave_detail_capacity_used_total);
        boxesNumberView = (TextView) findViewById(R.id.cave_detail_boxes_number);
        arrangementRecyclerView = (RecyclerView) findViewById(R.id.cave_detail_arrangement_pattern);
        ViewCompat.setNestedScrollingEnabled(arrangementRecyclerView, false);

        arrangementRecyclerViewProgress = (ProgressBar) findViewById(R.id.cave_detail_arrangement_pattern_progress);
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
                arrangementTooltipView.setVisibility(View.GONE);
                boxesNumberView.setVisibility(View.GONE);

                bottlesAdapter = new BottlesAdapter(this, cave.getBottles(), true, cave.CaveArrangement.TotalCapacity - cave.CaveArrangement.TotalUsed, BottleIdInHighlight);
                bottlesAdapter.setOnBottleBindListener(this::setHolderPropertiesFromBottle);
                onBottleDrunkClickListener = (int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates) -> {
                    cave.CaveArrangement.unplaceBottle(bottleId, quantity);
                    BottleManager.drinkBottle(this, bottleId, quantity);
                    bottlesAdapter.MaxBottleToPlace += quantity;
                    CaveManager.editCave(this, cave);
                    capacityUsedView.setText(getResources().getQuantityString(R.plurals.cave_used_capacity, cave.CaveArrangement.TotalCapacity,
                            cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
                    bottlesAdapter.setBottles(cave.getBottles());
                };
                onBottleUnplacedClickListener = (int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates) -> {
                    cave.CaveArrangement.unplaceBottle(bottleId, quantity);
                    BottleManager.updateNumberPlaced(this, bottleId, -1 * quantity);
                    bottlesAdapter.MaxBottleToPlace += quantity;
                    CaveManager.editCave(this, cave);
                    capacityUsedView.setText(getResources().getQuantityString(R.plurals.cave_used_capacity, cave.CaveArrangement.TotalCapacity,
                            cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
                    bottlesAdapter.setBottles(cave.getBottles());
                };
                onSetHighlightlistener = (int bottleId) -> {
                    BottleIdInHighlight = bottleId;
                    bottlesAdapter.setBottleIdInHighlight(BottleIdInHighlight);
                };
                bottlesAdapter.setOnBottleClickListener((int bottleId) -> {
                    SeeBottleAlertDialog alertDialog = new SeeBottleAlertDialog(this, bottleId, null, null,
                            onBottleDrunkClickListener, onBottleUnplacedClickListener,
                            BottleIdInHighlight, onSetHighlightlistener, cave.getNumberBottles(bottleId));
                    alertDialog.show();
                });
                bottlesAdapter.setOnBottlePlacedClickListener((int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates) -> {
                    cave.CaveArrangement.placeBottle(bottleId, quantity);
                    BottleManager.placeBottle(this, bottleId, quantity);
                    bottlesAdapter.MaxBottleToPlace -= quantity;
                    CaveManager.editCave(this, cave);
                    capacityUsedView.setText(getResources().getQuantityString(R.plurals.cave_used_capacity, cave.CaveArrangement.TotalCapacity,
                            cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
                    bottlesAdapter.setBottles(cave.getBottles());
                });
                bottlesAdapter.setOnResetHighlightlistener((View v) -> {
                    BottleIdInHighlight = -1;
                    bottlesAdapter.setBottleIdInHighlight(BottleIdInHighlight);
                });
                break;
            case BOX:
                arrangementTooltipView.setVisibility(View.VISIBLE);
                boxesNumberView.setVisibility(View.VISIBLE);
                boxesNumberView.setText(getResources().getQuantityString(R.plurals.cave_boxes_number_detail, cave.CaveArrangement.NumberBoxes, cave.CaveArrangement.NumberBoxes));
                break;
            case RACK:
            case FRIDGE:
                arrangementTooltipView.setVisibility(View.VISIBLE);
                boxesNumberView.setVisibility(View.GONE);
                break;
        }
        drawArrangement();
    }

    private void drawArrangement() {
        switch (cave.CaveType) {
            case BULK:
                arrangementRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                arrangementRecyclerView.setAdapter(bottlesAdapter);
                arrangementRecyclerView.setVisibility(View.VISIBLE);
                arrangementRecyclerViewProgress.setVisibility(View.GONE);
                break;
            case BOX:
                setCaveArrangement(true);
                break;
            case RACK:
            case FRIDGE:
                setCaveArrangement(false);
                break;
        }
    }

    private void setCaveArrangement(boolean hasBorders) {
        CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(cave.CaveArrangement.PatternMap.keySet());
        if (maxRowCol.Col >= 0) {
            int nbCols = maxRowCol.Col + 1;
            int nbRows = maxRowCol.Row + 1;
            int marginLeftRight = (int) getResources().getDimension(R.dimen.horizontal_big_margin_between_elements);
            int activityMarginLeftRight = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
            int totalWidth = ScreenHelper.getScreenWidth(this) - (2 * marginLeftRight) - (2 * activityMarginLeftRight);

            if (hasBorders) {
                dividerItemDecoration = new SimpleDividerItemDecoration(this);
                arrangementRecyclerView.addItemDecoration(dividerItemDecoration);
            } else if (dividerItemDecoration != null) {
                arrangementRecyclerView.removeItemDecoration(dividerItemDecoration);
            }
            caveArrangementAdapter = new CaveArrangementAdapter(this, cave.CaveArrangement, nbRows, nbCols, totalWidth, BottleIdInHighlight);
            caveArrangementAdapter.setOnValueChangedListener((Map<CoordinatesModel, List<CoordinatesModel>> coordinatesToUpdate) -> {
                EditCaveTask editCaveTask = new EditCaveTask(this);
                editCaveTask.execute(cave);
                capacityUsedView.setText(getResources().getQuantityString(R.plurals.cave_used_capacity, cave.CaveArrangement.TotalCapacity,
                        cave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalCapacity));
                caveArrangementAdapter.updatePositions(arrangementRecyclerView, coordinatesToUpdate);
            });

            arrangementRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
            arrangementRecyclerView.setLayoutManager(new GridLayoutManager(this, nbCols));
            arrangementRecyclerView.setAdapter(caveArrangementAdapter);
            arrangementRecyclerView.setVisibility(View.INVISIBLE);
            arrangementRecyclerViewProgress.setVisibility(View.VISIBLE);
        } else {
            arrangementRecyclerView.setVisibility(View.GONE);
            arrangementRecyclerViewProgress.setVisibility(View.GONE);
        }
    }

    private void setHolderPropertiesFromBottle(BottleViewHolder holder, BottleModel bottle) {
        holder.setLabelViewText(bottle.Domain + " - " + bottle.Name);
        holder.setMillesimeViewText(bottle.Millesime == 0 ? "-" : String.valueOf(bottle.Millesime));
        holder.setStockLabelViewText(getString(R.string.bottles_here,
                cave.CaveArrangement.IntNumberPlacedBottlesByIdMap.containsKey(bottle.Id) ? (int) cave.CaveArrangement.IntNumberPlacedBottlesByIdMap.get(bottle.Id) : 0));
        int wineColorDrawableId = bottle.WineColor.DrawableResourceId;
        holder.setColorViewImageDrawable(wineColorDrawableId != -1 ? ContextCompat.getDrawable(this, wineColorDrawableId) : null);
        holder.setRating(bottle.Rating);
        holder.setPriceRating(bottle.PriceRating);
    }

    @Override
    protected void onResume() {
        if (NavigationManager.restartIfNeeded(this)) {
            finish();
        } else {
            super.onResume();
            cave = CaveManager.getCave(this, cave == null ? caveId : cave.Id);
            refreshActionBar();
            setLayoutValues();
            setupFloatingActionButtonsVisibility();
        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // redraw the grid
        RotationHelper.rotateWhenPossible(arrangementRecyclerView, this::drawArrangement);
    }

    public void onCaveArrangementLoaded() {
        arrangementRecyclerView.setVisibility(View.VISIBLE);
        arrangementRecyclerViewProgress.setVisibility(View.GONE);
    }
}
