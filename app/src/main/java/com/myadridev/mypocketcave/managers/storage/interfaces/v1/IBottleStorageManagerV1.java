package com.myadridev.mypocketcave.managers.storage.interfaces.v1;

import android.content.Context;

import com.myadridev.mypocketcave.models.v1.BottleModelV1;

import java.util.Collection;
import java.util.List;

@Deprecated
public interface IBottleStorageManagerV1 {

    List<BottleModelV1> getBottles();

    BottleModelV1 getBottle(int bottleId);

    int insertBottle(Context context, BottleModelV1 bottle);

    void updateBottle(Context context, BottleModelV1 bottle);

    void deleteBottle(Context context, int bottleId);

    int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime);

    int getBottlesPlacedCount();

    int getBottlesCount();

    int getBottlesCount(Collection<BottleModelV1> bottles);

    int getBottlesCount(int wineColorId);

    int getBottlesCount(Collection<BottleModelV1> bottles, int wineColorId);

    List<String> getDistinctPersons();

    List<String> getDistinctDomains();

    List<BottleModelV1> getNonPlacedBottles();

    void updateNumberPlaced(Context context, int bottleId, int increment);

    void drinkBottle(Context context, int bottleId, int quantity);

    void updateIndexes(Context context);
}
