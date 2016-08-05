package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.fragments.BottlesFragment;
import com.myadridev.mypocketcave.fragments.CavesFragment;
import com.myadridev.mypocketcave.fragments.IVisibleFragment;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context _context, int fragmentToDisplay) {
        super(fm);
        context = _context;
        fragments = new HashMap(2);
        CavesFragment cavesFragment = new CavesFragment();
        fragments.put(0, cavesFragment);
        cavesFragment.setIsDisplayedAtFirstLaunch(fragmentToDisplay == 0);

        BottlesFragment bottlesFragment = new BottlesFragment();
        fragments.put(1, bottlesFragment);
        bottlesFragment.setIsDisplayedAtFirstLaunch(fragmentToDisplay == 1);

        allFragments = new HashMap<>();
        for (Map.Entry<Integer, Fragment> fragmentEntry : fragments.entrySet()) {
            allFragments.put(fragmentEntry.getKey(), (IVisibleFragment)fragmentEntry.getValue());
        }
    }

    private final Map<Integer, Fragment> fragments;
    public final Map<Integer, IVisibleFragment> allFragments;

    @Override
    public Fragment getItem(int position) {
        if (fragments.containsKey(position)) {
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.title_caves);
            case 1:
                return context.getString(R.string.title_bottles);
        }
        return null;
    }
}
