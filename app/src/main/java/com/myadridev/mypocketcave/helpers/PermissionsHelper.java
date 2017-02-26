package com.myadridev.mypocketcave.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionsHelper {

    public static boolean askForPermissionIfNeeded(Activity activity, String permission, int permissionRequestCode) {
        if (!checkForPermission(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionRequestCode);
            return false;
        }
        return true; // permission is already granted
    }

    public static boolean checkForPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
