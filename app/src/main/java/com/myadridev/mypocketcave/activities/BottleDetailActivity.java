package com.myadridev.mypocketcave.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.dialogs.SeeCavesAlertDialog;
import com.myadridev.mypocketcave.helpers.FloatingActionButtonHelper;
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

import java.util.Calendar;

public class BottleDetailActivity extends AppCompatActivity {

    public int currentYear;
    private BottleModel bottle;
    private TextView stockView;
    private TextView placedView;
    private ImageView wineColorIconView;
    private TextView wineColorView;
    private TextView millesimeView;
    private TextView foodView;
    private TextView personView;
    private TextView commentsView;
    private Toolbar toolbar;
    private TextView domainView;

    private boolean isMenuOpened;
    private FloatingActionButton fabMenu;
    private FloatingActionButton fabSeeCaves;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_detail);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        toolbar = (Toolbar) findViewById(R.id.toolbar_bottle);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        refreshBottle(bundle.getInt("bottleId"));

        setupFloatingActionButtons();
        setupFloatingActionButtonsVisibility();
    }

    private void setupFloatingActionButtons() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu_bottle);
        fabMenu.setOnClickListener((View view) -> {
            if (isMenuOpened) {
                closeFloatingActionButtonsMenu();
            } else {
                openFloatingActionButtonsMenu();
            }
        });

        fabSeeCaves = (FloatingActionButton) findViewById(R.id.fab_see_in_caves);
        fabSeeCaves.setOnClickListener((View v) -> {
            SeeCavesAlertDialog seeCavesAlertDialog = new SeeCavesAlertDialog(this, bottle.Id);
            seeCavesAlertDialog.setTitle(R.string.title_see_caves);
            seeCavesAlertDialog.setOnDismissListener((DialogInterface dialog) -> closeFloatingActionButtonsMenu());
            seeCavesAlertDialog.show();
        });

        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_bottle);
        fabEdit.setOnClickListener((View view) -> {
            NavigationManager.navigateToBottleEdit(BottleDetailActivity.this, bottle.Id);
            finish();
        });

        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete_bottle);
        fabDelete.setOnClickListener((View view) -> {
            if (bottle.NumberPlaced > 0) {
                AlertDialog.Builder errorDeleteBottleDialogBuilder = new AlertDialog.Builder(BottleDetailActivity.this);
                errorDeleteBottleDialogBuilder.setCancelable(true);
                errorDeleteBottleDialogBuilder.setMessage(R.string.bottle_delete_error_some_placed);
                errorDeleteBottleDialogBuilder.setNegativeButton(R.string.error_ok, (DialogInterface dialog, int which) -> dialog.dismiss());
                errorDeleteBottleDialogBuilder.setOnDismissListener((DialogInterface dialog) -> closeFloatingActionButtonsMenu());
                errorDeleteBottleDialogBuilder.show();
            } else {
                AlertDialog.Builder deleteBottleDialogBuilder = new AlertDialog.Builder(BottleDetailActivity.this);
                deleteBottleDialogBuilder.setCancelable(true);
                deleteBottleDialogBuilder.setMessage(R.string.bottle_delete_confirmation);
                deleteBottleDialogBuilder.setNegativeButton(R.string.global_no, (DialogInterface dialog, int which) -> dialog.dismiss());
                deleteBottleDialogBuilder.setPositiveButton(R.string.global_yes, (DialogInterface dialog, int which) -> {
                    BottleManager.removeBottle(this, bottle.Id);
                    dialog.dismiss();
                    finish();
                });
                deleteBottleDialogBuilder.setOnDismissListener((DialogInterface dialog) -> closeFloatingActionButtonsMenu());
                deleteBottleDialogBuilder.show();
            }
        });
    }

    private void closeFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabSeeCaves, 1);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabEdit, 2);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabDelete, 3);

        FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterHide(fabMenu, 0);
        fabMenu.setSize(FloatingActionButton.SIZE_NORMAL);
        fabMenu.setImageResource(R.drawable.menu);
        isMenuOpened = !isMenuOpened;
    }

    private void openFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.showFloatingActionButton(this, fabSeeCaves, 1);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabEdit, 2);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabDelete, 3);

        FloatingActionButtonHelper.hideFloatingActionButton(this, fabMenu, 0);
        fabMenu.setSize(FloatingActionButton.SIZE_MINI);
        fabMenu.setImageResource(R.drawable.close);
        fabMenu.postDelayed(() -> FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterShow(fabMenu, 0), 20);
        isMenuOpened = !isMenuOpened;
    }

    private void setupFloatingActionButtonsVisibility() {
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.setClickable(true);
        fabSeeCaves.setVisibility(View.INVISIBLE);
        fabSeeCaves.setClickable(false);
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
        domainView = (TextView) findViewById(R.id.bottle_detail_domain);
        stockView = (TextView) findViewById(R.id.bottle_detail_stock);
        placedView = (TextView) findViewById(R.id.bottle_detail_placed);
        wineColorIconView = (ImageView) findViewById(R.id.bottle_detail_wine_color_icon);
        wineColorView = (TextView) findViewById(R.id.bottle_detail_wine_color);
        millesimeView = (TextView) findViewById(R.id.bottle_detail_millesime);
        foodView = (TextView) findViewById(R.id.bottle_detail_food);
        personView = (TextView) findViewById(R.id.bottle_detail_person);
        commentsView = (TextView) findViewById(R.id.bottle_detail_comments);
    }

    private void setLayoutValues() {
        domainView.setText(bottle.Domain);
        stockView.setText(getString(R.string.bottles_stock, bottle.Stock));
        placedView.setText(getResources().getQuantityString(R.plurals.bottles_placed, bottle.NumberPlaced, bottle.NumberPlaced));
        int wineColorDrawableId = bottle.WineColor.DrawableResourceId;
        if (wineColorDrawableId != -1) {
            wineColorIconView.setImageDrawable(ContextCompat.getDrawable(this, wineColorDrawableId));
        }
        wineColorView.setText(bottle.WineColor.StringResourceId);
        millesimeView.setText(bottle.Millesime == 0 ? getString(R.string.no_millesime) : String.valueOf(bottle.Millesime));
        foodView.setText(FoodToEatHelper.computeFoodViewText(this, bottle.FoodToEatWithList));
        personView.setText(bottle.PersonToShareWith);
        commentsView.setText(bottle.Comments);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBottle();
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
            supportActionBar.setTitle(bottle.Name);
        }
    }

    private void refreshBottle() {
        refreshBottle(bottle.Id);
    }

    private void refreshBottle(int bottleId) {
        bottle = new BottleModel(BottleManager.getBottle(bottleId));
    }
}
