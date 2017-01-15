package com.myadridev.mypocketcave.models;

import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
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
        PatternMap = new HashMap<>(caveArrangement.PatternMap);
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        BoxesNumberBottlesByColumn = caveArrangement.BoxesNumberBottlesByColumn;
        BoxesNumberBottlesByRow = caveArrangement.BoxesNumberBottlesByRow;
        floatNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.floatNumberPlacedBottlesByIdMap);
        IntNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.IntNumberPlacedBottlesByIdMap);
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
                    float oldNumberPlaced = numberPlacedBottlesByIdMap.containsKey(bottleId) ? numberPlacedBottlesByIdMap.get(bottleId) : 0f;
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
        if (!PatternMap.containsKey(new CoordinatesModel(0, 0))) return;
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

    public void unplaceBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId) {
        updateBottle(patternCoordinates, coordinates, bottleId, false, true);
    }

    public void unplaceBottle(int bottleId, int quantity) {
        updateBottle(bottleId, quantity, false, true);
    }

    public void placeBottle(int bottleId, int quantity) {
        updateBottle(bottleId, quantity, true, true);
    }

    public void placeBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId) {
        updateBottle(patternCoordinates, coordinates, bottleId, true, true);
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

    private void updateBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId, boolean isAddBottle, boolean updateBottleUsedQuantity) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CavePlaceModel cavePlace = pattern.PlaceMapWithBottles.get(coordinates);

        BottleModel bottle = BottleManager.getBottle(bottleId);

        if (cavePlace.PlaceType.isBottomLeft()) {
            updateBottomLeftBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle);
        } else if (cavePlace.PlaceType.isBottomRight()) {
            updateBottomRightBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle);
        } else if (cavePlace.PlaceType.isTopLeft()) {
            updateTopLeftBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle);
        } else if (cavePlace.PlaceType.isTopRight()) {
            updateTopRightBottle(patternCoordinates, coordinates, pattern, cavePlace, bottle, isAddBottle);
        }

        if (updateBottleUsedQuantity) {
            if (isAddBottle) {
                TotalUsed++;
            } else {
                TotalUsed--;
            }
        }
    }

    private void updateTopRightBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        Pair<CavePlaceModel, PatternModelWithBottles> rightPlaceAndPattern = getRightPlaceAndPattern(patternCoordinates, coordinates);
        if (rightPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(rightPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(rightPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> topPlaceAndPattern = getTopPlaceAndPattern(patternCoordinates, coordinates);
        if (topPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(topPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(topPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> topRightPlaceAndPattern = getTopRightPlaceAndPattern(patternCoordinates, coordinates);
        if (topRightPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(topRightPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(topRightPlaceAndPattern.second);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);
    }

    private void updateTopLeftBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        Pair<CavePlaceModel, PatternModelWithBottles> leftPlaceAndPattern = getLeftPlaceAndPattern(patternCoordinates, coordinates);
        if (leftPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(leftPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(leftPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> topPlaceAndPattern = getTopPlaceAndPattern(patternCoordinates, coordinates);
        if (topPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(topPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(topPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> topLeftPlaceAndPattern = getTopLeftPlaceAndPattern(patternCoordinates, coordinates);
        if (topLeftPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(topLeftPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(topLeftPlaceAndPattern.second);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);
    }

    private void updateBottomRightBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        Pair<CavePlaceModel, PatternModelWithBottles> rightPlaceAndPattern = getRightPlaceAndPattern(patternCoordinates, coordinates);
        if (rightPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(rightPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(rightPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> bottomPlaceAndPattern = getBottomPlaceAndPattern(patternCoordinates, coordinates);
        if (bottomPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(bottomPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(bottomPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> bottomRightPlaceAndPattern = getBottomRightPlaceAndPattern(patternCoordinates, coordinates);
        if (bottomRightPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(bottomRightPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(bottomRightPlaceAndPattern.second);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);
    }

    private void updateBottomLeftBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, PatternModelWithBottles pattern, CavePlaceModel cavePlace, BottleModel bottle, boolean isAddBottle) {
        int numberPlaces = 1;
        updateCavePlace(cavePlace, bottle, isAddBottle);
        List<PatternModelWithBottles> notNullPatterns = new ArrayList<>();
        notNullPatterns.add(pattern);

        Pair<CavePlaceModel, PatternModelWithBottles> leftPlaceAndPattern = getLeftPlaceAndPattern(patternCoordinates, coordinates);
        if (leftPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(leftPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(leftPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> bottomPlaceAndPattern = getBottomPlaceAndPattern(patternCoordinates, coordinates);
        if (bottomPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(bottomPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(bottomPlaceAndPattern.second);
        }
        Pair<CavePlaceModel, PatternModelWithBottles> bottomLeftPlaceAndPattern = getBottomLeftPlaceAndPattern(patternCoordinates, coordinates);
        if (bottomLeftPlaceAndPattern != null) {
            numberPlaces++;
            updateCavePlace(bottomLeftPlaceAndPattern.first, bottle, isAddBottle);
            notNullPatterns.add(bottomLeftPlaceAndPattern.second);
        }

        int sign = isAddBottle ? 1 : -1;
        updateNumberPlaced(bottle, notNullPatterns, numberPlaces * sign, sign);
    }

    private void updateNumberPlaced(BottleModel bottle, List<PatternModelWithBottles> notNullPatterns, float numberPlaces, int intNumberOfBottlesToAdd) {
        float floatNumberOfBottlesToAdd = 1f / numberPlaces;
        for (PatternModelWithBottles pat : notNullPatterns) {
            float oldNumberFloat = pat.FloatNumberPlacedBottlesByIdMap.containsKey(bottle.Id) ? pat.FloatNumberPlacedBottlesByIdMap.get(bottle.Id) : 0f;
            float newNumberFloat = oldNumberFloat + floatNumberOfBottlesToAdd;
            if (newNumberFloat == 0) {
                pat.FloatNumberPlacedBottlesByIdMap.remove(bottle.Id);
            } else {
                pat.FloatNumberPlacedBottlesByIdMap.put(bottle.Id, newNumberFloat);
            }
        }
        int oldNumberInt = IntNumberPlacedBottlesByIdMap.containsKey(bottle.Id) ? IntNumberPlacedBottlesByIdMap.get(bottle.Id) : 0;
        int newNumberInt = oldNumberInt + intNumberOfBottlesToAdd;
        if (newNumberInt == 0) {
            IntNumberPlacedBottlesByIdMap.remove(bottle.Id);
        } else {
            IntNumberPlacedBottlesByIdMap.put(bottle.Id, newNumberInt);
        }
    }

    private void updateNumberPlaced(BottleModel bottle, int intNumberOfBottlesToAdd) {
        float oldNumberFloat = floatNumberPlacedBottlesByIdMap.containsKey(bottle.Id) ? floatNumberPlacedBottlesByIdMap.get(bottle.Id) : 0f;
        float newNumberFloat = oldNumberFloat + intNumberOfBottlesToAdd;
        if (newNumberFloat == 0) {
            floatNumberPlacedBottlesByIdMap.remove(bottle.Id);
        } else {
            floatNumberPlacedBottlesByIdMap.put(bottle.Id, newNumberFloat);
        }
        int oldNumberInt = IntNumberPlacedBottlesByIdMap.containsKey(bottle.Id) ? IntNumberPlacedBottlesByIdMap.get(bottle.Id) : 0;
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
        cavePlaceToUpdate.PlaceType = isAddBottle ? getPlaceTypeByTypeAndColor(cavePlaceToUpdate.PlaceType, bottle.WineColor) : getEmptyPlaceType(cavePlaceToUpdate.PlaceType);
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getTopRightPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topRightCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(topRightCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(topRightCoordinates), pattern);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row + 1)) {
            // if pattern.PlaceMapWithBottles contains the row + 1 -> search rightPattern
            PatternModelWithBottles rightPattern = getRightPattern(patternCoordinates);
            if (rightPattern != null) {
                CoordinatesModel topRightOtherCoordinates = new CoordinatesModel(coordinates.Row + 1, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(topRightOtherCoordinates)) {
                    return new Pair<>(rightPattern.PlaceMapWithBottles.get(topRightOtherCoordinates), rightPattern);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col + 1 -> search topPattern
            PatternModelWithBottles topPattern = getTopPattern(patternCoordinates);
            if (topPattern != null) {
                CoordinatesModel topRightOtherCoordinates = new CoordinatesModel(0, coordinates.Col + 1);
                if (topPattern.PlaceMapWithBottles.containsKey(topRightOtherCoordinates)) {
                    return new Pair<>(topPattern.PlaceMapWithBottles.get(topRightOtherCoordinates), topPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getTopLeftPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topLeftCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(topLeftCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(topLeftCoordinates), pattern);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row + 1)) {
            // if pattern.PlaceMapWithBottles contains the row + 1 -> search leftPattern
            PatternModelWithBottles leftPattern = getLeftPattern(patternCoordinates);
            if (leftPattern != null) {
                CoordinatesModel topLeftOtherCoordinates = new CoordinatesModel(coordinates.Row + 1, CoordinatesManager.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(topLeftOtherCoordinates)) {
                    return new Pair<>(leftPattern.PlaceMapWithBottles.get(topLeftOtherCoordinates), leftPattern);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col - 1 -> search topPattern
            PatternModelWithBottles topPattern = getTopPattern(patternCoordinates);
            if (topPattern != null) {
                CoordinatesModel topLeftOtherCoordinates = new CoordinatesModel(0, coordinates.Col - 1);
                if (topPattern.PlaceMapWithBottles.containsKey(topLeftOtherCoordinates)) {
                    return new Pair<>(topPattern.PlaceMapWithBottles.get(topLeftOtherCoordinates), topPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getTopPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
        if (pattern.PlaceMapWithBottles.containsKey(topCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(topCoordinates), pattern);
        } else {
            // search topPattern
            PatternModelWithBottles topPattern = getTopPattern(patternCoordinates);
            if (topPattern != null) {
                CoordinatesModel topOtherCoordinates = new CoordinatesModel(0, coordinates.Col);
                if (topPattern.PlaceMapWithBottles.containsKey(topOtherCoordinates)) {
                    return new Pair<>(topPattern.PlaceMapWithBottles.get(topOtherCoordinates), topPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getBottomRightPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomRightCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(bottomRightCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(bottomRightCoordinates), pattern);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row - 1)) {
            // if pattern.PlaceMapWithBottles contains the row - 1 -> search rightPattern
            PatternModelWithBottles rightPattern = getRightPattern(patternCoordinates);
            if (rightPattern != null) {
                CoordinatesModel bottomRightOtherCoordinates = new CoordinatesModel(coordinates.Row - 1, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(bottomRightOtherCoordinates)) {
                    return new Pair<>(rightPattern.PlaceMapWithBottles.get(bottomRightOtherCoordinates), rightPattern);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col + 1 -> search bottomPattern
            PatternModelWithBottles bottomPattern = getBottomPattern(patternCoordinates);
            if (bottomPattern != null) {
                CoordinatesModel bottomRightOtherCoordinates = new CoordinatesModel(CoordinatesManager.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col + 1);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomRightOtherCoordinates)) {
                    return new Pair<>(bottomPattern.PlaceMapWithBottles.get(bottomRightOtherCoordinates), bottomPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getRightPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel rightCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(rightCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(rightCoordinates), pattern);
        } else {
            // search rightPattern
            PatternModelWithBottles rightPattern = getRightPattern(patternCoordinates);
            if (rightPattern != null) {
                CoordinatesModel rightOtherCoordinates = new CoordinatesModel(coordinates.Row, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(rightOtherCoordinates)) {
                    return new Pair<>(rightPattern.PlaceMapWithBottles.get(rightOtherCoordinates), rightPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getBottomLeftPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomLeftCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(bottomLeftCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(bottomLeftCoordinates), pattern);
        } else if (CoordinatesManager.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row - 1)) {
            // if pattern.PlaceMapWithBottles contains the row - 1 -> search leftPattern
            PatternModelWithBottles leftPattern = getLeftPattern(patternCoordinates);
            if (leftPattern != null) {
                CoordinatesModel bottomLeftOtherCoordinates = new CoordinatesModel(coordinates.Row - 1, CoordinatesManager.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(bottomLeftOtherCoordinates)) {
                    return new Pair<>(leftPattern.PlaceMapWithBottles.get(bottomLeftOtherCoordinates), leftPattern);
                }
            }
        } else if (CoordinatesManager.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col - 1)) {
            // if pattern.PlaceMapWithBottles contains the col - 1 -> search bottomPattern
            PatternModelWithBottles bottomPattern = getBottomPattern(patternCoordinates);
            if (bottomPattern != null) {
                CoordinatesModel bottomLeftOtherCoordinates = new CoordinatesModel(CoordinatesManager.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col - 1);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomLeftOtherCoordinates)) {
                    return new Pair<>(bottomPattern.PlaceMapWithBottles.get(bottomLeftOtherCoordinates), bottomPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getBottomPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col);
        if (pattern.PlaceMapWithBottles.containsKey(bottomCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(bottomCoordinates), pattern);
        } else {
            // search bottomPattern
            PatternModelWithBottles bottomPattern = getBottomPattern(patternCoordinates);
            if (bottomPattern != null) {
                CoordinatesModel bottomOtherCoordinates = new CoordinatesModel(CoordinatesManager.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomOtherCoordinates)) {
                    return new Pair<>(bottomPattern.PlaceMapWithBottles.get(bottomOtherCoordinates), bottomPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private Pair<CavePlaceModel, PatternModelWithBottles> getLeftPlaceAndPattern(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel leftCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(leftCoordinates)) {
            return new Pair<>(pattern.PlaceMapWithBottles.get(leftCoordinates), pattern);
        } else {
            // search leftPattern
            PatternModelWithBottles leftPattern = getLeftPattern(patternCoordinates);
            if (leftPattern != null) {
                CoordinatesModel leftOtherCoordinates = new CoordinatesModel(coordinates.Row, CoordinatesManager.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(leftOtherCoordinates)) {
                    return new Pair<>(leftPattern.PlaceMapWithBottles.get(leftOtherCoordinates), leftPattern);
                }
            }
        }
        return null;
    }

    @Nullable
    private PatternModelWithBottles getRightPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel rightPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col + 1);
        return PatternMap.containsKey(rightPatternCoordinates) ? PatternMap.get(rightPatternCoordinates) : null;
    }

    @Nullable
    private PatternModelWithBottles getTopPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel topPatternCoordinates = new CoordinatesModel(patternCoordinates.Row + 1, patternCoordinates.Col);
        return PatternMap.containsKey(topPatternCoordinates) ? PatternMap.get(topPatternCoordinates) : null;
    }

    @Nullable
    private PatternModelWithBottles getLeftPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel leftPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col - 1);
        return PatternMap.containsKey(leftPatternCoordinates) ? PatternMap.get(leftPatternCoordinates) : null;
    }

    @Nullable
    private PatternModelWithBottles getBottomPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel bottomPatternCoordinates = new CoordinatesModel(patternCoordinates.Row - 1, patternCoordinates.Col);
        return PatternMap.containsKey(bottomPatternCoordinates) ? PatternMap.get(bottomPatternCoordinates) : null;
    }

    private CavePlaceTypeEnum getPlaceTypeByTypeAndColor(CavePlaceTypeEnum placeType, WineColorEnum wineColor) {
        if (placeType.isBottomLeft()) {
            switch (wineColor) {
                case RED:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED;
                case WHITE:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_WHITE;
                case ROSE:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_ROSE;
                case CHAMPAGNE:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE;
                default:
                    return placeType;
            }
        } else if (placeType.isBottomRight()) {
            switch (wineColor) {
                case RED:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED;
                case WHITE:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_WHITE;
                case ROSE:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_ROSE;
                case CHAMPAGNE:
                    return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_CHAMPAGNE;
                default:
                    return placeType;
            }
        } else if (placeType.isTopLeft()) {
            switch (wineColor) {
                case RED:
                    return CavePlaceTypeEnum.PLACE_TOP_LEFT_RED;
                case WHITE:
                    return CavePlaceTypeEnum.PLACE_TOP_LEFT_WHITE;
                case ROSE:
                    return CavePlaceTypeEnum.PLACE_TOP_LEFT_ROSE;
                case CHAMPAGNE:
                    return CavePlaceTypeEnum.PLACE_TOP_LEFT_CHAMPAGNE;
                default:
                    return placeType;
            }
        } else if (placeType.isTopRight()) {
            switch (wineColor) {
                case RED:
                    return CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED;
                case WHITE:
                    return CavePlaceTypeEnum.PLACE_TOP_RIGHT_WHITE;
                case ROSE:
                    return CavePlaceTypeEnum.PLACE_TOP_RIGHT_ROSE;
                case CHAMPAGNE:
                    return CavePlaceTypeEnum.PLACE_TOP_RIGHT_CHAMPAGNE;
                default:
                    return placeType;
            }
        }
        return placeType;
    }

    private CavePlaceTypeEnum getEmptyPlaceType(CavePlaceTypeEnum placeType) {
        if (placeType.isBottomLeft()) {
            return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT;
        } else if (placeType.isBottomRight()) {
            return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT;
        } else if (placeType.isTopLeft()) {
            return CavePlaceTypeEnum.PLACE_TOP_LEFT;
        } else if (placeType.isTopRight()) {
            return CavePlaceTypeEnum.PLACE_TOP_RIGHT;
        }
        return placeType;
    }

    public void setPatternMapWithBoxes(int patternId, CaveModel oldCave) {
        int oldPatternId = -1;
        if (oldCave != null) {
            CoordinatesModel origin = new CoordinatesModel(0, 0);
            oldPatternId = oldCave.CaveArrangement.PatternMap.containsKey(origin) ? oldCave.CaveArrangement.PatternMap.get(origin).Id : -1;
        }
        boolean isSamePattern = patternId == oldPatternId;
        PatternMap.clear();
        PatternModel pattern = PatternManager.getPattern(patternId);
        for (int i = 0; i < NumberBoxes; i++) {
            CoordinatesModel coordinates = new CoordinatesModel(i, 0);
            PatternModelWithBottles newPattern = isSamePattern && oldCave.CaveArrangement.PatternMap.containsKey(coordinates)
                    ? oldCave.CaveArrangement.PatternMap.get(coordinates)
                    : new PatternModelWithBottles(pattern);
            PatternMap.put(coordinates, newPattern);
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
}
