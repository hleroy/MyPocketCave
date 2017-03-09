package com.myadridev.mypocketcave.helpers;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenHelper {

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
}
