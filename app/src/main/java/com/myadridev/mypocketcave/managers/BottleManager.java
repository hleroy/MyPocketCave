package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.IBottleStorageManager;
import com.myadridev.mypocketcave.models.v1.BottleModel;
import com.myadridev.mypocketcave.models.v1.CaveLightModel;
import com.myadridev.mypocketcave.models.v1.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.v1.SuggestBottleResultModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BottleManager {

    private static boolean listenerBottleRegistered = false;
    private static IBottleStorageManager bottleStorageManager = null;

    private static IBottleStorageManager getBottleStorageManager() {
        if (bottleStorageManager == null) {
            bottleStorageManager = DependencyManager.getSingleton(IBottleStorageManager.class,
                    listenerBottleRegistered ? null : (OnDependencyChangeListener) () -> bottleStorageManager = null);
            listenerBottleRegistered = true;
        }
        return bottleStorageManager;
    }

    public static List<BottleModel> getBottles() {
        return getBottleStorageManager().getBottles();
    }

    public static List<BottleModel> getBottles(Set<Integer> bottleIds) {
        List<BottleModel> allBottles = getBottles();
        List<BottleModel> bottles = new ArrayList<>(allBottles.size());
        for (BottleModel bottle : allBottles) {
            if (bottleIds.contains(bottle.Id)) {
                bottles.add(bottle);
            }
        }
        return bottles;
    }

    public static BottleModel getBottle(int bottleId) {
        return getBottleStorageManager().getBottle(bottleId);
    }

    public static int addBottle(Context context, BottleModel bottle) {
        return getBottleStorageManager().insertBottle(context, bottle);
    }

    public static void addBottles(Context context, List<BottleModel> bottles) {
        for (BottleModel bottle : bottles) {
            // we want to keep the ids of the bottles
            editBottle(context, bottle);
        }
        updateIndexes(context);
    }

    private static void updateIndexes(Context context) {
        getBottleStorageManager().updateIndexes(context);
    }

    public static void editBottle(Context context, BottleModel bottle) {
        getBottleStorageManager().updateBottle(context, bottle);
    }

    public static void removeBottle(Context context, int bottleId) {
        getBottleStorageManager().deleteBottle(context, bottleId);
    }

    public static void removeAllBottles(Context context) {
        for (BottleModel bottle : getBottles()) {
            removeBottle(context, bottle.Id);
        }
    }

    public static int getExistingBottleId(int id, String name, String domain, WineColorEnum wineColor, int millesime) {
        return getBottleStorageManager().getExistingBottleId(id, name, domain, wineColor.Id, millesime);
    }

    public static int getBottlesPlacedCount() {
        return getBottleStorageManager().getBottlesPlacedCount();
    }

    public static int getBottlesCount() {
        return getBottleStorageManager().getBottlesCount();
    }

    public static int getBottlesCount(Collection<BottleModel> bottles) {
        return getBottleStorageManager().getBottlesCount(bottles);
    }

    public static int getBottlesCount(WineColorEnum wineColor) {
        return getBottleStorageManager().getBottlesCount(wineColor.Id);
    }

    public static int getBottlesCount(Collection<BottleModel> bottles, WineColorEnum wineColor) {
        return getBottleStorageManager().getBottlesCount(bottles, wineColor.Id);
    }

    public static List<String> getAllDistinctPersons() {
        return getBottleStorageManager().getDistinctPersons();
    }

    public static List<String> getAllDistinctDomains() {
        return getBottleStorageManager().getDistinctDomains();
    }

    public static List<SuggestBottleResultModel> getSuggestBottles(SuggestBottleCriteria searchCriteria) {
        List<BottleModel> allBottles = getBottles();
        List<SuggestBottleResultModel> suggestBottles = new ArrayList<>(allBottles.size());

        for (BottleModel bottle : allBottles) {
            int wineColorScore = computeWineColorScore(bottle, searchCriteria);
            if (searchCriteria.IsWineColorRequired && wineColorScore == 0) {
                continue;
            }
            int domainScore = computeDomainScore(bottle, searchCriteria);
            if (searchCriteria.IsDomainRequired && domainScore == 0) {
                continue;
            }
            int millesimeScore = computeMillesimeScore(bottle, searchCriteria);
            if (searchCriteria.IsMillesimeRequired && millesimeScore == 0) {
                continue;
            }
            int ratingScore = computeRatingScore(bottle, searchCriteria);
            if (searchCriteria.IsRatingRequired && ratingScore == 0) {
                continue;
            }
            int priceRatingScore = computePriceRatingScore(bottle, searchCriteria);
            if (searchCriteria.IsPriceRatingRequired && priceRatingScore == 0) {
                continue;
            }
            int foodScore = computeFoodScore(bottle, searchCriteria);
            if (searchCriteria.IsFoodRequired && foodScore == 0) {
                continue;
            }
            int personScore = computePersonScore(bottle, searchCriteria);
            if (searchCriteria.IsPersonRequired && personScore == 0) {
                continue;
            }
            int caveScore = computeCaveScore(bottle, searchCriteria);
            if (searchCriteria.IsCaveRequired && caveScore == 0) {
                continue;
            }

            SuggestBottleResultModel suggestedBottle = new SuggestBottleResultModel();
            suggestedBottle.Bottle = bottle;
            suggestedBottle.Score = wineColorScore + domainScore + millesimeScore + ratingScore + priceRatingScore + foodScore + personScore + caveScore;

            suggestBottles.add(suggestedBottle);
        }

        Collections.sort(suggestBottles);
        return suggestBottles;
    }

    private static int computeWineColorScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.WineColor == WineColorEnum.ANY) return 1;
        if (searchCriteria.WineColor == bottle.WineColor) return 1;
        return 0;
    }

    private static int computeDomainScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.Domain == null || searchCriteria.Domain.isEmpty()) return 1;
        if (searchCriteria.Domain.equalsIgnoreCase(bottle.Domain)) return 1;
        return 0;
    }

    private static int computeMillesimeScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - bottle.Millesime;

        switch (searchCriteria.Millesime) {
            case ANY:
                return 1;
            case LESS_THAN_TWO:
                if (bottle.Millesime == 0) return 0;
                return age <= 2 ? 1 : 0;
            case THREE_TO_FIVE:
                if (bottle.Millesime == 0) return 0;
                return age > 2 && age <= 5 ? 1 : 0;
            case SIX_TO_TEN:
                if (bottle.Millesime == 0) return 0;
                return age > 5 && age <= 10 ? 1 : 0;
            case OVER_TEN:
                if (bottle.Millesime == 0) return 0;
                return age > 10 ? 1 : 0;
            default:
                return 0;
        }
    }

    private static int computeRatingScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.RatingMinValue <= bottle.Rating && searchCriteria.RatingMaxValue >= bottle.Rating)
            return 1;
        return 0;
    }

    private static int computePriceRatingScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.PriceRatingMinValue <= bottle.PriceRating && searchCriteria.PriceRatingMaxValue >= bottle.PriceRating)
            return 1;
        return 0;
    }

    private static int computeFoodScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.FoodToEatWithList.isEmpty()) return 1;

        for (FoodToEatWithEnum food : searchCriteria.FoodToEatWithList) {
            if (bottle.FoodToEatWithList.contains(food)) return 1;
        }

        return 0;
    }

    private static int computePersonScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.PersonToShareWith == null || searchCriteria.PersonToShareWith.isEmpty())
            return 1;
        if (searchCriteria.PersonToShareWith.equalsIgnoreCase(bottle.PersonToShareWith)) return 1;
        return 0;
    }

    private static int computeCaveScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.Cave == null) {
            return 1;
        }
        return CaveManager.isBottleInTheCave(bottle.Id, searchCriteria.Cave.Id) ? 1 : 0;
    }

    public static List<BottleModel> getNonPlacedBottles() {
        return getBottleStorageManager().getNonPlacedBottles();
    }

    public static void placeBottle(Context context, int bottleId, int quantity) {
        updateNumberPlaced(context, bottleId, quantity);
    }

    public static void placeBottle(Context context, int bottleId) {
        placeBottle(context, bottleId, 1);
    }

    public static void drinkBottle(Context context, int bottleId, int quantity) {
        getBottleStorageManager().drinkBottle(context, bottleId, quantity);
        updateNumberPlaced(context, bottleId, -1 * quantity);
    }

    public static void drinkBottle(Context context, int bottleId) {
        drinkBottle(context, bottleId, 1);
    }

    public static void updateNumberPlaced(Context context, int bottleId, int increment) {
        getBottleStorageManager().updateNumberPlaced(context, bottleId, increment);
    }

    public static void recomputeNumberPlaced(Context context) {
        for (BottleModel bottle : getBottles()) {
            List<CaveLightModel> caves = CaveManager.getLightCavesWithBottle(bottle.Id);
            int totalPlaced = 0;
            for (CaveLightModel cave : caves) {
                totalPlaced += cave.TotalUsed;
            }
            if (bottle.NumberPlaced != totalPlaced) {
                bottle.NumberPlaced = totalPlaced;
                editBottle(context, bottle);
            }
        }
    }
}
