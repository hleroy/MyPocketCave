package com.myadridev.mypocketcave.helpers;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.myadridev.mypocketcave.R;

public class FloatingActionButtonHelper {

    public static void showFloatingActionButton(Activity activity, FloatingActionButton fab, int position) {
        if (position != 0) {
            setFloatingActionButtonNewPositionAfterShow(fab, position);
        }
        fab.setVisibility(View.VISIBLE);
        fab.startAnimation(getShowFab(activity, position));
        fab.setClickable(true);
    }

    public static void setFloatingActionButtonNewPositionAfterShow(FloatingActionButton fab, int position) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.rightMargin += (int) (fab.getHeight() * (position == 0 ? 0.2 : 0));
        layoutParams.bottomMargin += (int) (fab.getWidth() * (getBottomMargin(position)));
        fab.setLayoutParams(layoutParams);
    }

    private static Animation getShowFab(Activity activity, int position) {
        switch (position) {
            case 1:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_show_1);
            case 2:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_show_2);
            case 3:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_show_3);
            default:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_show_0);
        }
    }

    public static void hideFloatingActionButton(Activity activity, final FloatingActionButton fab, int position) {
        if (position != 0) {
            setFloatingActionButtonNewPositionAfterHide(fab, position);
        }
        fab.startAnimation(getHideFab(activity, position));
        if (position != 0) {
            fab.setClickable(false);
            fab.setVisibility(View.INVISIBLE);
        }
    }

    public static void setFloatingActionButtonNewPositionAfterHide(FloatingActionButton fab, int position) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab.getHeight() * (position == 0 ? 0.2 : 0));
        layoutParams.bottomMargin -= (int) (fab.getWidth() * getBottomMargin(position));
        fab.setLayoutParams(layoutParams);
    }

    private static double getBottomMargin(int position) {
        switch (position) {
            case 0:
                return 0.2;
            default:
                return 1 + ((position - 1) * 1.2);
        }
    }

    private static Animation getHideFab(Activity activity, int position) {
        switch (position) {
            case 1:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_hide_1);
            case 2:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_hide_2);
            case 3:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_hide_3);
            default:
                return AnimationUtils.loadAnimation(activity.getApplication(), R.anim.fab_hide_0);
        }
    }
}
