package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.IStorableModel;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.SuggestBottleResultModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottleManager {

    public static BottleManager Instance;
    private static int _maxBottleId;
    private static boolean _isInitialized;
    private final Map<Integer, BottleModel> bottles = new HashMap<>();
    private final int storeFileResourceId = R.string.filename_bottles;
    private final int storeSetResourceId = R.string.store_bottles;

    public static int MaxBottleId() {
        return _maxBottleId;
    }

    private BottleManager(boolean withStorage) {
        _maxBottleId = 0;
        if (withStorage)
            initializeBottles();
    }

    private BottleManager() {
        _maxBottleId = 0;
        initializeBottles();
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void InitWithoutStorage() {
        Instance = new BottleManager(false);
        _isInitialized = true;
    }

    public static void Init() {
        Instance = new BottleManager();
        _isInitialized = true;
    }

    private void initializeBottles() {
        final Map<Integer, IStorableModel> bottlesAsIStorableModel = new HashMap<>();
        _maxBottleId = StorageManager.Instance.loadStoredData(storeFileResourceId, storeSetResourceId, BottleModel.class, bottlesAsIStorableModel);

        bottles.clear();
        for (Map.Entry<Integer, IStorableModel> storableModelEntry : bottlesAsIStorableModel.entrySet()) {
            IStorableModel storableModel = storableModelEntry.getValue();
            if (storableModel instanceof BottleModel) {
                bottles.put(storableModelEntry.getKey(), (BottleModel) storableModel);
            }
        }
    }

    public List<BottleModel> getBottles() {
        return new ArrayList<>(bottles.values());
    }

    public BottleModel getBottle(int id) {
        return bottles.containsKey(id) ? bottles.get(id) : null;
    }

    public int addBottleNoSave(BottleModel bottle) {
        _maxBottleId++;
        bottle.Id = _maxBottleId;
        bottles.put(_maxBottleId, bottle);
        return _maxBottleId;
    }

    public int addBottle(BottleModel bottle) {
        _maxBottleId++;
        bottle.Id = _maxBottleId;
        bottles.put(_maxBottleId, bottle);
        saveBottles();
        return _maxBottleId;
    }

    public void editBottleNoSave(BottleModel bottle) {
        bottles.put(bottle.Id, bottle);
    }

    public void editBottle(BottleModel bottle) {
        bottles.put(bottle.Id, bottle);
        saveBottles();
    }

    public void removeBottleNoSave(int id) {
        if (bottles.containsKey(id)) {
            bottles.remove(id);
            setMaxBottleId();
        }
    }

    public void removeBottle(int id) {
        if (bottles.containsKey(id)) {
            bottles.remove(id);
            setMaxBottleId();
            saveBottles();
        }
    }

    public int getExistingBottleId(int id, String name, String domain, WineColorEnum wineColor, int millesime) {
        for (BottleModel existingBottle : bottles.values()) {
            if (id != existingBottle.Id
                    && name.equalsIgnoreCase(existingBottle.Name)
                    && domain.equalsIgnoreCase(existingBottle.Domain)
                    && wineColor == existingBottle.WineColor
                    && millesime == existingBottle.Millesime) {
                return existingBottle.Id;
            }
        }

        return -1;
    }

    private void setMaxBottleId() {
        int maxId = 0;
        for (int id : bottles.keySet()) {
            if (id > maxId)
                maxId = id;
        }
        _maxBottleId = maxId;
    }

    private void saveBottles() {
        StorageManager.Instance.storeData(storeFileResourceId, storeSetResourceId, bottles);
    }

    public int getBottlesCount() {
        return getBottlesCount(bottles.values());
    }

    public int getBottlesCount(Collection<BottleModel> bottles) {
        int count = 0;
        for (BottleModel bottle : bottles) {
            count += bottle.Stock;
        }
        return count;
    }

    public int getBottlesCount(WineColorEnum wineColor) {
        return getBottlesCount(bottles.values(), wineColor);
    }

    public int getBottlesCount(Collection<BottleModel> bottles, WineColorEnum wineColor) {
        int count = 0;
        for (BottleModel bottle : bottles) {
            if (bottle.WineColor == wineColor)
                count += bottle.Stock;
        }
        return count;
    }

    public String[] getAllDifferentPersons() {
        List<String> differentPersons = new ArrayList<>();

        for (Map.Entry<Integer, BottleModel> bottleEntry : bottles.entrySet()) {
            String person = bottleEntry.getValue().PersonToShareWith;
            if (person != null && person != "" && !differentPersons.contains(person)) {
                differentPersons.add(person);
            }
        }

        Collections.sort(differentPersons);
        String[] differentPersonsArray = new String[differentPersons.size()];
        return differentPersons.toArray(differentPersonsArray);
    }

    public String[] getAllDifferentDomains() {
        List<String> differentDomains = new ArrayList<>();

        for (Map.Entry<Integer, BottleModel> bottleEntry : bottles.entrySet()) {
            String domain = bottleEntry.getValue().Domain;
            if (!differentDomains.contains(domain)) {
                differentDomains.add(domain);
            }
        }

        Collections.sort(differentDomains);
        String[] differentDomainsArray = new String[differentDomains.size()];
        return differentDomains.toArray(differentDomainsArray);
    }

    public List<SuggestBottleResultModel> getSuggestBottles(SuggestBottleCriteria searchCriteria) {
        List<SuggestBottleResultModel> suggestBottles = new ArrayList<>(bottles.size());

        for (Map.Entry<Integer, BottleModel> bottleEntry : bottles.entrySet()) {
            BottleModel bottle = bottleEntry.getValue();

            int wineColorScore = computeWineColorScore(bottle, searchCriteria);
            if (searchCriteria.IsWineColorRequired && wineColorScore == 0) continue;

            int domainScore = computeDomainScore(bottle, searchCriteria);
            if (searchCriteria.IsDomainRequired && domainScore == 0) continue;

            int millesimeScore = computeMillesimeScore(bottle, searchCriteria);
            if (searchCriteria.IsMillesimeRequired && millesimeScore == 0) continue;

            int foodScore = computeFoodScore(bottle, searchCriteria);
            if (searchCriteria.IsFoodRequired && foodScore == 0) continue;

            int personScore = computePersonScore(bottle, searchCriteria);
            if (searchCriteria.IsPersonRequired && personScore == 0) continue;

            int bottleScore = wineColorScore + domainScore + millesimeScore + foodScore + personScore;

            SuggestBottleResultModel suggestedBottle = new SuggestBottleResultModel();
            suggestedBottle.Score = bottleScore;
            suggestedBottle.Bottle = bottle;
            suggestBottles.add(suggestedBottle);
        }

        Collections.sort(suggestBottles);
        return suggestBottles;
    }

    private int computeWineColorScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.WineColor == WineColorEnum.ANY) return 1;
        if (searchCriteria.WineColor == bottle.WineColor) return 1;
        return 0;
    }

    private int computeDomainScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.Domain == null || searchCriteria.Domain.isEmpty()) return 1;
        if (searchCriteria.Domain.equalsIgnoreCase(bottle.Domain)) return 1;
        return 0;
    }

    private int computeMillesimeScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
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

    private int computeFoodScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.FoodToEatWithList.isEmpty()) return 1;

        for (FoodToEatWithEnum food : searchCriteria.FoodToEatWithList) {
            if (bottle.FoodToEatWithList.contains(food)) return 1;
        }

        return 0;
    }

    private int computePersonScore(BottleModel bottle, SuggestBottleCriteria searchCriteria) {
        if (searchCriteria.PersonToShareWith == null || searchCriteria.PersonToShareWith.isEmpty())
            return 1;
        if (searchCriteria.PersonToShareWith.equalsIgnoreCase(bottle.PersonToShareWith)) return 1;
        return 0;
    }
}
