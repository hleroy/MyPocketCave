package com.myadridev.mypocketcave.activities;

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

    private FloatingActionButton fabMenu;
    private FloatingActionButton fabCloseMenu;
    private FloatingActionButton fabSuggestBottle;
    private FloatingActionButton fabAddBottle;
    private FloatingActionButton fabAddCave;

    private boolean isPaused;

    public MainActivity() {
        isPaused = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bottle);
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
        setupFloatingActionButtonsVisibility();
    }

    private void setupFloatingActionButtons() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu_main);
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFloatingActionButtonsMenu();
            }
        });

        fabCloseMenu = (FloatingActionButton) findViewById(R.id.fab_close_menu_main);
        fabCloseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFloatingActionButtonsMenu();
            }
        });

        fabSuggestBottle = (FloatingActionButton) findViewById(R.id.fab_suggest_bottle);
        fabSuggestBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationManager.navigateToSuggestBottleSearch(MainActivity.this);
            }
        });

        fabAddBottle = (FloatingActionButton) findViewById(R.id.fab_add_bottle);
        fabAddBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create new bottle
                NavigationManager.navigateToBottleCreate(MainActivity.this);
            }
        });

        fabAddCave = (FloatingActionButton) findViewById(R.id.fab_add_cave);
        fabAddCave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create new cave
                NavigationManager.navigateToCaveCreate(MainActivity.this);
            }
        });
    }

    private void closeFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabCloseMenu, 0);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabSuggestBottle, 1);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabAddCave, 2);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabAddBottle, 3);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabMenu, 0);

        fabCloseMenu.postDelayed(new Runnable() {
            @Override
            public void run() {
                FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterHide(fabCloseMenu, 0);
            }
        }, 150);
    }

    private void openFloatingActionButtonsMenu() {
        FloatingActionButtonHelper.showFloatingActionButton(this, fabCloseMenu, 0);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabSuggestBottle, 1);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabAddCave, 2);
        FloatingActionButtonHelper.showFloatingActionButton(this, fabAddBottle, 3);
        FloatingActionButtonHelper.hideFloatingActionButton(this, fabMenu, 0);
        FloatingActionButtonHelper.setFloatingActionButtonNewPositionAfterShow(fabCloseMenu, 0);
    }

    private void setupFloatingActionButtonsVisibility() {
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.setClickable(true);
        fabCloseMenu.setVisibility(View.INVISIBLE);
        fabCloseMenu.setClickable(false);
        fabSuggestBottle.setVisibility(View.INVISIBLE);
        fabSuggestBottle.setClickable(false);
        fabAddBottle.setVisibility(View.INVISIBLE);
        fabAddBottle.setClickable(false);
        fabAddCave.setVisibility(View.INVISIBLE);
        fabAddCave.setClickable(false);
    }

    private int currentVisibleFragment;

    private void changeVisibleFab(int selectedTab) {
        if (fabCloseMenu.getVisibility() == View.VISIBLE) {
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
        if (fabCloseMenu.getVisibility() == View.VISIBLE) {
            closeFloatingActionButtonsMenu();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
