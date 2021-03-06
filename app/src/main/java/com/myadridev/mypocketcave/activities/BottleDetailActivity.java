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
import android.widget.RatingBar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.dialogs.SeeCavesAlertDialog;
import com.myadridev.mypocketcave.helpers.FloatingActionButtonHelper;
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.tasks.bottles.RemoveNotFoundBottleTask;

public class BottleDetailActivity extends AppCompatActivity {

    public int bottleId;
    private BottleModelV2 bottle;
    private TextView stockView;
    private TextView placedView;
    private ImageView seeCavesView;
    private ImageView wineColorIconView;
    private TextView wineColorView;
    private ImageView organicView;
    private TextView millesimeView;
    private TextView foodView;
    private TextView personView;
    private TextView commentsView;
    private TextView domainView;
    private RatingBar ratingBar;
    private RatingBar priceRatingBar;
    private boolean isMenuOpened;
    private FloatingActionButton fabMenu;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bottle_detail);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        bottleId = bundle.getInt("bottleId");
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

        seeCavesView = (ImageView) findViewById(R.id.see_in_caves_image);
        seeCavesView.setOnClickListener((View v) -> {
            SeeCavesAlertDialog seeCavesAlertDialog = new SeeCavesAlertDialog(this, bottle.Id);
            seeCavesAlertDialog.setTitle(R.string.title_see_caves);
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
                errorDeleteBottleDialogBuilder.setNegativeButton(R.string.global_ok, (DialogInterface dialog, int which) -> dialog.dismiss());
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
        domainView = (TextView) findViewById(R.id.bottle_detail_domain);
        stockView = (TextView) findViewById(R.id.bottle_detail_stock);
        placedView = (TextView) findViewById(R.id.bottle_detail_placed);
        wineColorIconView = (ImageView) findViewById(R.id.bottle_detail_wine_color_icon);
        wineColorView = (TextView) findViewById(R.id.bottle_detail_wine_color);
        millesimeView = (TextView) findViewById(R.id.bottle_detail_millesime);
        foodView = (TextView) findViewById(R.id.bottle_detail_food);
        personView = (TextView) findViewById(R.id.bottle_detail_person);
        commentsView = (TextView) findViewById(R.id.bottle_detail_comments);
        ratingBar = (RatingBar) findViewById(R.id.bottle_detail_rating);
        priceRatingBar = (RatingBar) findViewById(R.id.bottle_detail_price_rating);
        organicView = (ImageView) findViewById(R.id.bottle_detail_organic);
    }

    private void setLayoutValues() {
        domainView.setText(bottle.Domain);
        stockView.setText(getString(R.string.bottles_stock, bottle.Stock));
        placedView.setText(getResources().getQuantityString(R.plurals.bottles_placed, bottle.NumberPlaced, bottle.NumberPlaced));
        seeCavesView.setVisibility(bottle.NumberPlaced > 0 ? View.VISIBLE : View.GONE);
        int wineColorDrawableId = bottle.WineColor.DrawableResourceId;
        if (wineColorDrawableId != -1) {
            wineColorIconView.setImageDrawable(ContextCompat.getDrawable(this, wineColorDrawableId));
        }
        wineColorView.setText(bottle.WineColor.StringResourceId);
        organicView.setVisibility(bottle.Organic ? View.VISIBLE : View.GONE);
        millesimeView.setText(bottle.Millesime == 0 ? getString(R.string.no_millesime) : String.valueOf(bottle.Millesime));
        foodView.setText(FoodToEatHelper.computeFoodViewText(this, bottle.FoodToEatWithList));
        personView.setText(bottle.PersonToShareWith);
        commentsView.setText(bottle.Comments);

        ratingBar.setProgress(bottle.Rating);
        priceRatingBar.setProgress(bottle.PriceRating);
    }

    @Override
    protected void onResume() {
        if (NavigationManager.restartIfNeeded(this)) {
            finish();
            return;
        }
        super.onResume();

        bottle = BottleManager.getBottle(bottle == null ? bottleId : bottle.Id);
        if (bottle == null) {
            AlertDialog.Builder deleteBottleDialogBuilder = new AlertDialog.Builder(this);
            deleteBottleDialogBuilder.setCancelable(true);
            deleteBottleDialogBuilder.setMessage(R.string.bottle_not_found_delete_confirmation);
            deleteBottleDialogBuilder.setNegativeButton(R.string.global_no, (DialogInterface dialog, int which) -> {
                dialog.dismiss();
                finish();
            });
            deleteBottleDialogBuilder.setPositiveButton(R.string.global_yes, (DialogInterface dialog, int which) -> {
                RemoveNotFoundBottleTask removeNotFoundCaveTask = new RemoveNotFoundBottleTask(this, dialog);
                removeNotFoundCaveTask.execute(bottleId);
            });
            deleteBottleDialogBuilder.setOnDismissListener((DialogInterface dialog) -> closeFloatingActionButtonsMenu());
            deleteBottleDialogBuilder.show();
            return;
        }
        refreshActionBar();
        setLayoutValues();
        setupFloatingActionButtonsVisibility();
    }

    public void onRemovePostExecute() {
        finish();
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
}
