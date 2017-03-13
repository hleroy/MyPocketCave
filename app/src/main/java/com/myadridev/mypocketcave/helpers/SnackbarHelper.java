package com.myadridev.mypocketcave.helpers;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.myadridev.mypocketcave.R;

public class SnackbarHelper {

    public static Snackbar displaySuccessSnackbar(Context context, CoordinatorLayout coordinatorLayout, String messageResource, int actionMessageResourceId, int duration) {
        return displaySnackbar(context, coordinatorLayout, messageResource, actionMessageResourceId, duration, R.color.colorSuccess);
    }

    public static Snackbar displaySuccessSnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageResourceId, int actionMessageResourceId, int duration) {
        return displaySnackbar(context, coordinatorLayout, messageResourceId, actionMessageResourceId, duration, R.color.colorSuccess);
    }

    public static Snackbar displayErrorSnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageResourceId, int actionMessageResourceId, int duration) {
        return displaySnackbar(context, coordinatorLayout, messageResourceId, actionMessageResourceId, duration, R.color.colorError);
    }

    public static Snackbar displayInfoSnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageResourceId, int actionMessageResourceId, int duration) {
        return displaySnackbar(context, coordinatorLayout, messageResourceId, actionMessageResourceId, duration, R.color.colorInfo);
    }

    private static Snackbar displaySnackbar(Context context, CoordinatorLayout coordinatorLayout, String messageResource, int actionMessageResourceId, int duration, int actionColorResourceId) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, messageResource, duration);
        snackbar.setAction(actionMessageResourceId, (View v) -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(context, actionColorResourceId));
        snackbar.show();
        return snackbar;
    }

    private static Snackbar displaySnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageResourceId, int actionMessageResourceId, int duration, int actionColorResourceId) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, messageResourceId, duration);
        snackbar.setAction(actionMessageResourceId, (View v) -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(context, actionColorResourceId));
        snackbar.show();
        return snackbar;
    }
}
