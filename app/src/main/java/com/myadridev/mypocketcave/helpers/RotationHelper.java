package com.myadridev.mypocketcave.helpers;

import android.view.View;
import android.view.ViewTreeObserver;

public class RotationHelper {

    public static void rotateWhenPossible(View view, Action action) {
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                action.execute();
            }
        });
    }
}
