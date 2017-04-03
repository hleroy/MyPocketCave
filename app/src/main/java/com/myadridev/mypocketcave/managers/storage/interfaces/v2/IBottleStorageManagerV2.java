package com.myadridev.mypocketcave.managers.storage.interfaces.v2;

import android.content.Context;

import com.myadridev.mypocketcave.models.v2.BottleModelV2;

import java.util.Collection;
import java.util.List;

public interface IBottleStorageManagerV2 {

    List<BottleModelV2> getBottles();

    BottleModelV2 getBottle(int bottleId);

    int insertBottle(Context context, BottleModelV2 bottle, boolean needsNewId);

    void updateBottle(Context context, BottleModelV2 bottle);

    void deleteBottle(Context context, int bottleId);

    int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime);

    int getBottlesPlacedCount();

    int getBottlesCount();

    int getBottlesCount(Collection<BottleModelV2> bottles);

    int getBottlesCount(int wineColorId);

    int getBottlesCount(Collection<BottleModelV2> bottles, int wineColorId);

    List<String> getDistinctPersons();

    List<String> getDistinctDomains();

    List<BottleModelV2> getNonPlacedBottles();

    void updateNumberPlaced(Context context, int bottleId, int increment);

    void drinkBottle(Context context, int bottleId, int quantity);

    void updateIndexes(Context context);
}
