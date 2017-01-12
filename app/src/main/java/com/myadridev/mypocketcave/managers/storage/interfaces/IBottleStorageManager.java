package com.myadridev.mypocketcave.managers.storage.interfaces;

import android.content.Context;

import com.myadridev.mypocketcave.models.BottleModel;

import java.util.Collection;
import java.util.List;

public interface IBottleStorageManager {

    List<BottleModel> getBottles();

    BottleModel getBottle(int bottleId);

    int insertBottle(Context context, BottleModel bottle);

    void updateBottle(Context context, BottleModel bottle);

    void deleteBottle(Context context, int bottleId);

    int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime);

    int getBottlesCount();

    int getBottlesCount(Collection<BottleModel> bottles);

    int getBottlesCount(int wineColorId);

    int getBottlesCount(Collection<BottleModel> bottles, int wineColorId);

    List<String> getDistinctPersons();

    List<String> getDistinctDomains();

    List<BottleModel> getNonPlacedBottles();

    void updateNumberPlaced(Context context, int bottleId, int increment);

    void drinkBottle(Context context, int bottleId);
}
