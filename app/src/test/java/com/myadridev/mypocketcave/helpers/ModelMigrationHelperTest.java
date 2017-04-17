package com.myadridev.mypocketcave.helpers;

import com.myadridev.mypocketcave.enums.v1.CavePlaceTypeEnumV1;
import com.myadridev.mypocketcave.enums.v1.CaveTypeEnumV1;
import com.myadridev.mypocketcave.enums.v1.FoodToEatWithEnumV1;
import com.myadridev.mypocketcave.enums.v1.PatternTypeEnumV1;
import com.myadridev.mypocketcave.enums.v1.WineColorEnumV1;
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

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("deprecation")
public class ModelMigrationHelperTest {

    @Test
    public void getBottle() {
        // null
        assertNull(ModelMigrationHelper.getBottle(null));

        // incorrect
        IBottleModel bottle = new IBottleModel() {
        };
        assertNull(ModelMigrationHelper.getBottle(bottle));

        // V2
        bottle = new BottleModelV2();
        assertEquals(bottle, ModelMigrationHelper.getBottle(bottle));

        // V1
        BottleModelV1 bottleV1 = new BottleModelV1();
        bottleV1.Name = "Toto";
        bottleV1.FoodToEatWithList.add(FoodToEatWithEnumV1.Aperitif);
        bottleV1.Stock = 6;
        bottleV1.WineColor = WineColorEnumV1.CHAMPAGNE;
        bottleV1.Comments = "yt";
        bottleV1.Domain = "d";
        bottleV1.Id = 6;
        bottleV1.Millesime = 42;
        bottleV1.NumberPlaced = 4;
        bottleV1.PersonToShareWith = "hon";
        bottleV1.PriceRating = 2;
        bottleV1.Rating = 3;

        BottleModelV2 bottleV2 = new BottleModelV2();
        bottleV2.Name = "Toto";
        bottleV2.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottleV2.Stock = 6;
        bottleV2.WineColor = WineColorEnumV2.c;
        bottleV2.Comments = "yt";
        bottleV2.Domain = "d";
        bottleV2.Id = 6;
        bottleV2.Millesime = 42;
        bottleV2.NumberPlaced = 4;
        bottleV2.PersonToShareWith = "hon";
        bottleV2.PriceRating = 2;
        bottleV2.Rating = 3;

        BottleModelV2 migratedBottle = ModelMigrationHelper.getBottle(bottleV1);
        assertNotNull(migratedBottle);
        assertEquals(bottleV2.Name, migratedBottle.Name);
        assertEquals(bottleV2.Stock, migratedBottle.Stock);
        assertEquals(bottleV2.WineColor, migratedBottle.WineColor);
        assertEquals(bottleV2.FoodToEatWithList.size(), migratedBottle.FoodToEatWithList.size());
        for (FoodToEatWithEnumV2 food : migratedBottle.FoodToEatWithList) {
            assertTrue(bottleV2.FoodToEatWithList.contains(food));
        }
        assertEquals(bottleV2.Comments, migratedBottle.Comments);
        assertEquals(bottleV2.Domain, migratedBottle.Domain);
        assertEquals(bottleV2.Id, migratedBottle.Id);
        assertEquals(bottleV2.Millesime, migratedBottle.Millesime);
        assertEquals(bottleV2.NumberPlaced, migratedBottle.NumberPlaced);
        assertEquals(bottleV2.PersonToShareWith, migratedBottle.PersonToShareWith);
        assertEquals(bottleV2.PriceRating, migratedBottle.PriceRating);
        assertEquals(bottleV2.Rating, migratedBottle.Rating);
    }

    @Test
    public void getCaveLight() {
        // null
        assertNull(ModelMigrationHelper.getCaveLight(null));

        // incorrect
        ICaveLightModel caveLight = new ICaveLightModel() {
        };
        assertNull(ModelMigrationHelper.getCaveLight(caveLight));

        // V2
        caveLight = new CaveLightModelV2();
        assertEquals(caveLight, ModelMigrationHelper.getCaveLight(caveLight));

        // V1
        CaveLightModelV1 caveLightV1 = new CaveLightModelV1();
        caveLightV1.Name = "Toto";
        caveLightV1.Id = 6;
        caveLightV1.CaveType = CaveTypeEnumV1.BULK;
        caveLightV1.TotalUsed = 42;
        caveLightV1.TotalCapacity = 55;

        CaveLightModelV2 caveLightV2 = new CaveLightModelV2();
        caveLightV2.Name = "Toto";
        caveLightV2.Id = 6;
        caveLightV2.CaveType = CaveTypeEnumV2.bu;
        caveLightV2.TotalUsed = 42;
        caveLightV2.TotalCapacity = 55;

        CaveLightModelV2 migratedCave = ModelMigrationHelper.getCaveLight(caveLightV1);
        assertNotNull(migratedCave);
        assertEquals(caveLightV2.Name, migratedCave.Name);
        assertEquals(caveLightV2.Id, migratedCave.Id);
        assertEquals(caveLightV2.CaveType, migratedCave.CaveType);
        assertEquals(caveLightV2.TotalUsed, migratedCave.TotalUsed);
        assertEquals(caveLightV2.TotalCapacity, migratedCave.TotalCapacity);
    }

    @Test
    public void getPattern() {
        // null
        assertNull(ModelMigrationHelper.getPattern(null));

        // incorrect
        IPatternModel pattern = new IPatternModel() {
        };
        assertNull(ModelMigrationHelper.getPattern(pattern));

        // V2
        pattern = new PatternModelV2();
        assertEquals(pattern, ModelMigrationHelper.getPattern(pattern));

        // V1
        PatternModelV1 patternV1 = new PatternModelV1();
        patternV1.Id = 6;
        patternV1.IsHorizontallyExpendable = true;
        patternV1.IsVerticallyExpendable = false;
        patternV1.IsInverted = true;
        patternV1.Type = PatternTypeEnumV1.STAGGERED_ROWS;
        patternV1.Order = 3;
        patternV1.NumberBottlesByColumn = 4;
        patternV1.NumberBottlesByRow = 3;
        patternV1.PlaceMap.put(null, CavePlaceTypeEnumV1.NO_PLACE);
        patternV1.PlaceMap.put(new CoordinatesModelV1(2, 2), null);
        patternV1.PlaceMap.put(new CoordinatesModelV1(1, 3), CavePlaceTypeEnumV1.PLACE_BOTTOM_LEFT_BEER);

        PatternModelV2 patternV2 = new PatternModelV2();
        patternV2.Id = 6;
        patternV2.IsHorizontallyExpendable = true;
        patternV2.IsVerticallyExpendable = false;
        patternV2.IsInverted = true;
        patternV2.Type = PatternTypeEnumV2.s;
        patternV2.Order = 3;
        patternV2.NumberBottlesByColumn = 4;
        patternV2.NumberBottlesByRow = 3;
        patternV2.PlaceMap.put(new CoordinatesModelV2(1, 3), CavePlaceTypeEnumV2.blb);

        PatternModelV2 migratedPattern = ModelMigrationHelper.getPattern(patternV1);
        assertNotNull(migratedPattern);
        assertEquals(patternV2.Id, migratedPattern.Id);
        assertEquals(patternV2.IsHorizontallyExpendable, migratedPattern.IsHorizontallyExpendable);
        assertEquals(patternV2.IsVerticallyExpendable, migratedPattern.IsVerticallyExpendable);
        assertEquals(patternV2.IsInverted, migratedPattern.IsInverted);
        assertEquals(patternV2.Type, migratedPattern.Type);
        assertEquals(patternV2.Order, migratedPattern.Order);
        assertEquals(patternV2.NumberBottlesByColumn, migratedPattern.NumberBottlesByColumn);
        assertEquals(patternV2.NumberBottlesByRow, migratedPattern.NumberBottlesByRow);
        assertEquals(patternV2.PlaceMap.size(), migratedPattern.PlaceMap.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceTypeEnumV2> entry : migratedPattern.PlaceMap.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceTypeEnumV2 value = entry.getValue();
            assertTrue(patternV2.PlaceMap.containsKey(key));
            assertEquals(value, patternV2.PlaceMap.get(key));
        }
    }

    @Test
    public void getCoordinatesModel() {
        // null
        assertNull(ModelMigrationHelper.getCoordinatesModel(null));

        // incorrect
        ICoordinatesModel coordinates = new ICoordinatesModel() {
        };
        assertNull(ModelMigrationHelper.getCoordinatesModel(coordinates));

        // V2
        coordinates = new CoordinatesModelV2(4, 5);
        assertEquals(coordinates, ModelMigrationHelper.getCoordinatesModel(coordinates));

        // V1
        coordinates = new CoordinatesModelV1(2, 4);
        CoordinatesModelV2 coordinatesV2 = new CoordinatesModelV2(2, 4);
        CoordinatesModelV2 migratedCoordinates = ModelMigrationHelper.getCoordinatesModel(coordinates);
        assertNotNull(migratedCoordinates);
        assertEquals(coordinatesV2, migratedCoordinates);
    }

    @Test
    public void getCave() {
        // null
        assertNull(ModelMigrationHelper.getCave(null));

        // incorrect
        ICaveModel cave = new ICaveModel() {
        };
        assertNull(ModelMigrationHelper.getCave(cave));

        // V2
        cave = new CaveModelV2();
        assertEquals(cave, ModelMigrationHelper.getCave(cave));

        // V1
        CaveModelV1 caveV1 = new CaveModelV1();
        caveV1.Name = "Toto";
        caveV1.Id = 6;
        caveV1.CaveType = CaveTypeEnumV1.BULK;
        CaveArrangementModelV1 arrangement1 = new CaveArrangementModelV1();
        arrangement1.TotalUsed = 42;
        arrangement1.NumberBoxes = 3;
        caveV1.CaveArrangement = arrangement1;

        CaveModelV2 caveV2 = new CaveModelV2();
        caveV2.Name = "Toto";
        caveV2.Id = 6;
        caveV2.CaveType = CaveTypeEnumV2.bu;
        CaveArrangementModelV2 arrangement2 = new CaveArrangementModelV2();
        arrangement2.TotalUsed = 42;
        arrangement2.NumberBoxes = 3;
        caveV2.CaveArrangement = arrangement2;

        CaveModelV2 migratedCave = ModelMigrationHelper.getCave(caveV1);
        assertNotNull(migratedCave);
        assertEquals(caveV2.Name, migratedCave.Name);
        assertEquals(caveV2.Id, migratedCave.Id);
        assertEquals(caveV2.CaveType, migratedCave.CaveType);
        assertNotNull(migratedCave.CaveArrangement);
        assertEquals(caveV2.CaveArrangement.TotalUsed, migratedCave.CaveArrangement.TotalUsed);
        assertEquals(caveV2.CaveArrangement.NumberBoxes, migratedCave.CaveArrangement.NumberBoxes);
    }

    @Test
    public void getCaveArrangement() {
        // null
        assertNull(ModelMigrationHelper.getCaveArrangement(null));

        // incorrect
        ICaveArrangementModel arrangement = new ICaveArrangementModel() {
        };
        assertNull(ModelMigrationHelper.getCaveArrangement(arrangement));

        // V2
        arrangement = new CaveArrangementModelV2();
        assertEquals(arrangement, ModelMigrationHelper.getCaveArrangement(arrangement));

        // V1
        CaveArrangementModelV1 arrangementV1 = new CaveArrangementModelV1();
        arrangementV1.Id = 6;
        arrangementV1.BoxesNumberBottlesByColumn = 4;
        arrangementV1.BoxesNumberBottlesByRow = 3;
        arrangementV1.NumberBottlesBulk = 6;
        arrangementV1.NumberBoxes = 4;
        arrangementV1.TotalCapacity = 4;
        arrangementV1.TotalUsed = 6;
        arrangementV1.IntNumberPlacedBottlesByIdMap.put(42, 4);
        arrangementV1.IntNumberPlacedBottlesByIdMap.put(4, 7);
        arrangementV1.floatNumberPlacedBottlesByIdMap.put(5, 2.4f);
        arrangementV1.floatNumberPlacedBottlesByIdMap.put(3, 1f);
        arrangementV1.PatternMap.put(new CoordinatesModelV1(2, 2), null);
        arrangementV1.PatternMap.put(null, new PatternModelWithBottlesV1());
        PatternModelWithBottlesV1 pattern = new PatternModelWithBottlesV1();
        pattern.NumberBottlesByColumn = 5;
        pattern.Type = PatternTypeEnumV1.LINEAR;
        pattern.IsHorizontallyExpendable = true;
        arrangementV1.PatternMap.put(new CoordinatesModelV1(1, 3), pattern);

        CaveArrangementModelV2 arrangementV2 = new CaveArrangementModelV2();
        arrangementV2.Id = 6;
        arrangementV2.BoxesNumberBottlesByColumn = 4;
        arrangementV2.BoxesNumberBottlesByRow = 3;
        arrangementV2.NumberBottlesBulk = 6;
        arrangementV2.NumberBoxes = 4;
        arrangementV2.TotalCapacity = 4;
        arrangementV2.TotalUsed = 6;
        arrangementV2.IntNumberPlacedBottlesByIdMap.put(42, 4);
        arrangementV2.IntNumberPlacedBottlesByIdMap.put(4, 7);
        arrangementV2.FloatNumberPlacedBottlesByIdMap.put(5, 2.4f);
        arrangementV2.FloatNumberPlacedBottlesByIdMap.put(3, 1f);
        PatternModelWithBottlesV2 pattern2 = new PatternModelWithBottlesV2();
        pattern2.NumberBottlesByColumn = 5;
        pattern2.Type = PatternTypeEnumV2.l;
        pattern2.IsHorizontallyExpendable = true;
        arrangementV2.PatternMap.put(new CoordinatesModelV2(1, 3), pattern2);

        CaveArrangementModelV2 migratedArrangement = ModelMigrationHelper.getCaveArrangement(arrangementV1);
        assertNotNull(migratedArrangement);
        assertEquals(arrangementV2.Id, migratedArrangement.Id);
        assertEquals(arrangementV2.BoxesNumberBottlesByColumn, migratedArrangement.BoxesNumberBottlesByColumn);
        assertEquals(arrangementV2.BoxesNumberBottlesByRow, migratedArrangement.BoxesNumberBottlesByRow);
        assertEquals(arrangementV2.NumberBottlesBulk, migratedArrangement.NumberBottlesBulk);
        assertEquals(arrangementV2.NumberBoxes, migratedArrangement.NumberBoxes);
        assertEquals(arrangementV2.TotalCapacity, migratedArrangement.TotalCapacity);
        assertEquals(arrangementV2.TotalUsed, migratedArrangement.TotalUsed);
        assertEquals(arrangementV2.IntNumberPlacedBottlesByIdMap.size(), migratedArrangement.IntNumberPlacedBottlesByIdMap.size());
        for (Map.Entry<Integer, Integer> entry : migratedArrangement.IntNumberPlacedBottlesByIdMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            assertTrue(arrangementV2.IntNumberPlacedBottlesByIdMap.containsKey(key));
            assertEquals(value, (int) arrangementV2.IntNumberPlacedBottlesByIdMap.get(key));
        }
        assertEquals(arrangementV2.FloatNumberPlacedBottlesByIdMap.size(), migratedArrangement.FloatNumberPlacedBottlesByIdMap.size());
        for (Map.Entry<Integer, Float> entry : migratedArrangement.FloatNumberPlacedBottlesByIdMap.entrySet()) {
            int key = entry.getKey();
            float value = entry.getValue();
            assertTrue(arrangementV2.FloatNumberPlacedBottlesByIdMap.containsKey(key));
            assertEquals(value, arrangementV2.FloatNumberPlacedBottlesByIdMap.get(key), 0.5f);
        }
        assertEquals(arrangementV2.PatternMap.size(), migratedArrangement.PatternMap.size());
        for (Map.Entry<CoordinatesModelV2, PatternModelWithBottlesV2> entry : migratedArrangement.PatternMap.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            PatternModelWithBottlesV2 value = entry.getValue();
            assertTrue(arrangementV2.PatternMap.containsKey(key));
            PatternModelWithBottlesV2 patternWithBottles = arrangementV2.PatternMap.get(key);
            assertEquals(value.NumberBottlesByColumn, patternWithBottles.NumberBottlesByColumn);
            assertEquals(value.Type, patternWithBottles.Type);
            assertEquals(value.IsHorizontallyExpendable, patternWithBottles.IsHorizontallyExpendable);
        }
    }

    @Test
    public void getPatternModelWithBottles() {
        // null
        assertNull(ModelMigrationHelper.getPatternModelWithBottles(null));

        // incorrect
        IPatternModelWithBottles pattern = new IPatternModelWithBottles() {
        };
        assertNull(ModelMigrationHelper.getPatternModelWithBottles(pattern));

        // V2
        pattern = new PatternModelWithBottlesV2();
        assertEquals(pattern, ModelMigrationHelper.getPatternModelWithBottles(pattern));

        // V1
        PatternModelWithBottlesV1 patternV1 = new PatternModelWithBottlesV1();
        patternV1.Id = 6;
        patternV1.IsHorizontallyExpendable = true;
        patternV1.IsVerticallyExpendable = false;
        patternV1.IsInverted = true;
        patternV1.Type = PatternTypeEnumV1.STAGGERED_ROWS;
        patternV1.Order = 3;
        patternV1.NumberBottlesByColumn = 4;
        patternV1.NumberBottlesByRow = 3;
        patternV1.PlaceMap.put(null, CavePlaceTypeEnumV1.NO_PLACE);
        patternV1.PlaceMap.put(new CoordinatesModelV1(2, 2), null);
        patternV1.PlaceMap.put(new CoordinatesModelV1(1, 3), CavePlaceTypeEnumV1.PLACE_BOTTOM_LEFT_BEER);
        patternV1.PlaceMapWithBottles.put(null, new CavePlaceModelV1());
        patternV1.PlaceMapWithBottles.put(new CoordinatesModelV1(2, 2), null);
        CavePlaceModelV1 placeModel = new CavePlaceModelV1();
        placeModel.IsClickable = true;
        placeModel.BottleId = 42;
        placeModel.PlaceType = CavePlaceTypeEnumV1.PLACE_BOTTOM_LEFT;
        patternV1.PlaceMapWithBottles.put(new CoordinatesModelV1(1, 3), placeModel);
        patternV1.FloatNumberPlacedBottlesByIdMap.put(42, 1f);
        patternV1.FloatNumberPlacedBottlesByIdMap.put(43, 4.5f);

        PatternModelWithBottlesV2 patternV2 = new PatternModelWithBottlesV2();
        patternV2.Id = 6;
        patternV2.IsHorizontallyExpendable = true;
        patternV2.IsVerticallyExpendable = false;
        patternV2.IsInverted = true;
        patternV2.Type = PatternTypeEnumV2.s;
        patternV2.Order = 3;
        patternV2.NumberBottlesByColumn = 4;
        patternV2.NumberBottlesByRow = 3;
        patternV2.PlaceMap.put(new CoordinatesModelV2(1, 3), CavePlaceTypeEnumV2.blb);
        CavePlaceModelV2 placeV2 = new CavePlaceModelV2();
        placeV2.IsClickable = true;
        placeV2.BottleId = 42;
        placeV2.PlaceType = CavePlaceTypeEnumV2.bl;
        patternV2.PlaceMapWithBottles.put(new CoordinatesModelV2(1, 3), placeV2);
        patternV2.FloatNumberPlacedBottlesByIdMap.put(42, 1f);
        patternV2.FloatNumberPlacedBottlesByIdMap.put(43, 4.5f);

        PatternModelWithBottlesV2 migratedPattern = ModelMigrationHelper.getPatternModelWithBottles(patternV1);
        assertNotNull(migratedPattern);
        assertEquals(patternV2.Id, migratedPattern.Id);
        assertEquals(patternV2.IsHorizontallyExpendable, migratedPattern.IsHorizontallyExpendable);
        assertEquals(patternV2.IsVerticallyExpendable, migratedPattern.IsVerticallyExpendable);
        assertEquals(patternV2.IsInverted, migratedPattern.IsInverted);
        assertEquals(patternV2.Type, migratedPattern.Type);
        assertEquals(patternV2.Order, migratedPattern.Order);
        assertEquals(patternV2.NumberBottlesByColumn, migratedPattern.NumberBottlesByColumn);
        assertEquals(patternV2.NumberBottlesByRow, migratedPattern.NumberBottlesByRow);
        assertEquals(patternV2.PlaceMap.size(), migratedPattern.PlaceMap.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceTypeEnumV2> entry : migratedPattern.PlaceMap.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceTypeEnumV2 value = entry.getValue();
            assertTrue(patternV2.PlaceMap.containsKey(key));
            assertEquals(value, patternV2.PlaceMap.get(key));
        }
        assertEquals(patternV2.PlaceMapWithBottles.size(), migratedPattern.PlaceMapWithBottles.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> entry : migratedPattern.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceModelV2 value = entry.getValue();
            assertTrue(patternV2.PlaceMapWithBottles.containsKey(key));
            CavePlaceModelV2 place = patternV2.PlaceMapWithBottles.get(key);
            assertEquals(value.PlaceType, place.PlaceType);
            assertEquals(value.IsClickable, place.IsClickable);
            assertEquals(value.BottleId, place.BottleId);
        }
        assertEquals(patternV2.FloatNumberPlacedBottlesByIdMap.size(), migratedPattern.FloatNumberPlacedBottlesByIdMap.size());
        for (Map.Entry<Integer, Float> entry : migratedPattern.FloatNumberPlacedBottlesByIdMap.entrySet()) {
            int key = entry.getKey();
            float value = entry.getValue();
            assertTrue(patternV2.FloatNumberPlacedBottlesByIdMap.containsKey(key));
            assertEquals(value, patternV2.FloatNumberPlacedBottlesByIdMap.get(key), 0.5f);
        }
    }

    @Test
    public void getCavePlaceModel() {
        // null
        assertNull(ModelMigrationHelper.getCavePlaceModel(null));

        // incorrect
        ICavePlaceModel cavePlace = new ICavePlaceModel() {
        };
        assertNull(ModelMigrationHelper.getCavePlaceModel(cavePlace));

        // V2
        cavePlace = new CavePlaceModelV2();
        assertEquals(cavePlace, ModelMigrationHelper.getCavePlaceModel(cavePlace));

        // V1
        CavePlaceModelV1 cavePlaceV1 = new CavePlaceModelV1();
        cavePlaceV1.BottleId = 24;
        cavePlaceV1.IsClickable = true;
        cavePlaceV1.PlaceType = CavePlaceTypeEnumV1.PLACE_BOTTOM_LEFT_CIDER;

        CavePlaceModelV2 cavePlaceV2 = new CavePlaceModelV2();
        cavePlaceV2.BottleId = 24;
        cavePlaceV2.IsClickable = true;
        cavePlaceV2.PlaceType = CavePlaceTypeEnumV2.blci;

        CavePlaceModelV2 migratedCavePlace = ModelMigrationHelper.getCavePlaceModel(cavePlaceV1);
        assertNotNull(migratedCavePlace);
        assertEquals(cavePlaceV2.BottleId, migratedCavePlace.BottleId);
        assertEquals(cavePlaceV2.IsClickable, migratedCavePlace.IsClickable);
        assertEquals(cavePlaceV2.PlaceType, migratedCavePlace.PlaceType);
    }

    @Test
    public void getSync() {
        // null
        assertNull(ModelMigrationHelper.getSync(null));

        // incorrect
        ISyncModel sync = new ISyncModel() {
        };
        assertNull(ModelMigrationHelper.getSync(sync));

        // V2
        sync = new SyncModelV2();
        assertEquals(sync, ModelMigrationHelper.getSync(sync));

        // V1
        SyncModelV1 syncV1 = new SyncModelV1();
        syncV1.Version = "Toto";
        syncV1.Bottles.add(null);
        BottleModelV1 bottle1 = new BottleModelV1();
        bottle1.Id = 7;
        syncV1.Bottles.add(bottle1);
        CaveModelV1 cave1 = new CaveModelV1();
        cave1.Id = 6;
        syncV1.Caves.add(cave1);
        syncV1.Caves.add(null);
        syncV1.Patterns.add(null);
        PatternModelV1 pattern1 = new PatternModelV1();
        pattern1.Id = 9;
        syncV1.Patterns.add(pattern1);

        SyncModelV2 syncV2 = new SyncModelV2();
        syncV2.Version = "Toto";
        BottleModelV2 bottle2 = new BottleModelV2();
        bottle2.Id = 7;
        syncV2.Bottles.add(bottle2);
        CaveModelV2 cave2 = new CaveModelV2();
        cave2.Id = 6;
        syncV2.Caves.add(cave2);
        PatternModelV2 pattern2 = new PatternModelV2();
        pattern2.Id = 9;
        syncV2.Patterns.add(pattern2);

        SyncModelV2 migratedSync = ModelMigrationHelper.getSync(syncV1);
        assertNotNull(migratedSync);
        assertEquals(syncV2.Version, migratedSync.Version);
        assertEquals(syncV2.Bottles.size(), migratedSync.Bottles.size());
        for (int i = 0; i < migratedSync.Bottles.size(); i++) {
            assertEquals(syncV2.Bottles.get(i).Id, migratedSync.Bottles.get(i).Id);
        }
        assertEquals(syncV2.Caves.size(), migratedSync.Caves.size());
        for (int i = 0; i < migratedSync.Caves.size(); i++) {
            assertEquals(syncV2.Caves.get(i).Id, migratedSync.Caves.get(i).Id);
        }
        assertEquals(syncV2.Patterns.size(), migratedSync.Patterns.size());
        for (int i = 0; i < migratedSync.Patterns.size(); i++) {
            assertEquals(syncV2.Patterns.get(i).Id, migratedSync.Patterns.get(i).Id);
        }
    }
}