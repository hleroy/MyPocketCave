package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.managers.CaveManager;
import com.myadridev.mypocketcave.managers.CoordinatesManager;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = CaveArrangementModel.class)
public class CaveArrangementModel {

    public int TotalCapacity;
    public int TotalUsed;

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, PatternModelWithBottles> PatternMap;

    public int NumberBottlesBulk;
    public int NumberBoxes;
    public int NumberBottlesPerBox;

    public CaveArrangementModel() {
        PatternMap = new HashMap<>();
    }

    public CaveArrangementModel(CaveArrangementModel caveArrangement) {
        TotalCapacity = caveArrangement.TotalCapacity;
        TotalUsed = caveArrangement.TotalUsed;
        PatternMap = new HashMap<>(caveArrangement.PatternMap);
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        NumberBottlesPerBox = caveArrangement.NumberBottlesPerBox;
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

    public void placeBottle(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CavePlaceModel cavePlace = pattern.PlaceMapWithBottles.get(coordinates);

        BottleModel bottle = BottleManager.Instance.getBottle(bottleId);

        CavePlaceModel leftPlace = null;
        CavePlaceModel bottomPlace = null;
        CavePlaceModel bottomLeftPlace = null;
        CavePlaceModel rightPlace = null;
        CavePlaceModel bottomRightPlace = null;
        CavePlaceModel topPlace = null;
        CavePlaceModel topLeftPlace = null;
        CavePlaceModel topRightPlace = null;

        switch (cavePlace.PlaceType) {
            case PLACE_BOTTOM_LEFT:
                leftPlace = getLeftPlace(patternCoordinates, coordinates);
                bottomPlace = getBottomPlace(patternCoordinates, coordinates);
                bottomLeftPlace = getBottomLeftPlace(patternCoordinates, coordinates);
                break;
            case PLACE_BOTTOM_RIGHT:
                rightPlace = getRightPlace(patternCoordinates, coordinates);
                bottomPlace = getBottomPlace(patternCoordinates, coordinates);
                bottomRightPlace = getBottomRightPlace(patternCoordinates, coordinates);
                break;
            case PLACE_TOP_LEFT:
                leftPlace = getLeftPlace(patternCoordinates, coordinates);
                topPlace = getTopPlace(patternCoordinates, coordinates);
                topLeftPlace = getTopLeftPlace(patternCoordinates, coordinates);
                break;
            case PLACE_TOP_RIGHT:
                rightPlace = getRightPlace(patternCoordinates, coordinates);
                topPlace = getTopPlace(patternCoordinates, coordinates);
                topRightPlace = getTopRightPlace(patternCoordinates, coordinates);
                break;
            default:
                return;
        }

        updateCavePlace(cavePlace, bottle);
        if (leftPlace != null) {
            updateCavePlace(leftPlace, bottle);
        }
        if (bottomPlace != null) {
            updateCavePlace(bottomPlace, bottle);
        }
        if (bottomLeftPlace != null) {
            updateCavePlace(bottomLeftPlace, bottle);
        }
        if (rightPlace != null) {
            updateCavePlace(rightPlace, bottle);
        }
        if (bottomRightPlace != null) {
            updateCavePlace(bottomRightPlace, bottle);
        }
        if (topPlace != null) {
            updateCavePlace(topPlace, bottle);
        }
        if (topLeftPlace != null) {
            updateCavePlace(topLeftPlace, bottle);
        }
        if (topRightPlace != null) {
            updateCavePlace(topRightPlace, bottle);
        }

        BottleManager.Instance.placeBottle(bottleId);
        TotalUsed++;
        CaveManager.Instance.saveCaves();
    }

    private void updateCavePlace(CavePlaceModel cavePlaceToUpdate, BottleModel bottle) {
        cavePlaceToUpdate.BottleId = bottle.Id;
        cavePlaceToUpdate.PlaceType = getPlaceTypeByTypeAndColor(cavePlaceToUpdate.PlaceType, bottle.WineColor);
    }

    private CavePlaceModel getTopRightPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topRightCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(topRightCoordinates)) {
            return pattern.PlaceMapWithBottles.get(topRightCoordinates);
        } else if (CoordinatesManager.Instance.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row + 1)) {
            // if pattern.PlaceMapWithBottles contains the row + 1 -> search rightPattern
            PatternModelWithBottles rightPattern = getRightPattern(patternCoordinates);
            if (rightPattern != null) {
                CoordinatesModel topRightOtherCoordinates = new CoordinatesModel(coordinates.Row + 1, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(topRightOtherCoordinates)) {
                    return rightPattern.PlaceMapWithBottles.get(topRightOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.Instance.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col + 1 -> search topPattern
            PatternModelWithBottles topPattern = getTopPattern(patternCoordinates);
            if (topPattern != null) {
                CoordinatesModel topRightOtherCoordinates = new CoordinatesModel(0, coordinates.Col + 1);
                if (topPattern.PlaceMapWithBottles.containsKey(topRightOtherCoordinates)) {
                    return topPattern.PlaceMapWithBottles.get(topRightOtherCoordinates);
                }
            }
        }
        return null;
    }

    private CavePlaceModel getTopLeftPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topLeftCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(topLeftCoordinates)) {
            return pattern.PlaceMapWithBottles.get(topLeftCoordinates);
        } else if (CoordinatesManager.Instance.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row + 1)) {
            // if pattern.PlaceMapWithBottles contains the row + 1 -> search leftPattern
            PatternModelWithBottles leftPattern = getLeftPattern(patternCoordinates);
            if (leftPattern != null) {
                CoordinatesModel topLeftOtherCoordinates = new CoordinatesModel(coordinates.Row + 1, CoordinatesManager.Instance.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(topLeftOtherCoordinates)) {
                    return leftPattern.PlaceMapWithBottles.get(topLeftOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.Instance.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col - 1 -> search topPattern
            PatternModelWithBottles topPattern = getTopPattern(patternCoordinates);
            if (topPattern != null) {
                CoordinatesModel topLeftOtherCoordinates = new CoordinatesModel(0, coordinates.Col - 1);
                if (topPattern.PlaceMapWithBottles.containsKey(topLeftOtherCoordinates)) {
                    return topPattern.PlaceMapWithBottles.get(topLeftOtherCoordinates);
                }
            }
        }
        return null;
    }

    private CavePlaceModel getTopPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel topCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
        if (pattern.PlaceMapWithBottles.containsKey(topCoordinates)) {
            return pattern.PlaceMapWithBottles.get(topCoordinates);
        } else {
            // search topPattern
            PatternModelWithBottles topPattern = getTopPattern(patternCoordinates);
            if (topPattern != null) {
                CoordinatesModel topOtherCoordinates = new CoordinatesModel(0, coordinates.Col);
                if (topPattern.PlaceMapWithBottles.containsKey(topOtherCoordinates)) {
                    return topPattern.PlaceMapWithBottles.get(topOtherCoordinates);
                }
            }
        }
        return null;
    }

    private CavePlaceModel getBottomRightPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomRightCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(bottomRightCoordinates)) {
            return pattern.PlaceMapWithBottles.get(bottomRightCoordinates);
        } else if (CoordinatesManager.Instance.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row - 1)) {
            // if pattern.PlaceMapWithBottles contains the row - 1 -> search rightPattern
            PatternModelWithBottles rightPattern = getRightPattern(patternCoordinates);
            if (rightPattern != null) {
                CoordinatesModel bottomRightOtherCoordinates = new CoordinatesModel(coordinates.Row - 1, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(bottomRightOtherCoordinates)) {
                    return rightPattern.PlaceMapWithBottles.get(bottomRightOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.Instance.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col + 1)) {
            // if pattern.PlaceMapWithBottles contains the col + 1 -> search bottomPattern
            PatternModelWithBottles bottomPattern = getBottomPattern(patternCoordinates);
            if (bottomPattern != null) {
                CoordinatesModel bottomRightOtherCoordinates = new CoordinatesModel(CoordinatesManager.Instance.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col + 1);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomRightOtherCoordinates)) {
                    return bottomPattern.PlaceMapWithBottles.get(bottomRightOtherCoordinates);
                }
            }
        }
        return null;
    }

    private CavePlaceModel getRightPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel rightCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
        if (pattern.PlaceMapWithBottles.containsKey(rightCoordinates)) {
            return pattern.PlaceMapWithBottles.get(rightCoordinates);
        } else {
            // search rightPattern
            PatternModelWithBottles rightPattern = getRightPattern(patternCoordinates);
            if (rightPattern != null) {
                CoordinatesModel rightOtherCoordinates = new CoordinatesModel(coordinates.Row, 0);
                if (rightPattern.PlaceMapWithBottles.containsKey(rightOtherCoordinates)) {
                    return rightPattern.PlaceMapWithBottles.get(rightOtherCoordinates);
                }
            }
        }
        return null;
    }

    private CavePlaceModel getBottomLeftPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomLeftCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(bottomLeftCoordinates)) {
            return pattern.PlaceMapWithBottles.get(bottomLeftCoordinates);
        } else if (CoordinatesManager.Instance.containsRow(pattern.PlaceMapWithBottles.keySet(), coordinates.Row - 1)) {
            // if pattern.PlaceMapWithBottles contains the row - 1 -> search leftPattern
            PatternModelWithBottles leftPattern = getLeftPattern(patternCoordinates);
            if (leftPattern != null) {
                CoordinatesModel bottomLeftOtherCoordinates = new CoordinatesModel(coordinates.Row - 1, CoordinatesManager.Instance.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(bottomLeftOtherCoordinates)) {
                    return leftPattern.PlaceMapWithBottles.get(bottomLeftOtherCoordinates);
                }
            }
        } else if (CoordinatesManager.Instance.containsCol(pattern.PlaceMapWithBottles.keySet(), coordinates.Col - 1)) {
            // if pattern.PlaceMapWithBottles contains the col - 1 -> search bottomPattern
            PatternModelWithBottles bottomPattern = getBottomPattern(patternCoordinates);
            if (bottomPattern != null) {
                CoordinatesModel bottomLeftOtherCoordinates = new CoordinatesModel(CoordinatesManager.Instance.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col - 1);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomLeftOtherCoordinates)) {
                    return bottomPattern.PlaceMapWithBottles.get(bottomLeftOtherCoordinates);
                }
            }
        }
        return null;
    }

    private CavePlaceModel getBottomPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel bottomCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col);
        if (pattern.PlaceMapWithBottles.containsKey(bottomCoordinates)) {
            return pattern.PlaceMapWithBottles.get(bottomCoordinates);
        } else {
            // search bottomPattern
            PatternModelWithBottles bottomPattern = getBottomPattern(patternCoordinates);
            if (bottomPattern != null) {
                CoordinatesModel bottomOtherCoordinates = new CoordinatesModel(CoordinatesManager.Instance.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet()), coordinates.Col);
                if (bottomPattern.PlaceMapWithBottles.containsKey(bottomOtherCoordinates)) {
                    return bottomPattern.PlaceMapWithBottles.get(bottomOtherCoordinates);
                }
            }
        }
        return null;
    }

    private CavePlaceModel getLeftPlace(CoordinatesModel patternCoordinates, CoordinatesModel coordinates) {
        PatternModelWithBottles pattern = PatternMap.get(patternCoordinates);
        CoordinatesModel leftCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col - 1);
        if (pattern.PlaceMapWithBottles.containsKey(leftCoordinates)) {
            return pattern.PlaceMapWithBottles.get(leftCoordinates);
        } else {
            // search leftPattern
            PatternModelWithBottles leftPattern = getLeftPattern(patternCoordinates);
            if (leftPattern != null) {
                CoordinatesModel leftOtherCoordinates = new CoordinatesModel(coordinates.Row, CoordinatesManager.Instance.getMaxCol(leftPattern.PlaceMapWithBottles.keySet()));
                if (leftPattern.PlaceMapWithBottles.containsKey(leftOtherCoordinates)) {
                    return leftPattern.PlaceMapWithBottles.get(leftOtherCoordinates);
                }
            }
        }
        return null;
    }

    private PatternModelWithBottles getRightPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel rightPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col + 1);
        return PatternMap.containsKey(rightPatternCoordinates) ? PatternMap.get(rightPatternCoordinates) : null;
    }

    private PatternModelWithBottles getTopPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel topPatternCoordinates = new CoordinatesModel(patternCoordinates.Row + 1, patternCoordinates.Col);
        return PatternMap.containsKey(topPatternCoordinates) ? PatternMap.get(topPatternCoordinates) : null;
    }

    private PatternModelWithBottles getLeftPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel leftPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col - 1);
        return PatternMap.containsKey(leftPatternCoordinates) ? PatternMap.get(leftPatternCoordinates) : null;
    }

    private PatternModelWithBottles getBottomPattern(CoordinatesModel patternCoordinates) {
        CoordinatesModel bottomPatternCoordinates = new CoordinatesModel(patternCoordinates.Row - 1, patternCoordinates.Col);
        return PatternMap.containsKey(bottomPatternCoordinates) ? PatternMap.get(bottomPatternCoordinates) : null;
    }

    private CavePlaceTypeEnum getPlaceTypeByTypeAndColor(CavePlaceTypeEnum placeType, WineColorEnum wineColor) {
        switch (wineColor) {
            case RED:
                switch (placeType) {
                    case PLACE_BOTTOM_LEFT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED;
                    case PLACE_BOTTOM_RIGHT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED;
                    case PLACE_TOP_LEFT:
                        return CavePlaceTypeEnum.PLACE_TOP_LEFT_RED;
                    case PLACE_TOP_RIGHT:
                        return CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED;
                    default:
                        return placeType;
                }
            case WHITE:
                switch (placeType) {
                    case PLACE_BOTTOM_LEFT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_WHITE;
                    case PLACE_BOTTOM_RIGHT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_WHITE;
                    case PLACE_TOP_LEFT:
                        return CavePlaceTypeEnum.PLACE_TOP_LEFT_WHITE;
                    case PLACE_TOP_RIGHT:
                        return CavePlaceTypeEnum.PLACE_TOP_RIGHT_WHITE;
                    default:
                        return placeType;
                }
            case ROSE:
                switch (placeType) {
                    case PLACE_BOTTOM_LEFT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_ROSE;
                    case PLACE_BOTTOM_RIGHT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_ROSE;
                    case PLACE_TOP_LEFT:
                        return CavePlaceTypeEnum.PLACE_TOP_LEFT_ROSE;
                    case PLACE_TOP_RIGHT:
                        return CavePlaceTypeEnum.PLACE_TOP_RIGHT_ROSE;
                    default:
                        return placeType;
                }
            case CHAMPAGNE:
                switch (placeType) {
                    case PLACE_BOTTOM_LEFT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE;
                    case PLACE_BOTTOM_RIGHT:
                        return CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_CHAMPAGNE;
                    case PLACE_TOP_LEFT:
                        return CavePlaceTypeEnum.PLACE_TOP_LEFT_CHAMPAGNE;
                    case PLACE_TOP_RIGHT:
                        return CavePlaceTypeEnum.PLACE_TOP_RIGHT_CHAMPAGNE;
                    default:
                        return placeType;
                }
            default:
                return placeType;
        }
    }
}
