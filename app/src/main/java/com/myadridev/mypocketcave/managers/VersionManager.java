package com.myadridev.mypocketcave.managers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.myadridev.mypocketcave.R;

public class VersionManager {

    public static String getVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return context.getString(R.string.about_version_default);
        }
    }
}
