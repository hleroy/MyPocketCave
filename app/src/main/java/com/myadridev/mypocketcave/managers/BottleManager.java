package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.storage.interfaces.IBottleStorageManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.SuggestBottleResultModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BottleManager {

    private static IBottleStorageManager bottleStorageManager = null;

    private static IBottleStorageManager getBottleStorageManager() {
        if (bottleStorageManager == null) {
            bottleStorageManager = DependencyManager.getSingleton(IBottleStorageManager.class);
        }
        return bottleStorageManager;
    }

    public static List<BottleModel> getBottles() {
        return getBottleStorageManager().getBottles();
    }

    public static BottleModel getBottle(int bottleId) {
        return getBottleStorageManager().getBottle(bottleId);
    }

    public static int addBottle(BottleModel bottle) {
        return getBottleStorageManager().insertBottle(bottle);
    }

    public static void editBottle(BottleModel bottle) {
        getBottleStorageManager().updateBottle(bottle);
    }

    public static void removeBottle(int bottleId) {
        getBottleStorageManager().deleteBottle(bottleId);
    }

    public static int getExistingBottleId(int id, String name, String domain, WineColorEnum wineColor, int millesime) {
        return getBottleStorageManager().getExistingBottleId(id, name, domain, wineColor.Id, millesime);
    }

    public static int getBottlesCount() {
        return getBottleStorageManager().getBottlesCount();
    }

    public static int getBottlesCount(Collection<BottleModel> bottles) {
        int count = 0;
        for (BottleModel bottle : bottles) {
            count += bottle.Stock;
        }
        return count;
    }

    public static int getBottlesCount(WineColorEnum wineColor) {
        return getBottleStorageManager().getBottlesCount(wineColor.Id);
    }

    public static int getBottlesCount(Collection<BottleModel> bottles, WineColorEnum wineColor) {
        int count = 0;
        for (BottleModel bottle : bottles) {
            if (bottle.WineColor == wineColor)
                count += bottle.Stock;
        }
        return count;
    }

    public static String[] getAllDistinctPersons() {
        return getBottleStorageManager().getDistinctPersons();
    }

    public static String[] getAllDistinctDomains() {
        return getBottleStorageManager().getDistinctDomains();
    }

    public static List<SuggestBottleResultModel> getSuggestBottles(SuggestBottleCriteria searchCriteria) {
        List<BottleModel> allBottles = getBottleStorageManager().getBottles();
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
            int foodScore = computeFoodScore(bottle, searchCriteria);
            if (searchCriteria.IsFoodRequired && foodScore == 0) {
                continue;
            }
            int personScore = computePersonScore(bottle, searchCriteria);
            if (searchCriteria.IsPersonRequired && personScore == 0) {
                continue;
            }

            SuggestBottleResultModel suggestedBottle = new SuggestBottleResultModel();
            suggestedBottle.Bottle = bottle;
            suggestedBottle.Score = wineColorScore + domainScore + millesimeScore + foodScore + personScore;

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

    public static List<BottleModel> getNonPlacedBottles() {
        List<BottleModel> nonPlacedBottles = getBottleStorageManager().getNonPlacedBottles();
        Collections.sort(nonPlacedBottles);
        return nonPlacedBottles;
    }

    public static void placeBottle(int bottleId) {
        updateNumberPlaced(bottleId, 1);
    }

    public static void drinkBottle(int bottleId) {
        updateNumberPlaced(bottleId, -1);
    }

    public static void updateNumberPlaced(int bottleId, int increment) {
        getBottleStorageManager().updateNumberPlaced(bottleId, increment);
    }
}
