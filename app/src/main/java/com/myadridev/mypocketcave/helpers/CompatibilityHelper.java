package com.myadridev.mypocketcave.helpers;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

public class CompatibilityHelper {

    public static int currentApiVersion = android.os.Build.VERSION.SDK_INT;

    public static void setNestedScrollEnable(RecyclerView recyclerView, boolean isEnabled) {
        if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.setNestedScrollingEnabled(isEnabled);
        } else {
            ViewCompat.setNestedScrollingEnabled(recyclerView, isEnabled);
        }
    }
}