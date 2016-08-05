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
import com.myadridev.mypocketcave.managers.ManagersHelper;
import com.myadridev.mypocketcave.managers.NavigationManager;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
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
        ManagersHelper.initializeAllManagers(this);

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

        FloatingActionButton fabSuggestBottle = (FloatingActionButton) findViewById(R.id.fab_suggest_bottle);
        assert fabSuggestBottle != null;
        fabSuggestBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationManager.navigateToSuggestBottleSearch(MainActivity.this);
            }
        });

        fabAddBottle = (FloatingActionButton) findViewById(R.id.fab_add_bottle);
        assert fabAddBottle != null;
        fabAddBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create new bottle
                NavigationManager.navigateToBottleCreate(MainActivity.this);
            }
        });

        fabAddCave = (FloatingActionButton) findViewById(R.id.fab_add_cave);
        assert fabAddCave != null;
        fabAddCave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create new cave
                NavigationManager.navigateToCaveCreate(MainActivity.this);
            }
        });
    }

    private int currentVisibleFragment;

    private void changeVisibleFab(int selectedTab) {
        for (Map.Entry<Integer, IVisibleFragment> fragmentEntry : viewPagerAdapter.allFragments.entrySet()) {
            fragmentEntry.getValue().setIsVisible(fragmentEntry.getKey() == selectedTab);
        }

        currentVisibleFragment = selectedTab;

        switch (selectedTab) {
            case 0:
                fabAddBottle.setVisibility(View.GONE);
                fabAddCave.setVisibility(View.VISIBLE);
                break;
            case 1:
                fabAddBottle.setVisibility(View.VISIBLE);
                fabAddCave.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onPause() {
        viewPagerAdapter.allFragments.get(currentVisibleFragment).setIsVisible(false);
        isPaused = true;
        super.onPause();
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
