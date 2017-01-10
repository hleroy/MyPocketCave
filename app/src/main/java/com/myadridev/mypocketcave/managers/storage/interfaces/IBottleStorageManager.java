package com.myadridev.mypocketcave.managers.storage.interfaces;

import com.myadridev.mypocketcave.models.BottleModel;

import java.util.Collection;
import java.util.List;

public interface IBottleStorageManager {

    List<BottleModel> getBottles();

    BottleModel getBottle(int bottleId);

    int insertBottle(BottleModel bottle);

    void updateBottle(BottleModel bottle);

    void deleteBottle(int bottleId);

    int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime);

    int getBottlesCount();

    int getBottlesCount(Collection<BottleModel> bottles);

    int getBottlesCount(int wineColorId);

    int getBottlesCount(Collection<BottleModel> bottles, int wineColorId);

    String[] getDistinctPersons();

    String[] getDistinctDomains();

    List<BottleModel> getNonPlacedBottles();

    void updateNumberPlaced(int bottleId, int increment);

    void drinkBottle(int bottleId);
}
