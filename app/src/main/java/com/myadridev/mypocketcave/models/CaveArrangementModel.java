package com.myadridev.mypocketcave.models;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.managers.PatternManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(as = CaveArrangementModel.class)
public class CaveArrangementModel {

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, PatternModelWithBottles> PatternMap;
    public final Map<Integer, Integer> IntNumberPlacedBottlesByIdMap;
    public int Id;
    public int TotalCapacity;
    public int TotalUsed;
    public int NumberBottlesBulk;
    public int NumberBoxes;
    public int BoxesNumberBottlesByColumn;
    public int BoxesNumberBottlesByRow;
    private Map<Integer, Float> floatNumberPlacedBottlesByIdMap;

    public CaveArrangementModel() {
        PatternMap = new HashMap<>();
        floatNumberPlacedBottlesByIdMap = new HashMap<>();
        IntNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public CaveArrangementModel(CaveArrangementModel caveArrangement) {
        Id = caveArrangement.Id;
        TotalCapacity = caveArrangement.TotalCapacity;
        TotalUsed = caveArrangement.TotalUsed;
        PatternMap = new HashMap<>(caveArrangement.PatternMap.size());
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : caveArrangement.PatternMap.entrySet()) {
            PatternMap.put(patternEntry.getKey(), new PatternModelWithBottles(patternEntry.getValue()));
        }
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        BoxesNumberBottlesByColumn = caveArrangement.BoxesNumberBottlesByColumn;
        BoxesNumberBottlesByRow = caveArrangement.BoxesNumberBottlesByRow;
        floatNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.floatNumberPlacedBottlesByIdMap);
        IntNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.IntNumberPlacedBottlesByIdMap);
    }

    public void resetFloatNumberPlacedBottlesByIdMap() {
        floatNumberPlacedBottlesByIdMap.clear();
    }

    public Map<Integer, Float> getFloatNumberPlacedBottlesByIdMap() {
        if (floatNumberPlacedBottlesByIdMap.size() > 0) {
            return floatNumberPlacedBottlesByIdMap;
        } else {
            Map<Integer, Float> numberPlacedBottlesByIdMap = new HashMap<>();
            for (PatternModelWithBottles pattern : PatternMap.values()) {
                for (Map.Entry<Integer, Float> patternNumberEntry : pattern.FloatNumberPlacedBottlesByIdMap.entrySet()) {
                    int bottleId = patternNumberEntry.getKey();
                    float numberPlaced = patternNumberEntry.getValue();
                    float oldNumberPlaced = CollectionsHelper.getValueOrDefault(numberPlacedBottlesByIdMap, bottleId, 0f);
                    numberPlacedBottlesByIdMap.put(bottleId, oldNumberPlaced + numberPlaced);
                }
            }
            return numberPlacedBottlesByIdMap;
        }
    }

    public void computeTotalCapacityWithBulk() {
        TotalCapacity = NumberBottlesBulk;
    }

    public void computeTotalCapacityWithBoxes() {
        TotalCapacity = BoxesNumberBottlesByColumn * BoxesNumberBottlesByRow * NumberBoxes;
    }

    public void computeTotalCapacityWithPattern() {
        int capacity = 0;
        int capacityDoubled = 0; // for the half-places, there will be added twice

        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();

            capacity += pattern.getCapacityAlone();

            // if pattern.IsHorizontallyExpendable look at left and right pattern
            if (pattern.IsHorizontallyExpendable) {
                // number of half-places : pattern.NumberBottlesByColumn (-1 if not inverted)
                CoordinatesModel leftCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col - 1);
                if (PatternMap.containsKey(leftCoordinates)) {
                    PatternModelWithBottles leftPattern = PatternMap.get(leftCoordinates);
                    if (pattern.isPatternHorizontallyCompatible(leftPattern)) {
                        capacityDoubled += pattern.NumberBottlesByColumn - (pattern.IsInverted ? 0 : 1);
                    }
                }
                CoordinatesModel rightCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col + 1);
                if (PatternMap.containsKey(rightCoordinates)) {
                    PatternModelWithBottles rightPattern = PatternMap.get(rightCoordinates);
                    if (pattern.isPatternHorizontallyCompatible(rightPattern)) {
                        capacityDoubled += pattern.NumberBottlesByColumn - (pattern.IsInverted ? 0 : 1);
                    }
                }
            }

            // if pattern.IsVerticallyExpendable look at top and bottom pattern
            if (pattern.IsVerticallyExpendable) {
                // number of half-places : pattern.NumberBottlesByRow (-1 if not inverted)
                CoordinatesModel bottomCoordinates = new CoordinatesModel(patternCoordinates.Row - 1, patternCoordinates.Col);
                if (PatternMap.containsKey(bottomCoordinates)) {
                    PatternModelWithBottles bottomPattern = PatternMap.get(bottomCoordinates);
                    if (pattern.isPatternVerticallyCompatible(bottomPattern)) {
                        capacityDoubled += pattern.NumberBottlesByRow - (pattern.IsInverted ? 0 : 1);
                    }
                }
                CoordinatesModel topCoordinates = new CoordinatesModel(patternCoordinates.Row + 1, patternCoordinates.Col);
                if (PatternMap.containsKey(topCoordinates)) {
                    PatternModelWithBottles topPattern = PatternMap.get(topCoordinates);
                    if (pattern.isPatternVerticallyCompatible(topPattern)) {
                        capacityDoubled += pattern.NumberBottlesByRow - (pattern.IsInverted ? 0 : 1);
                    }
                } else {
                    capacity += pattern.NumberBottlesByRow - (pattern.IsInverted ? 0 : 1);
                }
            }
        }
        capacity += capacityDoubled / 2;
        TotalCapacity = capacity;
    }

    public void movePatternMapToLeft() {
        Map<CoordinatesModel, PatternModelWithBottles> patternMapToLeft = new HashMap<>(PatternMap.size());

        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternMapEntry : PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternMapEntry.getKey();
            patternMapToLeft.put(new CoordinatesModel(coordinates.Row, coordinates.Col - 1), patternMapEntry.getValue());
        }

        PatternMap.clear();
        PatternMap.putAll(patternMapToLeft);
    }

    public void movePatternMapToRight() {
        movePatternMapToRight(false);
    }

    public void movePatternMapToRight(boolean isForceMove) {
        if (!isForceMove && !PatternMap.containsKey(new CoordinatesModel(0, 0))) return;
        Map<CoordinatesModel, PatternModelWithBottles> patternMapToRight = new HashMap<>(PatternMap.size());

        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternMapEntry : PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternMapEntry.getKey();
            patternMapToRight.put(new CoordinatesModel(coordinates.Row, coordinates.Col + 1), patternMapEntry.getValue());
        }

        PatternMap.clear();
        PatternMap.putAll(patternMapToRight);
    }

    public void setClickablePlaces() {
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternMapEntry : PatternMap.entrySet()) {
            PatternModelWithBottles pattern = patternMapEntry.getValue();
            pattern.setClickablePlaces();

            CoordinatesModel coordinates = patternMapEntry.getKey();
            if (pattern.IsHorizontallyExpendable) {
                CoordinatesModel coordinatesRight = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
                if (PatternMap.containsKey(coordinatesRight)) {
                    PatternModelWithBottles rightPattern = PatternMap.get(coordinatesRight);
                    if (pattern.isPatternHorizontallyCompatible(rightPattern)) {
                        pattern.setRightClickablePlaces();
                        rightPattern.setLeftClickablePlaces();
                    }
                }
            }

            if (pattern.IsVerticallyExpendable) {
                CoordinatesModel coordinatesTop = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
                if (PatternMap.containsKey(coordinatesTop)) {
                    PatternModelWithBottles topPattern = PatternMap.get(coordinatesTop);
                    if (pattern.isPatternVerticallyCompatible(topPattern)) {
                        pattern.setTopClickablePlaces();
                        topPattern.setBottomClickablePlaces();
                    }
                } else {
                    pattern.setTopClickablePlaces();
                }
            }
        }
    }

    public Map<CoordinatesModel, List<CoordinatesModel>> unplaceBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId) {
        return updateBottle(patternCoordinates, coordinates, bottleId, false, true);
    }

    public void unplaceBottle(int bottleId, int quantity) {
        updateBottle(bottleId, quantity, false, true);
    }

    public void placeBottle(int bottleId, int quantity) {
        updateBottle(bottleId, quantity, true, true);
    }

    public Map<CoordinatesModel, List<CoordinatesModel>> placeBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId) {
        return updateBottle(patternCoordinates, coordinates, bottleId, true, true);
    }

    private void updateBottle(int bottleId, int quantity, boolean isAddBottle, boolean updateBottleUsedQuantity) {
        BottleModel bottle = BottleManager.getBottle(bottleId);
        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, sign * quantity);

        if (updateBottleUsedQuantity) {
            if (isAddBottle) {
                TotalUsed += quantity;
            } else {
                TotalUsed -= quantity;
            }
        }
    }

    private Map<CoordinatesModel, List<CoordinatesModel>> updateBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId, boolean isAddBottle, boolean updateBottleUsedQuantity) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CavePlaceModel cavePlace = pattern.PlaceMapWithBottles.get(coordinates);

        BottleModel bottle = BottleManager.getBottle(bottleId);

        Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = new HashMap<>();
        switch (cavePlace.PlaceType.Position) {
            case BOTTOM_LEFT:
                coordinatesUpdated.putAll(updateBottomLeftBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle));
                break;
            case BOTTOM_RIGHT:
                coordinatesUpdated.putAll(updateBottomRightBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle));
                break;
            case TOP_LEFT:
                coordinatesUpdated.putAll(updateTopLeftBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle));
                break;
            case TOP_RIGHT:
                coordinatesUpdated.putAll(updateTopRightBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle));
                break;
        }

        if (updateBottleUsedQuantity) {
            if (isAddBottle) {
                TotalUsed++;
            } else {
                TotalUsed--;
            }
        }
        return coordinatesUpdated;
    }

    private Map<CoordinatesModel, List<CoordinatesModel>> updateTopRightBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = new HashMap<>();
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        addCoordinateToUpdate(coordinatesUpdated, patternCoordinates, coordinates);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        PatternInfos rightPatternInfos = getRightPatternInfos(patternCoordinates, coordinates);
        if (rightPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(rightPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(rightPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, rightPatternInfos.PatternCoordinates, rightPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos topPatternInfos = getTopPatternInfos(patternCoordinates, coordinates);
        if (topPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(topPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(topPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, topPatternInfos.PatternCoordinates, topPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos topRightPatternInfos = getTopRightPatternInfos(patternCoordinates, coordinates);
        if (topRightPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(topRightPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(topRightPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, topRightPatternInfos.PatternCoordinates, topRightPatternInfos.CavePlaceCoordinates);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);

        return coordinatesUpdated;
    }

    private Map<CoordinatesModel, List<CoordinatesModel>> updateTopLeftBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = new HashMap<>();
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        addCoordinateToUpdate(coordinatesUpdated, patternCoordinates, coordinates);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        PatternInfos leftPatternInfos = getLeftPatternInfos(patternCoordinates, coordinates);
        if (leftPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(leftPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(leftPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, leftPatternInfos.PatternCoordinates, leftPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos topPatternInfos = getTopPatternInfos(patternCoordinates, coordinates);
        if (topPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(topPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(topPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, topPatternInfos.PatternCoordinates, topPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos topLeftPatternInfos = getTopLeftPatternInfos(patternCoordinates, coordinates);
        if (topLeftPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(topLeftPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(topLeftPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, topLeftPatternInfos.PatternCoordinates, topLeftPatternInfos.CavePlaceCoordinates);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);

        return coordinatesUpdated;
    }

    private Map<CoordinatesModel, List<CoordinatesModel>> updateBottomRightBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = new HashMap<>();
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        addCoordinateToUpdate(coordinatesUpdated, patternCoordinates, coordinates);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        PatternInfos rightPatternInfos = getRightPatternInfos(patternCoordinates, coordinates);
        if (rightPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(rightPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(rightPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, rightPatternInfos.PatternCoordinates, rightPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos bottomPatternInfos = getBottomPatternInfos(patternCoordinates, coordinates);
        if (bottomPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(bottomPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(bottomPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, bottomPatternInfos.PatternCoordinates, bottomPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos bottomRightPatternInfos = getBottomRightPatternInfos(patternCoordinates, coordinates);
        if (bottomRightPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(bottomRightPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(bottomRightPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, bottomRightPatternInfos.PatternCoordinates, bottomRightPatternInfos.CavePlaceCoordinates);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);

        return coordinatesUpdated;
    }

    private Map<CoordinatesModel, List<CoordinatesModel>> updateBottomLeftBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated = new HashMap<>();
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        addCoordinateToUpdate(coordinatesUpdated, patternCoordinates, coordinates);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        PatternInfos leftPatternInfos = getLeftPatternInfos(patternCoordinates, coordinates);
        if (leftPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(leftPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(leftPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, leftPatternInfos.PatternCoordinates, leftPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos bottomPatternInfos = getBottomPatternInfos(patternCoordinates, coordinates);
        if (bottomPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(bottomPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(bottomPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, bottomPatternInfos.PatternCoordinates, bottomPatternInfos.CavePlaceCoordinates);
        }
        PatternInfos bottomLeftPatternInfos = getBottomLeftPatternInfos(patternCoordinates, coordinates);
        if (bottomLeftPatternInfos != null) {
            numberPlaces++;
            updateCavePlace(bottomLeftPatternInfos.CavePlace, bottle, isAddBottle);
            notNullPatterns.add(bottomLeftPatternInfos.Pattern);
            addCoordinateToUpdate(coordinatesUpdated, bottomLeftPatternInfos.PatternCoordinates, bottomLeftPatternInfos.CavePlaceCoordinates);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);

        return coordinatesUpdated;
    }

    private void addCoordinateToUpdate(Map<CoordinatesModel, List<CoordinatesModel>> coordinatesUpdated, CoordinatesModel patternCoordinates, CoordinatesModel cavePlaceCoordinates) {
        if (!coordinatesUpdated.containsKey(patternCoordinates)) {
            coordinatesUpdated.put(patternCoordinates, new ArrayList<>());
        }
        coordinatesUpdated.get(patternCoordinates).add(cavePlaceCoordinates);
    }

    private void updateNumberPlaced(BottleModel bottle, List<PatternModelWithBottles> notNullPatterns, float numberPlaces, int intNumberOfBottlesToAdd) {
        float floatNumberOfBottlesToAdd = 1f / numberPlaces;
        for (PatternModelWithBottles pat : notNullPatterns) {
            float oldNumberFloat = CollectionsHelper.getValueOrDefault(pat.FloatNumberPlacedBottlesByIdMap, bottle.Id, 0f);
            float newNumberFloat = oldNumberFloat + floatNumberOfBottlesToAdd;
            if (newNumberFloat == 0) {
                pat.FloatNumberPlacedBottlesByIdMap.remove(bottle.Id);
            } else {
                pat.FloatNumberPlacedBottlesByIdMap.put(bottle.Id, newNumberFloat);
            }
        }
        int oldNumberInt = CollectionsHelper.getValueOrDefault(IntNumberPlacedBottlesByIdMap, bottle.Id, 0);
        int newNumberInt = oldNumberInt + intNumberOfBottlesToAdd;
        if (newNumberInt == 0) {
            IntNumberPlacedBottlesByIdMap.remove(bottle.Id);
        } else {
            IntNumberPlacedBottlesByIdMap.put(bottle.Id, newNumberInt);
        }
    }

    private void updateNumberPlaced(BottleModel bottle, int intNumberOfBottlesToAdd) {
        float oldNumberFloat = CollectionsHelper.getValueOrDefault(floatNumberPlacedBottlesByIdMap, bottle.Id, 0f);
        float newNumberFloat = oldNumberFloat + intNumberOfBottlesToAdd;
        if (newNumberFloat == 0) {
            floatNumberPlacedBottlesByIdMap.remove(bottle.Id);
        } else {
            floatNumberPlacedBottlesByIdMap.put(bottle.Id, newNumberFloat);
        }
        int oldNumberInt = CollectionsHelper.getValueOrDefault(IntNumberPlacedBottlesByIdMap, bottle.Id, 0);
        int newNumberInt = oldNumberInt + intNumberOfBottlesToAdd;
        if (newNumberInt == 0) {
            IntNumberPlacedBottlesByIdMap.remove(bottle.Id);
        } else {
            IntNumberPlacedBottlesByIdMap.put(bottle.Id, newNumberInt);
        }
    }

    private void updateCavePlace(CavePlaceModel cavePlaceToUpdate, BottleModel bottle, boolean isAddBottle) {
        if (cavePlaceToUpdate == null) return;
        cavePlaceToUpdate.BottleId = isAddBottle ? bottle.Id : -1;
        cavePlaceToUpdate.PlaceType = isAddBottle
                ? CavePlaceTypeEnum.getByPositionAndColor(cavePlaceToUpdate.PlaceType.Position, bottle.WineColor)
                : CavePlaceTypeEnum.getEmptyByPosition(cavePlaceToUpdate.PlaceType.Position);
    }

    @Nullable
    private PatternInfos getTopRightPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topRightCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(topRightCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(topRightCoordinates), pattern, patternCoordinates, topRightCoordinates);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row + 1)) {
            // if pattern.PlaceMapWithBottles contains the row + 1 -> search rightPattern
            Pair<PatternModelWithBottles, CoordinatesModel> rightPatternAndCoordinates = getRightPatternAndCoordinates(patternCoordinates);
            if (rightPatternAndCoordinates != null) {
                PatternModelWithBottles rightPattern = rightPatternAndCoordinates.first;
                CoordinatesModel topRightOtherCoordinates = new CoordinatesModel(coordinates.Row + 1, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(topRightOtherCoordinates)) {
                    return new PatternInfos(rightPattern.PlaceMapWithBottles.get(topRightOtherCoordinates), rightPattern, rightPatternAndCoordinates.second, topRightOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col + 1 -> search topPattern
            Pair<PatternModelWithBottles, CoordinatesModel> topPatternAndCoordinates = getTopPatternAndCoordinates(patternCoordinates);
            if (topPatternAndCoordinates != null) {
                PatternModelWithBottles topPattern = topPatternAndCoordinates.first;
                CoordinatesModel topRightOtherCoordinates = new CoordinatesModel(0, coordinates.Col + 1);
                if (topPattern.PlaceMapWithBottles.containsKey(topRightOtherCoordinates)) {
                    return new PatternInfos(topPattern.PlaceMapWithBottles.get(topRightOtherCoordinates), topPattern, topPatternAndCoordinates.second, topRightOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternInfos getTopLeftPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topLeftCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(topLeftCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(topLeftCoordinates), pattern, patternCoordinates, topLeftCoordinates);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row + 1)) {
            // if pattern.PlaceMapWithBottles contains the row + 1 -> search leftPattern
            Pair<PatternModelWithBottles, CoordinatesModel> leftPatternAndCoordinates = getLeftPatternAndCoordinates(patternCoordinates);
            if (leftPatternAndCoordinates != null) {
                PatternModelWithBottles leftPattern = leftPatternAndCoordinates.first;
                CoordinatesModel topLeftOtherCoordinates = new CoordinatesModel(coordinates.Row + 1, CoordinatesManager.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(topLeftOtherCoordinates)) {
                    return new PatternInfos(leftPattern.PlaceMapWithBottles.get(topLeftOtherCoordinates), leftPattern, leftPatternAndCoordinates.second, topLeftOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col - 1 -> search topPattern
            Pair<PatternModelWithBottles, CoordinatesModel> topPatternAndCoordinates = getTopPatternAndCoordinates(patternCoordinates);
            if (topPatternAndCoordinates != null) {
                PatternModelWithBottles topPattern = topPatternAndCoordinates.first;
                CoordinatesModel topLeftOtherCoordinates = new CoordinatesModel(0, coordinates.Col - 1);
                if (topPattern.PlaceMapWithBottles.containsKey(topLeftOtherCoordinates)) {
                    return new PatternInfos(topPattern.PlaceMapWithBottles.get(topLeftOtherCoordinates), topPattern, topPatternAndCoordinates.second, topLeftOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternInfos getTopPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
        if (pattern.PlaceMapWithBottles.containsKey(topCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(topCoordinates), pattern, patternCoordinates, topCoordinates);
        } else {
            // search topPattern
            Pair<PatternModelWithBottles, CoordinatesModel> topPatternAndCoordinates = getTopPatternAndCoordinates(patternCoordinates);
            if (topPatternAndCoordinates != null) {
                PatternModelWithBottles topPattern = topPatternAndCoordinates.first;
                CoordinatesModel topOtherCoordinates = new CoordinatesModel(0, coordinates.Col);
                if (topPattern.PlaceMapWithBottles.containsKey(topOtherCoordinates)) {
                    return new PatternInfos(topPattern.PlaceMapWithBottles.get(topOtherCoordinates), topPattern, topPatternAndCoordinates.second, topOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternInfos getBottomRightPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomRightCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(bottomRightCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(bottomRightCoordinates), pattern, patternCoordinates, bottomRightCoordinates);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row - 1)) {
            // if pattern.PlaceMapWithBottles contains the row - 1 -> search rightPattern
            Pair<PatternModelWithBottles, CoordinatesModel> rightPatternAndCoordinates = getRightPatternAndCoordinates(patternCoordinates);
            if (rightPatternAndCoordinates != null) {
                PatternModelWithBottles rightPattern = rightPatternAndCoordinates.first;
                CoordinatesModel bottomRightOtherCoordinates = new CoordinatesModel(coordinates.Row - 1, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(bottomRightOtherCoordinates)) {
                    return new PatternInfos(rightPattern.PlaceMapWithBottles.get(bottomRightOtherCoordinates), rightPattern, rightPatternAndCoordinates.second, bottomRightOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col + 1 -> search bottomPattern
            Pair<PatternModelWithBottles, CoordinatesModel> bottomPatternAndCoordinates = getBottomPatternAndCoordinates(patternCoordinates);
            if (bottomPatternAndCoordinates != null) {
                PatternModelWithBottles bottomPattern = bottomPatternAndCoordinates.first;
                CoordinatesModel bottomRightOtherCoordinates = new CoordinatesModel(CoordinatesManager.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col + 1);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomRightOtherCoordinates)) {
                    return new PatternInfos(bottomPattern.PlaceMapWithBottles.get(bottomRightOtherCoordinates), bottomPattern, bottomPatternAndCoordinates.second, bottomRightOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternInfos getRightPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel rightCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(rightCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(rightCoordinates), pattern, patternCoordinates, rightCoordinates);
        } else {
            // search rightPattern
            Pair<PatternModelWithBottles, CoordinatesModel> rightPatternAndCoordinates = getRightPatternAndCoordinates(patternCoordinates);
            if (rightPatternAndCoordinates != null) {
                PatternModelWithBottles rightPattern = rightPatternAndCoordinates.first;
                CoordinatesModel rightOtherCoordinates = new CoordinatesModel(coordinates.Row, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(rightOtherCoordinates)) {
                    return new PatternInfos(rightPattern.PlaceMapWithBottles.get(rightOtherCoordinates), rightPattern, rightPatternAndCoordinates.second, rightOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternInfos getBottomLeftPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomLeftCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(bottomLeftCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(bottomLeftCoordinates), pattern, patternCoordinates, bottomLeftCoordinates);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row - 1)) {
            // if pattern.PlaceMapWithBottles contains the row - 1 -> search leftPattern
            Pair<PatternModelWithBottles, CoordinatesModel> leftPatternAndCoordinates = getLeftPatternAndCoordinates(patternCoordinates);
            if (leftPatternAndCoordinates != null) {
                PatternModelWithBottles leftPattern = leftPatternAndCoordinates.first;
                CoordinatesModel bottomLeftOtherCoordinates = new CoordinatesModel(coordinates.Row - 1, CoordinatesManager.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(bottomLeftOtherCoordinates)) {
                    return new PatternInfos(leftPattern.PlaceMapWithBottles.get(bottomLeftOtherCoordinates), leftPattern, leftPatternAndCoordinates.second, bottomLeftOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col - 1)) {
            // if pattern.PlaceMapWithBottles contains the col - 1 -> search bottomPattern
            Pair<PatternModelWithBottles, CoordinatesModel> bottomPatternAndCoordinates = getBottomPatternAndCoordinates(patternCoordinates);
            if (bottomPatternAndCoordinates != null) {
                PatternModelWithBottles bottomPattern = bottomPatternAndCoordinates.first;
                CoordinatesModel bottomLeftOtherCoordinates = new CoordinatesModel(CoordinatesManager.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col - 1);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomLeftOtherCoordinates)) {
                    return new PatternInfos(bottomPattern.PlaceMapWithBottles.get(bottomLeftOtherCoordinates), bottomPattern, bottomPatternAndCoordinates.second, bottomLeftOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternInfos getBottomPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col);
        if (pattern.PlaceMapWithBottles.containsKey(bottomCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(bottomCoordinates), pattern, patternCoordinates, bottomCoordinates);
        } else {
            // search bottomPattern
            Pair<PatternModelWithBottles, CoordinatesModel> bottomPatternAndCoordinates = getBottomPatternAndCoordinates(patternCoordinates);
            if (bottomPatternAndCoordinates != null) {
                PatternModelWithBottles bottomPattern = bottomPatternAndCoordinates.first;
                CoordinatesModel bottomOtherCoordinates = new CoordinatesModel(CoordinatesManager.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomOtherCoordinates)) {
                    return new PatternInfos(bottomPattern.PlaceMapWithBottles.get(bottomOtherCoordinates), bottomPattern, bottomPatternAndCoordinates.second, bottomOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternInfos getLeftPatternInfos(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel leftCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(leftCoordinates)) {
            return new PatternInfos(pattern.PlaceMapWithBottles.get(leftCoordinates), pattern, patternCoordinates, leftCoordinates);
        } else {
            // search leftPattern
            Pair<PatternModelWithBottles, CoordinatesModel> leftPatternAndCoordinates = getLeftPatternAndCoordinates(patternCoordinates);
            if (leftPatternAndCoordinates != null) {
                PatternModelWithBottles leftPattern = leftPatternAndCoordinates.first;
                CoordinatesModel leftOtherCoordinates = new CoordinatesModel(coordinates.Row, CoordinatesManager.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(leftOtherCoordinates)) {
                    return new PatternInfos(leftPattern.PlaceMapWithBottles.get(leftOtherCoordinates), leftPattern, leftPatternAndCoordinates.second, leftOtherCoordinates);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<PatternModelWithBottles, CoordinatesModel> getRightPatternAndCoordinates(CoordinatesModel patternCoordinates) {
        CoordinatesModel rightPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col + 1);
        return PatternMap.containsKey(rightPatternCoordinates) ? new Pair<>(PatternMap.get(rightPatternCoordinates), rightPatternCoordinates) : null;
    }

    @Nullable
    private Pair<PatternModelWithBottles, CoordinatesModel> getTopPatternAndCoordinates(CoordinatesModel patternCoordinates) {
        CoordinatesModel topPatternCoordinates = new CoordinatesModel(patternCoordinates.Row + 1, patternCoordinates.Col);
        return PatternMap.containsKey(topPatternCoordinates) ? new Pair<>(PatternMap.get(topPatternCoordinates), topPatternCoordinates) : null;
    }

    @Nullable
    private Pair<PatternModelWithBottles, CoordinatesModel> getLeftPatternAndCoordinates(CoordinatesModel patternCoordinates) {
        CoordinatesModel leftPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col - 1);
        return PatternMap.containsKey(leftPatternCoordinates) ? new Pair<>(PatternMap.get(leftPatternCoordinates), leftPatternCoordinates) : null;
    }

    @Nullable
    private Pair<PatternModelWithBottles, CoordinatesModel> getBottomPatternAndCoordinates(CoordinatesModel patternCoordinates) {
        CoordinatesModel bottomPatternCoordinates = new CoordinatesModel(patternCoordinates.Row - 1, patternCoordinates.Col);
        return PatternMap.containsKey(bottomPatternCoordinates) ? new Pair<>(PatternMap.get(bottomPatternCoordinates), bottomPatternCoordinates) : null;
    }

    public void setPatternMapWithBoxes(Context context, int patternId, CaveModel oldCave) {
        int oldPatternId = -1;
        int oldNumberBoxes = 0;
        if (oldCave != null) {
            CoordinatesModel origin = new CoordinatesModel(0, 0);
            oldPatternId = oldCave.CaveArrangement.PatternMap.containsKey(origin) ? oldCave.CaveArrangement.PatternMap.get(origin).Id : -1;
            oldNumberBoxes = oldCave.CaveArrangement.NumberBoxes;
        }
        boolean isSamePattern = patternId == oldPatternId;
        PatternMap.clear();
        PatternModel pattern = PatternManager.getPattern(patternId);
        for (int i = 0; i < NumberBoxes; i++) {
            CoordinatesModel coordinates = new CoordinatesModel(i, 0);
            PatternModelWithBottles newPattern = isSamePattern && oldCave != null && oldCave.CaveArrangement.PatternMap.containsKey(coordinates)
                    ? oldCave.CaveArrangement.PatternMap.get(coordinates)
                    : new PatternModelWithBottles(pattern);
            PatternMap.put(coordinates, newPattern);
        }
        if (!isSamePattern) {
            TotalUsed = 0;
        } else if (oldNumberBoxes > NumberBoxes && oldCave != null) {
            float totalPlaced = 0f;
            for (int i = NumberBoxes; i < oldNumberBoxes; i++) {
                for (Map.Entry<Integer, Float> numberPlaced : oldCave.CaveArrangement.PatternMap.get(new CoordinatesModel(i, 0)).FloatNumberPlacedBottlesByIdMap.entrySet()) {
                    int bottleId = numberPlaced.getKey();
                    float value = numberPlaced.getValue();
                    totalPlaced += value;
                    int delta = -1 * (int) Math.ceil(value);
                    IntNumberPlacedBottlesByIdMap.put(bottleId, IntNumberPlacedBottlesByIdMap.get(bottleId) + delta);
                    if (IntNumberPlacedBottlesByIdMap.get(bottleId) == 0) {
                        IntNumberPlacedBottlesByIdMap.remove(bottleId);
                    }
                    BottleManager.updateNumberPlaced(context, bottleId, delta);
                }
            }
            int intTotalPlaced = (int) Math.ceil(totalPlaced);
            TotalUsed -= intTotalPlaced;
            TotalUsed = Math.max(0, TotalUsed);
        }
    }

    public boolean hasDifferentPattern(CaveArrangementModel caveArrangement) {
        if (caveArrangement == null || PatternMap.size() != caveArrangement.PatternMap.size()) {
            return true;
        }

        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();

            if (!caveArrangement.PatternMap.containsKey(coordinates) || pattern.Id != caveArrangement.PatternMap.get(coordinates).Id) {
                return true;
            }
        }
        return false;
    }

    public void recomputeBottlesPlaced(Context context, CaveModel oldCave) {
        CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(PatternMap.keySet());
        for (int col = 0; col < maxRowCol.Col + 1; col++) {
            for (int row = 0; row < maxRowCol.Row + 1; row++) {
                PatternModelWithBottles pattern = CollectionsHelper.getValueOrDefault(PatternMap, new CoordinatesModel(row, col), null);
                if (pattern == null) {
                    continue;
                }
                CoordinatesModel patternMaxRowCol = CoordinatesManager.getMaxRowCol(pattern.PlaceMapWithBottles.keySet());

                if (pattern.IsVerticallyExpendable) {
                    PatternModelWithBottles patternTop = CollectionsHelper.getValueOrDefault(PatternMap, new CoordinatesModel(row + 1, col), null);
                    if (!pattern.isPatternVerticallyCompatible(patternTop)) {
                        patternTop = null;
                    }

                    PatternModelWithBottles patternBottom = CollectionsHelper.getValueOrDefault(PatternMap, new CoordinatesModel(row - 1, col), null);
                    if (!pattern.isPatternVerticallyCompatible(patternBottom)) {
                        patternBottom = null;
                    }
                    CoordinatesModel patternBottomMaxRowCol = patternBottom != null ? CoordinatesManager.getMaxRowCol(patternBottom.PlaceMapWithBottles.keySet()) : null;

                    for (int pCol = 0; pCol < patternMaxRowCol.Col + 1; pCol++) {
                        // look for half bottles on the top : for each 1/2 bottle found, if the second half is not found, remove it
                        CavePlaceModel cavePlaceTop = pattern.PlaceMapWithBottles.get(new CoordinatesModel(patternMaxRowCol.Row, pCol));
                        if (cavePlaceTop.BottleId != -1) {
                            // there is a bottle
                            if ((oldCave.CaveArrangement.PatternMap.containsKey(new CoordinatesModel(row + 1, col)) && patternTop == null)
                                    || (patternTop != null && patternTop.PlaceMapWithBottles.get(new CoordinatesModel(0, pCol)).BottleId != cavePlaceTop.BottleId)) {
                                float numberBottlesToRemove = oldCave.CaveArrangement.PatternMap.containsKey(new CoordinatesModel(row + 1, col)) ? 0.25f : 0.5f;

                                float newNumber = Math.max(0, pattern.FloatNumberPlacedBottlesByIdMap.get(cavePlaceTop.BottleId) - numberBottlesToRemove);
                                if (newNumber != 0) {
                                    pattern.FloatNumberPlacedBottlesByIdMap.put(cavePlaceTop.BottleId, newNumber);
                                } else {
                                    pattern.FloatNumberPlacedBottlesByIdMap.remove(cavePlaceTop.BottleId);
                                }

                                cavePlaceTop.BottleId = -1;
                                cavePlaceTop.PlaceType = CavePlaceTypeEnum.getEmptyByPosition(cavePlaceTop.PlaceType.Position);
                            }
                        }
                        // look for half bottles on the bottom : for each 1/2 bottle found, if the second half is not found, remove it
                        CavePlaceModel cavePlaceBottom = pattern.PlaceMapWithBottles.get(new CoordinatesModel(0, pCol));
                        if (cavePlaceBottom.BottleId != -1) {
                            // there is a bottle
                            if (patternBottom == null || patternBottom.PlaceMapWithBottles.get(new CoordinatesModel(patternBottomMaxRowCol.Row, pCol)).BottleId != cavePlaceBottom.BottleId) {
                                float newNumber = Math.max(0, pattern.FloatNumberPlacedBottlesByIdMap.get(cavePlaceBottom.BottleId) - 0.25f);
                                if (newNumber != 0) {
                                    pattern.FloatNumberPlacedBottlesByIdMap.put(cavePlaceBottom.BottleId, newNumber);
                                } else {
                                    pattern.FloatNumberPlacedBottlesByIdMap.remove(cavePlaceBottom.BottleId);
                                }

                                cavePlaceBottom.BottleId = -1;
                                cavePlaceBottom.PlaceType = CavePlaceTypeEnum.getEmptyByPosition(cavePlaceBottom.PlaceType.Position);
                            }
                        }
                    }
                }

                if (pattern.IsHorizontallyExpendable) {
                    PatternModelWithBottles patternLeft = CollectionsHelper.getValueOrDefault(PatternMap, new CoordinatesModel(row, col - 1), null);
                    if (!pattern.isPatternHorizontallyCompatible(patternLeft)) {
                        patternLeft = null;
                    }
                    CoordinatesModel patternLeftMaxRowCol = patternLeft != null ? CoordinatesManager.getMaxRowCol(patternLeft.PlaceMapWithBottles.keySet()) : null;

                    PatternModelWithBottles patternRight = CollectionsHelper.getValueOrDefault(PatternMap, new CoordinatesModel(row, col + 1), null);
                    if (!pattern.isPatternHorizontallyCompatible(patternRight)) {
                        patternRight = null;
                    }

                    for (int pRow = 0; pRow < patternMaxRowCol.Row + 1; pRow++) {
                        // look for half bottles on the left : for each 1/2 bottle found, if the second half is not found, remove it
                        CavePlaceModel cavePlaceLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(pRow, 0));
                        if (cavePlaceLeft.BottleId != -1) {
                            // there is a bottle
                            if (patternLeft == null || patternLeft.PlaceMapWithBottles.get(new CoordinatesModel(pRow, patternLeftMaxRowCol.Col)).BottleId != cavePlaceLeft.BottleId) {
                                float newNumber = Math.max(0, pattern.FloatNumberPlacedBottlesByIdMap.get(cavePlaceLeft.BottleId) - 0.25f);
                                if (newNumber != 0) {
                                    pattern.FloatNumberPlacedBottlesByIdMap.put(cavePlaceLeft.BottleId, newNumber);
                                } else {
                                    pattern.FloatNumberPlacedBottlesByIdMap.remove(cavePlaceLeft.BottleId);
                                }

                                cavePlaceLeft.BottleId = -1;
                                cavePlaceLeft.PlaceType = CavePlaceTypeEnum.getEmptyByPosition(cavePlaceLeft.PlaceType.Position);
                            }
                        }
                        // look for half bottles on the right : for each 1/2 bottle found, if the second half is not found, remove it
                        CavePlaceModel cavePlaceRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(pRow, patternMaxRowCol.Col));
                        if (cavePlaceRight.BottleId != -1) {
                            // there is a bottle
                            if (patternRight == null || patternRight.PlaceMapWithBottles.get(new CoordinatesModel(pRow, 0)).BottleId != cavePlaceRight.BottleId) {
                                float newNumber = Math.max(0, pattern.FloatNumberPlacedBottlesByIdMap.get(cavePlaceRight.BottleId) - 0.25f);
                                if (newNumber != 0) {
                                    pattern.FloatNumberPlacedBottlesByIdMap.put(cavePlaceRight.BottleId, newNumber);
                                } else {
                                    pattern.FloatNumberPlacedBottlesByIdMap.remove(cavePlaceRight.BottleId);
                                }

                                cavePlaceRight.BottleId = -1;
                                cavePlaceRight.PlaceType = CavePlaceTypeEnum.getEmptyByPosition(cavePlaceRight.PlaceType.Position);
                            }
                        }
                    }
                }
            }
        }

        Map<Integer, Integer> oldIntNumberPlacedBottlesByIdMap = new HashMap<>(IntNumberPlacedBottlesByIdMap);
        IntNumberPlacedBottlesByIdMap.clear();
        Map<Integer, Float> numberPlacedBottlesById = new HashMap<>();
        for (PatternModelWithBottles pattern : PatternMap.values()) {
            for (Map.Entry<Integer, Float> numberPlacedEntry : pattern.FloatNumberPlacedBottlesByIdMap.entrySet()) {
                int bottleId = numberPlacedEntry.getKey();
                float numberPlaced = numberPlacedEntry.getValue();
                if (numberPlacedBottlesById.containsKey(bottleId)) {
                    numberPlacedBottlesById.put(bottleId, numberPlacedBottlesById.get(bottleId) + numberPlaced);
                } else {
                    numberPlacedBottlesById.put(bottleId, numberPlaced);
                }
            }
        }

        // compute the new total of bottles for the whole arrangement
        for (Map.Entry<Integer, Float> numberPlacedEntry : numberPlacedBottlesById.entrySet()) {
            IntNumberPlacedBottlesByIdMap.put(numberPlacedEntry.getKey(), (int) Math.ceil(numberPlacedEntry.getValue()));
        }

        for (Map.Entry<Integer, Integer> oldNumberPlacedEntry : oldIntNumberPlacedBottlesByIdMap.entrySet()) {
            int bottleId = oldNumberPlacedEntry.getKey();
            int oldNumberPlaced = oldNumberPlacedEntry.getValue();
            int newNumberPlaced = CollectionsHelper.getValueOrDefault(IntNumberPlacedBottlesByIdMap, bottleId, 0);
            int delta = newNumberPlaced - oldNumberPlaced;
            if (delta != 0) {
                BottleManager.updateNumberPlaced(context, bottleId, delta);
            }
        }

        int totalUsed = 0;
        for (Map.Entry<Integer, Integer> newNumberPlacedEntry : IntNumberPlacedBottlesByIdMap.entrySet()) {
            int bottleId = newNumberPlacedEntry.getKey();
            int newNumberPlaced = newNumberPlacedEntry.getValue();
            totalUsed += newNumberPlaced;
            if (!oldIntNumberPlacedBottlesByIdMap.containsKey(bottleId)) {
                BottleManager.updateNumberPlaced(context, bottleId, newNumberPlaced);
            }
        }
        TotalUsed = totalUsed;
    }

    public void resetBottlesPlacedIfNeeded(Context context, CaveTypeEnum caveType, CaveModel oldCave) {
        switch (caveType) {
            case BULK:
            case BOX:
                if (oldCave.CaveType != caveType) {
                    resetBottlePlaced(context);
                    resetFloatNumberPlacedBottlesByIdMap();
                    TotalUsed = 0;
                }
                break;
        }
    }

    private void resetBottlePlaced(Context context) {
        for (Map.Entry<Integer, Integer> oldNumberPlacedEntry : IntNumberPlacedBottlesByIdMap.entrySet()) {
            BottleManager.updateNumberPlaced(context, oldNumberPlacedEntry.getKey(), -1 * oldNumberPlacedEntry.getValue());
        }
        IntNumberPlacedBottlesByIdMap.clear();
    }
}
