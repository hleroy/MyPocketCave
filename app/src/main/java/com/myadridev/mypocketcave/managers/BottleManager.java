package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.enums.v2.FarmingTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IBottleStorageManagerV2;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.SuggestBottleCriteriaV2;
import com.myadridev.mypocketcave.models.v2.SuggestBottleResultModelV2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BottleManager {

    private static boolean listenerBottleRegistered = false;
    private static IBottleStorageManagerV2 bottleStorageManager = null;

    private static IBottleStorageManagerV2 getBottleStorageManager() {
        if (bottleStorageManager == null) {
            bottleStorageManager = DependencyManager.getSingleton(IBottleStorageManagerV2.class,
                    listenerBottleRegistered ? null : (OnDependencyChangeListener) () -> bottleStorageManager = null);
            listenerBottleRegistered = true;
        }
        return bottleStorageManager;
    }

    public static List<BottleModelV2> getBottles() {
        return getBottleStorageManager().getBottles();
    }

    public static List<BottleModelV2> getBottles(Set<Integer> bottleIds) {
        List<BottleModelV2> allBottles = getBottles();
        List<BottleModelV2> bottles = new ArrayList<>(allBottles.size());
        for (BottleModelV2 bottle : allBottles) {
            if (bottleIds.contains(bottle.Id)) {
                bottles.add(bottle);
            }
        }
        return bottles;
    }

    public static BottleModelV2 getBottle(int bottleId) {
        return getBottleStorageManager().getBottle(bottleId);
    }

    public static int addBottle(Context context, BottleModelV2 bottle) {
        return getBottleStorageManager().insertBottle(context, bottle, true);
    }

    public static void addBottles(Context context, List<BottleModelV2> bottles) {
        for (BottleModelV2 bottle : bottles) {
            // we want to keep the ids of the bottles
            editBottle(context, bottle);
        }
        updateIndexes(context);
    }

    private static void updateIndexes(Context context) {
        getBottleStorageManager().updateIndexes(context);
    }

    public static void editBottle(Context context, BottleModelV2 bottle) {
        getBottleStorageManager().updateBottle(context, bottle);
    }

    public static void removeBottle(Context context, int bottleId) {
        getBottleStorageManager().deleteBottle(context, bottleId);
    }

    public static void removeAllBottles(Context context) {
        for (BottleModelV2 bottle : getBottles()) {
            removeBottle(context, bottle.Id);
        }
    }

    public static int getExistingBottleId(int id, String name, String domain, WineColorEnumV2 wineColor, int millesime) {
        return getBottleStorageManager().getExistingBottleId(id, name, domain, wineColor.Id, millesime);
    }

    public static int getBottlesPlacedCount() {
        return getBottleStorageManager().getBottlesPlacedCount();
    }

    public static int getBottlesCount() {
        return getBottleStorageManager().getBottlesCount();
    }

    public static int getBottlesCount(Collection<BottleModelV2> bottles) {
        return getBottleStorageManager().getBottlesCount(bottles);
    }

    public static int getBottlesCount(WineColorEnumV2 wineColor) {
        return getBottleStorageManager().getBottlesCount(wineColor.Id);
    }

    public static int getBottlesCount(Collection<BottleModelV2> bottles, WineColorEnumV2 wineColor) {
        return getBottleStorageManager().getBottlesCount(bottles, wineColor.Id);
    }

    public static List<String> getAllDistinctPersons() {
        return getBottleStorageManager().getDistinctPersons();
    }

    public static List<String> getAllDistinctDomains() {
        return getBottleStorageManager().getDistinctDomains();
    }

    public static List<SuggestBottleResultModelV2> getSuggestBottles(SuggestBottleCriteriaV2 searchCriteria) {
        List<BottleModelV2> allBottles = getBottles();
        List<SuggestBottleResultModelV2> suggestBottles = new ArrayList<>(allBottles.size());

        for (BottleModelV2 bottle : allBottles) {
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
            int farmingTypeScore = computeFarmingTypeScore(bottle, searchCriteria);
            if (searchCriteria.IsFarmingTypeRequired && farmingTypeScore == 0) {
                continue;
            }

            SuggestBottleResultModelV2 suggestedBottle = new SuggestBottleResultModelV2();
            suggestedBottle.Bottle = bottle;
            suggestedBottle.Score = wineColorScore + domainScore + millesimeScore + ratingScore + priceRatingScore + foodScore + personScore + caveScore + farmingTypeScore;

            suggestBottles.add(suggestedBottle);
        }

        Collections.sort(suggestBottles);
        return suggestBottles;
    }

    private static int computeWineColorScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        if (searchCriteria.WineColor == WineColorEnumV2.a) return 1;
        if (searchCriteria.WineColor == bottle.WineColor) return 1;
        return 0;
    }

    private static int computeDomainScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        if (searchCriteria.Domain == null || searchCriteria.Domain.isEmpty()) return 1;
        if (searchCriteria.Domain.equalsIgnoreCase(bottle.Domain)) return 1;
        return 0;
    }

    private static int computeMillesimeScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - bottle.Millesime;

        switch (searchCriteria.Millesime) {
            case a:
                return 1;
            case ltt:
                if (bottle.Millesime == 0) return 0;
                return age <= 2 ? 1 : 0;
            case ttf:
                if (bottle.Millesime == 0) return 0;
                return age > 2 && age <= 5 ? 1 : 0;
            case stt:
                if (bottle.Millesime == 0) return 0;
                return age > 5 && age <= 10 ? 1 : 0;
            case ot:
                if (bottle.Millesime == 0) return 0;
                return age > 10 ? 1 : 0;
            default:
                return 0;
        }
    }

    private static int computeRatingScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        if (searchCriteria.RatingMinValue <= bottle.Rating && searchCriteria.RatingMaxValue >= bottle.Rating)
            return 1;
        return 0;
    }

    private static int computePriceRatingScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        if (searchCriteria.PriceRatingMinValue <= bottle.PriceRating && searchCriteria.PriceRatingMaxValue >= bottle.PriceRating)
            return 1;
        return 0;
    }

    private static int computeFoodScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        if (searchCriteria.FoodToEatWithList.isEmpty()) return 1;

        for (FoodToEatWithEnumV2 food : searchCriteria.FoodToEatWithList) {
            if (bottle.FoodToEatWithList.contains(food)) return 1;
        }

        return 0;
    }

    private static int computePersonScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        if (searchCriteria.PersonToShareWith == null || searchCriteria.PersonToShareWith.isEmpty())
            return 1;
        if (searchCriteria.PersonToShareWith.equalsIgnoreCase(bottle.PersonToShareWith)) return 1;
        return 0;
    }

    private static int computeCaveScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        if (searchCriteria.Cave == null) {
            return 1;
        }
        return CaveManager.isBottleInTheCave(bottle.Id, searchCriteria.Cave.Id) ? 1 : 0;
    }

    private static int computeFarmingTypeScore(BottleModelV2 bottle, SuggestBottleCriteriaV2 searchCriteria) {
        switch (searchCriteria.FarmingType) {
            case af:
                return 1;
            case of:
                if (bottle.Organic) return 1;
            case nof:
                if (!bottle.Organic) return 1;
            default:
                return 0;
        }
    }

    public static List<BottleModelV2> getNonPlacedBottles() {
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
        for (BottleModelV2 bottle : getBottles()) {
            List<CaveLightModelV2> caves = CaveManager.getLightCavesWithBottle(bottle.Id);
            int totalPlaced = 0;
            for (CaveLightModelV2 cave : caves) {
                totalPlaced += cave.TotalUsed;
            }
            if (bottle.NumberPlaced != totalPlaced) {
                bottle.NumberPlaced = totalPlaced;
                editBottle(context, bottle);
            }
        }
    }
}
