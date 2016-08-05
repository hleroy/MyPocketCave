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
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.models.BottleModel;

import java.util.Calendar;

public class BottleDetailActivity extends AppCompatActivity {

    public int currentYear;
    private BottleModel bottle;
    private TextView stockView;
    private ImageView wineColorIconView;
    private TextView wineColorView;
    private TextView millesimeView;
    private TextView foodView;
    private TextView personView;
    private TextView commentsView;
    private Toolbar toolbar;
    private TextView domainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_detail);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        toolbar = (Toolbar) findViewById(R.id.toolbar_bottle);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        refreshBottle(bundle.getInt("bottleId"));

        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_bottle);
        assert fabEdit != null;
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationManager.navigateToBottleEdit(BottleDetailActivity.this, bottle.Id);
                finish();
            }
        });

        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete_bottle);
        assert fabDelete != null;
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteBottleDialogBuilder = new AlertDialog.Builder(BottleDetailActivity.this);
                deleteBottleDialogBuilder.setCancelable(true);
                deleteBottleDialogBuilder.setMessage(R.string.bottle_delete_confirmation);
                deleteBottleDialogBuilder.setNegativeButton(R.string.global_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                deleteBottleDialogBuilder.setPositiveButton(R.string.global_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BottleManager.Instance.removeBottle(bottle.Id);
                        dialog.dismiss();
                        finish();
                    }
                });
                deleteBottleDialogBuilder.show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setLayout();
    }

    private void setLayout() {
        domainView = (TextView) findViewById(R.id.bottle_detail_domain);
        stockView = (TextView) findViewById(R.id.bottle_detail_stock);
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
        int wineColorDrawableId = bottle.WineColor.drawableResourceId;
        if (wineColorDrawableId != -1) {
            wineColorIconView.setImageDrawable(ContextCompat.getDrawable(this, wineColorDrawableId));
        }
        wineColorView.setText(bottle.WineColor.stringResourceId);
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
        bottle = new BottleModel(BottleManager.Instance.getBottle(bottleId));
    }
}
