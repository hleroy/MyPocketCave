package com.myadridev.mypocketcave.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myadridev.mypocketcave.activities.AboutActivity;
import com.myadridev.mypocketcave.activities.BottleCreateActivity;
import com.myadridev.mypocketcave.activities.BottleDetailActivity;
import com.myadridev.mypocketcave.activities.BottleEditActivity;
import com.myadridev.mypocketcave.activities.CaveCreateActivity;
import com.myadridev.mypocketcave.activities.CaveDetailActivity;
import com.myadridev.mypocketcave.activities.CaveEditActivity;
import com.myadridev.mypocketcave.activities.MainActivity;
import com.myadridev.mypocketcave.activities.PatternCreateActivity;
import com.myadridev.mypocketcave.activities.PatternSelectionActivity;
import com.myadridev.mypocketcave.activities.SplashScreenActivity;
import com.myadridev.mypocketcave.activities.SuggestBottleResultActivity;
import com.myadridev.mypocketcave.activities.SuggestBottleSearchActivity;
import com.myadridev.mypocketcave.activities.SyncActivity;
import com.myadridev.mypocketcave.enums.ActivityRequestEnum;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;

public class NavigationManager {

    public static void navigateToMain(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void navigateToBottleDetail(Context context, int bottleId) {
        Bundle bundle = new Bundle();
        bundle.putInt("bottleId", bottleId);
        Intent intent = new Intent(context, BottleDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void navigateToBottleCreate(Context context) {
        context.startActivity(new Intent(context, BottleCreateActivity.class));
    }

    public static void navigateToBottleEdit(Context context, int bottleId) {
        Bundle bundle = new Bundle();
        bundle.putInt("bottleId", bottleId);
        Intent intent = new Intent(context, BottleEditActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void navigateToAbout(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    public static void navigateToSuggestBottleSearch(Context context) {
        context.startActivity(new Intent(context, SuggestBottleSearchActivity.class));
    }

    public static boolean navigateToSuggestBottleResult(Context context, SuggestBottleCriteria searchCriteria) {
        ObjectMapper jsonMapper = new ObjectMapper();
        String searchCriteriaJson;
        try {
            searchCriteriaJson = jsonMapper.writeValueAsString(searchCriteria);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("searchCriteria", searchCriteriaJson);
        Intent intent = new Intent(context, SuggestBottleResultActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
        return true;
    }

    public static void navigateToCaveDetail(Context context, int caveId) {
        Bundle bundle = new Bundle();
        bundle.putInt("caveId", caveId);
        Intent intent = new Intent(context, CaveDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void navigateToCaveDetail(Context context, int caveId, int bottleId) {
        Bundle bundle = new Bundle();
        bundle.putInt("caveId", caveId);
        bundle.putInt("bottleIdInHighlight", bottleId);
        Intent intent = new Intent(context, CaveDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void navigateToCaveCreate(Context context) {
        context.startActivity(new Intent(context, CaveCreateActivity.class));
    }

    public static void navigateToCaveEdit(Context context, int caveId) {
        Bundle bundle = new Bundle();
        bundle.putInt("caveId", caveId);
        Intent intent = new Intent(context, CaveEditActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void navigateToPatternSelection(Activity activity) {
        activity.startActivityForResult(new Intent(activity, PatternSelectionActivity.class), ActivityRequestEnum.PATTERN_SELECTION.Id);
    }

    public static void navigateToCreatePattern(PatternSelectionActivity activity) {
        activity.startActivityForResult(new Intent(activity, PatternCreateActivity.class), ActivityRequestEnum.CREATE_PATTERN.Id);
    }

    public static void navigateToSync(Context context) {
        context.startActivity(new Intent(context, SyncActivity.class));
    }

    public static void restartIfNeeded(Activity activity) {
        if (DependencyManager.needsRestart()) {
            restart(activity);
            activity.finish();
        }
    }

    private static void restart(Context context) {
        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
