package com.myadridev.mypocketcave.managers.storage.interfaces;

import com.myadridev.mypocketcave.models.BottleModel;

import java.util.List;

/**
 * Created by adrien on 01/10/2016.
 */

public interface IBottleStorageManager {

    List<BottleModel> getBottles();

    BottleModel getBottle(int bottleId);

    int insertBottle(BottleModel bottle);

    void updateBottle(BottleModel bottle);

    void deleteBottle(int bottleId);

    int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime);

    int getBottlesCount();

    int getBottlesCount(int wineColorId);

    String[] getDistinctPersons();

    String[] getDistinctDomains();

    List<BottleModel> getNonPlacedBottles();

    void updateNumberPlaced(int bottleId, int increment);
}
