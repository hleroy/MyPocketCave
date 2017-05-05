package com.myadridev.mypocketcave.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.ViewPagerAdapter;
import com.myadridev.mypocketcave.fragments.IVisibleFragment;
import com.myadridev.mypocketcave.helpers.FloatingActionButtonHelper;
import com.myadridev.mypocketcave.managers.NavigationManager;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    private boolean isMenuOpened;
    private FloatingActionButton fabMenu;
    private FloatingActionButton fabAddBottle;
    private FloatingActionButton fabAddCave;

    private boolean isPaused;
    private int currentVisibleFragment;

    public MainActivity() {
        isPaused = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setLogo(R.mipmap.logo);
        }
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, 0);

        viewPager = (ViewPager) findViewById(R.id.container);
        assert viewPager != null;
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeVisibleFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        setupFloatingActionButtons();
    }

    private void setupFloatingActionButtons() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu_main);
        fabMenu.setOnClickListener((View view) -> {
            if (isMenuOpened) {
                closeFloatingActionButtonsMenu();
            } else {
                openFloatingActionButtonsMenu();
            }
        });

        fabAddBottle = (FloatingActionButton) findViewById(R.id.fab_add_bottle);
        fabAddBottle.setOnClickListener((View view) -> {
            // create new bottle
            NavigationManager.navigateToBottleCreate(MainActivity.this);
        });

        fabAddCave = (FloatingActionButton) findViewById(R.id.fab_add_cave);
        fabAddCave.setOnClickListener((View view) -> {
            // create new cave
            NavigationManager.navigateToCaveCreate(MainActivity.this);
        });
    }

    private void closeFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabAddCave, 1);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabAddBottle, 2);

        FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterHide(fabMenu, 0);
        fabMenu.setSize(FloatingActionButton.SIZE_NORMAL);
        fabMenu.setImageResource(R.drawable.menu);
        isMenuOpened = !isMenuOpened;
    }

    private void openFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.showFloatingActionButton(this, fabAddCave, 1);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabAddBottle, 2);

        FloatingActionButtonHelper.hideFloatingActionButton(this, fabMenu, 0);
        fabMenu.setSize(FloatingActionButton.SIZE_MINI);
        fabMenu.setImageResource(R.drawable.close);
        fabMenu.postDelayed(() -> FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterShow(fabMenu, 0), 20);
        isMenuOpened = !isMenuOpened;
    }

    private void setupFloatingActionButtonsVisibility() {
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.setClickable(true);
        fabAddBottle.setVisibility(View.INVISIBLE);
        fabAddBottle.setClickable(false);
        fabAddCave.setVisibility(View.INVISIBLE);
        fabAddCave.setClickable(false);
    }

    private void changeVisibleFab(int selectedTab) {
        if (isMenuOpened) {
            closeFloatingActionButtonsMenu();
        }
        for (Map.Entry<Integer, IVisibleFragment> fragmentEntry : viewPagerAdapter.allFragments.entrySet()) {
            fragmentEntry.getValue().setIsVisible(fragmentEntry.getKey() == selectedTab);
        }

        currentVisibleFragment = selectedTab;
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPagerAdapter.allFragments.get(currentVisibleFragment).setIsVisible(false);
        isPaused = true;
        if (isMenuOpened) {
            closeFloatingActionButtonsMenu();
        }
    }

    @Override
    protected void onResume() {
        if (NavigationManager.restartIfNeeded(this)) {
            finish();
            return;
        }
        super.onResume();
        setupFloatingActionButtonsVisibility();
        if (isPaused) {
            viewPagerAdapter.allFragments.get(currentVisibleFragment).setIsVisible(true);
            isPaused = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                NavigationManager.navigateToAbout(this);
                return true;
            case R.id.sync:
                NavigationManager.navigateToSync(this);
                return true;
            case R.id.suggest:
                NavigationManager.navigateToSuggestBottleSearch(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitConfirmationDialogBuilder = new AlertDialog.Builder(this);
        exitConfirmationDialogBuilder.setCancelable(true);
        exitConfirmationDialogBuilder.setMessage(R.string.global_exit_confirmation);
        exitConfirmationDialogBuilder.setNegativeButton(R.string.global_stay, (DialogInterface dialog, int which) -> dialog.dismiss());
        exitConfirmationDialogBuilder.setPositiveButton(R.string.global_exit, (DialogInterface dialog, int which) -> {
            dialog.dismiss();
            finish();
        });
        exitConfirmationDialogBuilder.show();
    }
}
