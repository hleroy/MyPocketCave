package com.myadridev.mypocketcave.uiTestsHelpers;

import android.app.Activity;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class ContextHelper {

    public static Activity getCurrentActivity() {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(() -> {
            for (Activity act : ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)) {
                currentActivity[0] = act;
                break;
            }
        });

        return currentActivity[0];
    }
}
