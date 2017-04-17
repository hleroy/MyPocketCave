package com.myadridev.mypocketcave.helpers;

import com.myadridev.mypocketcave.enums.v1.CavePlaceTypeEnumV1;
import com.myadridev.mypocketcave.enums.v1.FoodToEatWithEnumV1;
import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.models.inferfaces.IBottleModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveArrangementModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.ICavePlaceModel;
import com.myadridev.mypocketcave.models.inferfaces.ICoordinatesModel;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModel;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModelWithBottles;
import com.myadridev.mypocketcave.models.inferfaces.ISyncModel;
import com.myadridev.mypocketcave.models.v1.BottleModelV1;
import com.myadridev.mypocketcave.models.v1.CaveArrangementModelV1;
import com.myadridev.mypocketcave.models.v1.CaveLightModelV1;
import com.myadridev.mypocketcave.models.v1.CaveModelV1;
import com.myadridev.mypocketcave.models.v1.CavePlaceModelV1;
import com.myadridev.mypocketcave.models.v1.CoordinatesModelV1;
import com.myadridev.mypocketcave.models.v1.PatternModelV1;
import com.myadridev.mypocketcave.models.v1.PatternModelWithBottlesV1;
import com.myadridev.mypocketcave.models.v1.SyncModelV1;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveArrangementModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;
import com.myadridev.mypocketcave.models.v2.CavePlaceModelV2;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelWithBottlesV2;
import com.myadridev.mypocketcave.models.v2.SyncModelV2;

import java.util.Map;

@SuppressWarnings("deprecation")
public class ModelMigrationHelper {

    public static BottleModelV2 getBottle(IBottleModel iBottle) {
        if (iBottle == null) return null;
        if (iBottle instanceof BottleModelV2) return (BottleModelV2) iBottle;
        else if (iBottle instanceof BottleModelV1) {
            BottleModelV1 bottle = (BottleModelV1) iBottle;
            BottleModelV2 newBottle = new BottleModelV2();
            newBottle.Id = bottle.Id;
            newBottle.Name = bottle.Name;
            newBottle.Domain = bottle.Domain;
            newBottle.Millesime = bottle.Millesime;
            newBottle.Comments = bottle.Comments;
            newBottle.PersonToShareWith = bottle.PersonToShareWith;
            if (bottle.WineColor != null) {
                newBottle.WineColor = WineColorEnumV2.getById(bottle.WineColor.Id);
            }
            newBottle.FoodToEatWithList.clear();
            for (FoodToEatWithEnumV1 food : bottle.FoodToEatWithList) {
                if (food == null) continue;
                FoodToEatWithEnumV2 newFood = FoodToEatWithEnumV2.getById(food.Id);
                if (newFood == null) continue;
                newBottle.FoodToEatWithList.add(newFood);
            }
            newBottle.Stock = bottle.Stock;
            newBottle.NumberPlaced = bottle.NumberPlaced;
            newBottle.Rating = bottle.Rating;
            newBottle.PriceRating = bottle.PriceRating;
            return newBottle;
        }
        return null;
    }

    public static CaveLightModelV2 getCaveLight(ICaveLightModel iCave) {
        if (iCave == null) return null;
        if (iCave instanceof CaveLightModelV2) return (CaveLightModelV2) iCave;
        else if (iCave instanceof CaveLightModelV1) {
            CaveLightModelV1 cave = (CaveLightModelV1) iCave;
            CaveLightModelV2 newCave = new CaveLightModelV2();
            newCave.Id = cave.Id;
            newCave.Name = cave.Name;
            if (cave.CaveType != null) {
                newCave.CaveType = CaveTypeEnumV2.getById(cave.CaveType.Id);
            }
            newCave.TotalCapacity = cave.TotalCapacity;
            newCave.TotalUsed = cave.TotalUsed;
            return newCave;
        }
        return null;
    }

    public static PatternModelV2 getPattern(IPatternModel iPattern) {
        if (iPattern == null) return null;
        if (iPattern instanceof PatternModelV2) return (PatternModelV2) iPattern;
        else if (iPattern instanceof PatternModelV1) {
            PatternModelV1 pattern = (PatternModelV1) iPattern;
            PatternModelV2 newPattern = new PatternModelV2();
            newPattern.Id = pattern.Id;
            if (pattern.Type != null) {
                newPattern.Type = PatternTypeEnumV2.getById(pattern.Type.Id);
            }
            newPattern.NumberBottlesByColumn = pattern.NumberBottlesByColumn;
            newPattern.NumberBottlesByRow = pattern.NumberBottlesByRow;
            newPattern.IsHorizontallyExpendable = pattern.IsHorizontallyExpendable;
            newPattern.IsVerticallyExpendable = pattern.IsVerticallyExpendable;
            newPattern.IsInverted = pattern.IsInverted;
            newPattern.PlaceMap.clear();
            for (Map.Entry<CoordinatesModelV1, CavePlaceTypeEnumV1> placeMapEntry : pattern.PlaceMap.entrySet()) {
                CoordinatesModelV2 coordinates = getCoordinatesModel(placeMapEntry.getKey());
                CavePlaceTypeEnumV1 placeType = placeMapEntry.getValue();
                if (placeType == null) continue;
                CavePlaceTypeEnumV2 cavePlaceType = CavePlaceTypeEnumV2.getById(placeType.Id);
                if (coordinates == null || cavePlaceType == null) continue;
                newPattern.PlaceMap.put(coordinates, cavePlaceType);
            }
            newPattern.Order = pattern.Order;
            return newPattern;
        }
        return null;
    }

    public static CoordinatesModelV2 getCoordinatesModel(ICoordinatesModel iCoordinates) {
        if (iCoordinates == null) return null;
        if (iCoordinates instanceof CoordinatesModelV2) return (CoordinatesModelV2) iCoordinates;
        else if (iCoordinates instanceof CoordinatesModelV1) {
            CoordinatesModelV1 coordinates = (CoordinatesModelV1) iCoordinates;
            CoordinatesModelV2 newCoordinates = new CoordinatesModelV2();
            newCoordinates.Row = coordinates.Row;
            newCoordinates.Col = coordinates.Col;
            return newCoordinates;
        }
        return null;
    }

    public static CaveModelV2 getCave(ICaveModel iCave) {
        if (iCave == null) return null;
        if (iCave instanceof CaveModelV2) return (CaveModelV2) iCave;
        else if (iCave instanceof CaveModelV1) {
            CaveModelV1 cave = (CaveModelV1) iCave;
            CaveModelV2 newCave = new CaveModelV2();
            newCave.Id = cave.Id;
            newCave.Name = cave.Name;
            if (cave.CaveType != null) {
                newCave.CaveType = CaveTypeEnumV2.getById(cave.CaveType.Id);
            }
            CaveArrangementModelV2 caveArrangement = getCaveArrangement(cave.CaveArrangement);
            if (caveArrangement != null) {
                newCave.CaveArrangement = new CaveArrangementModelV2(caveArrangement);
            }
            return newCave;
        }
        return null;
    }

    public static CaveArrangementModelV2 getCaveArrangement(ICaveArrangementModel iCaveArrangement) {
        if (iCaveArrangement == null) return null;
        if (iCaveArrangement instanceof CaveArrangementModelV2)
            return (CaveArrangementModelV2) iCaveArrangement;
        else if (iCaveArrangement instanceof CaveArrangementModelV1) {
            CaveArrangementModelV1 caveArrangement = (CaveArrangementModelV1) iCaveArrangement;
            CaveArrangementModelV2 newCaveArrangement = new CaveArrangementModelV2();
            newCaveArrangement.Id = caveArrangement.Id;
            newCaveArrangement.TotalCapacity = caveArrangement.TotalCapacity;
            newCaveArrangement.TotalUsed = caveArrangement.TotalUsed;
            newCaveArrangement.PatternMap.clear();
            for (Map.Entry<CoordinatesModelV1, PatternModelWithBottlesV1> patternEntry : caveArrangement.PatternMap.entrySet()) {
                CoordinatesModelV2 coordinates = getCoordinatesModel(patternEntry.getKey());
                PatternModelWithBottlesV2 patternModelWithBottles = getPatternModelWithBottles(patternEntry.getValue());
                if (coordinates == null || patternModelWithBottles == null) continue;
                newCaveArrangement.PatternMap.put(coordinates, new PatternModelWithBottlesV2(patternModelWithBottles));
            }
            newCaveArrangement.NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
            newCaveArrangement.NumberBoxes = caveArrangement.NumberBoxes;
            newCaveArrangement.BoxesNumberBottlesByColumn = caveArrangement.BoxesNumberBottlesByColumn;
            newCaveArrangement.BoxesNumberBottlesByRow = caveArrangement.BoxesNumberBottlesByRow;
            newCaveArrangement.FloatNumberPlacedBottlesByIdMap.clear();
            for (Map.Entry<Integer, Float> entry : caveArrangement.floatNumberPlacedBottlesByIdMap.entrySet()) {
                newCaveArrangement.FloatNumberPlacedBottlesByIdMap.put(entry.getKey(), entry.getValue());
            }
            newCaveArrangement.IntNumberPlacedBottlesByIdMap.clear();
            for (Map.Entry<Integer, Integer> entry : caveArrangement.IntNumberPlacedBottlesByIdMap.entrySet()) {
                newCaveArrangement.IntNumberPlacedBottlesByIdMap.put(entry.getKey(), entry.getValue());
            }
            return newCaveArrangement;
        }
        return null;
    }

    public static PatternModelWithBottlesV2 getPatternModelWithBottles(IPatternModelWithBottles iPattern) {
        if (iPattern == null) return null;
        if (iPattern instanceof PatternModelWithBottlesV2)
            return (PatternModelWithBottlesV2) iPattern;
        else if (iPattern instanceof PatternModelWithBottlesV1) {
            PatternModelWithBottlesV1 pattern = (PatternModelWithBottlesV1) iPattern;
            PatternModelWithBottlesV2 newPattern = new PatternModelWithBottlesV2();
            newPattern.Id = pattern.Id;
            if (pattern.Type != null) {
                newPattern.Type = PatternTypeEnumV2.getById(pattern.Type.Id);
            }
            newPattern.NumberBottlesByColumn = pattern.NumberBottlesByColumn;
            newPattern.NumberBottlesByRow = pattern.NumberBottlesByRow;
            newPattern.IsHorizontallyExpendable = pattern.IsHorizontallyExpendable;
            newPattern.IsVerticallyExpendable = pattern.IsVerticallyExpendable;
            newPattern.IsInverted = pattern.IsInverted;
            newPattern.PlaceMap.clear();
            for (Map.Entry<CoordinatesModelV1, CavePlaceTypeEnumV1> placeMapEntry : pattern.PlaceMap.entrySet()) {
                CoordinatesModelV2 coordinates = getCoordinatesModel(placeMapEntry.getKey());
                CavePlaceTypeEnumV1 placeType = placeMapEntry.getValue();
                if (placeType == null) continue;
                CavePlaceTypeEnumV2 cavePlaceType = CavePlaceTypeEnumV2.getById(placeType.Id);
                if (coordinates == null || cavePlaceType == null) continue;
                newPattern.PlaceMap.put(coordinates, cavePlaceType);
            }
            newPattern.Order = pattern.Order;
            newPattern.PlaceMapWithBottles.clear();
            for (Map.Entry<CoordinatesModelV1, CavePlaceModelV1> placeEntry : pattern.PlaceMapWithBottles.entrySet()) {
                CoordinatesModelV2 coordinates = getCoordinatesModel(placeEntry.getKey());
                CavePlaceModelV2 cavePlaceModel = getCavePlaceModel(placeEntry.getValue());
                if (coordinates == null || cavePlaceModel == null) continue;
                newPattern.PlaceMapWithBottles.put(coordinates, cavePlaceModel);
            }
            newPattern.FloatNumberPlacedBottlesByIdMap.clear();
            for (Map.Entry<Integer, Float> entry : pattern.FloatNumberPlacedBottlesByIdMap.entrySet()) {
                newPattern.FloatNumberPlacedBottlesByIdMap.put(entry.getKey(), entry.getValue());
            }
            return newPattern;
        }
        return null;
    }

    public static CavePlaceModelV2 getCavePlaceModel(ICavePlaceModel iCavePlace) {
        if (iCavePlace == null) return null;
        if (iCavePlace instanceof CavePlaceModelV2) return (CavePlaceModelV2) iCavePlace;
        else if (iCavePlace instanceof CavePlaceModelV1) {
            CavePlaceModelV1 cavePlace = (CavePlaceModelV1) iCavePlace;
            CavePlaceModelV2 newCavePlace = new CavePlaceModelV2();
            newCavePlace.BottleId = cavePlace.BottleId;
            newCavePlace.IsClickable = cavePlace.IsClickable;
            if (cavePlace.PlaceType != null) {
                newCavePlace.PlaceType = CavePlaceTypeEnumV2.getById(cavePlace.PlaceType.Id);
            }
            return newCavePlace;
        }
        return null;
    }

    public static SyncModelV2 getSync(ISyncModel iSync) {
        if (iSync == null) return null;
        if (iSync instanceof SyncModelV2) return (SyncModelV2) iSync;
        else if (iSync instanceof SyncModelV1) {
            SyncModelV1 sync = (SyncModelV1) iSync;
            SyncModelV2 newSync = new SyncModelV2();
            newSync.Version = sync.Version;
            newSync.Caves.clear();
            for (CaveModelV1 cave : sync.Caves) {
                CaveModelV2 newCave = getCave(cave);
                if (newCave == null) continue;
                newSync.Caves.add(newCave);
            }
            newSync.Bottles.clear();
            for (BottleModelV1 bottle : sync.Bottles) {
                BottleModelV2 newBottle = getBottle(bottle);
                if (newBottle == null) continue;
                newSync.Bottles.add(newBottle);
            }
            newSync.Patterns.clear();
            for (PatternModelV1 pattern : sync.Patterns) {
                PatternModelV2 newPattern = getPattern(pattern);
                if (newPattern == null) continue;
                newSync.Patterns.add(newPattern);
            }
            return newSync;
        }
        return null;
    }
}
