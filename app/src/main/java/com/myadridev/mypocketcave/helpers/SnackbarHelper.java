package com.myadridev.mypocketcave.helpers;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.myadridev.mypocketcave.R;

public class SnackbarHelper {

    public static void displaySuccessSnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageResourceId, int actionMessageResourceId, int lengthIndefinite) {
        displaySnackbar(context, coordinatorLayout, messageResourceId, actionMessageResourceId, lengthIndefinite, R.color.colorSuccess);
    }

    public static void displayErrorSnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageResourceId, int actionMessageResourceId, int lengthIndefinite) {
        displaySnackbar(context, coordinatorLayout, messageResourceId, actionMessageResourceId, lengthIndefinite, R.color.colorError);
    }

    private static void displaySnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageResourceId, int actionMessageResourceId, int lengthIndefinite, int actionColorResourceId) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, messageResourceId, lengthIndefinite);
        snackbar.setAction(actionMessageResourceId, (View v) -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(context, actionColorResourceId));
        snackbar.show();
    }
}
