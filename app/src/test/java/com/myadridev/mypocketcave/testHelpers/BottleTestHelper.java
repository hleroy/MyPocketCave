package com.myadridev.mypocketcave.testHelpers;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.BottleModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BottleTestHelper {

    public static BottleTestHelper Instance;
    private static boolean _isInitialized = false;

    public static void Init() {
        Instance = new BottleTestHelper();
        _isInitialized = true;
    }

    public static boolean IsInitialized(){
        return _isInitialized;
    }

    private BottleTestHelper() {
        if (!BottleManager.IsInitialized()) {
            BottleManager.InitWithoutStorage();
        }
    }

    public List<Integer> createSearchBottleSet() {
        List<Integer> bottleCreatedIndexList = new ArrayList<>();
        ArrayList<FoodToEatWithEnum>[] foods = new ArrayList[2];
        foods[0] = new ArrayList<>();
        foods[1] = new ArrayList<>();
        foods[1].add(FoodToEatWithEnum.PorkProduct);
        foods[1].add(FoodToEatWithEnum.Fish);

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        int count = 0;
        int numberDifferentDomains = 2;
        for (int i = 0; i < numberDifferentDomains; i++) {
            String domain = "AA - Domaine " + (i + 1);
            for (int c = 0; c < 2; c++) {
                WineColorEnum color = WineColorEnum.getById(c);
                for (int j = 0; j < 4; j++) {
                    int millesime = currentYear - (3 * j);
                    for (int k = 0; k < 2; k++) {
                        String person = "Person " + (k + 1);
                        for (int l = 0; l < 2; l++) {
                            count++;
                            String name = "AA - Suggest " + count;

                            BottleModel bottle = new BottleModel();
                            bottle.Domain = domain;
                            bottle.WineColor = color;
                            bottle.Millesime = millesime;
                            bottle.PersonToShareWith = person;
                            bottle.Name = name;
                            bottle.FoodToEatWithList.addAll(foods[l]);
                            bottle.Stock = 1;

                            bottleCreatedIndexList.add(BottleManager.Instance.addBottleNoSave(bottle));
                        }
                    }
                }
            }
        }
        return bottleCreatedIndexList;
    }

    public void deleteBottleSet(List<Integer> bottleCreatedIndexList) {
        for (int bottleId : bottleCreatedIndexList) {
            BottleManager.Instance.removeBottleNoSave(bottleId);
        }
    }
}
