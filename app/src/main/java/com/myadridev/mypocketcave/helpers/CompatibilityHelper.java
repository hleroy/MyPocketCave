package com.myadridev.mypocketcave.helpers;

import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class CompatibilityHelper {

    private static int currentApiVersion = android.os.Build.VERSION.SDK_INT;

    public static void setNestedScrollEnable(RecyclerView recyclerView, boolean isEnabled) {
        if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.setNestedScrollingEnabled(isEnabled);
        } else {
            ViewCompat.setNestedScrollingEnabled(recyclerView, isEnabled);
        }
    }

    public static void setTextAppearance(TextView textView, int resId) {
        if (currentApiVersion >= android.os.Build.VERSION_CODES.M) {
            textView.setTextAppearance(resId);
        } else {
            TextViewCompat.setTextAppearance(textView, resId);
        }
    }
}
